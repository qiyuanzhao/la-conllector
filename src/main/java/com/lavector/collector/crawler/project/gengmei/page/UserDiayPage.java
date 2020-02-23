package com.lavector.collector.crawler.project.gengmei.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.gengmei.entity.ArrProduct;
import com.lavector.collector.crawler.project.gengmei.entity.HotProduct;
import com.lavector.collector.crawler.project.gengmei.entity.UserDiary;
import com.lavector.collector.crawler.util.RegexUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.List;

public class UserDiayPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.soyoung.com/searchNew/diary.*", url);
    }

    @Override
    public Result parse(Page page) {
        String text = page.getRawText();
        System.out.println(text);

        List<UserDiary> userDiaries = new ArrayList<>();

        JSONObject jsonObject = JSONObject.fromObject(text);
        JSONArray jsonArray = jsonObject.getJSONObject("responseData").getJSONArray("diary_arr");
        for (Object object : jsonArray) {
            JSONObject jsonObject1 = JSONObject.fromObject(object);
            JSONObject hot_product = jsonObject1.getJSONObject("end");
            String pid = (String) jsonObject1.getJSONObject("hot_product").get("pid");
            jsonObject1.remove("avatar");
            jsonObject1.remove("end");
            jsonObject1.remove("top");
            jsonObject1.remove("middle");
            jsonObject1.remove("item");
            jsonObject1.remove("item_list");
            jsonObject1.remove("other");
            jsonObject1.remove("hot_product");
            jsonObject1.remove("productProp");
            jsonObject1.remove("suggest_search_words");

            HotProduct hotProduct = (HotProduct) JSONObject.toBean(hot_product, HotProduct.class);
            hotProduct.setPid(pid);
            UserDiary userDiary = (UserDiary) JSONObject.toBean(jsonObject1, UserDiary.class);
            userDiary.setHot_product(hotProduct);
            userDiary.setUrl("https://www.soyoung.com/dpg" + userDiary.getGroup_id());
            userDiaries.add(userDiary);

            //https://www.soyoung.com/dpg8465811/
        }

        if (jsonArray.size() > 0) {
            String url = page.getUrl().get();
            String number = url.substring(url.indexOf("page=") + 5);
            int anInt = Integer.parseInt(number) + 1;
            if (anInt <= 100) {
                Request request = new Request("https://www.soyoung.com/searchNew/diary?keyword=%E8%89%BE%E8%8E%89%E8%96%87&cityId=&page_size=200&_json=1&page=" + anInt);
                page.addTargetRequest(request);
            }
        }
        page.getRequest().putExtra("userDiaries", userDiaries);
        page.putField("userDiaries", userDiaries);
        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
