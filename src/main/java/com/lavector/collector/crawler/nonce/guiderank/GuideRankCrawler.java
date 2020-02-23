package com.lavector.collector.crawler.nonce.guiderank;

import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.nonce.NonceDownloader;
import com.lavector.collector.crawler.nonce.guiderank.entity.Category;
import com.lavector.collector.crawler.util.JsonMapper;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 12/07/2018.
 *
 * @author zeng.zhao
 */
public class GuideRankCrawler {

    public static void main(String[] args) throws IOException {
        MySpider spider = MySpider.create(new GuideRankPageProcessor());
//        Request request = new Request("https://zone.guiderank-app.com/guiderank-web/app/ranking/getAllCategory.do");
//        request.setMethod(HttpConstant.Method.POST);
//        spider.addRequest(request);
        addRequest(spider);
        spider.thread(3).start();
    }

    private static void addRequest(Spider spider) throws IOException {
        JsonMapper mapper = JsonMapper.buildNormalBinder();
        BufferedReader reader = Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/guide_rank_global.json"));
        String line = reader.readLine();
        while (line != null) {
            Category category = mapper.fromJson(line, Category.class);
            if (category.getCategoryGroups() != null) {
                category.getCategoryGroups().forEach(category1 -> {
                    if (category1.getCategoryGroups() != null) {
                        category1.getCategoryGroups().forEach(category2 -> {
                            if (category2.getType().equals(3)) {
                                Map<String, String> map = new HashMap<>();
                                map.put("categoryId", category2.getCategoryId());

                                HttpRequestBody body = new HttpRequestBody();
                                body.setContentType("application/json");
                                body.setEncoding("utf-8");
                                body.setBody(mapper.toJson(map).getBytes());

                                Request request = new Request("https://zone.guiderank-app.com/guiderank-web/app/ranking/getRankingByCategoryId.do");
                                request.setMethod(HttpConstant.Method.POST);
                                request.setRequestBody(body);
                                spider.addRequest(request);
                            }
                        });
                    }
                });
            }
            line = reader.readLine();
        }
    }
}
