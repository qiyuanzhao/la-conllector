package com.lavector.collector.crawler.project.gengmei.page.calender;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class CalendarPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(CalendarPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.soyoung.com/dpg[0-9]+", url);
    }

    @Override
    public Result parse(Page page) {

        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");

        List<Selectable> nodes = page.getHtml().xpath("div[@class='container']//div[@class='diary-list']//p[@class='describe']").nodes();
        if (!CollectionUtils.isEmpty(nodes)) {
            for (Selectable selectable : nodes) {
                String s = selectable.xpath("/p[@class='describe']/a[@class='show-all']/@href").get();
                String url = "https://www.soyoung.com" + s;
                logger.info("****DETILEURL:{}" , url);
                Request request = new Request(url).putExtra("url", page.getUrl().get()).putExtra("skuData",skuData);
                page.addTargetRequest(request);
            }
        }
        logger.info("****URL:{}",skuData.getUrl());
        page.setSkip(true);
        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
