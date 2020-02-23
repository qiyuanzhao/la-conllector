package com.lavector.collector.crawler.nonce.guiderank;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.util.JsonMapper;
import net.minidev.json.JSONArray;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 12/07/2018.
 *
 * @author zeng.zhao
 */
public class Test11 {

    public static void main(String[] args) throws IOException {
        String json = getContent();
        JSONArray counts = JsonPath.read(json, "$.data.ranking.rankingBrandHots.[*].voteCount");
        counts.stream()
                .map(count -> Integer.parseInt(count.toString()))
                .reduce((a, b) -> a + b)
                .ifPresent(sum -> {
                    System.out.println(sum);
                });
    }

    private static String getContent() throws IOException {
        JsonMapper mapper = JsonMapper.buildNormalBinder();
        String url = "https://zone.guiderank-app.com/guiderank-web/app/ranking/getRankingByCategoryId.do";
        Map<String, String> map = new HashMap<>();
        map.put("categoryId", "14893893411517414535");
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(mapper.toJson(map), "utf-8"));
        HttpResponse response = httpClient.execute(post);
        String content = EntityUtils.toString(response.getEntity());
        System.out.println(content);

        EntityUtils.consumeQuietly(response.getEntity());
        ((CloseableHttpClient) httpClient).close();
        return content;
    }
}
