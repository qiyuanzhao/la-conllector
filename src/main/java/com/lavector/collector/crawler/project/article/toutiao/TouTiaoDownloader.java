package com.lavector.collector.crawler.project.article.toutiao;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created on 2017/12/25.
 *
 * @author zeng.zhao
 */
public class TouTiaoDownloader extends DynamicProxyDownloader implements Downloader {

    private CloseableHttpClient httpClient;

    private Logger logger = LoggerFactory.getLogger(TouTiaoDownloader.class);

    public TouTiaoDownloader() {
        init();
    }

    private HttpGet getHttpRequest (String url, Site site) {

        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setRedirectsEnabled(false);
        builder.setConnectTimeout(site.getTimeOut());
        builder.setCookieSpec(CookieSpecs.DEFAULT);
        builder.setConnectionRequestTimeout(site.getTimeOut());
        builder.setSocketTimeout(site.getTimeOut());
        HttpGet httpRequest = new HttpGet(url);
        httpRequest.setConfig(builder.build());
        return httpRequest;
    }

    @Override
    public Page download(Request request, Task task) {
        String url = request.getUrl();
//        url = url.replace("http://", "https://");
        if (url.contains("comsslocal")) {
            return Page.fail();
        }
        Site site = task.getSite();
        sleep(site.getSleepTime());
        HttpGet httpGet = getHttpRequest(url, site);
        httpGet.addHeader("Proxy-Authorization", getAuthHeader());
        int user_agent_index = (int) (Math.random() * (USER_AGENTS.length));
        httpGet.addHeader("user-agent", USER_AGENTS[user_agent_index]);

        CloseableHttpResponse httpResponse = null;
        int statusCode = 0;
        try {
            httpResponse = httpClient.execute(httpGet);
            String content = EntityUtils.toString(httpResponse.getEntity());
            statusCode = httpResponse.getStatusLine().getStatusCode();
            if ((statusCode >= 200 && statusCode < 300)) {
                logger.info("downloader successful! {}", url);

                return createSuccessPage(content, url, request);
            } else if (statusCode >= 300 && statusCode < 400) {
                Header header = httpResponse.getFirstHeader("Location");
                String locationUrl = header.getValue();
                HttpGet httpGet1 = getHttpRequest(locationUrl, site);
                httpGet1.addHeader("Proxy-Authorization", getAuthHeader());
                int user_agent_index1 = (int) (Math.random() * (USER_AGENTS.length));
                httpGet1.addHeader("user-agent", USER_AGENTS[user_agent_index1]);
                CloseableHttpResponse httpResponse1 = httpClient.execute(httpGet1);
                int statusCode1 = httpResponse1.getStatusLine().getStatusCode();
                if (statusCode1 >= 200 && 300 > statusCode1) {
                    String content1 = EntityUtils.toString(httpResponse1.getEntity());
                    logger.info("downloader successful! {}", locationUrl);
                    EntityUtils.consumeQuietly(httpResponse1.getEntity());
                    return createSuccessPage(content1, locationUrl, request);
                } else if (statusCode1 >= 300 && 400 > statusCode1) {
                    Header header1 = httpResponse1.getFirstHeader("Location");
                    String locationUrl2 = header1.getValue();
                    HttpGet httpGet2 = new HttpGet(locationUrl2);
                    httpGet2.addHeader("Proxy-Authorization", getAuthHeader());
                    int user_agent_index2 = (int) (Math.random() * (USER_AGENTS.length));
                    httpGet2.addHeader("user-agent", USER_AGENTS[user_agent_index2]);
                    CloseableHttpResponse httpResponse2 = httpClient.execute(httpGet2);
                    int statusCode2 = httpResponse2.getStatusLine().getStatusCode();
                    if (statusCode2 >= 200 && 300 > statusCode2) {
                        String content2 = EntityUtils.toString(httpResponse2.getEntity());
                        logger.info("downloader successful! {}", locationUrl2);
                        EntityUtils.consumeQuietly(httpResponse2.getEntity());

                        return createSuccessPage(content2, locationUrl2, request);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("downloading page error : {}", e.getMessage());
            return Page.fail();
        } finally {
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }
        logger.warn("状态异常 === {}, ** {}", statusCode, url);
        sleep(site.getSleepTime());
        return Page.fail();
    }

    private Page createSuccessPage(String content, String url, Request request) {
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new Json(url));
        request.setUrl(url);
        page.setRequest(request);
        page.setStatusCode(200);
        return page;
    }

    private void init () {
        HttpClientBuilder builder = HttpClients.custom();
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", buildSSLConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(100);


        SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
        socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
        socketConfigBuilder.setSoTimeout(15 * 1000);
        SocketConfig socketConfig = socketConfigBuilder.build();
        connectionManager.setDefaultSocketConfig(socketConfig);


//        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(site.getRetryTimes(), true));
        builder.setDefaultSocketConfig(socketConfig);
        builder.setConnectionManager(connectionManager);
        builder.setDefaultCookieStore(new BasicCookieStore());
        builder.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        builder.setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123));
        try {
            builder.setSSLSocketFactory(new SSLConnectionSocketFactory(createIgnoreVerifySSL(), NoopHostnameVerifier.INSTANCE));
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        builder.setDefaultRequestConfig(RequestConfig.custom().setRedirectsEnabled(false).build());
        this.httpClient = builder.build();
    }


    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
        try {
            return new SSLConnectionSocketFactory(createIgnoreVerifySSL()); // 优先绕过安全证书
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            logger.error("ssl connection fail", e);
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }


    @Override
    public void setThread(int threadNum) {

    }

    public static void main (String[] args) throws Exception {
        String url = "https://www.toutiao.com/group/6438606024885895426/";
        TouTiaoDownloader downloader = new TouTiaoDownloader();
        PageProcessor pageProcessor = new PageProcessor() {
            Site site = Site.me();
            @Override
            public void process(Page page) {

            }

            @Override
            public Site getSite() {
                return site;
            }
        };
        downloader.download(new Request(url), new Spider(pageProcessor));
    }
}
