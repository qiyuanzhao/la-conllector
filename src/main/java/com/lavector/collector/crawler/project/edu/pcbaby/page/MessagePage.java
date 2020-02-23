package com.lavector.collector.crawler.project.edu.pcbaby.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.Keywords;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/11/15.
 *
 * @author zeng.zhao
 */
public class MessagePage implements PageParse {


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private Logger logger = LoggerFactory.getLogger(MessagePage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://bbs.pcbaby.com.cn/topic-\\d+\\.html", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String site = page.getRequest().getExtra("site").toString();
        String keyword = page.getRequest().getExtra("keyword").toString();
        Html html = page.getHtml();
        String content = html.xpath("//div[@class='replyBody']/allText()").get();
        String replyCount = html.xpath("//div[@id='topic_wrap']//td[@class='post_left']/p/span[2]/text()").get();
        String timePublish = html.xpath("//div[@id='topic_wrap']//td[@class='post_right']/div[@class='post_info']/div[1]/text()")
                .regex("\\d+-\\d+-\\d+\\ \\d+:\\d+").get();
        Date publish = parseTime(timePublish);
        String views = getViews(page.getUrl().get());
        if (StringUtils.isBlank(views)) {
            logger.error("获取查看数失败！{}", page.getUrl().get());
            return result;
        }
        String interaction = "views:" + views + ";reply:" + replyCount;
        if (publish.after(Keywords.targetDate)) {
            String var1 = content.replaceAll(",", "，");
            StringBuilder stringBuilder = new StringBuilder(var1);
            stringBuilder.append(",");
            stringBuilder.append(timePublish);
            stringBuilder.append(",");
            stringBuilder.append(interaction);
            stringBuilder.append(",");
            stringBuilder.append(page.getUrl().get());
            stringBuilder.append(",");
            String writeContent = stringBuilder.toString();
            executorService.execute(() -> {
                WriteFile.write(writeContent, "/Users/zeng.zhao/Desktop/edu/" + site + "/" + keyword + ".csv");
                System.out.println("写入成功！");
            });
        } else {
            System.out.println("该帖子不在日期范围内！" + timePublish + "===" + page.getUrl().get());
        }
        return result;
    }

    private Date parseTime (String time) {
        try {
            return simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    @Override
    public String pageName() {
        return null;
    }

    private String getViews (String referer) {
        String tid = new Json(referer).regex("http://bbs.pcbaby.com.cn/topic-(\\d+)\\.html").get();
        String url = "http://bbs.pcbaby.com.cn/intf/topic/counter.ajax?tid=" + tid;
        try {
            sleep();
            String content = Request.Get(url)
                    .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                    .addHeader("referer", referer)
                    .execute().returnContent().asString();
            return new Json(content).regex("\"views\":(\\d+)").get();
        } catch (IOException e) {
            try {
                sleep();
                String content = Request.Get(url)
                        .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .addHeader("referer", referer)
                        .execute()
                        .returnContent()
                        .asString();
                return new Json(content).regex("\"views\":(\\d+)").get();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) throws Exception {
        String url = "http://bbs.pcbaby.com.cn/topic-5010498.html";
        String content = Request.Get(url).execute().returnContent().asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url).putExtra("site", "太平洋亲子网").putExtra("keyword", "儿童教育"));
        page.setUrl(new Json(url));
        MessagePage messagePage = new MessagePage();
        messagePage.parse(page);
    }
}
