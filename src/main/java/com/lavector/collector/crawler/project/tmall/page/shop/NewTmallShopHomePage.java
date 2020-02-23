package com.lavector.collector.crawler.project.tmall.page.shop;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;

/**
 * Created by qyz on 2019/11/12.
 */
public class NewTmallShopHomePage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return false;
    }

    @Override
    public Result parse(Page page) {
        String host = page.getRequest().getExtra("host").toString();


        String productListUrlEnd = page.getHtml().xpath("//div[@id='page']/div[@id='content']/div[@id='bd']//input[@id='J_ShopAsynSearchURL']/@value").get();

        String productListUrl = host + productListUrlEnd;

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
