package com.lavector.collector.crawler.nonce.marykay;

import com.google.common.io.FileWriteMode;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.selector.Html;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 30/05/2018.
 *
 * @author zeng.zhao
 */
public class MaryKayProcessor {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        process();
    }

    private static void process() throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/玫琳凯URL.txt"));
        List<String> urls = new ArrayList<>();
        String line = reader.readLine();
        do {
            urls.add(line.trim());
            line = reader.readLine();
        } while (line != null);

        AtomicInteger count = new AtomicInteger(0);
        urls.forEach(url -> {
            executorService.execute(() -> {
                Html html = getHtml(url);
                List<String> contents = html.xpath("//div[@class='article-content']/p/allText()").all();
                if (contents == null || contents.size() == 0) {
                    System.out.println();
                }
                String content = String.join("", contents);
                List<String> images = html.xpath("//div[@class='article-content']/p/img/@src").all();
                String image = String.join(" ", images);
                String text = url + "," + content + "," + image;
                write(text);
                System.out.println("success." + count.incrementAndGet());
            });
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

//        errorUrls.forEach(errorUrl -> {
//            Html html = getHtml(errorUrl);
//            List<String> contents = html.xpath("//div[@class='article-content']/p/allText()").all();
//            String content = String.join("", contents);
//            if (StringUtils.isBlank(content)) {
//                System.out.println("error : " + errorUrl);
//            }
//            List<String> images = html.xpath("//div[@class='article-content']/p/img/@src").all();
//            String image = String.join(" ", images);
//            String text = errorUrl + "," + content + "," + image;
//            write(text);
//            System.out.println("success." + count.incrementAndGet());
//        });
    }

    public static Html getHtml(String url) {
        String body = null;
        try (CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidating().build()) {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            get.setConfig(RequestConfig.custom().setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123)).build());
            get.addHeader("accept-encoding", "");
            get.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
            get.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
            try (CloseableHttpResponse response = httpClient.execute(get)) {
                body = IOUtils.toString(response.getEntity().getContent(), Charset.forName("utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(body)) {
            System.out.println("error : " + url);
            try {
                com.google.common.io.Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/marykay-error.txt").toFile(),
                        StandardCharsets.UTF_8, FileWriteMode.APPEND)
                        .write(url + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Html(body);
    }

    private static synchronized void write(String text) {
        try {
            com.google.common.io.Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/marykay.csv").toFile(),
                    StandardCharsets.UTF_8, FileWriteMode.APPEND)
                    .write(text + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
