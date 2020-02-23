package com.lavector.collector.crawler.project.weibo.weixinTitle.page;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class ProxyDownloader implements Downloader {
    @Override
    public Page download(Request request, Task task) {
        Page page = this.myDownloader(request, task);
        if (!task.getSite().getAcceptStatCode().contains(page.getStatusCode())) {
            page.setDownloadSuccess(false);
        }
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }

    private int count = 1;

    private Page myDownloader(Request request, Task task) {
        Page page = Page.fail();
        page.setUrl(new Json(request.getUrl()));
        page.setRequest(request);
        try (CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidating().build()) {
            HttpGet httpGet = new HttpGet(request.getUrl());
            task.getSite().getHeaders().forEach(httpGet::addHeader);
            RequestConfig config = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setProxy(new HttpHost(getListProxy().getHost(), getListProxy().getPort()))
                    .build();
            httpGet.setConfig(config);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {

                String content = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
                int statusCode = response.getStatusLine().getStatusCode();
                page.setRawText(content);
                page.setStatusCode(statusCode);
                page.setDownloadSuccess(true);
                return page;
            } catch (IOException e) {
                if (count>10){
                    e.printStackTrace();
                }
                myDownloader(request, task);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }


    public static ProxyIp getListProxy() {

        String urlNameString = "http://183.129.244.16:88/open?user_name=lavector_0318114902_auto&timestamp=1552880969&md5=8C09C32D43AD50AEC26C6881FCE78883&pattern=json&number=5";
        String result = "";
        try {
            // 根据地址获取请求
            HttpGet request = new HttpGet(urlNameString);//这里发送get请求
            // 获取当前客户端对象
            HttpClient httpClient = new DefaultHttpClient();
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);

            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(result);
        String host = jsonObject.get("domain").toString();
        List<Integer> domain = (List<Integer>) jsonObject.get("port");

        Integer port = domain.get(domain.size() - 1);
        ProxyIp proxyIp = new ProxyDownloader().new ProxyIp(host, port);
        System.out.println(proxyIp.toString());
        return proxyIp;

    }

    class ProxyIp {

        public ProxyIp(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        private String host;
        private Integer port;

        public String getHost() {
            return host;
        }

        public Integer getPort() {
            return port;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "ProxyIp{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }

}
