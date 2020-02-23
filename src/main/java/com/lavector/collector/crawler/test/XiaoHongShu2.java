package com.lavector.collector.crawler.test;

import com.google.common.base.Joiner;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/10/16.
 *
 * @author zeng.zhao
 */
public class XiaoHongShu2 {

    private static final String app_key = "175626467";
    private static final String secret = "8de6bc69f697bf7a28f94de361188ea2";

    public static void main(String[] args) throws Exception {
        test4();
    }

    private static void test1() throws Exception {
        String url = "http://www.xiaohongshu.com/api/sns/v8/search/notes?deviceId=BF0CD16B-492F-47A7-94FC-6E70FBA2B82D&device_fingerprint=20180828173923c0782026755bbfe85f2925ea12ecd57c01185d50dd6eedcf&device_fingerprint1=20180828173923c0782026755bbfe85f2925ea12ecd57c01185d50dd6eedcf&keyword=%E7%9C%BC%E7%9D%9B&keyword_type=normal&lang=zh&page=1&page_size=20&platform=iOS&search_id=05BFDD8A44F10BDD30C8CFAFD3380277&sid=session.1221397545423164646&sort=general&source=explore_feed&t=1539655961&sign=c447ed84e66ace07b97a6b6f47585f51";
        CloseableHttpClient client = new DynamicProxyDownloader().ignoreValidating().build();
        HttpGet httpGet = new HttpGet(url);
//        httpGet.addHeader("Host", "www.xiaohongshu.com");
        httpGet.addHeader("shield", "bedfc3259034d9048cb9fc42c66c64750be09a1998c922ec2c9ee3abb7064399");
//        httpGet.addHeader("Accept-Encoding", "br, gzip, deflate");
//        httpGet.addHeader("Host", "www.xiaohongshu.com");
//        httpGet.addHeader("Accept-Language", "zh-Hans-CN;q=1");
//        httpGet.addHeader("X-Tingyun-Id", "LbxHzUNcfig;c=2;r=1967273824;u=e0234d7641fc0e6ff8f1bf7b7fef828d35bbb830d5b2c4c9544365500511dd91f178523a60609ed4661246e9c53820af::89BDD3D14165886A");
//        httpGet.addHeader("device_id", "BF0CD16B-492F-47A7-94FC-6E70FBA2B82D");
//        httpGet.addHeader("Authorization", "session.1221397545423164646");
//        httpGet.addHeader("shield", "bedfc3259034d9048cb9fc42c66c64750be09a1998c922ec2c9ee3abb7064399");
//        httpGet.addHeader("Accept", "application/json");
        httpGet.addHeader("User-Agent", "discover/5.26.1 (iPhone; iOS 11.4; Scale/3.00) Resolution/1080*1920 Version/5.26.1 Build/5261001 Device/(Apple Inc.;iPhone10,2) NetType/Unknown");

        CloseableHttpResponse response = client.execute(httpGet);
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(content);
    }

    private static void test2() throws IOException {
        String url = "https://www.baidu.com/s?q1=%E8%B5%84%E7%94%9F%E5%A0%82&q2=&q3=&q4=&gpc=stf&ft=&q5=&q6=xiaohongshu.com&tn=baiduadv";
        String content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        System.out.println(content);
    }

    private static void test3() throws Exception {
        String url = "http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baiduadv&wd=site%3A(xiaohongshu.com)%20%E8%B5%84%E7%94%9F%E5%A0%82&oq=site%3A(xiaohongshu.com)%20%E8%B5%84%E7%94%9F%E5%A0%82&rsv_pq=dbeb9a2200002d9e&rsv_t=2b4250hlylTVaXuwIxt8znoZYWIbj5foS4xLF3k8syr%2BUUoweHQ20g1fmCCXTtE&rqlang=cn&rsv_enter=1&gpc=stf%3D1540881728%2C1541486528%7Cstftype%3D1&tfflag=1&si=(xiaohongshu.com)&ct=2097152";
        for (int i = 0; i < 1000; i++) {
            String content = Request.Get(url)
                    .execute()
                    .returnContent()
                    .asString();
            List<Selectable> selectableList = new Html(content).xpath("//div[@class='result c-container']").nodes();
            if (selectableList.size() == 0) {
                System.out.println(i);
            }
            System.out.println(i);
        }
    }

    private static void test4() throws Exception {
        String url = "http://www.baidu.com/link?url=2nIakBXX0QM7PuUr5ALhOHE9FU1LpRJcSt5ilgvCJokltulXmZYbRmsp2Xt9Yt4ye1FZ5btCuCNpZ7rGrpv71ffeR51djNWur6rExPusZke";
        for (int i = 0; i < 1000; i++) {
            String header = new XiaoHongShu2().getHeader();
            System.out.println(header);
//            Connection.Response response = Jsoup.connect(url).timeout(30 * 1000).method(Connection.Method.GET).followRedirects(false)
//                    .proxy("s2.proxy.mayidaili.com", 8123)
//                    .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
//                    .header("Proxy-Authorization", header)
//                    .execute();
//            System.out.println(response.header("Location") + " = = " + i);
        }
    }

    private String getHeader() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("app_key", app_key);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));//使用中国时间，以免时区不同导致认证错误
        paramMap.put("timestamp", format.format(new Date()));

        // 对参数名进行排序
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        // 拼接有序的参数名-值串
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(secret);
        for (String key : keyArray) {
            stringBuilder.append(key).append(paramMap.get(key));
        }

        stringBuilder.append(secret);
        String codes = stringBuilder.toString();

        // MD5编码并转为大写， 这里使用的是Apache codec
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(codes).toUpperCase();

        paramMap.put("sign", sign);

        // 拼装请求头Proxy-Authorization的值，这里使用 guava 进行map的拼接

        return "MYH-AUTH-MD5 " + Joiner.on('&').withKeyValueSeparator("=").join(paramMap);
    }
}
