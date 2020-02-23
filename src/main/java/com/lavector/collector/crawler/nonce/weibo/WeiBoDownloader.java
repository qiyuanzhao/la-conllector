package com.lavector.collector.crawler.nonce.weibo;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import org.apache.http.HttpHost;
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
import java.util.concurrent.TimeUnit;

/**
 * Created on 18/05/2018.
 *
 * @author zeng.zhao
 */
public class WeiBoDownloader implements Downloader {

    @Override
    public Page download(Request request, Task task) {
        Page page = myDown(request);
        if (page.getStatusCode() != 200) {
            page.setDownloadSuccess(false);
        }
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }

    private Page myDown(Request request) {
        Page page = Page.fail();
        page.setUrl(new Json(request.getUrl()));
        page.setRequest(request);
        try (CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidating().build()) {
            HttpGet httpGet = new HttpGet(request.getUrl());
            RequestConfig config = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                    .setConnectTimeout(30 * 1000)
                    .setSocketTimeout(30 * 1000)
                    .setConnectionRequestTimeout(30 * 1000)
                    .setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123))
                    .build();
            httpGet.setConfig(config);
            httpGet.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
            httpGet.addHeader("cookie", "SINAGLOBAL=638387246136.2206.1527731616813; un=13241300169; UOR=,,login.sina.com.cn; YF-Ugrow-G0=9642b0b34b4c0d569ed7a372f8823a8e; login_sid_t=84450a33f72deef42e6a6347ca8a2b29; cross_origin_proto=SSL; YF-V5-G0=69afb7c26160eb8b724e8855d7b705c6; WBStorage=e8781eb7dee3fd7f|undefined; wb_view_log=1440*9002; _s_tentry=passport.weibo.com; Apache=9927662342238.775.1540173251910; ULV=1540173251915:64:11:1:9927662342238.775.1540173251910:1539914700449; SCF=AvXliNK2E1oyuXpLHOqFJwmipzSDe8bW4H38H0zcNHJ4XD5WMD8U4Cpa1pNq7dW-_gKSlD7FAIzUm14J1Rnfr9A.; SUB=_2A252yV4RDeRhGeNG71AS8C3NyzSIHXVVv8jZrDV8PUNbmtANLXmkkW9NS0I5FEHabMlDLSK9VOLf6tqAhpJcPLxn; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFnzPQIAGZpce2AE4Ms3o7Q5JpX5KzhUgL.Fo-RShz0ehepehn2dJLoIEXLxK-LBKBLB-2LxKMLBK.LB.2LxKML1-2L1hBLxKqLBo5LBKMLxKqLBo5L1KBt; SUHB=0ebdEFe7VIEDbf; ALF=1571709377; SSOLoginState=1540173377; wvr=6; YF-Page-G0=061259b1b44eca44c2f66c85297e2f50; wb_view_log_5842303108=1440*9002");
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
        } finally {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return page;
    }
}
