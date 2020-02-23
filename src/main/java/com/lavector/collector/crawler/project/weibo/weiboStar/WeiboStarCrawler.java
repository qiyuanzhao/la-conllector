package com.lavector.collector.crawler.project.weibo.weiboStar;

import com.lavector.collector.crawler.project.weibo.weiboStar.page.*;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WeiboStarCrawler {

    private static String starturl = "https://weibo.com/aj/v6/comment/big?ajwvr=6&id=";
    private static String endUrl = "&from=singleWeiBo";

    private static String startajax = "https://weibo.com/aj/v6/comment/big?from=singleWeiBo&id=";
    private static String endajax = "&page=20";


    public static void main(String[] args) {

        List<SkuData> skuDatas = ReadCsv.getSkuData();

        Spider spider = Spider.create(new WeibolStarPageProcessor())
                .addPipeline(new MyFilePipeline("G:/text/newWeibo/data"));

//        DynamicProxyDownloader downloader = new DynamicProxyDownloader();
//        ProxyProvider proxyProvider = SimpleProxyProvider.from(new Proxy("s2.proxy.mayidaili.com", 8123));
//        downloader.setProxyProvider(proxyProvider);
//        spider.setDownloader(downloader);

        //?is_all=1&is_search=1&key_word=代言
        for (SkuData skudata : skuDatas) {
            Request request = new Request();
            Person person = new Person();
            request.setUrl(handlerUrl(skudata.getUrl(),person));
            person.setStarName(skudata.getName());
            request.putExtra("person", person);
            spider.addRequest(request);
        }
        spider.thread(10);
        spider.start();
    }

    private static String handlerUrl(String url,Person person) {
        Pattern pattern = Pattern.compile("mid=[0-9]+");
        Matcher matcher = pattern.matcher(url);
        String group = "";
        while(matcher.find()){
            group = matcher.group();
        }
        String newUrl = starturl + url + endUrl;
        person.setId(url);
        person.setUrl(newUrl);
        return startajax + url + endajax;
    }
}
