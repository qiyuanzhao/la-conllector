package com.lavector.collector.crawler.project.yanying.bilibili.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.project.yanying.ContentType;
import com.lavector.collector.crawler.project.yanying.Message;
import com.lavector.collector.crawler.project.yanying.bilibili.BiliBiliCrawler;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created on 2018/2/8.
 *
 * @author zeng.zhao
 */
public class BiliDanMuPage implements PageParse {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public boolean handleUrl(String url) {
        return url.contains("http://comment.bilibili.com/");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        JsonMapper mapper = JsonMapper.buildNonNullBinder();
        Html html = page.getHtml();
        String type = ContentType.DANMU.get();
        String qmid = page.getRequest().getExtra("mid").toString();
//        List<String> contents = html.xpath("//d/text()").all();
        List<Selectable> nodes = html.xpath("//d").nodes();
        for (Selectable node : nodes) {
            String content = node.xpath("//d/text()").get();
            String timeNum = node.regex("1519\\d+").get();
            Message message = new Message();
            message.setContent(content);
            message.setType(type);
            message.setQmid(qmid);
            message.setTime(simpleDateFormat.format(Integer.parseInt(timeNum) * 1000L));
            String messageJson = mapper.toJson(message);
            WriteFile.write(messageJson, BiliBiliCrawler.OUT_FILE);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String url = "http://comment.bilibili.com/32517066.xml";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url).putExtra("mid", ""));
        page.setRawText(content);
        BiliDanMuPage danMuPage = new BiliDanMuPage();
        danMuPage.parse(page);
    }
}
