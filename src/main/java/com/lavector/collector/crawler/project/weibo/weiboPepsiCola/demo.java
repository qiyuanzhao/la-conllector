package com.lavector.collector.crawler.project.weibo.weiboPepsiCola;

import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.proxy.Proxy;

import java.io.IOException;
import java.util.List;

public class demo {

    private static Proxy[] proxies = new Proxy[100];

    private static String apiUrl = "http://dps.kdlapi.com/api/getdps/?orderid=994599064140830&num=10&pt=1&format=json&sep=1";

    static {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(apiUrl);

            System.out.println("Executing request " + httpget.getURI());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                String reseponse = EntityUtils.toString(response.getEntity());//获取API返回内容

                JSONObject jsonObject = JSONObject.fromObject(reseponse);
                JSONObject obj = (JSONObject) jsonObject.get("data");
                Object list = obj.get("proxy_list");
                List<String> ips = (List) obj.get("proxy_list");
                int i=0;
                for (String ip : ips) {
                    String[] split = ip.split(":");
                    Proxy proxy = new Proxy(split[0],Integer.parseInt(split[1]));
                    proxies[i] = proxy;
                    i++;
                }


            } finally {
                response.close();
            }
        }catch (Exception e ){
            e.printStackTrace();
        }finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        System.out.println(proxies);

    }

}
