package com.lavector.collector.crawler.project.douyin;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.lavector.collector.crawler.base.Message;
import com.lavector.collector.crawler.util.JsonMapper;
import us.codecraft.webmagic.selector.Json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on 2018/4/4.
 *
 * @author zeng.zhao
 */
public class UserListSort {

    public static void main(String[] args) throws Exception {
        convert();
    }

    private static void sort() throws IOException {
        JsonMapper mapper = JsonMapper.buildNonNullBinder();
        List<DouYinAccount> accounts = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/douyin_user.json"));
        String line = reader.readLine();
        while (line != null) {
            DouYinAccount account = mapper.fromJson(line, DouYinAccount.class);
            accounts.add(account);
            line = reader.readLine();
        }

        accounts.sort(Comparator.comparing(DouYinAccount::getFollowerCount).reversed());

        accounts.stream().limit(100).forEach(account -> {
            String json = mapper.toJson(account);
            try {
                Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/douyin_user_top.json").toFile(), StandardCharsets.UTF_8, FileWriteMode.APPEND)
                        .write(json + "\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void convert() throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        JsonMapper mapper = JsonMapper.buildNormalBinder();
        List<DouYinVideo> videos = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/douyin_video.json"));
        String line;
        while ((line = reader.readLine()) != null) {

            DouYinVideo video = mapper.fromJson(line, DouYinVideo.class);
            videos.add(video);
        }

        videos.stream().filter(video -> {
            for (String keyword : DouYinCrawler.keywords) {
                if (video.getDescription().contains(keyword)) {
                    return true;
                }
            }
            return false;
        }).forEach(video -> {
            Message message = Message.custom()
                    .setComments(Integer.parseInt(video.getCommentCount().replaceAll("w+", "0001").replaceAll("\\.", "")))
                    .setContent(video.getDescription().replaceAll("\\n", "").replaceAll("\\r", ""))
                    .setLikes(Integer.parseInt(video.getLikeCount().replaceAll("w+", "0001").replaceAll("\\.", "")))
                    .setMid(new Json(video.getUrl()).regex("video/(\\d+)").get())
                    .setPlayCount(Integer.parseInt(video.getPlayCount().replaceAll("w+", "0001").replaceAll("\\.", "")))
                    .setReposts(Integer.parseInt(video.getShareCount().replaceAll("w+", "0001").replaceAll("\\.", "")))
                    .setTime(format.format(new Date(video.getCreateTime() * 1000L)))
                    .setSite("douyin")
                    .setType("Post")
                    .setUserId(video.getUserId()).build();
            String json = mapper.toJson(message);
            try {
                Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/douyin_message.json").toFile(), StandardCharsets.UTF_8, FileWriteMode.APPEND)
                        .write(json + "\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
