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
import us.codecraft.webmagic.selector.Json;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created on 2018/2/8.
 *
 * @author zeng.zhao
 *
 * 二级评论
 */
public class BiliReplyPage implements PageParse {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("api.bilibili.com/x/v2/reply/reply");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().toString();
        JSONArray replies = JsonPath.read(json, "$.data.replies");
        if (replies == null || replies.size() == 0) {
            return result;
        }

        JsonMapper mapper = JsonMapper.buildNonNullBinder();

        for (Object reply : replies) {
            Map map = (Map) reply;
            String mid = map.get("rpid").toString();
            String qmid = map.get("parent").toString();
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
            message.setType(ContentType.REPLY.get());
            message.setContent(content);
            message.setMid(mid);
            message.setUserId(userId);
            message.setLike(like);
            message.setTime(simpleDateFormat.format(date));
            String messageJson = mapper.toJson(message);
            WriteFile.write(messageJson, BiliBiliCrawler.OUT_FILE);
        }

        String currentPage = page.getUrl().regex("pn=(\\d+)").get();
        Integer nextPage = Integer.parseInt(currentPage) + 1;
        String nextUrl = page.getUrl().get().replace("pn=" + currentPage, "pn=" + nextPage);
        result.addRequest(new Request(nextUrl));
        return result;
    }
}