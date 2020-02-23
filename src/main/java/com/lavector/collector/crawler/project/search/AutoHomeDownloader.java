package com.lavector.collector.crawler.project.search;

import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/11/30.
 *
 * @author zeng.zhao
 */
public class AutoHomeDownloader extends DynamicProxyDownloader implements Downloader {

    private Logger logger = LoggerFactory.getLogger(AutoHomeDownloader.class);

    private HttpClient httpClient;

    public AutoHomeDownloader() {
        init();
    }

    private void init() {
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setRedirectsEnabled(false).build();
//        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
//        poolingHttpClientConnectionManager.setMaxTotal(100);
        httpClient = ignoreValidating()
//                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Override
    public void setThread(int threadNum) {

    }

    @Override
    public Page download(Request request, Task task) {
        request.setUrl(request.getUrl().replace("http://", "https://"));
        Site site = task.getSite();
        sleep(site.getSleepTime());
        HttpGet httpRequestBase = new HttpGet(request.getUrl());
        RequestConfig.Builder custom = RequestConfig.custom();
        if (request.getUrl().contains("thread") || request.getUrl().contains("forum")) {
            for (Map.Entry<String, String> header : site.getHeaders().entrySet()) {
                httpRequestBase.addHeader(header.getKey(), header.getValue());
            }
        }


        RequestConfig requestConfig = custom.setConnectTimeout(site.getTimeOut())
                .setSocketTimeout(site.getTimeOut())
                .setConnectionRequestTimeout(site.getTimeOut())
                .build();
        httpRequestBase.setConfig(requestConfig);
        httpRequestBase.addHeader("Proxy-Authorization", getAuthHeader());

        try {
            HttpResponse response = httpClient.execute(httpRequestBase);
            int status = response.getStatusLine().getStatusCode();
            String rawText = IOUtils.toString(response.getEntity().getContent(), site.getCharset());
            httpRequestBase.releaseConnection();
            if (status >= 200 && status < 300) {
                logger.info("downloader successful! {}", request.getUrl());
                return createPage(request, rawText, status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("错误页面: " + request.getUrl());
        }

        logger.warn("失败页面： {}", request.getUrl());
        return Page.fail();
    }

    private Page createPage (Request request, String rawText, int status) {
        Page page = new Page();
        page.setSkip(true);
        page.setUrl(new Json(request.getUrl()));
        page.setRawText(rawText);
        page.setRequest(request);
        page.setDownloadSuccess(true);
        page.setStatusCode(status);
        return page;
    }


    public static void main(String[] args) throws Exception {
//        String url = "https://www.ipip.net/";
//        AutoHomeDownloader downloader = new AutoHomeDownloader();
//        CloseableHttpClient httpClient = downloader.ignoreValidationHttpClient();
//        for (int i = 0; i < 10; i++) {
//            HttpGet httpGet = new HttpGet(url);
//            httpGet.addHeader("Proxy-Authorization", downloader.getAuthHeader());
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//            String content = EntityUtils.toString(response.getEntity());
//            String text = new Html(content).xpath("//div[@class='ip_text']/text()").get();
//            String origin = new Html(content).xpath("//div[@class='area']/text()").get();
//            System.out.println(text + " ::: " + origin);
//            TimeUnit.SECONDS.sleep(1);
//        }

        autohomeTest();
    }

    public static void autohomeTest () {
        String url = "https://www.ipip.net/";
        DynamicProxyDownloader dynamicProxyDownloader = new  DynamicProxyDownloader();
        Spider spider = Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {

            }

            @Override
            public Site getSite() {
                return Site.me()
                        .setTimeOut(15 * 1000)
                        .addHeader("referer", "http://www.ipip.net/")
                        .addHeader("cookie", "__jsluid=8ee876190f07e39216a72ff49b85b7fd; _ga=GA1.2.958619929.1533623111; __jsl_clearance=1535510149.091|0|D1fTBEzNNjvusuIW0gmmBUPKEj4%3D; LOVEAPP_SESSID=5cc5cb8ba408f126f94a4d43e47803a7fccd3616; _gid=GA1.2.336646624.1535510152")
                        .addHeader("host", "www.ipip.net")
                        .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")
                        .addHeader("Proxy-Authorization", dynamicProxyDownloader.getAuthHeader());
            }
        });
        Request request = new Request(url);
        ProxyProvider proxyProvider = SimpleProxyProvider.from(new Proxy("s2.proxy.mayidaili.com", 8123));
        HttpClientDownloader downloader = new HttpClientDownloader();
        downloader.setProxyProvider(proxyProvider);
        for (int i = 0; i < 10; i++) {
            Page page = downloader.download(request, spider);
            Html html = page.getHtml();
            String ip = html.xpath("/html/body/div[2]/div/ul/li[1]/text()").get();
            String address = html.xpath("/html/body/div[2]/div/ul/li[2]/text()").get();
            System.out.println(ip + "===" + address);
        }
    }
}
