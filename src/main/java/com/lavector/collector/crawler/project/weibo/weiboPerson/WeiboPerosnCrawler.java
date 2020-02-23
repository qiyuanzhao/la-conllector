package com.lavector.collector.crawler.project.weibo.weiboPerson;


import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.WeiboPersonDownloader;
import com.lavector.collector.crawler.project.weibo.weiboPerson.pipeline.PersonPipeline;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.proxy.Proxy;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiboPerosnCrawler extends BaseCrawler {

    public static void main(String[] args) {

        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/newWeibo/qqq.txt", ",");


        Spider spider = Spider.create(new WeiboPersonProcesser(new CrawlerInfo()))
                .addPipeline(new PersonPipeline("G:/text/newWeibo/star"));

        int i = 1;
        String str = "";
        for (SkuData skudata : skuDatas) {
            String brand = skudata.getBrand();
            str += brand + ",";

//            if (i == 50) {
//                i = 0;
//                System.out.println(str);
//                str = "";
//            }

            skudata.setHeadWords(getList());
            Request request = new Request();
            System.out.println("第" + i + "个");
            String newUrl = handle(skudata.getBrand());
//            String newUrl = skudata.getUrl();
            System.out.println(newUrl);
            if (!"".equals(newUrl)) {
                request.setUrl(newUrl);
                request.putExtra("skuData", skudata);
//                request.addHeader("cookie", CookieUtils.getCookie());
                spider.addRequest(request);
            }
            i++;
        }

        spider.thread(10);
        Downloader downloader = new WeiboPersonDownloader();
//        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(getListProxy()));
//        spider.setDownloader(httpClientDownloader);
//        spider.setDownloader(downloader);
        spider.start();
    }


    private static String handle(String url) {
//        String group = "";
//        if (url != null && !"".equals(url)) {
//            Pattern pattern = Pattern.compile("://weibo.com/p/[0-9]+");
//            Matcher matcher = pattern.matcher(url);
//            while (matcher.find()) {
//                group = matcher.group();
//            }
//        }
//        String code = group.substring(group.lastIndexOf("/") + 1);

        //https://weibo.com/u1298216681?is_hot=1
        return "https://weibo.com/u/" + url + "?is_hot=1";

//        if (url.startsWith("https:")){
//            return url;
//        }

//        return "https" + group + "/follow?page=1";
    }

    public static List<String> getList() {
        List<String> list = new LinkedList<>();

        list.add("用户id");
        list.add("url");
        list.add("性别");
        list.add("年龄");
        list.add("姓名");
        list.add("城市");
        list.add("简介");
        list.add("标签");
        return list;
    }

    public static List<String> getFollowList() {
        List<String> list = new LinkedList<>();

        list.add("用户id");
        list.add("url");
        list.add("性别");
        list.add("姓名");
        list.add("地址");
        list.add("简介");
        return list;
    }

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        return null;
    }

//https://weibo.com/p/1005051249819153/follow?page=1#Pl_Official_HisRelation__59
    public static Proxy[] getListProxy() {

        String urlNameString = "http://proxyip.lavector.com/getIP";
        String result = "";
        try {
            // 根据地址获取请求
            HttpGet request = new HttpGet(urlNameString);//这里发送get请求
            // 获取当前客户端对象
            HttpClient httpClient = new DefaultHttpClient();
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);

            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(result);
        String ipArray = jsonObject.get("data").toString();
        JSONArray jsonArray = JSONArray.fromObject(ipArray);

        Proxy proxy[] = new Proxy[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            String post = jsonArray.getJSONObject(i).get("port").toString();
            String host = jsonArray.getJSONObject(i).get("ip").toString();
            Proxy newProxy = new Proxy(host, Integer.parseInt(post));
            proxy[i] = newProxy;
        }
        System.out.println(proxy.toString());
        return proxy;

    }

//    public static void main(String[] args) {
//        getListProxy();
//    }
}
