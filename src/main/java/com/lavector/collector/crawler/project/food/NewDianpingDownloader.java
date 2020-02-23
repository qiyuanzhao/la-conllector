package com.lavector.collector.crawler.project.food;

import com.google.common.io.Files;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/1/18.
 *
 * @author zeng.zhao
 */
public class NewDianpingDownloader extends HttpClientDownloader {

    private Logger logger = LoggerFactory.getLogger(NewDianpingDownloader.class);

    private CookieStore cookieStore = new BasicCookieStore();

    @Override
    public Page download(Request request, Task task) {
        Site site = task.getSite();
        Page page = this.myDownloader(request, task);
        if (page.isDownloadSuccess() && !site.getAcceptStatCode().contains(page.getStatusCode())) {
            page.setDownloadSuccess(false);
            logger.error("status code error : {}, url : {}", page.getStatusCode(), request.getUrl());
//            WriteFile.write(page.getUrl().get(), "/Users/zeng.zhao/Desktop/Error_Users.txt");
            return page;
        }
        if (!page.isDownloadSuccess() || page.getRawText().contains("页面不存在")) {
            logger.error("error id write. ");
            page.setDownloadSuccess(false);
//            WriteFile.write(page.getUrl().get(), "/Users/zeng.zhao/Desktop/Error_Users.txt");
            return page;
        }
        return page;
    }

    private Page myDownloader(Request request, Task task) {
        Page page = Page.fail();
        page.setUrl(new Json(request.getUrl()));
        String content = "";
        int statusCode = 200;
        int count = 0;
        do {
            try (CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidating()
                    .build()) {
                HttpGet httpGet = new HttpGet(request.getUrl());
                RequestConfig config = RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                        .setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123))
                        .build();
                httpGet.setConfig(config);
                httpGet.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
                httpGet.addHeader("Cookie", "_lxsdk_s=165a31ed52f-1f3-fcc-658%7C%7C" + random(3) + "; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; cy=5; cye=nanjing; ll=7fd06e815b796be3df069dec7836c3df; dper=51591cc5506f2801e090062a9c7d3ae6f6d489f372ac59fc64befbb32f385041ca297ea261addcfcfeea35fccd9b30ac414f7564d21bf268295cca48321eafd48b8614009590f623cf8ed5fb91a095782d3f34a6528f2fd91024b9f8b36a88c8; ua=dpuser_2956140119; uamo=15510083108; s_ViewType=10; ctu=d1123a22f332317e09ff2123f7139509e160ffde0840b5b93b192bf85817baf3; _hc.v=a26bc646-667c-a1cb-376a-9a054c963018.1533174515; _lxsdk=164f85283cac8-02407009539b4a-1b451e24-13c680-164f85283cec8; _lxsdk_cuid=164f85283cac8-02407009539b4a-1b451e24-13c680-164f85283cec8");
                httpGet.addHeader("Referer", request.getExtra("referer").toString());
                httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/604.3.5 (KHTML, like Gecko) Version/11.0.1 Safari/604.3.5");
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    content = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
//                    content = IOUtils.toString(new GzipDecompressingEntity(response.getEntity()).getContent(), "utf-8");
                    statusCode = response.getStatusLine().getStatusCode();

                }
                TimeUnit.MILLISECONDS.sleep(task.getSite().getSleepTime());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            count++;

        } while ((content.contains("验证中心") || statusCode != 200 || StringUtils.isBlank(content)) && count < 10);
        if (count == 10) {
            statusCode = 403;
            logger.info(request.getUrl() + " 下载失败，一直处于验证中心.");
        }
        page.setRequest(request);
        page.setRawText(content);
        page.setStatusCode(statusCode);
        page.setDownloadSuccess(true);
        return page;
    }

//    private synchronized void getCookie() {
//        System.setProperty("webdriver.chrome.driver", "/Users/zeng.zhao/Develop/chromedriver");
//        ChromeDriver driver = new ChromeDriver();
//        driver.get("http://www.dianping.com");
//        Set<Cookie> cookies = driver.manage().getCookies();
//        StringBuilder stringBuilder = new StringBuilder();
//        cookies.forEach(cookie -> {
//            stringBuilder.append(cookie.getName())
//                    .append("=")
//                    .append(cookie.getValue())
//                    .append("; ");
//        });
//
//        try {
//            Files.asCharSink(Paths.get(System.getProperty("user.dir") + "/dianping_cookie.txt").toFile(), StandardCharsets.UTF_8)
//                    .write(stringBuilder.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        driver.close();
//    }

    private String randomCookie() {
        Random random = new Random();
        String[] cookiePool = new String[2];
        cookiePool[0] = "_lxsdk_s=1658449f3c4-a22-0d3-ad1%7C%7C" + random(3) + "; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; cy=5; cye=nanjing; ll=7fd06e815b796be3df069dec7836c3df; s_ViewType=10; dper=51591cc5506f2801e090062a9c7d3ae6cfc50d6a95ef15e898784fe4196996fde112345edfb2b5a5c5ea0d4ebf0a98620d24aac632ee8113d680cbc292b014647d2725924484960947b137c83bc0ac737f68a126de9d5f3fa7dc1c6de778c8c9; ua=dpuser_2956140119; uamo=15510083108; ctu=d1123a22f332317e09ff2123f7139509e160ffde0840b5b93b192bf85817baf3; _hc.v=a26bc646-667c-a1cb-376a-9a054c963018.1533174515; _lxsdk=164f85283cac8-02407009539b4a-1b451e24-13c680-164f85283cec8; _lxsdk_cuid=164f85283cac8-02407009539b4a-1b451e24-13c680-164f85283cec8";
        cookiePool[1] = "__mta=40663837.1532076998643.1535104734851.1535105338396.43; _hc.v=167d0c4a-9c97-40e6-ddfe-ee76b4910fb4.1528078742; _lxsdk_cuid=163c897342c28-03e18d81657652-33697706-13c680-163c897342dc8; _lxsdk=163c897342c28-03e18d81657652-33697706-13c680-163c897342dc8; s_ViewType=10; ctu=d1123a22f332317e09ff2123f7139509c42f4bc0993642c42ba7a097bf17f2b1; cityid=1; switchcityflashtoast=1; _tr.u=7OzjpC5JH9tOtIQk; aburl=1; _dp.ac.v=4d131c4a-4c81-4aa2-9893-9950f9d2ec87; dper=dea716dc6cdd7e8396ae7318b6899d248fbba90cb85edf5d0bea1654b9233a07597cd4f7dfee85010713f3d716ae8fd09c68b71bfc1d562521cbf72cab1080da31228dd17f01cfc7446290a07d4b48f04ca3f101b39191d77929affa8c9dfdaf; ua=%E5%A4%AA%E7%A9%BA%E7%9A%84%E6%B0%A7%E6%B0%94; __mta=40663837.1532076998643.1533192341515.1533192424089.18; Hm_lvt_dbeeb675516927da776beeb1d9802bd4=1533094362,1534989595,1535103931; cityInfo=%7B%22cityId%22%3A208%2C%22cityEnName%22%3A%22foshan%22%2C%22cityName%22%3A%22%E4%BD%9B%E5%B1%B1%22%7D; ll=7fd06e815b796be3df069dec7836c3df; cy=5; cye=nanjing; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; _lxsdk_s=165845b5ee6-28a-a03-384%7C%7C" + random(2);
        return cookiePool[random.nextInt(2)];
    }

    private String random(int range) {
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < range; i++) {
            s.append(random.nextInt(10));
        }
        return s.toString();
    }

    // 评论 cookie
    //Cookie: _lxsdk_s=165178e1a9c-16-727-4d9%7C%7C210; s_ViewType=10; cy=2; cye=beijing; ll=7fd06e815b796be3df069dec7836c3df; dper=51591cc5506f2801e090062a9c7d3ae6cfc50d6a95ef15e898784fe4196996fde112345edfb2b5a5c5ea0d4ebf0a98620d24aac632ee8113d680cbc292b014647d2725924484960947b137c83bc0ac737f68a126de9d5f3fa7dc1c6de778c8c9; ua=dpuser_2956140119; uamo=15510083108; ctu=d1123a22f332317e09ff2123f7139509e160ffde0840b5b93b192bf85817baf3; _hc.v=a26bc646-667c-a1cb-376a-9a054c963018.1533174515; _lxsdk=164f85283cac8-02407009539b4a-1b451e24-13c680-164f85283cec8; _lxsdk_cuid=164f85283cac8-02407009539b4a-1b451e24-13c680-164f85283cec8
    //Cookie: _lxsdk_s=1658382656d-ab-a9-baf%7C%7C285; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; cy=2; cye=beijing; ll=7fd06e815b796be3df069dec7836c3df; s_ViewType=10; dper=51591cc5506f2801e090062a9c7d3ae6cfc50d6a95ef15e898784fe4196996fde112345edfb2b5a5c5ea0d4ebf0a98620d24aac632ee8113d680cbc292b014647d2725924484960947b137c83bc0ac737f68a126de9d5f3fa7dc1c6de778c8c9; ua=dpuser_2956140119; uamo=15510083108; ctu=d1123a22f332317e09ff2123f7139509e160ffde0840b5b93b192bf85817baf3; _hc.v=a26bc646-667c-a1cb-376a-9a054c963018.1533174515; _lxsdk=164f85283cac8-02407009539b4a-1b451e24-13c680-164f85283cec8; _lxsdk_cuid=164f85283cac8-02407009539b4a-1b451e24-13c680-164f85283cec8
    //店铺 cookie
    // Cookie: _lxsdk_s=165790fc33d-37a-972-933%7C%7C33; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; cy=2; cye=beijing; ll=7fd06e815b796be3df069dec7836c3df; s_ViewType=10; dper=51591cc5506f2801e090062a9c7d3ae6cfc50d6a95ef15e898784fe4196996fde112345edfb2b5a5c5ea0d4ebf0a98620d24aac632ee8113d680cbc292b014647d2725924484960947b137c83bc0ac737f68a126de9d5f3fa7dc1c6de778c8c9; ua=dpuser_2956140119; uamo=15510083108; ctu=d1123a22f332317e09ff2123f7139509e160ffde0840b5b93b192bf85817baf3; _hc.v=a26bc646-667c-a1cb-376a-9a054c963018.1533174515; _lxsdk=164f85283cac8-02407009539b4a-1b451e24-13c680-164f85283cec8; _lxsdk_cuid=164f85283cac8-02407009539b4a-1b451e24-13c680-164f85283cec8



    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 10; i++) {
            porxyTest();
        }
    }

    private static void porxyTest() {
        String url = "https://www.ip.cn/";
        try (CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidating().build()) {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig config = RequestConfig.custom()
                    .setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123))
                    .build();
            httpGet.setConfig(config);
            httpGet.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
//            httpGet.addHeader("cookie", "cy=2; cye=beijing; _lxsdk_cuid=16348c6cf3b60-05a2fbdb06cc52-33627f06-13c680-16348c6cf3cc8; _lxsdk=16348c6cf3b60-05a2fbdb06cc52-33627f06-13c680-16348c6cf3cc8; _hc.v=d2e17354-b3e3-2fd5-d3b9-45aa123b6913.1525934379; s_ViewType=10; cityid=2; switchcityflashtoast=1; m_flash2=1; default_ab=shop%3AA%3A1%7Cindex%3AA%3A1%7CshopList%3AA%3A1%7Cshopreviewlist%3AA%3A1%7Csinglereview%3AA%3A1; ctu=d1123a22f332317e09ff2123f71395095bb9ae7adc0c5f40ede2fc43c371a152; dper=81f1c7cae2c72d9914b5a676687f6863184f4cccb06ab68ed047d7aa2b811d1ebd970e3a30ab7888641600c0095d7dd7cd186ebc207a566f5dea0b824adc9c11f3b09fbf6e04887f599536b5f566f04ee5df6d0aa415d2b1e2f8fdf74c910917; ua=%E5%A4%AA%E7%A9%BA%E7%9A%84%E6%B0%A7%E6%B0%94; uamo=13241300169; gr_user_id=b672d869-6fa9-4e20-9a0f-51d1e165a7c9; __mta=150845065.1526020460454.1526020460454.1526314638085.2; ll=7fd06e815b796be3df069dec7836c3df; _lx_utm=utm_source%3Dco_temp%26utm_campaign%3Dtoplistshoprank; _lxsdk_s=163627d0b70-910-1c-892%7C%7C45");
//            httpGet.addHeader("host", "www.dianping.com");
            httpGet.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
//            httpGet.addHeader("referer", "http://www.dianping.com");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String content = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
                Html html = new Html(content);
                String ip = html.xpath("//*[@id=\"result\"]/div/p[1]/code/text()").get();
                String addr = html.xpath("//*[@id=\"result\"]/div/p[2]/code/text()").get();
                System.out.println(ip + " === " + addr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
