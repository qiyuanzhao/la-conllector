package com.lavector.collector.crawler.project.xsh.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

public class SearchPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(SearchPage.class);

    @Override
    public boolean handleUrl(String url) {

        return url.contains("https://www.baidu.com");
    }

    @Override
    public Result parse(Page page) {
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");

        String pageUrl = page.getUrl().get();
        String substring = pageUrl.substring(pageUrl.indexOf("&pn=") + 4, pageUrl.indexOf("&oq="));
        int parseInt = Integer.parseInt(substring);

        Html html = page.getHtml();
        List<Selectable> nodes = html.xpath("//div[@id='content_left']/div[@class='result c-container ']").nodes();
        List<String> urlList = new ArrayList<>();
        for (Selectable selectable : nodes) {
            String detileUrl = selectable.xpath("//div[@class='result c-container ']/div[@class='f13']/a/@href").get();
            logger.info("获取的url：" + detileUrl);
            urlList.add(detileUrl);
//            page.addTargetRequest(new Request(detileUrl));
        }
        if (parseInt < 100) {
            String nextUrl = html.xpath("//div[@id='page']//a[@class='n']/@href").get();
            page.addTargetRequest(new Request("https://www.baidu.com" + nextUrl).putExtra("skuData", skuData));
        }
        page.getRequest().putExtra("urlList", urlList).putExtra("skuData", skuData);
        page.putField("urlList",urlList);


        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
