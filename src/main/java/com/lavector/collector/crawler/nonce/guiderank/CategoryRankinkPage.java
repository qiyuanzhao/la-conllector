package com.lavector.collector.crawler.nonce.guiderank;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.util.JsonMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import us.codecraft.webmagic.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 12/07/2018.
 *
 * @author zeng.zhao
 */
public class CategoryRankinkPage implements PageParse {

    private JsonMapper mapper = JsonMapper.buildNormalBinder();

    @Override
    public boolean handleUrl(String url) {
        return url.equals("https://zone.guiderank-app.com/guiderank-web/app/ranking/getRankingByCategoryId.do");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        Map<String, Object> map = new HashMap<>();
        Object rankingObj = JsonPath.read(json, "$.data.ranking");
        if (rankingObj != null) {
            String rankingId = JsonPath.read(rankingObj, "$.rankingId");
            String categoryId = JsonPath.read(rankingObj, "$.categoryId");
            String categoryName = JsonPath.read(rankingObj, "$.categoryName");
            String totalVoteCount = JsonPath.read(rankingObj, "$.voteCount").toString();

            map.put("rankingId", rankingId);
            map.put("categoryId", categoryId);
            map.put("categoryName", categoryName);
            map.put("totalVoteCount", totalVoteCount);

            JSONArray rankingBrandGlobals = JsonPath.read(rankingObj, "$.rankingBrandGlobals");
            if (rankingBrandGlobals.size() > 0) {
                List<Map<String, String>> brands = new ArrayList<>();
                rankingBrandGlobals.forEach(rankingBrandGlobal -> {
                    Map<String, String> brand = new HashMap<>();

                    String brandRankingId = JsonPath.read(rankingBrandGlobal, "$.rankingId");
                    String brandId = JsonPath.read(rankingBrandGlobal, "$.brand.brandId");
                    String name = JsonPath.read(rankingBrandGlobal, "$.brand.name");
                    String logo = JsonPath.read(rankingBrandGlobal, "$.brand.logo");
                    String score = JsonPath.read(rankingBrandGlobal, "$.score").toString();
                    brand.put("rankingId", brandRankingId);
                    brand.put("brandId", brandId);
                    brand.put("name", name);
                    brand.put("score", score);
                    brand.put("logo", logo);

                    JSONArray rankingTags = JsonPath.read(rankingBrandGlobal, "$.rankingTags");
                    if (rankingTags.size() > 0) {
                        rankingTags.forEach(rankingTag -> {
                            String k = JsonPath.read(rankingTag, "$.name");
                            String v = JsonPath.read(rankingTag, "$.tagScore").toString();
                            brand.put(k, v);
                        });
                    }

                    brands.add(brand);
                });

                map.put("rankingBrandGlobals", brands); //综合榜



            }

            // 人气榜
            JSONArray rankingBrandHots = JsonPath.read(rankingObj, "$.rankingBrandHots");
            if (rankingBrandHots.size() > 0) {
                List<Map<String, String>> brands = new ArrayList<>();
                rankingBrandHots.forEach(rankingBrandHot -> {
                    Map<String, String> hotBrand = new HashMap<>();
                    String hotRankingId = JsonPath.read(rankingBrandHot, "$.rankingId");
                    String voteCount = JsonPath.read(rankingBrandHot, "$.voteCount").toString();
                    String brandId = JsonPath.read(rankingBrandHot, "$.brand.brandId");
                    String name = JsonPath.read(rankingBrandHot, "$.brand.name");

                    hotBrand.put("rankingId", hotRankingId);
                    hotBrand.put("voteCount", voteCount);
                    hotBrand.put("brandId", brandId);
                    hotBrand.put("name", name);

                    brands.add(hotBrand);
                });
                map.put("rankingBrandHots", brands);
            }

        } else {
            System.out.println("this category is haven't ranking");
        }


        String content = mapper.toJson(map);
        FileUtils.write(content, "/Users/zeng.zhao/Desktop/guide_rank_hot.json");
        return result;
    }
}
