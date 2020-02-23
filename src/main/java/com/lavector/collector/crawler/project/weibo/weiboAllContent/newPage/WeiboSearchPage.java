package com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage;

import com.lavector.collector.crawler.base.PageParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;


public class WeiboSearchPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(WeiboSearchPage.class);

    private String dtileUrl = "https://detail.tmall.com/item.htm?id=";


    @Override
    public boolean handleUrl(String url) {
        return url.contains("https://s.taobao.com/search");
    }

    @Override
    public Result parse(Page page) {
        logger.info("获取到列表页面");
        Request request = page.getRequest();
        SkuData skuData = (SkuData) request.getExtra("skuData");





        Result result = Result.get();



        return result;
    }



    @Override
    public String pageName() {
        return null;
    }

    @Override
    public <T> T getRequestExtra(String key, Page page) {
        return null;
    }

    @Override
    public boolean handleRequest(Request request) {
        return false;
    }


}
