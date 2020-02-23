package com.lavector.collector.crawler.test;


import com.google.common.collect.Lists;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/10/16.
 *
 * @author zeng.zhao
 */
public class XiaoHongShu1 {

    public static void main(String[] args) throws Exception {

        String url = "https://www.xiaohongshu.com/api/sns/v8/search/notes?deviceId=BF0CD16B-492F-47A7-94FC-6E70FBA2B82D&device_fingerprint=20180828173923c0782026755bbfe85f2925ea12ecd57c01185d50dd6eedcf&device_fingerprint1=20180828173923c0782026755bbfe85f2925ea12ecd57c01185d50dd6eedcf&keyword=%E7%9C%BC%E7%9D%9B&keyword_type=normal&lang=zh&page=1&page_size=20&platform=iOS&search_id=05BFDD8A44F10BDD30C8CFAFD3380277&sid=session.1221397545423164646&sign=c447ed84e66ace07b97a6b6f47585f51&sort=general&source=explore_feed&t=1539655961";

        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        final HostnameVerifier verifiedAllHostname = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Lists.newArrayList(Protocol.HTTP_1_1))
                .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier(verifiedAllHostname)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Request request = new Request.Builder()
                .addHeader("Accept-Encoding", "br, gzip, deflate")
                .addHeader("Host", "www.xiaohongshu.com")
                .addHeader("Accept-Language", "zh-Hans-CN;q=1")
                .addHeader("X-Tingyun-Id", "LbxHzUNcfig;c=2;r=1967273824;u=e0234d7641fc0e6ff8f1bf7b7fef828d35bbb830d5b2c4c9544365500511dd91f178523a60609ed4661246e9c53820af::89BDD3D14165886A")
                .addHeader("device_id", "BF0CD16B-492F-47A7-94FC-6E70FBA2B82D")
                .addHeader("Authorization", "session.1221397545423164646")
                .addHeader("shield", "bedfc3259034d9048cb9fc42c66c64750be09a1998c922ec2c9ee3abb7064399")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "discover/5.26.1 (iPhone; iOS 11.4; Scale/3.00) Resolution/1080*1920 Version/5.26.1 Build/5261001 Device/(Apple Inc.;iPhone10,2) NetType/Unknown")
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

}
