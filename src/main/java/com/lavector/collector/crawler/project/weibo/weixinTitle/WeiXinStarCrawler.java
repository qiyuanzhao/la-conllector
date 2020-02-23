package com.lavector.collector.crawler.project.weibo.weixinTitle;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.*;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.project.weibo.weixinTitle.page.*;
import com.lavector.collector.crawler.util.UrlUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.List;


public class WeiXinStarCrawler {

    //https://weixin.sogou.com/weixin?type=2&ie=utf8&query=%E6%AC%A7%E8%8E%B1%E9%9B%85%E8%B6%B3%E7%90%83&tsn=5&ft=2019-02-14&et=2019-03-14&interation=&wxid=&usip=
    private static String rl = "https://weixin.sogou.com/weixin?type=2&ie=utf8&query=%E6%AC%A7%E8%8E%B1%E9%9B%85%E8%B6%B3%E7%90%83";
    private static String startUrl = "https://weixin.sogou.com/weixin?type=2&page=1&query=";
    private static String endUrl = "&tsn=5&ft=2019-03-18&et=2019-03-18&page=1";


    public static void main(String[] args) {

        List<SkuData> skuDatas = ReadCsv.getSkuData();

        Spider spider = Spider.create(new WeiXinPageProcessor())
                .addPipeline(new MyFilePipeline("G:/text/weixin/data"));


//        DynamicProxyDownloader downloader = new DynamicProxyDownloader();
//        ProxyProvider proxyProvider = SimpleProxyProvider.from(new Proxy("s2.proxy.mayidaili.com",8123));
//        downloader.setProxyProvider(proxyProvider);

        for (SkuData skudata : skuDatas) {
            for (String keyWord : skudata.getWords()) {
                Project project = new Project();
                Request request = new Request();
                project.setKeyWord(keyWord);
                request.setUrl(startUrl + UrlUtils.encode(keyWord) + endUrl);
                request.putExtra("project", project);
                request.addHeader("Referer", startUrl + UrlUtils.encode(keyWord));
                spider.addRequest(request);

            }
        }
        Downloader downloader = new SougouWeixinDownloader();
        spider.setDownloader(downloader);
        spider.thread(1);
        spider.start();
    }
}
