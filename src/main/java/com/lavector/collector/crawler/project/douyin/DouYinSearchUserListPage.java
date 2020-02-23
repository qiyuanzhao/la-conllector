package com.lavector.collector.crawler.project.douyin;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.JsonMapper;
import net.minidev.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2018/4/4.
 *
 * @author zeng.zhao
 */
public class DouYinSearchUserListPage implements PageParse {

    private static final JsonMapper mapper = JsonMapper.buildNonNullBinder();

    private ExecutorService service = Executors.newFixedThreadPool(3);

    @Override
    public boolean handleUrl(String url) {
        return url.contains("api.amemv.com/aweme/v1/discover/search/");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String pageJson = page.getJson().get();
        JSONArray userList = JsonPath.read(pageJson, "$.user_list");
        if (CollectionUtils.isNotEmpty(userList)) {
            userList.forEach(user -> {
                String nickName = JsonPath.read(user, "$.user_info.nickname");
                Integer followerCount = JsonPath.read(user, "$.user_info.follower_count");
                String description = JsonPath.read(user, "$.user_info.signature");
                String douyinId = JsonPath.read(user, "$.user_info.uid");
                Integer gender = JsonPath.read(user, "$.user_info.gender");
                Integer videoCount = JsonPath.read(user, "$.user_info.aweme_count");
                Integer likes = JsonPath.read(user, "$.user_info.total_favorited");
                Integer followingCount = JsonPath.read(user, "$.user_info.following_count");
                DouYinAccount account = DouYinAccount.custom().setDescription(description)
                        .setNickName(nickName).setDouyinId(douyinId)
                        .setFollowerCount(followerCount)
                        .setFollowingCount(followingCount)
                        .setGender(gender).setVideoCount(videoCount)
                        .setLikes(likes).build();
                write(account);
            });
            String currentCursor = page.getUrl().regex("cursor=(\\d+)&keyword").get();
            Integer nextCursor = Integer.parseInt(currentCursor) + 10;
            String nextUrl = page.getUrl().replace("cursor=" + currentCursor, "cursor=" + nextCursor).get();
            result.addRequest(new Request(nextUrl));
        }
        return result;
    }

    private static synchronized void write(Object o) {
        String json = mapper.toJson(o);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/zeng.zhao/Desktop/douyin_user.json", true))) {
            writer.write(json);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
