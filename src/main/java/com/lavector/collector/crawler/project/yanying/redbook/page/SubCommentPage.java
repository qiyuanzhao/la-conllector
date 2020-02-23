package com.lavector.collector.crawler.project.yanying.redbook.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.project.yanying.ContentType;
import com.lavector.collector.crawler.project.yanying.Message;
import com.lavector.collector.crawler.project.yanying.redbook.RedBookCrawler;
import com.lavector.collector.crawler.project.yanying.redbook.RedBookPageProcessor;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.StringToDateConverter;
import net.minidev.json.JSONArray;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created on 2018/2/7.
 *
 * @author zeng.zhao
 */
public class SubCommentPage implements PageParse {

    private JsonMapper mapper = JsonMapper.buildNonNullBinder();

    private StringToDateConverter converter = new StringToDateConverter();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("sub_comment");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        JSONArray jsonArray = JsonPath.read(json, "$.data.sub_comments");
        if (jsonArray.size() == 0) {
            return result;
        }

        boolean isWrite = false;

        String type = ContentType.REPLY.get();
        for (Object aJsonArray : jsonArray) {
            Map map = (Map) aJsonArray;
            String content = map.get("content").toString();
            String mid = map.get("id").toString();
            String likes = map.get("likes").toString();
            String time = map.get("time").toString();
            String userId = ((Map) map.get("user")).get("id").toString();
            String qmid = ((Map) map.get("target_comment")).get("id").toString();

            NonceMessage message = new NonceMessage();
            message.setSite("redBook");
            message.setQmid(qmid);
            message.setUserId(userId);
            message.setType(type);
            message.setContent(content);
            message.setTime(format.format(converter.convert(time)));
            message.setMid(mid);
            NonceMessage.SocialCount socialCount = new NonceMessage.SocialCount();
            socialCount.setLikes(Integer.parseInt(likes));
            message.setSocialCount(socialCount);

            if (converter.convert(time).after(RedBookPageProcessor.startTime)) {
                if (!isWrite) {
                    isWrite = true;
                }
                String messageJson = mapper.toJson(message);
                WriteFile.write(messageJson, RedBookCrawler.OUT_FILE);
            }
        }

        if (isWrite) {
            String end_id = ((Map) jsonArray.get(jsonArray.size() - 1)).get("id").toString();
            String currentUrl = page.getUrl().get();
            String nextUrl;
            if (currentUrl.contains("end_id")) {
                String current_end_id = page.getUrl().regex("end_id=(.*)?").get();
                nextUrl = currentUrl.replace("end_id=" + current_end_id, "end_id=" + end_id);
            } else {
                nextUrl = currentUrl + "?end_id=" + end_id;
            }
            result.addRequest(new Request(nextUrl));
        }
        return result;
    }
}
