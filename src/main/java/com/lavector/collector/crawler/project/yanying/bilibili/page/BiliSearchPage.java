package com.lavector.collector.crawler.project.yanying.bilibili.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.project.yanying.ContentType;
import com.lavector.collector.crawler.project.yanying.Message;
import com.lavector.collector.crawler.project.yanying.bilibili.BiliBiliCrawler;
import com.lavector.collector.crawler.util.JsonMapper;
import net.minidev.json.JSONArray;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created on 2018/2/8.
 *
 * @author zeng.zhao
 */
public class BiliSearchPage implements PageParse {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("search.bilibili.com/api/search");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        if (JsonPath.read(json, "$.result") == null) {
            return result;
        }
        JsonMapper mapper = JsonMapper.buildNonNullBinder();
        JSONArray jsonArray = JsonPath.read(json, "$.result.video");
        String type = ContentType.VIDEO.get();
        for (Object ajsonArray : jsonArray) {
            Map map = (Map) ajsonArray;
            Object send_date = map.get("pubdate");
            Date date = new Date((Integer) send_date * 1000L);
            String mid = map.get("id").toString();
            String userId = map.get("mid").toString();
            Integer comment = (Integer) map.get("review");
            Integer playCount = (Integer) map.get("play");
            Integer danmuCount = (Integer) map.get("video_review");
            Integer favorite = (Integer) map.get("favorites");
            String url = map.get("arcurl").toString();
            String title = map.get("title").toString();

            if (title.contains("眼影") && (date.after(BiliBiliCrawler.startTime) && date.before(BiliBiliCrawler.endTime))) {
                Message message = new Message();
                message.setComment(comment);
                message.setTime(simpleDateFormat.format(date));
                message.setUrl(url);
                message.setMid(mid);
                message.setUserId(userId);
                message.setPlayCount(playCount);
                message.setDanmuCount(danmuCount);
                message.setCollect(favorite);
                message.setTitle(new Html(title).xpath("//body/allText()").get());
                message.setType(type);

                String messageJson = mapper.toJson(message);
                WriteFile.write(messageJson, BiliBiliCrawler.OUT_FILE);

                String commentUrl = "https://api.bilibili.com/x/v2/reply?callback=&jsonp=jsonp&pn=1&type=1&oid=" +
                        mid + "&sort=0";
                result.addRequest(new us.codecraft.webmagic.Request(commentUrl));
                result.addRequest(new us.codecraft.webmagic.Request(url));
            }

        }

        String currentPage = page.getUrl().regex("page=(\\d+)").get();
        Integer nextPage = Integer.parseInt(currentPage) + 1;
        String nextUrl = page.getUrl().toString().replace("page=" + currentPage, "page=" + nextPage);
        result.addRequest(new us.codecraft.webmagic.Request(nextUrl));
        return result;
    }

    public static void main(String[] args) throws Exception{
//        String url = "https://search.bilibili.com/api/search?search_type=all&keyword=%E7%9C%BC%E5%BD%B1&from_source=banner_search&page=2";
//        String content = Request.Get(url)
//                .execute()
//                .returnContent()
//                .asString(Charset.forName("utf-8"));
//        Page page = new Page();
//        page.setUrl(new Json(url));
//        page.setRequest(new us.codecraft.webmagic.Request(url));
//        page.setRawText(content);
//        BiliSearchPage searchPage = new BiliSearchPage();
//        searchPage.parse(page);

        String s = "【仓大王】<em class=\"keyword\">眼影</em>盘之最！<em class=\"keyword\">眼影</em>盘TAG——最喜欢？最后悔？最实用？";
        System.out.println(new Html(s).xpath("//body/allText()"));
    }
}
