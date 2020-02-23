package com.lavector.collector.crawler.nonce;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created on 18/05/2018.
 *
 * @author zeng.zhao
 */
public class CookieTest {
    public static void main(String[] args) throws Exception {
        test3();
    }

    private static void test1() throws Exception {
        String url = "http://huanbao.bjx.com.cn/news/20180518/898780.shtml";
        String content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                .execute()
                .returnContent()
                .asString(Charset.forName("gbk"));
        System.out.println(content);
    }

    private static void test2() throws Exception {
        HttpClientContext context = new HttpClientContext();
        context.setTargetHost(new HttpHost("www.dianping.com"));
        CookieStore cookieStore = new BasicCookieStore();
        context.setCookieStore(cookieStore);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet get = new HttpGet("http://www.dianping.com/");
        httpClient.execute(get, context);
        List<Cookie> cookies = cookieStore.getCookies();
        System.out.println(cookies);
    }

    private static void test3() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 15, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), r -> {
                    Thread t = new Thread(r);
                    t.setName("test");
                    return t;
                },
                (r, executor) -> {
                    executor.shutdown();
                });

        Future<?> future = threadPoolExecutor.submit(() -> System.out.println("haha"));
        future.cancel(false);
        System.out.println("sasasa");
    }
}
