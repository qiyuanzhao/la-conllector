package com.lavector.collector.crawler.project.douyin;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.Message;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.JsonMapper;
import net.minidev.json.JSONArray;
import us.codecraft.webmagic.Page;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created on 2018/4/9.
 *
 * @author zeng.zhao
 */
public class DouYinUserHomePage implements PageParse {

    private static final JsonMapper mapper = JsonMapper.buildNonNullBinder();

    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.douyin.com/aweme/v1/aweme/post");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String accountId = page.getRequest().getExtra("userId").toString();
        String pageJson = page.getJson().get();
        JSONArray videoList = JsonPath.read(pageJson, "$.aweme_list");
        videoList.forEach(video -> {
            Object playCount = JsonPath.read(video, "$.statistics.play_count");
            Object commentCount = JsonPath.read(video, "$.statistics.comment_count");
            Object shareCount = JsonPath.read(video, "$.statistics.share_count");
            Object likeCount = JsonPath.read(video, "$.statistics.digg_count");
            String videoUrl = JsonPath.read(video, "$.share_info.share_url");
            Integer createTime = JsonPath.read(video, "$.create_time");
            String description = JsonPath.read(video, "$.desc");
            DouYinVideo.Builder builder = DouYinVideo.custom();
            DouYinVideo douYinVideo = builder.setPlayCount(playCount.toString())
                    .setCommentCount(commentCount.toString())
                    .setShareCount(shareCount.toString())
                    .setLikeCount(likeCount.toString())
                    .setUrl(videoUrl)
                    .setCreateTime(createTime)
                    .setDescription(description)
                    .setUserId(accountId)
                    .build();
            String json = mapper.toJson(douYinVideo);
            write(json);
        });
        return result;
    }

    private static synchronized void write(String json) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/zeng.zhao/Desktop/douyin_video.json", true))) {
            writer.write(json);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
