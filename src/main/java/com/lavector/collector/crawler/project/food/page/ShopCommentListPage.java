package com.lavector.collector.crawler.project.food.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/12/28.
 *
 * @author zeng.zhao
 */
public class ShopCommentListPage implements PageParse {


    private static final Map<String, String> commentStar = new HashMap<>();

    private static final Map<String, String> reviewRank = new HashMap<>();

    static {
        commentStar.put("sml-rank-stars sml-str50 star", "5");
        commentStar.put("sml-rank-stars sml-str40 star", "4");
        commentStar.put("sml-rank-stars sml-str30 star", "3");
        commentStar.put("sml-rank-stars sml-str20 star", "2");
        commentStar.put("sml-rank-stars sml-str10 star", "1");

        reviewRank.put("非常好", "5");
        reviewRank.put("很好", "4");
        reviewRank.put("好", "3");
        reviewRank.put("一般", "2");
        reviewRank.put("差", "1");
    }

    private Logger logger = LoggerFactory.getLogger(ShopCommentListPage.class);


    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch(".*www.dianping.com/shop/\\d+/review_all.*", url);
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String shopId = html.xpath("//div[@class='review-list-header']/h1/a/@href").regex("\\d+").toString();
//        String city = html.xpath("//meta[@name='location']").regex("city=(.*)\"").toString();
        String shopName = html.xpath("//a[@class='shop-name']/text()").toString();

        List<Selectable> commentList = html.xpath("//div[@class='reviews-items']/ul/li").nodes();

        for (Selectable li : commentList) {
            String timeCreatedString = li.xpath("span[@class='time']/text()")
                    .regex("(\\d+-\\d+-\\d+ \\d+:\\d+)").toString();

            if (StringUtils.isBlank(timeCreatedString)) {
                timeCreatedString = li.xpath("span[@class='time']/text()")
                        .regex("(\\d+-\\d+-\\d+ \\d+:\\d+:\\d+)").toString();
            }

            if (StringUtils.isBlank(timeCreatedString)) {
                timeCreatedString = li.xpath("span[@class='time']/text()")
                        .regex("(\\d+-\\d+-\\d+)").toString();
            }

            String userId = li.xpath("//a[@class='name']/@href").regex("\\d+").toString();//用户id

            String content = li.xpath("//div[@class='review-words']/text()").toString();//评论
            if (content == null) {
                content = li.css(".content .comment-entry div[id]").xpath("//div/text()").toString();
            }


            String grade = li.xpath("//div[@class='review-rank']/span[1]/@class").get();//星数


            String kouwei = li.xpath("//div[@class='review-rank']/span[2]/span[1]/text()").get();//口味：3
            String huanjing = li.xpath("//div[@class='review-rank']/span[2]/span[2]/text()").get();//口味：3
            String fuwu = li.xpath("//div[@class='review-rank']/span[2]/span[3]/text()").get();//口味：3

            String writeLine = shopName + "," + content.replaceAll(",", "，") + ","
                    + commentStar.get(grade) + ","
                    + userId + ",";
            if (StringUtils.isNotBlank(kouwei)) {
                writeLine = writeLine + processReviewRank(kouwei) + ",";
            }

            if (StringUtils.isNotBlank(huanjing)) {
                writeLine = writeLine + processReviewRank(huanjing) + ",";
            }

            if (StringUtils.isNotBlank(fuwu)) {
                writeLine = writeLine + processReviewRank(fuwu) + ",";
            }

            writeLine = writeLine + page.getUrl().get() + ","
                    + timeCreatedString;
            DishCommentListPage.wirte(writeLine, "/Users/zeng.zhao/Desktop/dianping/shop_comment/" + shopId + ".csv");
            logger.info("写入成功！{}", shopName);
        }

        String nextUrl = html.xpath("//div[@class='reviews-pages']/a[@class='NextPage']/@href").get();
        if (StringUtils.isNotBlank(nextUrl)) {
            if (!nextUrl.contains("www.dianping.com")) {
                nextUrl = "http://www.dianping.com" + nextUrl;
            }
            result.addRequest(new Request(nextUrl));
        }

        return result;
    }


    public static String processReviewRank(String review) {
        String[] split = review.split("：");
        if (split.length == 0) {
            split = review.split(":");
        }
        String category = split[0].trim();
        String rating = split[1].trim();
        return category + reviewRank.get(rating);
    }
}
