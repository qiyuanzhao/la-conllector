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
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created on 2018/2/7.
 *
 * @author zeng.zhao
 */
public class CommentPage implements PageParse {

    private JsonMapper mapper = JsonMapper.buildNonNullBinder();

    private StringToDateConverter converter = new StringToDateConverter();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.xiaohongshu.com/web_api/sns/v1/note/") && !url.contains("sub_comment");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        String qmid = page.getRequest().getExtra("mid").toString(); //post mid
        JSONArray jsonArray = JsonPath.read(json, "$.data.comments");
        if (jsonArray.size() == 0) {
            return result;
        }

        boolean isWrite = false;

        String type = ContentType.COMMENT.get();
        for (Object aJsonArray : jsonArray) {
            Map map = ((LinkedHashMap) aJsonArray);
            String content = map.get("content").toString();
            String mid = map.get("id").toString();
            String time = map.get("time").toString();
            String userId = ((Map) map.get("user")).get("id").toString();
            String like = map.get("likes").toString();


            NonceMessage message = new NonceMessage();
            message.setMid(mid);
            message.setTime(format.format(converter.convert(time)));
            message.setContent(content);
            message.setType(type);
            message.setUserId(userId);
            message.setQmid(qmid);
            message.setSite("redBook");

            NonceMessage.SocialCount socialCount = new NonceMessage.SocialCount();
            socialCount.setLikes(Integer.parseInt(like));
            message.setSocialCount(socialCount);

            if (converter.convert(time).after(RedBookPageProcessor.startTime)) {
                if (!isWrite) {
                    isWrite = true;
                }
                String sub_comment_url = "http://www.xiaohongshu.com/web_api/sns/v1/note/"
                        + qmid + "/comment/" + mid + "/sub_comment";
                result.addRequest(new us.codecraft.webmagic.Request(sub_comment_url).putExtra("mid", mid));
                String messageJson = mapper.toJson(message);
                WriteFile.write(messageJson, RedBookCrawler.OUT_FILE);
            }

        }
        if (isWrite) {
            String end_id = ((LinkedHashMap) jsonArray.get(jsonArray.size() - 1)).get("id").toString();
            String nextUrl = "http://www.xiaohongshu.com/web_api/sns/v1/note/" + qmid + "/comment?end_id=" + end_id;
            result.addRequest(new us.codecraft.webmagic.Request(nextUrl).putExtra("mid", qmid));
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.xiaohongshu.com/web_api/sns/v1/note/5a598f2591c72c4d77223eef/comment";
        String json = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        JSONArray jsonArray = JsonPath.read(json, "$.data.comments");

    }
}
