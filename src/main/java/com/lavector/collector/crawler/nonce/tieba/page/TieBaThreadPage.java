package com.lavector.collector.crawler.nonce.tieba.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.nonce.tieba.TieBaPageProcessor;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.StringToDateConverter;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 23/06/2018.
 *
 * @author zeng.zhao
 */
public class TieBaThreadPage implements PageParse {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private StringToDateConverter converter = new StringToDateConverter();

    private JsonMapper mapper = JsonMapper.buildNormalBinder();

    @Override
    public boolean handleUrl(String url) {
        return url.contains("tieba.baidu.com/p/");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String title = html.xpath("//div[@id='j_core_title_wrap']/h3/a/text()").get();
        List<Selectable> divs = html.xpath("//div[@id='j_p_postlist']/div").nodes();
        List<NonceMessage> messages = new ArrayList<>();
        String forumId = new PlainText(page.getRawText()).regex("'forumId': \"(\\d+)\", ").get();
        divs.stream()
                .filter(div -> {
                    if (div.xpath("//span[@class='label_text']/text()").get() != null) {
                        return false;
                    }
                    List<String> times = div.xpath("//div[@class='post-tail-wrap']/span/text()").all();
                    String time;
                    if (times.size() == 0) {
                        String contentInfo = div.xpath("/div/@data-field").get();
                        time = JsonPath.read(contentInfo, "$.content.date");
                    } else {
                        time = times.get(times.size() - 1);
                    }
                    Date date = converter.convert(time);
                    return date != null && date.after(TieBaPageProcessor.startTime);
                })
                .forEach(div -> {
                    String contentInfo = div.xpath("/div/@data-field").get();
                    String mid = JsonPath.read(contentInfo, "$.content.post_id").toString();
                    String contentSource = div.xpath("//div[@id='post_content_" + mid + "']/text()").get();
                    String content;
                    if (contentSource.contains("<a") || contentSource.contains("<img")) {
                        content = new Html(contentSource).xpath("//body/allText()").get();
                    } else {
                        content = contentSource;
                    }
                    String userId = JsonPath.read(contentInfo, "author.user_id").toString();
                    List<String> times = div.xpath("//div[@class='post-tail-wrap']/span/text()").all();
                    String time;
                    if (times.size() == 0) {
                        time = JsonPath.read(contentInfo, "$.content.date");
                    } else {
                        time = times.get(times.size() - 1);
                    }
                    Date date = converter.convert(time);

                    NonceMessage message = new NonceMessage();
                    message.setContent(content);
                    message.setUrl(page.getUrl().get());
                    message.setSite("tieba");
                    message.setUserId(userId);
                    message.setTitle(title);
                    message.setType("Post");
                    message.setTime(format.format(date));
                    message.setMid(mid);

                    messages.add(message);
                });

        String pn = page.getUrl().regex("pn=(\\d+)").get();
        if (!StringUtils.equals(forumId, "0")) {
            String threadId = page.getUrl().regex("tieba.baidu.com/p/(\\d+)").get();
            String commentUrl = "https://tieba.baidu.com/p/totalComment?tid="
                    + threadId + "&fid=" + forumId + "&pn=" +
                    pn + "&see_lz=0";
            String commentInfo = getComment(commentUrl);
            if (commentInfo != null) {
                List<NonceMessage> commentMessages = new ArrayList<>();
                messages.stream()
                        .filter(message -> {
                            String mid = message.getMid();
                            return commentInfo.contains(mid);
                        })
                        .forEach(message -> {
                            String mid = message.getMid();
                            JSONArray comments = JsonPath.read(commentInfo, "$.data.comment_list." + mid + ".comment_info");
                            comments.stream()
                                    .filter(comment -> {
                                        String time = JsonPath.read(comment, "$.now_time").toString();//时间戳
                                        Date date = new Date(Long.parseLong(time + "000"));
                                        return date.after(TieBaPageProcessor.startTime);
                                    })
                                    .forEach(comment -> {
                                        String commentId = JsonPath.read(comment, "$.comment_id");
                                        String userId = JsonPath.read(comment, "$.user_id").toString();
                                        String time = JsonPath.read(comment, "$.now_time").toString();//时间戳
                                        String contentInfo = JsonPath.read(comment, "$.content");
                                        String content;
                                        if (contentInfo.contains("<a")
                                                || contentInfo.contains("<img")) {
                                            content = new Html(contentInfo).xpath("//body/allText()").get();
                                        } else {
                                            content = contentInfo;
                                        }

                                        NonceMessage message1 = new NonceMessage();
                                        message1.setMid(commentId);
                                        message1.setQmid(mid);
                                        message1.setType("Reply");
                                        message1.setUserId(userId);
                                        message1.setSite("tieba");
                                        message1.setUrl(page.getUrl().get());
                                        message1.setContent(content);

                                        message1.setTime(format.format(new Date(Long.parseLong(time + "000"))));
                                        commentMessages.add(message1);
                                    });
                        });
                commentMessages.forEach(message -> {
                    String s = mapper.toJson(message);
                    FileUtils.write(s, "/Users/zeng.zhao/Desktop/tieba(芬达).json");
                });
            }

        }

        messages.forEach(message -> {
            String s = mapper.toJson(message);
            FileUtils.write(s, "/Users/zeng.zhao/Desktop/tieba(芬达).json");
        });

        if (messages.size() > 0 && divs.size() == 30) {
            Integer nextPn = Integer.parseInt(pn) + 1;
            String nextPage = page.getUrl().get().replaceAll("pn=" + pn, "pn=" + nextPn);
            result.addRequest(new us.codecraft.webmagic.Request(nextPage));
        }
        return result;
    }

    private String getComment(String url) {
        try {
            return Request.Get(url)
                    .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36")
                    .execute()
                    .returnContent()
                    .asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        String url = "https://tieba.baidu.com/p/5761160262";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setUrl(new Json(url));
        new TieBaThreadPage().parse(page);
    }
}
