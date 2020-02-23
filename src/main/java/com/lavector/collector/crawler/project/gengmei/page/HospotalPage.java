package com.lavector.collector.crawler.project.gengmei.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.gengmei.entity.ArrProduct;
import com.lavector.collector.crawler.project.gengmei.entity.Hospital;
import com.lavector.collector.crawler.project.gengmei.entity.HotProduct;
import com.lavector.collector.crawler.project.gengmei.entity.UserDiary;
import com.lavector.collector.crawler.util.RegexUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import us.codecraft.webmagic.Page;

import java.util.ArrayList;
import java.util.List;

public class HospotalPage implements PageParse {


    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.soyoung.com/searchNew/hospital.*", url);
    }

    @Override
    public Result parse(Page page) {
        String text = page.getRawText();
        System.out.println(text);

        JSONObject jsonObject = JSONObject.fromObject(text);
        JSONArray jsonArray = jsonObject.getJSONObject("responseData").getJSONArray("hospital_list");
        for (Object object : jsonArray) {
            JSONObject jsonObject1 = JSONObject.fromObject(object);
            JSONArray products = jsonObject1.getJSONArray("products");
            jsonObject1.remove("menu1");
            jsonObject1.remove("hot_menu1");
            jsonObject1.remove("products");
            List<ArrProduct> arrProducts = new ArrayList<>();
            for (Object o : products) {
                JSONObject fromObject = JSONObject.fromObject(o);
                fromObject.remove("wei_kuan");
                fromObject.remove("wei_kuan_list");
                fromObject.remove("is_pin_tuan_yn");
                fromObject.remove("tuan");
                fromObject.remove("pin_tuan");
                fromObject.remove("img_cover");
                fromObject.remove("doctor");
                ArrProduct arrProduct = (ArrProduct) JSONObject.toBean(fromObject, ArrProduct.class);
                arrProducts.add(arrProduct);
            }
            Hospital hospital = (Hospital) JSONObject.toBean(jsonObject1, Hospital.class);
            hospital.setProducts(arrProducts);
            hospital.setUrl("https://y.soyoung.com/hospital/" + hospital.getHospital_id());
            break;
        }

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
