package com.lavector.collector.crawler.nonce.dianping;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.dianping.entity.DianPingMessage;
import com.lavector.collector.crawler.nonce.dianping.entity.MessageType;
import com.lavector.collector.crawler.nonce.dianping.entity.Person;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.crawler.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.Selector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 11/05/2018.
 *
 * @author zeng.zhao
 */
public class DianPingCommentListPage implements PageParse {

    private JsonMapper mapper = JsonMapper.buildNormalBinder();

    private Map<String, String> commentStar = new HashMap<>();

    private Map<String, String> reviewRank = new HashMap<>();

    private static ZoneId zoneId = ZoneId.systemDefault();

    private static LocalDate localDate = LocalDate.of(2018, 1, 1);
    private static ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
    public static Date startTime = Date.from(zonedDateTime.toInstant());

    private static LocalDate localDate1 = LocalDate.of(2018, 9, 20);
    private static ZonedDateTime zonedDateTime1 = localDate1.atStartOfDay(zoneId);
    public static Date endTime = Date.from(zonedDateTime1.toInstant());

    {
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

    @Override
    public boolean handleUrl(String url) {
        return url.contains("queryType=sortType&queryVal=latest");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        List<Selectable> comments = html.xpath("//div[@class='reviews-items']/ul/li").nodes();
        String shopId = page.getUrl().regex("shop/(\\d+)").get();
        List<DianPingMessage> messages = new ArrayList<>();
        comments.forEach(comment -> {
            String userId = comment.xpath("//a[@class='name']/@href").regex("\\d+").get();
            String userName = comment.xpath("//a[@class='name']/text()").get();
            String content = comment.xpath("//div[@class='review-words Hide']/text()").get();
            if (content == null) {
                content = comment.xpath("//div[@class='review-words']/text()").get();
            }
            String time = comment.xpath("//span[@class='time']/text()").regex("更新于(.*)").get();
            if (time == null) {
                time = comment.xpath("//span[@class='time']/text()").get().trim();
            }
            if (!(TimeUtils.fromStringToDate(time.trim()).after(startTime) && TimeUtils.fromStringToDate(time.trim()).before(endTime))) {
                return;
            }
            String star = comment.xpath("//div[@class='review-rank']/span[1]/@class").get();
            List<Selectable> scoreNodes = comment.xpath("//div[@class='review-rank']/span[2]/span").nodes();
            List<Map<String, String>> scores = scoreNodes
                    .stream()
                    .map(score -> {
                        String[] scoreKV = score.xpath("span/text()").get().trim().split("：");
                        Map<String, String> map = new HashMap<>();
                        map.put(scoreKV[0], scoreKV[1]);
                        return map;
                    })
                    .collect(Collectors.toList());

            DianPingMessage message = new DianPingMessage();
            message.setContent(content);
            message.setRank(commentStar.get(star));
            message.setReviewRank(scores);
            message.setShopId(shopId);
            message.setTime(time);
            message.setType(MessageType.SHOP);
            message.setPerson(new Person(userId, userName));

            messages.add(message);
        });


        if (messages.size() == 0) {
            return result;
        }

//        DianPingPageProcessor.write(mapper.toJson(messages));
        WriteFile.write(mapper.toJson(messages), "/Users/zeng.zhao/Desktop/dianping_message.json");
//        if (TimeUtils.fromStringToDate(messages.get(messages.size() - 1).getTime().trim()).after(DateUtils.addYears(new Date(), -1))) {
        String nextUrl = html.xpath("//a[@class='NextPage']/@href").get();
        if (StringUtils.isNotBlank(nextUrl)) {
            result.addRequest(new Request("http://www.dianping.com" + nextUrl)
                    .putExtra("referer", page.getUrl().get()));
        }
        return result;
    }
}
