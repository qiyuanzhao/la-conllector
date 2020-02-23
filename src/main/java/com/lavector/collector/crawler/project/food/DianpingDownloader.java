package com.lavector.collector.crawler.project.food;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.selector.Json;

import javax.net.ssl.SSLContext;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/11/7.
 *
 * @author zeng.zhao
 */
public class DianpingDownloader extends DynamicProxyDownloader {

    private Logger logger = LoggerFactory.getLogger(DianpingDownloader.class);

    private HttpClient httpClient;

    public DianpingDownloader() {
        try {
            this.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        SSLContext ignoreVerifySSL = createIgnoreVerifySSL();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", new SSLConnectionSocketFactory(ignoreVerifySSL))
                .build();
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(registry);
        manager.setDefaultMaxPerRoute(100);
        manager.setMaxTotal(100);

        httpClient = ignoreValidating()
                .setMaxConnTotal(100)
                .setMaxConnPerRoute(100)
                .setConnectionTimeToLive(10, TimeUnit.SECONDS)
                .setConnectionManager(manager)
                .setProxy(httpHost)
                .build();
    }

//    @Override
    public Page download(Request request, Task task) {
        Site site = task.getSite();
        sleep(site.getSleepTime());
        if (StringUtils.equals(site.getDomain(), "autohome")) {
            request.setUrl(request.getUrl().replace("http://", "https://"));
        }
        HttpGet httpRequestBase = new HttpGet(request.getUrl());
        RequestConfig.Builder custom = RequestConfig.custom();
        if (StringUtils.equals(site.getDomain(), "autohome")) {
            if (request.getUrl().contains("thread") || request.getUrl().contains("forum")) {
                custom.setRedirectsEnabled(false);
                for (Map.Entry<String, String> header : site.getHeaders().entrySet()) {
                    httpRequestBase.addHeader(header.getKey(), header.getValue());
                }
            }
        } else {
            for (Map.Entry<String, String> header : site.getHeaders().entrySet()) {
                httpRequestBase.addHeader(header.getKey(), header.getValue());
            }
        }

        int user_agent_index = (int) (Math.random() * (USER_AGENTS.length - 1));
        httpRequestBase.addHeader("user-agent", USER_AGENTS[user_agent_index]);

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
            if (status >= 200 && status < 300) {
                logger.info("downloader successful! {}", request.getUrl());
                return createPage(request, rawText, status);
            } else {
                logger.info("状态异常 ： " + status + "====" + request.getUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("错误页面: " + request.getUrl());
        } finally {
            httpRequestBase.releaseConnection();
            httpRequestBase.abort();
        }

        logger.warn("失败页面： {}", request.getUrl());
        return Page.fail();
    }

    private Page createPage(Request request, String content, int statusCode) {
        Page page = new Page();
        page.setStatusCode(statusCode);
        page.setRawText(content);
        page.setRequest(request);
        page.setSkip(true);
        page.setUrl(new Json(request.getUrl()));
        return page;
    }
}
