package com.lavector.collector.crawler.project.meituan.com.page;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.meituan.com.entity.MeituanProduct;
import com.lavector.collector.crawler.project.meituan.com.entity.MeituanShopEntity;
import com.lavector.collector.crawler.project.meituan.com.entity.ShopEntity;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeituanSearchPage implements PageParse {


    private static Map<String, String> map = new HashMap<>();


    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://i.waimai.meituan.com/openh5/search/poi.*", url);
    }

    @Override
    public Result parse(Page page) {

        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText);
        String commentListStr = jsonObject.getJSONObject("data").getJSONArray("searchPoiList").toJSONString();

        List<MeituanShopEntity> shopEntities = JSONObject.parseArray(commentListStr, MeituanShopEntity.class);
        handleMeituanShopEntity(shopEntities);

        page.getRequest().putExtra("shopEntities", shopEntities);
        page.putField("shopEntities", shopEntities);

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }


    private void handleMeituanShopEntity(List<MeituanShopEntity> shopEntities) {

        for (MeituanShopEntity meituanShopEntity : shopEntities) {

            if (meituanShopEntity.monthSalesTip.length()>4){
                meituanShopEntity.monthSalesTip = handleString(meituanShopEntity.monthSalesTip.substring(2));
            }

            if (meituanShopEntity.wmPoiScore.length()>4){
                meituanShopEntity.wmPoiScore = handleString(meituanShopEntity.wmPoiScore);
            }

            if (meituanShopEntity.averagePriceTip.length()>4){
                meituanShopEntity.averagePriceTip = handleString(meituanShopEntity.averagePriceTip.substring(4));
            }

            List<MeituanProduct> productList = meituanShopEntity.productList;

            for (MeituanProduct meituanProduct : productList){
                if (meituanProduct.praiseContent.length()>4){
                    meituanProduct.praiseContent = handleString(meituanProduct.praiseContent.substring(1));
                }
            }
        }


    }

    private String handleString(String substring) {
        String[] split = substring.split(";");
        String monthSalesTipStr = "";
        for (String str : split) {

            if (str.length() > 4) {

                String key = str.substring(3);

                str = map.get(key.toUpperCase());
            }

            monthSalesTipStr += str;
        }
        return monthSalesTipStr;
    }


    static {
        map.put("F767", "0");
        map.put("EAF5", "2");
        map.put("F05A", "9");
        map.put("E94B", "3");
        map.put("F695", "5");
        map.put("EC3A", "6");
        map.put("F0BF", "8");
        map.put("EEF3", "1");
        map.put("E65A", "7");
        map.put("EACD", "4");

        map.put("F742", "0");
        map.put("F0C1", "3");
        map.put("F7CD", "7");
        map.put("F0EC", "2");
        map.put("E385", "4");
        map.put("F090", "8");
        map.put("F46F", "1");

        map.put("EE31", "6");

        map.put("E760", "5");
        map.put("F6FF", "6");


        map.put("F518", "0");
        map.put("F75A", "7");
        map.put("E0D1", "1");

        map.put("EE31", "9");

        map.put("E1BE", "2");
        map.put("E1EA", "9");
        map.put("E0DE", "8");
        map.put("EE58", "4");
        map.put("EE48", "3");
        map.put("E7FB", "5");

        map.put("F662", "5");
        map.put("F19C", "4");
        map.put("ED4C", "6");
        map.put("E633", "2");
        map.put("E2D0", "9");
        map.put("E745", "3");
        map.put("F8BA", "7");
        map.put("E1F0", "8");
        map.put("E183", "0");
        map.put("E5A7", "1");

        map.put("F6C3", "4");
        map.put("E77F", "7");
        map.put("E3EB", "0");
        map.put("E978", "6");
        map.put("EC98", "1");
        map.put("E80C", "2");
        map.put("EE6D", "9");
        map.put("EF44", "3");
        map.put("E39F", "5");
        map.put("EB62", "8");

        map.put("EDDE", "8");
        map.put("ED8C", "7");
        map.put("EBD6", "4");
        map.put("F6B6", "2");
        map.put("F7B0", "3");
        map.put("F284", "6");
        map.put("E900", "5");
        map.put("F647", "9");
        map.put("ECB9", "0");
        map.put("E3F9", "1");

    }

    public static void main(String[] args) {
        String str = "赞也&#xe0b1;&#xf09a;";

        String substring = str.substring(2);
//        MeituanSearchPage searchPage = new MeituanSearchPage();
//        String s = searchPage.handleString(str);
        System.out.println(substring);

    }
}
