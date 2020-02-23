package com.lavector.collector.crawler.nonce.dianping;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import net.dongliu.requests.Proxies;
import net.dongliu.requests.Requests;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 19/05/2018.
 *
 * @author zeng.zhao
 */
public class DianPingTest {

    public static void main(String[] args) throws Exception {
        test4();
    }

    private static void test2() throws Exception {
        String url = "http://www.dianping.com/shop/74696609/review_all/p1?queryType=sortType&queryVal=lates";
        String content = Request.Get(url)
                .addHeader("host", "www.dianping.com")
                .addHeader("referer", "http://www.dianping.com/shop/74696609/review_all")
                .addHeader("cookie", "cy=2; cye=beijing; _lxsdk_cuid=16348c6cf3b60-05a2fbdb06cc52-33627f06-13c680-16348c6cf3cc8; _lxsdk=16348c6cf3b60-05a2fbdb06cc52-33627f06-13c680-16348c6cf3cc8; _hc.v=d2e17354-b3e3-2fd5-d3b9-45aa123b6913.1525934379; s_ViewType=10; cityid=2; switchcityflashtoast=1; default_ab=shop%3AA%3A1%7Cindex%3AA%3A1%7CshopList%3AA%3A1%7Cshopreviewlist%3AA%3A1%7Csinglereview%3AA%3A1; ctu=d1123a22f332317e09ff2123f71395095bb9ae7adc0c5f40ede2fc43c371a152; ua=%E5%A4%AA%E7%A9%BA%E7%9A%84%E6%B0%A7%E6%B0%94; uamo=13241300169; gr_user_id=b672d869-6fa9-4e20-9a0f-51d1e165a7c9; __mta=150845065.1526020460454.1526020460454.1526314638085.2; ctu=0f576141ffb8c50db195e38ac2a2af96a44ace87f96dde49b3bfbc9ae44625052e126931f4f565520bcc39244dc4e3fb; thirdtoken=163BCFB9E3259BEC86B8B11AFFCA3FFE; JSESSIONID=03FA5B93A63EB6AFB2ECAD9A7355122A; dper=81f1c7cae2c72d9914b5a676687f68635c15adeb9f7687493fe8c208d43fc46eb10c2e64cc439ac9c64aa0672b022994cddd15684e36f84a89ae7f4aeb45a3b5b8d402df5ed12592ed66899fd77eeca3e97ca0c6125bab876a1c98ca2006236e; ll=7fd06e815b796be3df069dec7836c3df; _lx_utm=utm_source%3Dco_temp%26utm_campaign%3Dtoplistshoprank; _lxsdk_s=163771d542d-c1c-760-127%7C%7C122")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        System.out.println(content);
    }

    private static void test1() {
        Map<String, String> headers = new HashMap<>();
        headers.put("host", "www.dianping.com");
        headers.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        headers.put("referer", "http://www.dianping.com/shop/74696609/review_all");
        headers.put("cookie", "cy=2; cye=beijing; _lxsdk_cuid=16348c6cf3b60-05a2fbdb06cc52-33627f06-13c680-16348c6cf3cc8; _lxsdk=16348c6cf3b60-05a2fbdb06cc52-33627f06-13c680-16348c6cf3cc8; _hc.v=d2e17354-b3e3-2fd5-d3b9-45aa123b6913.1525934379; s_ViewType=10; cityid=2; switchcityflashtoast=1; default_ab=shop%3AA%3A1%7Cindex%3AA%3A1%7CshopList%3AA%3A1%7Cshopreviewlist%3AA%3A1%7Csinglereview%3AA%3A1; ctu=d1123a22f332317e09ff2123f71395095bb9ae7adc0c5f40ede2fc43c371a152; ua=%E5%A4%AA%E7%A9%BA%E7%9A%84%E6%B0%A7%E6%B0%94; uamo=13241300169; gr_user_id=b672d869-6fa9-4e20-9a0f-51d1e165a7c9; __mta=150845065.1526020460454.1526020460454.1526314638085.2; dper=81f1c7cae2c72d9914b5a676687f68635c15adeb9f7687493fe8c208d43fc46eb10c2e64cc439ac9c64aa0672b022994cddd15684e36f84a89ae7f4aeb45a3b5b8d402df5ed12592ed66899fd77eeca3e97ca0c6125bab876a1c98ca2006236e; ll=7fd06e815b796be3df069dec7836c3df; _lx_utm=utm_source%3Dco_temp%26utm_campaign%3Dtoplistshoprank; _lxsdk_s=1637b7f13b5-a76-936-2f%7C%7C278");
        headers.put("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
        String url = "http://www.dianping.com/shop/74696609/review_all/p1?queryType=sortType&queryVal=lates";
        String content = Requests.get(url)
                .proxy(Proxies.httpProxy("s2.proxy.mayidaili.com", 8123))
                .verify(false)
                .headers(headers)
                .send()
                .readToText();
        System.out.println(content);
    }

    private static void test3() throws Exception {
        String url = "http://www.dianping.com/shop/74696609/review_all/p1?queryType=sortType&queryVal=lates";
//        try (CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidationHttpClient()) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            get.addHeader("host", "www.dianping.com");
            get.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
            get.addHeader("referer", "http://www.dianping.com/shop/74696609/review_all");
            get.addHeader("cookie", "cy=2; cye=beijing; _lxsdk_cuid=16348c6cf3b60-05a2fbdb06cc52-33627f06-13c680-16348c6cf3cc8; _lxsdk=16348c6cf3b60-05a2fbdb06cc52-33627f06-13c680-16348c6cf3cc8; _hc.v=d2e17354-b3e3-2fd5-d3b9-45aa123b6913.1525934379; s_ViewType=10; cityid=2; switchcityflashtoast=1; default_ab=shop%3AA%3A1%7Cindex%3AA%3A1%7CshopList%3AA%3A1%7Cshopreviewlist%3AA%3A1%7Csinglereview%3AA%3A1; ctu=d1123a22f332317e09ff2123f71395095bb9ae7adc0c5f40ede2fc43c371a152; ua=%E5%A4%AA%E7%A9%BA%E7%9A%84%E6%B0%A7%E6%B0%94; uamo=13241300169; gr_user_id=b672d869-6fa9-4e20-9a0f-51d1e165a7c9; __mta=150845065.1526020460454.1526020460454.1526314638085.2; dper=81f1c7cae2c72d9914b5a676687f68635c15adeb9f7687493fe8c208d43fc46eb10c2e64cc439ac9c64aa0672b022994cddd15684e36f84a89ae7f4aeb45a3b5b8d402df5ed12592ed66899fd77eeca3e97ca0c6125bab876a1c98ca2006236e; ll=7fd06e815b796be3df069dec7836c3df; _lx_utm=utm_source%3Dco_temp%26utm_campaign%3Dtoplistshoprank; _lxsdk_s=1637b7f13b5-a76-936-2f%7C%7C278");
            get.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());

            try (CloseableHttpResponse response = httpClient.execute(get)) {
                String content = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
                System.out.println(content);
            }
        }
    }

    private static void test4() throws Exception {
        String url = "https://www.ipip.net";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        System.out.println(content);
    }
}
