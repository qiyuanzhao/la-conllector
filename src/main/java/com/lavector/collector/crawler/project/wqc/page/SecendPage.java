package com.lavector.collector.crawler.project.wqc.page;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.List;


public class SecendPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.contains("https://www.aedas.com/feedcall.php");
    }

    @Override
    public Result parse(Page page) {
        String name1 = page.getRequest().getExtra("name1").toString();
        String name2 = page.getRequest().getExtra("name2").toString();

        String rawText = page.getRawText();

        List<String> lists = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        JSONObject jsonObject1 = JSON.parseObject(rawText);
        JSONArray nodes = jsonObject1.getJSONArray("nodes");


        for (int i = 0; i < nodes.size(); i++) {
            JSONObject jsonObject = nodes.getJSONObject(i);
            JSONObject node = jsonObject.getJSONObject("node");
            String link = node.get("Link").toString();
            String title = node.get("title").toString();
            lists.add(link);
            titles.add(title);
        }



        for (int i = 0; i < lists.size(); i++) {

            String newUrl = "https://www.aedas.com" + lists.get(i);
            String name3 = titles.get(i);
            Request request = new Request(newUrl).putExtra("name2", name2).putExtra("name1", name1).putExtra("name3", name3);

            page.addTargetRequest(request);
        }

        page.setSkip(true);
        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
