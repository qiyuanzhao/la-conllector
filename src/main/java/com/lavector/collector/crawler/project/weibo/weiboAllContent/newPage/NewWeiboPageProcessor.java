package com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wmm on 2018/11/20.
 */
public class NewWeiboPageProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(NewWeiboPageProcessor.class);


    private static Site site = Site.me()
            .setSleepTime(2000)
            .setCycleRetryTimes(3)
            .setCharset("gbk")
            .addHeader("cookie", "miid=1039964368417626953; t=960894f2a131c5c60fd8e542b104ae32; cna=6KZmFH+sOWkCAXjAWe1mcgAH; tracknick=question0001; lgc=question0001; tg=0; l=ApmZsJcnXBQjycA0KB9egyv2KY5zcY3Y; enc=JhovhKt9pjEyminEM5UVZBemsE9aUi8OcMwzVvd67dMbqRYbarudLMGza%2B55n2Qf0En%2FJfbIl%2BypvuRdNtjLEg%3D%3D; uc3=vt3=F8dByR6ulKtkuDgPBgA%3D&id2=UU6nRR5oExz1%2FA%3D%3D&nk2=EuC2vKAXDBsbHsZN&lg2=WqG3DMC9VAQiUQ%3D%3D; _cc_=W5iHLLyFfA%3D%3D; thw=cn; mt=ci=-1_0; v=0; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; cookie2=1f2ee97e1e1859e22ac7bcf0735201f4; _tb_token_=5ee5688b3f736; hng=CN%7Czh-CN%7CCNY%7C156; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; swfstore=118445; _uab_collina=154270641149284646362814; x5sec=7b227365617263686170703b32223a226339373338626564623164393566376262623866313938373836303335356439434e764c7a3938464549714c3666544677396d6b67514561444449324e6a51344d6a49794e7a4d374e513d3d227d; JSESSIONID=5393EBF4AE97E73D669DD12904A6FD5B; uc1=cookie14=UoTYNOgbDj3abw%3D%3D; isg=BKeni5VdsE0g7DTfZNXxV9IINtuxhHqGtKj8Y3kUyTZdaMcqgf69XyXqjijTgFOG")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36")
            .setTimeOut(15 * 1000);

    private WeiboSearchPage tmallSearchPage = new WeiboSearchPage();
    private WeiboDetilPage tmallDetilPage = new WeiboDetilPage();

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        if (url.contains("https://detail.tmall.com")) {
            tmallDetilPage.parse(page);
        }else if(url.contains("search.htm")){

            // /i/asynSearch.htm?mid=w-14859464013-0&wid=14859464013&path=/search.htm&amp;search=y&amp;spm=a1z10.1-b-s.w4006-16897160462.1.5ade1f4dTuR2qc&amp;scene=taobao_shop
            String endUrl = page.getHtml().xpath("//input[@id='J_ShopAsynSearchURL']/@value").toString();
            String pageUrl = page.getUrl().get();
            String startUrl = StringUtils.substring(pageUrl, 0, pageUrl.indexOf(".com", 1) + 4);
            Request request = page.getRequest();

            String finalUrl = startUrl + endUrl;

            Set<String> detailUrls = this.getDetailUrl(finalUrl,page);

            List<Request> list = new ArrayList<>();
            for(String detailUrl:detailUrls){
                Request request1 = new Request();
                request1.putExtra("skuData", request.getExtra("skuData"));
                request1.setUrl("https://"+detailUrl);
                list.add(request1);
                page.setSkip(true);
                page.addTargetRequest(request1);
            }


        }else {
            String newUrl = url.substring(0, url.indexOf(".com", 1)+4);
            Request request = page.getRequest();
            request.setUrl(newUrl+"/search.htm");
            page.setSkip(true);
            page.addTargetRequest(request);
        }
    }

    //获取详情页面的链接
    private Set<String> getDetailUrl(String finalUrl,Page page) {
        Set<String> list = new LinkedHashSet<>();
        CloseableHttpClient aDefault = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(finalUrl);
        String pageUrl = page.getUrl().get();
        String referer = pageUrl.substring(0, pageUrl.indexOf("search.htm", 1)+10);

        httpGet.setHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36");
        httpGet.setHeader("referer",referer);
        httpGet.setHeader("cookie","cna=6KZmFH+sOWkCAXjAWe1mcgAH; otherx=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; x=__ll%3D-1%26_ato%3D0; lid=question0001; enc=BDgS2%2Be0BxJxe6UwrzvMyc%2ByCANaqitOdopxOQhqhWPGfFTfvO46sQT4TKJBZ%2FrEcjYhAe7MPjqOIxntKmrTPg%3D%3D; hng=CN%7Czh-CN%7CCNY%7C156; t=960894f2a131c5c60fd8e542b104ae32; tracknick=question0001; lgc=question0001; _tb_token_=ee753e8b593ea; cookie2=18fc6e424918eac6369715d4cc220e59; uc1=cookie16=Vq8l%2BKCLySLZMFWHxqs8fwqnEw%3D%3D&cookie21=U%2BGCWk%2F7p4mBoUyS4E9C&cookie15=V32FPkk%2Fw0dUvg%3D%3D&existShop=false&pas=0&cookie14=UoTYNclMD7a9Fg%3D%3D&tag=8&lng=zh_CN; uc3=vt3=F8dByR6kMQaO0sq8KpU%3D&id2=UU6nRR5oExz1%2FA%3D%3D&nk2=EuC2vKAXDBsbHsZN&lg2=URm48syIIVrSKA%3D%3D; ck1=\"\"; csg=f631b5d9; skt=897558ce244b959f; pnm_cku822=; swfstore=293595; whl=-1%260%260%260; cq=ccp%3D1; x5sec=7b2273686f7073797374656d3b32223a223964663932343732306539653932616666353434616237383237396134393261434e54782b4e3846454c47342f4f57447736486b3577453d227d; isg=BDc32H0yIPDsn6RvaF_XQKltxitBVDnYhHiMM4nkUIZtOFd6kc-Fr3-eHtjD0OPW");
        try {
            CloseableHttpResponse response = aDefault.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String string = EntityUtils.toString(httpEntity, "utf-8");

            Pattern pattern = Pattern.compile("(detail\\.tmall\\.com/item.htm\\?\\S*abbucket=[0-9]+)");// 匹配的模式
            Matcher m = pattern.matcher(string);
            while (m.find()){
                String group = m.group();
                list.add(group);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public Site getSite() {
        return site;
    }
}
