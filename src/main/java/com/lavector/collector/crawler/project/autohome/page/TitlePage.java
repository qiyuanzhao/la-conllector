package com.lavector.collector.crawler.project.autohome.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.entity.readData.SkuData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;

public class TitlePage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(TitlePage.class);


    @Override
    public boolean handleUrl(String url) {

        return url.contains("https://club.autohome.com.cn/bbs/thread/");
    }

    @Override
    public Result parse(Page page) {
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        AutoHomeEntity autoHomeEntity = (AutoHomeEntity) page.getRequest().getExtra("autoHomeEntity");
        //点击数
        String views = page.getHtml().xpath("//div[@class='conmain']//div[@class='consnav']/span/font[@id='x-views']/text()").get();
        //回复数
        String replys = page.getHtml().xpath("//div[@class='conmain']//div[@class='consnav']/span/font[@id='x-replys']/text()").get();

        //文本内容
        String text = page.getHtml().xpath("//div[@class='conttxt']/div[@class='w740']/allText()").get();

        autoHomeEntity.setContent(text);
        autoHomeEntity.setView(views);
        autoHomeEntity.setReplys(replys);
        autoHomeEntity.setTitleUrl(page.getUrl().get());

        logger.info("文本内容:" + text);

//        logger.info("成功下载一篇文章...");
        page.getRequest().putExtra("autoHomeEntity",autoHomeEntity);

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
