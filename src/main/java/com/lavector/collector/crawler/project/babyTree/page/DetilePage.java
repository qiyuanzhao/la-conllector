package com.lavector.collector.crawler.project.babyTree.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetilePage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        Pattern pattern = Pattern.compile("http://www.babytree.com/community/[a-z 0-9]+/topic_[0-9]+.html");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }


    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        BabyPrams babyPrams = (BabyPrams) page.getRequest().getExtra("babyPrams");
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");

        List<Selectable> nodes = page.getHtml().xpath("//div[@class='postBody']//div[@class='postContent']").nodes();
        List<Selectable> selectables;
        if (nodes.size()>0){
            selectables = nodes.get(0).xpath("//div[@class='postContent']/p").nodes();
        }else {
            return result;
        }


        String finalContent = "";
        for (Selectable node : selectables) {
            String text = node.xpath("//p/allText()").get();
            finalContent += text;
        }


        String collection = page.getHtml().xpath("//table[@id='DivHbbs']//script[@type='text/javascript']/html()").regex("[0-9]+").get();
        babyPrams.setContent(finalContent);
        babyPrams.setCollectionNumber(collection);

        page.putField("babyPrams", babyPrams);
        page.putField("skuData", skuData);
        return result;
    }


    @Override
    public String pageName() {
        return null;
    }
}
