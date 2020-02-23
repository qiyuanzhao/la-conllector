package com.lavector.collector.crawler.project.yanying.bilibili.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.project.yanying.ContentType;
import com.lavector.collector.crawler.project.yanying.Message;
import com.lavector.collector.crawler.project.yanying.bilibili.BiliBiliCrawler;
import com.lavector.collector.crawler.util.JsonMapper;
import net.minidev.json.JSONArray;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created on 2018/2/8.
 *
 * @author zeng.zhao
 */
public class BiliCommentPage implements PageParse {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("api.bilibili.com/x/v2/reply") && !url.contains("reply/reply");
    }

    @Override
    public String pageName() {
        return null;
    }


    //一级评论
    //https://api.bilibili.com/x/v2/reply?callback=&jsonp=jsonp&pn=1&type=1&oid=17613111&sort=0
    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().toString();
        String oid = page.getUrl().regex("oid=(\\d+)").get();
        Integer currentPage = Integer.parseInt(page.getUrl().regex("pn=(\\d+)").get());

        JSONArray jsonArray = JsonPath.read(json, "$.data.replies");
        if (jsonArray == null || jsonArray.size() == 0) {
            return result;
        }
        serviceMessage(jsonArray, oid, result);

        if (currentPage <= 1) {
//            JSONArray hots = JsonPath.read(json, "$.data.hots");
//            serviceMessage(hots, oid, result);

            Object upper = JsonPath.read(json, "$.data.upper.top");
            if (upper != null) {
                serviceMessage(new JSONArray().appendElement(upper), oid, result);
            }
        }

        Integer nextPage = currentPage + 1;
        String nextUrl = page.getUrl().get().replace("pn=" + currentPage, "pn=" + nextPage);
        result.addRequest(new Request(nextUrl));
        return result;
    }

    private void serviceMessage(JSONArray jsonArray, String oid, Result result) {
        JsonMapper mapper = JsonMapper.buildNonNullBinder();
        for (Object ajsonArray : jsonArray) {
            Map map = (Map) ajsonArray;
            String mid = map.get("rpid").toString();
            String qmid = map.get("oid").toString();
            String userId = map.get("mid").toString();
            Integer ctime = (Integer) map.get("ctime");
            Date date = new Date(ctime * 1000L);
            String content = ((Map) map.get("content")).get("message").toString();
            Integer like = (Integer) map.get("like");

            if (date.before(BiliBiliCrawler.startTime) || date.after(BiliBiliCrawler.endTime)) {
                continue;
            }

            Message message = new Message();
            message.setQmid(qmid);
            message.setType(ContentType.COMMENT.get());
            message.setContent(content);
            message.setMid(mid);
            message.setUserId(userId);
            message.setLike(like);
            message.setTime(simpleDateFormat.format(date));
            String messageJson = mapper.toJson(message);
            WriteFile.write(messageJson, BiliBiliCrawler.OUT_FILE);

            //获取二级评论url
            String replayUrl = "https://api.bilibili.com/x/v2/reply/reply?callback=" +
                    "&jsonp=jsonp&pn=1&type=1&oid=" + oid + "&ps=10&root=" + mid;
            result.addRequest(new Request(replayUrl));
        }
    }
}
