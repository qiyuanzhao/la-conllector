package com.lavector.collector.crawler.project.gengmei.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.gengmei.entity.ArrProduct;
import com.lavector.collector.crawler.project.gengmei.entity.Doctor;
import com.lavector.collector.crawler.util.RegexUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.List;

public class DocterPage implements PageParse {


    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.soyoung.com/searchNew/doctor.*", url);
    }

    @Override
    public Result parse(Page page) {
        String text = page.getRawText();
        System.out.println(text);

        JSONObject jsonObject = JSONObject.fromObject(text);
        JSONArray jsonArray = jsonObject.getJSONObject("responseData").getJSONArray("arr_product");
        List<Doctor> doctors = new ArrayList<>();
        for (Object object : jsonArray) {
            JSONObject jsonObject1 = JSONObject.fromObject(object);
            jsonObject1.remove("hot_menu1");
            jsonObject1.remove("menu1");
            jsonObject1.remove("products");
            Doctor doctor = (Doctor) JSONObject.toBean(jsonObject1, Doctor.class);
            doctor.setUrl("https://y.soyoung.com/d" + doctor.getDoctor_id());
            doctors.add(doctor);
        }


        page.setSkip(true);

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
