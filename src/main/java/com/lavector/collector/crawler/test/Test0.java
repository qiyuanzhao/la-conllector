package com.lavector.collector.crawler.test;

import com.google.common.io.Files;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2017/11/22.
 *
 * @author zeng.zhao
 */
public class Test0 extends DynamicProxyDownloader implements Runnable {

    private AtomicInteger count = new AtomicInteger();

    public int getCount() {
        return this.count.get();
    }

    public void getAndIncrement() {
        count.getAndIncrement();
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            getAndIncrement();
        }
    }


    private static int getContent(String url) throws Exception {
        return Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                .execute()
                .returnResponse()
                .getStatusLine()
                .getStatusCode();
    }

    private static void tianyaTest() throws Exception {
        String url = "http://bbs.tianya.cn/post-news-370581-1.shtml";
        System.out.println(getContent(url));
    }

    private static void mopTest() throws Exception {
        String url = "http://tt.mop.com/a/171206093649685339325.html";
        System.out.println(getContent(url));
    }

    private static void huxiuTest() throws Exception {
        String url = "https://www.huxiu.com/article/224813.html";
        System.out.println(getContent(url));
    }

    private void dianpingTest() throws Exception {
        String url = "http://www.dianping.com/shop/21040337";
        HttpClient httpClient = HttpClients.custom()
//                .setProxy(httpHost)
                .build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setRedirectsEnabled(true)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
//        httpGet.addHeader("Proxy-Authorization", getAuthHeader());
//        httpGet.addHeader("referer", "http://www.dianping.com/shop/21040337");
        httpGet.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
//        httpGet.addHeader("cookie", "_lxsdk_cuid=15efff6c2e0c8-0872794bcbcba2-31657c00-13c680-15efff6c2e0c8; _lxsdk=15efff6c2e0c8-0872794bcbcba2-31657c00-13c680-15efff6c2e0c8; _hc.v=\"\\\"62d8bc4f-716b-4961-853e-433c0e313d73.1507532915\\\"\"; __utma=1.2053760143.1509960774.1509960774.1509960774.1; __utmz=1.1509960774.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); cityid=2; switchcityflashtoast=1; s_ViewType=10; aburl=1; cye=guangzhou; m_flash2=1; source=m_browser_test_22; pvhistory=\"6aaW6aG1Pjo8Lz46PDE1MTM1ODQxODQ4MDBdX1vov5Tlm54+Ojwvc3NuZXc/a2V5d29yZD1yb25neGlhbyZfPTE1MTM1ODQxOTE4MzkmY2FsbGJhY2s9WmVwdG8xNTEzNTg0MTg1Mzk4Pjo8MTUxMzU4NDE5MTgzOF1fWw==\"; default_ab=shop%3AA%3A1%7Cindex%3AA%3A1%7CshopList%3AA%3A1%7Cshopreviewlist%3AA%3A1%7Cshopdish%3AA%3A1%7Cdishlist%3AA%3A1%7Csinglereview%3AA%3A1; cy=2; _lxsdk_s=1606dc07088-492-efc-1d6%7C%7C66");
        HttpResponse response = httpClient.execute(httpGet);
        String content = EntityUtils.toString(response.getEntity());
        System.out.println(content);
    }


    private static void yandexTest() throws Exception {
        String url = "https://yandex.com/images/search?serpid=nS8Hby3C780wwZRCF4jovg&uinfo=sw-1440-sh-900-ww-1440-wh-345-pd-2-wp-16x10_2560x1600&rpt=imageview&format=json&request=%7B%22blocks%22%3A%5B%7B%22block%22%3A%22b-page_type_search-by-image__link%22%7D%5D%7D";
        File file = new File("/Users/zeng.zhao/Desktop/u=49366202,632101467&fm=27&gp=0.jpg");
        FileBody fileBody = new FileBody(file, ContentType.create("image/jpeg"));
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("upfile", fileBody)
                .build();

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        byte[] bytes = IOUtils.toByteArray(response.getEntity().getContent());
        String content = new String(bytes, "utf-8");
        System.out.println(content);
    }

    private static void yandexTest2() throws Exception {
        String url = "https://yandex.com/images/search?format=json&request=%7B%22blocks%22%3A%5B%7B%22block%22%3A%22i-global__params%3Aajax%22%2C%22params%22%3A%7B%7D%2C%22version%22%3A2%7D%2C%7B%22block%22%3A%22cookies_ajax%22%2C%22params%22%3A%7B%7D%2C%22version%22%3A2%7D%2C%7B%22block%22%3A%22cbir-intent__image-link%22%2C%22params%22%3A%7B%7D%2C%22version%22%3A2%7D%2C%7B%22block%22%3A%22content_type_search-by-image%22%2C%22params%22%3A%7B%7D%2C%22version%22%3A2%7D%2C%7B%22block%22%3A%22serp-controller%22%2C%22params%22%3A%7B%7D%2C%22version%22%3A2%7D%5D%2C%22bmt%22%3A%7B%22lb%22%3A%22f3glb%22%7D%2C%22amt%22%3A%7B%22las%22%3A%22%22%7D%7D&yu=3068679121514943597&cbir_id=1023282%2FQSN7-jtHLYlinydDAsNxSw&rpt=imageview&from=&uinfo=sw-1440-sh-900-ww-1440-wh-345-pd-2-wp-16x10_2560x1600&source-serpid=nS8Hby3C780wwZRCF4jovg";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        System.out.println(content);

    }


    private void xcfTest() throws Exception {
        String url = "http://brand.ppsj.com.cn/index117.html";
        HttpClient httpClient = ignoreValidating()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123))
//                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("proxy-authorization", getAuthHeader());
        for (int i = 0; i < 10000; i++) {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println(statusCode + "===" + i);
            EntityUtils.consumeQuietly(httpResponse.getEntity());
        }
    }


    private static void optionalTest() {
        String s = "342a";
        Integer integer = Optional.ofNullable(s)
                .map(s1 -> new Json(s).regex("\\d+").get())
                .map(Integer::parseInt)
                .orElse(0);

        String a = null;
        String b = Optional.ofNullable(s).orElse("0");
        System.out.println(b);
    }


    private static void videoTest() throws IOException {
        String url = "http://124.192.151.139/videos/v0/20140613/dd/1e/27/5e04cf4cf5d084a2feb23fc510e969f9.f4v?key=01838ad0446fc2d07b091c38602139563&dis_k=283daaa0523725e0537f58776579b1118&dis_t=1516675189&dis_dz=GWBN-BeiJing&dis_st=42&src=iqiyi.com&uuid=79454bc2-5a66a075-128&rn=1516675189332&id=713434962940&qd_ip=79454bc2&qd_vipdyn=0&qd_tm=1516675189702&qd_k=83aff7d50a2ae179ec0a18d2eb851b04&cross-domain=1&qd_uid=1383029387&qd_aid=123787500&qd_stert=0&qypid=123787500_01080031010000000000&qd_p=79454bc2&qd_src=01010031010000000000&qd_tvid=123787500&qd_index=1&qd_vip=1&pri_idc=beijing8_dxt&qyid=be5c085707468588ef930886d204bd6b&qd_vipres=0&pv=0.1";

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        httpGet.addHeader("host", "data.video.iqiyi.com");

        InputStream inputStream = httpClient.execute(httpGet).getEntity().getContent();

        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("/Users/zeng.zhao/Desktop/iqiyi.f4v"));
        byte[] by = new byte[1024];
        int read = bis.read(by);
        while (read != -1) {
            bos.write(by, 0, read);
            read = bis.read(by);
        }

        bos.close();
        bis.close();
    }


    static class ThreadTest {

        AtomicInteger integer = new AtomicInteger(0);


        void add() {
            System.out.println(integer.incrementAndGet());
        }

        void dec() {
            System.out.println(integer.decrementAndGet());
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++) {
            test00();
        }
    }

    private static void htmlunitTest() throws Exception {
        String content = Request.Get("https://dl.reg.163.com/webzj/m163_2/pub/index_dl.html?cd=https%3A%2F%2Fmimg.127.net&cf=%2Findex%2F163%2Fscripts%2F2017%2Fpc%2Fcss%2Furs_v1.css&wdaId=&pkid=CvViHzl&product=mail163")
                .addHeader("referer", "https://mail.163.com/")
                .execute()
                .returnContent()
                .asString(Charset.forName("utf-8"));
        System.out.println(content);

    }

    private static void tmallTest() throws Exception {
        String url = "https://list.tmall.com/search_product.htm?q=手机";
        DynamicProxyDownloader proxyDownloader = new DynamicProxyDownloader();
        CloseableHttpClient closeableHttpClient = proxyDownloader.ignoreValidationHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
        httpGet.addHeader("cookie", "enc=TCnnn%2BFNUslh9ftHim5twrPGO%2B%2BFBv9aDgCV%2F2T4tWd2s6tBilUQ327uM7APrmOvvPytHr4QC2SNpsDVw1shwQ%3D%3D; isg=BHJyoXhafFi3NkNCa4w-hnoKwb5UA3adqiA96zxLLCUTzxLJJ5PGrXgku-PzpO41; cq=ccp%3D0; pnm_cku822=098%23E1hvKQvUvbpvUvCkvvvvvjiPPFMysj1Wn2zvQjljPmPygjDPPLcvsj1RnL59ljt8RphvChCvvvvPvpvhvv2MMQhCvvXvppvvvvmtvpvIphvvcvvvphEvpCBAvvC2ChCvHUUvvhPjphvZ99vvpAtvpCBAvvC2bIyCvvXnLp9WeEkivpvUphvhkJ3aniyEvpCWpFZRv8R6NZDlpwoQRqwiL7CpqU0QKoZHaZMEDLuTWD19C7zUdigIvVDHD70fd56bb64B9C97Ectz8SoxdByaWox%2FAj7J%2B3%2BKjLVxf4wCvvBvpvpZ; res=scroll%3A1421*6238-client%3A1421*839-offset%3A1421*6238-screen%3A1440*900; tt=login.tmall.com; _l_g_=Ug%3D%3D; _nk_=%5Cu4F1A%5Cu98DE%5Cu7684%5Cu4E43%5Cu4F0A; _tb_token_=ee579ed67353f; cookie1=BxEwN7U6JB9JO6%2BoFRsibEBmuKwzkNYJLAkdkXxv3Po%3D; cookie17=UU269Z1KOpXG6g%3D%3D; cookie2=131fcca1ac44ddaafcb81e40e9fd6310; csg=4240bc8d; dnk=%E4%BC%9A%E9%A3%9E%E7%9A%84%E4%B9%83%E4%BC%8A; lgc=%5Cu4F1A%5Cu98DE%5Cu7684%5Cu4E43%5Cu4F0A; lid=%E4%BC%9A%E9%A3%9E%E7%9A%84%E4%B9%83%E4%BC%8A; login=true; sg=%E4%BC%8A45; t=6d3de88bc982a19ec5299e34c38e4b1b; tracknick=%5Cu4F1A%5Cu98DE%5Cu7684%5Cu4E43%5Cu4F0A; uc1=cookie14=UoTePTwaJPBecg%3D%3D&lng=zh_CN&cookie16=UIHiLt3xCS3yM2h4eKHS9lpEOw%3D%3D&existShop=false&cookie21=VFC%2FuZ9aiKCaj7AzMHh1&tag=8&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D&pas=0; uc3=nk2=2Dms%2FkGeTDPcWw%3D%3D&id2=UU269Z1KOpXG6g%3D%3D&vt3=F8dBz4D93qWO%2FMnZEic%3D&lg2=V32FPkk%2Fw0dUvg%3D%3D; unb=2589486804; cna=N/V5EjnjW0sCAXaQhSStz1B+; _med=dw:1920&dh:1080&pw:1920&ph:1080&ist:0");
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
        String content = EntityUtils.toString(response.getEntity());
        Selectable xpath = new Html(content).xpath("//div[@id='J_ItemList']");
        System.out.println(xpath);
        EntityUtils.consumeQuietly(response.getEntity());
    }

    private static void imageDown() {
        String url = "https://upload.chinaz.com/2018/0410/201804101108398223.jpg";
        try {
            InputStream inputStream = Request.Get(url)
                    .addHeader("", "")
                    .execute().returnContent().asStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            byte[] by = new byte[102400];
            inputStream.read(by);
//            Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/image.jpg").toFile(), StandardCharsets.UTF_8)
//                    .write(s);
            Files.write(by, Paths.get("/Users/zeng.zhao/Desktop/image.jpg").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test00() throws Exception {
//        String url = "http://www.huangye88.com/zuixingongsi/";
        String url = "http://b2b.huangye88.com/qiye2638148/";
        int content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36")
                .addHeader("referer", "")
                .execute()
                .returnResponse()
                .getStatusLine().getStatusCode();
        System.out.println(content);
    }
}