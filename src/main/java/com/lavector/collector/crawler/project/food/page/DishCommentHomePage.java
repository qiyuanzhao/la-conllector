package com.lavector.collector.crawler.project.food.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/12/22.
 *
 * @author zeng.zhao
 */
public class DishCommentHomePage implements PageParse {

    private static Map<String, String> starMap = new HashMap<>();

    static {
        starMap.put("item-rank-rst irr-star50", "5.0");
        starMap.put("item-rank-rst irr-star45", "4.5");
        starMap.put("item-rank-rst irr-star40", "4.0");
        starMap.put("item-rank-rst irr-star35", "3.5");
        starMap.put("item-rank-rst irr-star30", "3.0");
        starMap.put("item-rank-rst irr-star25", "2.5");
        starMap.put("item-rank-rst irr-star20", "2.0");
        starMap.put("item-rank-rst irr-star15", "1.5");
        starMap.put("item-rank-rst irr-star10", "1.0");
        starMap.put("item-rank-rst irr-star0", "0.0");
        starMap.put("item-rank-rst irr-star5", "0.5");
    }

    private Logger logger = LoggerFactory.getLogger(DishCommentHomePage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.dianping.com/shop/\\d+/dish\\d+", url);
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
//        String shopid = page.getRequest().getExtra("shopid").toString();
//        String dishid = page.getRequest().getExtra("dishid").toString();
        String shopid = page.getUrl().regex("shop/(\\d+)/").get();
        String dishid = page.getUrl().regex("dish(\\d+)").get();
        Html html = page.getHtml();
//        String commentsListUrl = html.xpath("//div[@class='dish-more-comment']/a/@href").get();
//        if (StringUtils.isNotBlank(commentsListUrl)) {
//            if (!commentsListUrl.contains("http://www")) {
//                commentsListUrl = "http://www.dianping.com" + commentsListUrl;
//            }
//            result.addRequest(new Request(commentsListUrl).putExtra("shopid", shopid).putExtra("dishid", dishid));
//        }

        String shopName = html.xpath("//div[@class='upper-basic-info']/h2/a/text()").get();
        String dishName = html.xpath("//div[@class='dish-name']/text()").get();
        List<Selectable> selectables = html.xpath("//li[@class='comment-list-item']").nodes();
        if (CollectionUtils.isEmpty(selectables)) {
            return result;
        }
        for (Selectable node : selectables) {
            String star = node.xpath("//div[@class='shop-evaluation']/div[1]/@class").get();
            String starRate = starMap.get(star);
            String userId = node.xpath("//div[@class='owner-name']/a/@href").regex("\\d+").get();
            String content = node.xpath("//div[@class='comment-content']/text()").get();
//            String date = node.xpath("//span[@class='comment-date']/text()").get();
            String kouwei = node.xpath("//div[@class='comment-rst']/span[1]/text()").get();
            String huanjing = node.xpath("//div[@class='comment-rst']/span[2]/text()").get();
            String fuwu = node.xpath("//div[@class='comment-rst']/span[3]/text()").get();

            String writeLine = shopName + "," + dishid + "," + dishName.replace("“", "") + ","
                    + userId + "," + (starRate == null ? "0" : starRate) + "," + content.replaceAll(",", "，");
            writeLine += "," + page.getUrl().get();

            if (StringUtils.isNotBlank(kouwei)) {
                writeLine = writeLine + "," + kouwei.trim();
            }
            if (StringUtils.isNotBlank(huanjing)) {
                writeLine = writeLine + "," + huanjing.trim();
            }

            if (StringUtils.isNotBlank(fuwu)) {
                writeLine = writeLine + "," + fuwu.trim();
            }

            DishCommentListPage.wirte(writeLine, "/Users/zeng.zhao/Desktop/dianping/comment/" + shopid + ".csv");
            logger.info("评论写入成功！{}", dishName);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.dianping.com/shop/2301610/dish70159991";
        String content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url).putExtra("shopid", 231).putExtra("dishid", 2423));
        page.setUrl(new Json(url));
        DishCommentHomePage listPage = new DishCommentHomePage();
        listPage.parse(page);
    }
}
