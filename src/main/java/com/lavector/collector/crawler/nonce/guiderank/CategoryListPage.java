package com.lavector.collector.crawler.nonce.guiderank;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.nonce.guiderank.entity.Category;
import com.lavector.collector.crawler.util.JsonMapper;
import jdk.nashorn.internal.parser.JSONParser;
import net.minidev.json.JSONArray;
import us.codecraft.webmagic.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 12/07/2018.
 *
 * @author zeng.zhao
 */
public class CategoryListPage implements PageParse {

    private JsonMapper mapper = JsonMapper.buildNonNullBinder();
    @Override
    public boolean handleUrl(String url) {
        return url.equals("https://zone.guiderank-app.com/guiderank-web/app/ranking/getAllCategory.do");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        JSONArray categories = JsonPath.read(json, "$.data.categories");
        categories.forEach(category -> {
            String categoryId = JsonPath.read(category, "$.categoryId");
            String name = JsonPath.read(category, "$.name");
//            String photoUrl = JsonPath.read(category, "$.photoUrl");
            Integer type = JsonPath.read(category, "$.type");
            Category categoryObj = new Category();
            categoryObj.setCategoryId(categoryId);
            categoryObj.setName(name);
//            categoryObj.setPhotoUrl(photoUrl);
            categoryObj.setType(type);
            JSONArray categoryGroups = JsonPath.read(category, "$.categoryGroups"); //二级
            if (categoryGroups.size() > 0) {
                List<Category> firstList = new ArrayList<>();
                categoryGroups.forEach(categoryGroup -> {
                    String secCategoryId = JsonPath.read(categoryGroup, "$.categoryId");
                    String secName = JsonPath.read(categoryGroup, "$.name");
//                    String secPhotoUrl = JsonPath.read(categoryGroup, "$.photoUrl");

                    Integer secType = JsonPath.read(categoryGroup, "$.type");

                    Category secCategory = new Category();
                    secCategory.setType(secType);
//                    secCategory.setPhotoUrl(secPhotoUrl);
                    secCategory.setCategoryId(secCategoryId);
                    secCategory.setName(secName);
                    JSONArray secCategoryGroups =  JsonPath.read(categoryGroup, "$.categories"); // 三级
                    if (secCategoryGroups.size() > 0) {
                        List<Category> secList = new ArrayList<>();
                        secCategoryGroups.forEach(secCategoryGroup -> {
                            String thiCategoryId = JsonPath.read(secCategoryGroup, "$.categoryId");
                            String thiName = JsonPath.read(secCategoryGroup, "$.name");
//                            String thiPhotoUrl = JsonPath.read(secCategoryGroup, "$.photoUrl");
                            Integer thiType = JsonPath.read(secCategoryGroup, "$.type");

                            Category thiCategory = new Category();
                            thiCategory.setType(thiType);
//                            thiCategory.setPhotoUrl(thiPhotoUrl);
                            thiCategory.setCategoryId(thiCategoryId);
                            thiCategory.setName(thiName);

                            secList.add(thiCategory);
                        });

                        secCategory.setCategoryGroups(secList);
                    }

                    firstList.add(secCategory);
                });

                categoryObj.setCategoryGroups(firstList);
                String categoryJson = mapper.toJson(categoryObj);
                FileUtils.write(categoryJson, "/Users/zeng.zhao/Desktop/guide_rank_global.json");
            }
        });

        return result;
    }
}
