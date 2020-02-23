package com.lavector.collector.crawler.nonce;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created on 18/05/2018.
 *
 * @author zeng.zhao
 */
public class NonceDownloader implements Downloader {

    @Override
    public Page download(Request request, Task task) {
        Page page = Page.fail();
        page.setUrl(new Json(request.getUrl()));
        page.setRequest(request);
        try (CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidating().build()) {
            HttpGet httpGet = new HttpGet(request.getUrl());
            RequestConfig config = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123))
                    .build();
            httpGet.setConfig(config);
            httpGet.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
            httpGet.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String content = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
                int statusCode = response.getStatusLine().getStatusCode();
                page.setRawText(content);
                page.setStatusCode(statusCode);
                page.setDownloadSuccess(true);
                return page;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }
}
