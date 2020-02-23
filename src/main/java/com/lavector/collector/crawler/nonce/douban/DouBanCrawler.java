package com.lavector.collector.crawler.nonce.douban;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/**
 * Created on 05/06/2018.
 *
 * @author zeng.zhao
 */
public class DouBanCrawler extends BaseCrawler {


    private String[] urls = {
            "https://www.douban.com/group/571820/discussion?start=32700", //大学生兼职实习Θ校园招聘信息交流
//            "https://www.douban.com/group/425704/discussion?start=16625", //大学生兼职
//            "https://www.douban.com/group/454016/discussion?start=25450", //上海大学生兼职&实习
//            "https://www.douban.com/group/zjsaibo/discussion?start=4175", //大学生创业园
//            "https://www.douban.com/group/205339/discussion?start=8725", //大学生交友
//            "https://www.douban.com/group/11535/discussion?start=7375", //北京大学生
//            "https://www.douban.com/group/389733/discussion?start=150" //大学生搭伴旅行计划团
    };

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider mySpider = MySpider.createUseProxy(new DouBanPageProcessor(crawlerInfo));
        HttpClientDownloader downloader = new DouBanDownloader();
        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("s2.proxy.mayidaili.com", 8123)));
        mySpider.setDownloader(downloader);
        mySpider.addRequest(new Request(urls[0]).putExtra("groupName", "大学生兼职实习Θ校园招聘信息交流"));
//        mySpider.addRequest(new Request(urls[1]).putExtra("groupName", "大学生兼职"));
//        mySpider.addRequest(new Request(urls[2]).putExtra("groupName", "上海大学生兼职&实习"));
//        mySpider.addRequest(new Request(urls[3]).putExtra("groupName", "大学生创业园"));
//        mySpider.addRequest(new Request(urls[4]).putExtra("groupName", "大学生交友"));
//        mySpider.addRequest(new Request(urls[5]).putExtra("groupName", "北京大学生"));
//        mySpider.addRequest(new Request(urls[6]).putExtra("groupName", "大学生搭伴旅行计划团"));
        mySpider.thread(2);
        return mySpider;
    }

    public static void main(String[] args) {
        new DouBanCrawler().createSpider(new CrawlerInfo()).start();
    }
}
