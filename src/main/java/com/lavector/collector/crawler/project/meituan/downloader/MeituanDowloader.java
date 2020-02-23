package com.lavector.collector.crawler.project.meituan.downloader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.project.weibo.weiboPerson.PersonUtils;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.selector.Json;

import java.nio.charset.Charset;

/**
 * Created by qyz on 2019/8/30.
 */
public class MeituanDowloader implements Downloader {

    private static Logger logger = LoggerFactory.getLogger(SougouWeixinDownloader.class);


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


        return get(request, task, page);
//        return loginWeibo(request, request.getUrl(), task, page);


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
//            final HttpGet requestget = new HttpGet(url);
            HttpPost requestget = new HttpPost(url);

            Site site = task.getSite();
            String cookie = site.getHeaders().get("Cookie");
            requestget.addHeader("Cookie", cookie);

            HttpParams params = new BasicHttpParams();
            params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
            requestget.setParams(params);

//            if ( RegexUtil.isMatch("https://i.waimai.meituan.com/openh5/order/myuncompleteorder.*", request.getUrl())){
            requestget.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
//            }
//            String proxy = site.getHeaders().get("Proxy-Authorization");
            requestget.addHeader("User-Agent", task.getSite().getHeaders().get("User-Agent"));
            requestget.addHeader("Accept-Language", task.getSite().getHeaders().get("Accept-Language"));
            requestget.addHeader("Referer", referer);
            requestget.addHeader("Origin", "https://h5.waimai.meituan.com");
            requestget.addHeader("Sec-Fetch-Mode", "cors");
//            requestget.addHeader("Proxy-Authorization", proxy);


            CloseableHttpResponse response = httpClient.execute(requestget);
            int responseCode = response.getStatusLine().getStatusCode();
            logger.info("-----状态值:" + responseCode);
//            if (responseCode == 302) {
//                return page;
//            }
            logger.info("-----url:{}", url);
            Header[] allHeaders = response.getAllHeaders();
            for (Header header : allHeaders) {
                String name = header.getName();
                if ("Location".equalsIgnoreCase(name)) {
                    CloseableHttpClient httpClient1 = getHttpclient();
                    String value = header.getValue();
                    logger.info("获取的Location:{}", value);
                    String newUrl = /*"https://weibo.com" +*/ value;
                    logger.info("获取的链接:{}", newUrl);
                    HttpGet newHttpGet = getHttpGet(newUrl, task);
                    response = httpClient1.execute(newHttpGet);
                }
            }
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
