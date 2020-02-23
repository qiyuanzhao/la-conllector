package com.lavector.collector.crawler.project.instagram;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.Message;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.JsonMapper;
import net.minidev.json.JSONArray;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on 16/04/2018.
 *
 * @author zeng.zhao
 */
public class InstagramHotPage implements PageParse {

    private JsonMapper mapper = JsonMapper.buildNormalBinder();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.instagram.com/explore/tags");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().toString();
        JSONArray edges = JsonPath.read(json, "$.graphql.hashtag.edge_hashtag_to_top_posts.edges");
        edges.forEach(edge -> {
            String mid = JsonPath.read(edge, "$.node.id");
            String userId = JsonPath.read(edge, "$.node.owner.id");
            Integer createdTime = JsonPath.read(edge, "$.node.taken_at_timestamp");
            Integer likes = JsonPath.read(edge, "$.node.edge_liked_by.count");
            Integer comments = JsonPath.read(edge, "$.node.edge_media_to_comment.count");
            String content = JsonPath.read(edge, "$.node.edge_media_to_caption.edges[0].node.text");
            Message message = Message.custom()
                    .setUserId(userId)
                    .setMid(mid)
                    .setType("Post")
                    .setSite("instagram")
                    .setTime(format.format(new Date(createdTime * 1000L)))
                    .setLikes(likes)
                    .setComments(comments)
                    .setContent(content.replaceAll("\\n", ""))
                    .build();
            String s = mapper.toJson(message);
            try {
                Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/ins_message.json").toFile(), StandardCharsets.UTF_8, FileWriteMode.APPEND).write(s + "\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return result;
    }
}
