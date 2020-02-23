package com.lavector.collector.crawler.test;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.config.CookieSpecs;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.ssl.H2TlsStrategy;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.http2.config.H2Config;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/10/11.
 *
 * @author zeng.zhao
 */
public class XiaoHongShu {

    public static void main(String[] args) throws Exception {
        http2();
    }

    private static void http2() throws Exception {
        String url = "http://www.xiaohongshu.com/api/sns/v3/search/trending?deviceId=BF0CD16B-492F-47A7-94FC-6E70FBA2B82D&device_fingerprint=20180828173923c0782026755bbfe85f2925ea12ecd57c01185d50dd6eedcf&device_fingerprint1=20180828173923c0782026755bbfe85f2925ea12ecd57c01185d50dd6eedcf&geo=eyJsYXRpdHVkZSI6MzkuOTA2MDA3MTQ1MTM0MDcsImxvbmdpdHVkZSI6MTE2LjQ2NjY2OTAzNDIzNTZ9&lang=zh&platform=iOS&sid=session.1221397545423164646&sign=b289b3d67111d666aadcf51f41878136&source=explore_feed&t=1539330918";

        final H2Config h2Config = H2Config.custom()
                .setPushEnabled(true)
                .build();

        final CloseableHttpAsyncClient client = getHttpAsyncClient();
//                HttpAsyncClients.custom()
//                .setIOReactorConfig(ioReactorConfig)
//                .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_2)
//                .setH2Config(h2Config)
//                .build();

        client.start();

        SimpleHttpRequest request = new SimpleHttpRequest("GET", new URI(url));


        Future<SimpleHttpResponse> responseFuture = client.execute(request, new FutureCallback<SimpleHttpResponse>() {
            @Override
            public void completed(SimpleHttpResponse simpleHttpResponse) {
                System.out.println("dasdasd");
            }

            @Override
            public void failed(Exception e) {

            }

            @Override
            public void cancelled() {

            }
        });

        SimpleHttpResponse simpleHttpResponse = responseFuture.get();
        System.out.println(simpleHttpResponse.getBodyText());
    }


    private static SSLContext getSSLContext() {
        final TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        try {
            final SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            sslContext.getServerSessionContext().setSessionCacheSize(1000);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Registry<TlsStrategy> getSSLRegistryAsync() {
        return RegistryBuilder.<TlsStrategy>create()
//                .register("https", new SSLIOSessionStrategy(
//                        getSSLContext(), null, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)).build();
                .register("https", new H2TlsStrategy(getSSLContext()))
                .build();
    }

    private static PoolingAsyncClientConnectionManager getPoolingNHttpClientConnectionManager() {
        final PoolingAsyncClientConnectionManager connectionManager =
                new PoolingAsyncClientConnectionManager(getSSLRegistryAsync());
//            connectionManager.setMaxTotal(connectionPoolMax);
//            connectionManager.setDefaultMaxPerRoute(connectionPoolMaxPerRoute);

        return connectionManager;
    }

    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(10 * 1000L, TimeUnit.SECONDS)
                .setConnectionRequestTimeout(10 * 1000L, TimeUnit.SECONDS)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();
    }

    private static CloseableHttpAsyncClient getHttpAsyncClient() {
        final CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom()
                .setConnectionManager(getPoolingNHttpClientConnectionManager())
                .setDefaultRequestConfig(getRequestConfig())
                .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_2)
                .build();
        return httpAsyncClient;
    }
}
