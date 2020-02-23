package com.lavector.collector.crawler.project.babyTreeUserInfo.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.babyTree.page.BabyPrams;
import com.lavector.collector.crawler.project.babyTree.page.ConmentUser;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Page;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfoPage implements PageParse{


    @Override
    public boolean handleUrl(String url) {
        Pattern pattern = Pattern.compile("http://home.babytree.com/u[0-9]+");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        BabyPrams babyPrams = (BabyPrams) page.getRequest().getExtra("babyPrams");
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        ConmentUser conmentUser = babyPrams.getConmentUser();
        String location = page.getHtml().xpath("//div[@id='mytree-profile']//div[@id='mytree-basic-info']//span[@class='location']/text()").get();
        String babyAge = page.getHtml().xpath("//div[@id='mytree-profile']//div[@id='mytree-basic-info']//span[@class='girl' or @class='boy']/text()").get();
        String name = page.getHtml().xpath("//div[@id='mytree-profile']//div[@id='mytree-username']//span/text()").get();

        if (babyPrams.getUserFalg()){
            babyPrams.setAddress(location);
            babyPrams.setBabyAge(babyAge);
        }else {
            conmentUser.setAge(babyAge);
            conmentUser.setAddress(location);
            conmentUser.setName(name);
        }

        page.putField("babyPrams", babyPrams);
        page.putField("skuData", skuData);
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }
}
