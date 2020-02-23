package com.lavector.collector.crawler.project.autohome.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.entity.readData.SkuData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.List;

public class LuntanSearchPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(LuntanSearchPage.class);


    @Override
    public boolean handleUrl(String url) {
        return url.contains("https://sou.autohome.com.cn/luntan?q=");
    }

    @Override
    public Result parse(Page page) {
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");


        List<String> urls = null;
        try {
            urls = page.getHtml().xpath("//div[@class='result-list']//div[@class='topiclist']//a/@href").all();
        } catch (Exception e) {
            logger.info("下载页面失败....");
            return null;
        }

        if (urls != null && urls.size() > 0) {
            urls.forEach(url -> {
                Request request = new Request(url);
                request.putExtra("skuData", skuData);
                page.addTargetRequest(request);
            });

        }
        page.setSkip(true);
        return null;

    }

    @Override
    public String pageName() {
        return null;
    }
}
