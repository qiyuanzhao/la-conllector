package com.lavector.collector.crawler.project.weibo.weiboPerson.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.project.weibo.weiboPerson.PersonUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.selector.Json;

import java.nio.charset.Charset;

@Component
public class WeiboPersonDownloader implements Downloader {

    private static Logger logger = LoggerFactory.getLogger(WeiboPersonDownloader.class);

    @Override
    public Page download(Request request, Task task) {
        Page page = this.myDownloader(request, task);
        if (!task.getSite().getAcceptStatCode().contains(page.getStatusCode())) {
            page.setDownloadSuccess(false);
        }
        return page;
    }


    @Override
    public void setThread(int threadNum) {

    }

    private Page myDownloader(Request request, Task task) {
        Page page = Page.fail();
        page.setUrl(new Json(request.getUrl()));
        page.setRequest(request);


//        return getTmall(request,page);
//
        return get(request, task, page);

//        return loginWeibo(request, request.getUrl(), task, page);


    }


    private static Page getTmall(Request request, Page page) {

        PhantomJSDriver driver = PersonUtils.getPhantomJSDriver();
        driver.get(request.getUrl());

        String pageSource = driver.getPageSource();
        System.out.println(pageSource);

        driver.close();

        return page;
    }


    private static Page loginWeibo(Request request, String url, Task task, Page page) {
        try (CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidationHttpClient()) {
            HttpGet httpGet = getHttpGet(request.getUrl(), task);

            HttpParams params = new BasicHttpParams();
            params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
            httpGet.setParams(params);


            CloseableHttpResponse response;
            try {
                response = httpClient.execute(httpGet);
                Header[] allHeaders = response.getAllHeaders();
                for (Header header : allHeaders) {
                    String name = header.getName();
                    if ("Location".equalsIgnoreCase(name)) {
                        String value = header.getValue();
                        String newUrl = "https://weibo.com" + value + "?is_hot=1";
                        HttpGet newHttpGet = getHttpGet(newUrl, task);
                        response = httpClient.execute(newHttpGet);
                    }
                }

                int statusCode = response.getStatusLine().getStatusCode();
                String content = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));

                if (content.contains("content=\"text/html; charset=gb2312\"")) {
                    PhantomJSDriver driver = PersonUtils.getPhantomJSDriver();
                    driver.get(request.getUrl());

                    driver.findElementById("loginname").sendKeys("13295431080");
                    driver.findElementByName("password").sendKeys("lyp82nlf");
                    driver.findElementByClassName("W_btn_a btn_32px").click();

                    SessionId sessionId = driver.getSessionId();
                    String s = sessionId.toString();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    content = driver.getPageSource();
                    String currentUrl = driver.getCurrentUrl();
                    logger.info("获取的url" + currentUrl);
                    page.getRequest().putExtra("url", currentUrl);
                    driver.close();
                }

                page.setRawText(content);
                page.setStatusCode(statusCode);
                page.setDownloadSuccess(true);


                response.close();
                httpClient.close();
                return page;
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpGet.abort();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("======={加载页面异常}");
        }
        return page;
    }

    public static Proxy getListProxy() {

        String urlNameString = "http://proxyip.lavector.com/api/selectOne";
        String result = "";
        CloseableHttpClient httpClient;
        HttpGet request;
        CloseableHttpResponse response;
        try {
            // 根据地址获取请求
            request = new HttpGet(urlNameString);//这里发送get请求
            // 获取当前客户端对象
            httpClient = HttpClients.createDefault();
            // 通过请求对象获取响应对象
            response = httpClient.execute(request);
            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
            request.abort();
            httpClient.close();
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String data = JSON.parseObject(result).get("data").toString();

        JSONObject jsonObject = JSONObject.parseObject(data);
        String post = jsonObject.get("port").toString();
        String host = jsonObject.get("ip").toString();
        Proxy newProxy = new Proxy(host, Integer.parseInt(post));
        return newProxy;

    }

    public static HttpGet getHttpGet(String url, Task task) {
        HttpGet httpGet = new HttpGet(url);
        task.getSite().getHeaders().forEach(httpGet::addHeader);
//            String host = getListProxy().getHost();
//            int port = getListProxy().getPort();
        RequestConfig config = RequestConfig.custom()
//                    .setCookieSpec(CookieSpecs.STANDARD_STRICT)
//                    .setProxy(new HttpHost(host, port))
//                    .setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123))
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .build();
//            logger.info("host {}, port {}", host, port);
        httpGet.setConfig(config);
        return httpGet;
    }

    private static CloseableHttpClient getHttpclient() {
//        HttpClientBuilder builder =  HttpClients.custom();
//        builder.setProxy(DynamicProxyDownloader.httpHost);
//        CloseableHttpClient httpClient = builder.build();
        CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidationHttpClient();
        return httpClient;
    }

    public static Page get(Request request, Task task, Page page) {

        CloseableHttpClient httpClient = getHttpclient();

        try {
            String url = request.getUrl();
            String referer = request.getHeaders().get("Referer");
            if (StringUtils.isEmpty(referer)) {
                referer = task.getSite().getHeaders().get("Referer");
            }

            final HttpGet requestget = new HttpGet(url);
//            HttpPost requestget = new HttpPost(url);

            Site site = task.getSite();
            String cookie = request.getHeaders().get("Cookie");
            if (StringUtils.isEmpty(cookie)) {
                cookie = site.getHeaders().get("Cookie");
            }
            requestget.addHeader("Cookie", cookie);

//            HttpParams params = new BasicHttpParams();
//            params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
//            requestget.setParams(params);

            requestget.addHeader("User-Agent", task.getSite().getHeaders().get("User-Agent"));
            requestget.addHeader("Referer", referer);

            String proxy = site.getHeaders().get("Proxy-Authorization");
            requestget.addHeader("Proxy-Authorization", proxy);
//            String proxy = site.getHeaders().get("Proxy-Authorization");
//            requestget.addHeader("Proxy-Authorization", proxy);


//            requestget.addHeader(":path","/search_product.htm?spm=a220m.1000858.1000724.4.544448dfxszm0U&cat=56240006&sort=d&style=g&search_condition=7&from=sn_1_cat-tmhk&industryCatId=56252006&smAreaId=110100");
//            requestget.addHeader(":authority","list.tmall.com");
//            requestget.addHeader(":method","GET");
//            requestget.addHeader(":scheme","https");
//            requestget.addHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
//            requestget.addHeader("sec-fetch-mode","navigate");
//            requestget.addHeader("sec-fetch-site","same-origin");
//            requestget.addHeader("sec-fetch-user","?1");
//            requestget.addHeader("upgrade-insecure-requests","1");
//            requestget.addHeader("accept-language","zh-CN,zh;q=0.9");
//            requestget.addHeader("accept-encoding","gzip, deflate, br");


            CloseableHttpResponse response = httpClient.execute(requestget);
            int responseCode = response.getStatusLine().getStatusCode();
            logger.info("-----状态值:" + responseCode);
//            if (responseCode == 302) {
//                return page;
//            }

//            if (responseCode != 200) {
//                String path = "G:/text/newWeibo/star/爬取失败.txt";
//                String name = request.getExtra("name").toString();
//                try {
//                    if (!new File(path).exists()) {
//                        boolean newFile = new File(path).createNewFile();
//                        if (newFile) {
//                            FileUtils.writeStringToFile(new File(path), "\n" + name + "," + page.getUrl().get(), Charset.forName("UTF-8"), true);
//                        }
//                    }
//                    FileUtils.writeStringToFile(new File(path), "\n" + name + "," + page.getUrl().get(), Charset.forName("UTF-8"), true);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }


            logger.info("-----url:{}", url);
//            Header[] allHeaders = response.getAllHeaders();
//            for (Header header : allHeaders) {
//                String name = header.getName();
//                if ("Location".equalsIgnoreCase(name)) {
//                    CloseableHttpClient httpClient1 = getHttpclient();
//                    String value = header.getValue();
//                    logger.info("获取的Location:{}", value);
//                    String newUrl = "https://weibo.com" + value;
//                    logger.info("获取的链接:{}", newUrl);
//                    HttpGet newHttpGet = getHttpGet(newUrl, task);
//                    response = httpClient1.execute(newHttpGet);
//                }
//            }
            int statusCode = response.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));

            page.setRawText(content);
            page.setStatusCode(statusCode);
            page.setDownloadSuccess(true);
            httpClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }


}
