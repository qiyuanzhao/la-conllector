package com.lavector.collector.crawler.project.tmall.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by qyz on 2019/9/25.
 */
public class AllBrandPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(AllBrandPage.class);

    @Override
    public boolean handleUrl(String url) {
        return url.contains("allBrandShowForGaiBan.htm");
    }

    @Override
    public Result parse(Page page) {


        String brand = page.getRequest().getExtra("brand").toString();

        String rawText = page.getRawText();
        JSONArray jsonArray = JSON.parseArray(rawText);

        String title = "";
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            title = title + jsonObject.get("title").toString() + "，";
        }


        String path = "G:/text/tmall/data/" + "tmallBrand" + ".csv";
        try {
            if (!new File(path).exists()) {
                boolean newFile = new File(path).createNewFile();
                if (newFile) {
                    String header = "关键词,商品,月销量,价格,评论量,链接\n";
                    FileUtils.writeStringToFile(new File(path), header, Charset.forName("GBK"), true);
                }
            }
            String writeContent = brand + "," + title + "\n";
            FileUtils.writeStringToFile(new File(path), writeContent, Charset.forName("GBK"), true);
            logger.info("写入一条数据。。。");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
