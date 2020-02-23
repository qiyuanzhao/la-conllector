package com.lavector.collector.crawler.project.edu.mama.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.Keywords;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.util.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2017/11/16.
 *
 * @author zeng.zhao
 */
public class MessagePage implements PageParse {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private Logger logger = LoggerFactory.getLogger(MessagePage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://q.mama.cn/topic/\\d+\\?so_param=.*", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String site = page.getRequest().getExtra("site").toString();
        String keyword = page.getRequest().getExtra("keyword").toString();
        String content = html.xpath("//div[@class='news_body']/allText()").get();
        String replyCount = html.xpath("//div[@class='infocount']/span[1]/text()").regex("\\d+").get();
        String views = html.xpath("//div[@class='infocount']/span[2]/text()").regex("\\d+").get();
        String timePublish = html.xpath("//div[@class='re_from']/span[1]/text()").get();
        Date publish = parseTime(timePublish);
        String interaction = "views:" + views + ";reply:" + replyCount;
        String var1 = content.replaceAll(",", "，");
        if (publish.after(Keywords.targetDate)) {
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
        }
        return result;
    }


    private Date parseTime (String time) {
        try {
            return simpleDateFormat.parse(time);
        } catch (ParseException e) {
            logger.error("解析时间失败！{}", time);
        }
        return new Date();
    }

    @Override
    public String pageName() {
        return null;
    }
}
