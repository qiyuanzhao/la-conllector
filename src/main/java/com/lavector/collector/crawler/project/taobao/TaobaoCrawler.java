package com.lavector.collector.crawler.project.taobao;


import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.taobao.page.MyFilePipeline;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;

import java.util.LinkedList;
import java.util.List;

public class TaobaoCrawler extends BaseCrawler {


    public static void main(String[] args) {

        List<SkuData> skuDataList = ReadTextUtils.getSkuData("G:/text/taobao/text.txt", ",");

        Spider spider = Spider.create(new BabyTreeProcessor(new CrawlerInfo()));

        for (SkuData skuData : skuDataList) {
            skuData.setHeadWords(getList());
            Request request = new Request();
            request.setUrl(skuData.getUrl());
//            String newUrl = handleUrl(skuData.getUrl(),request);
//            request.setUrl("https://s.taobao.com/search?q=Vitafusion%E7%BB%B4%E7%94%9F%E7%B4%A0&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20190926&ie=utf8&cps=yes&cat=55854025&sort=sale-desc&s=220");
            request.putExtra("skuData", skuData).putExtra("number", 0);
//            request.setUrl(newUrl);
//            request.putExtra("skuData", skuData).putExtra("currentPageNum", 1);

            spider.addRequest(request);

            spider.addPipeline(new MyFilePipeline("G:/text/taobao/data"));
            Downloader downloader = new SougouWeixinDownloader();
            spider.thread(1);
            spider.setDownloader(downloader);
            spider.run();
        }
//        spider.setScheduler(new RedisScheduler("localhost"));

    }

    private static String handleUrl(String url,Request request) {
        String codeStr = url.substring(url.indexOf("id=") + 3, url.indexOf("&", 1));
        request.addHeader("","https://item.taobao.com/item.htm?id="+codeStr);
        request.putExtra("codeStr",codeStr);
        return "https://rate.taobao.com/feedRateList.htm?auctionNumId=" + codeStr +"&currentPageNum=1&pageSize=20&orderType=feedbackdate";
    }

    public static List<String> getList() {
        List<String> list = new LinkedList<>();

//        list.add("关键词");
//        list.add("用户名");
//        list.add("评论时间");
//        list.add("url");
//        list.add("评论内容");

        list.add("关键词");
        list.add("标题");
        list.add("产品链接");
        list.add("店名");
        list.add("付款人数");
        list.add("评论数");
        list.add("价格");
        list.add("类型");

        return list;
    }


    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {

        return null;
    }

}
