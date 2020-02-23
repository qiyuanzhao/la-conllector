package com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage;


import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadCsv {

    public static List<SkuData> getSkuData() {
        File csv = new File("G:/text/root.txt");
               List<SkuData> dataList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";

        try {
            while ((line = br.readLine()) != null) {
                SkuData skuData = new SkuData();

                String[] data = StringUtils.split(line,",");

                if (data.length>2){
                    List words = new ArrayList();
                    String[] split = StringUtils.split(data[2], "，");
                    for (int i = 0; i < split.length; i++) {
                        words.add(split[i]);
                    }

                    skuData.setBrand(data[0]);
                    skuData.setUrl(data[1]);
                    skuData.setWords(words);
                }else{
                    skuData.setBrand(data[0]);
                    skuData.setUrl(data[1]);
                }

                dataList.add(skuData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }


    public static void main(String[] args) {
//        ReadCsv readCsv = new ReadCsv();
//        readCsv.getSkuData();

//        String pageUrl = "https://lamer.tmall.com/i/asynSearch.htm?_ksTS=1543300776657_125&callback=jsonp126&mid=w-14859464013-0&wid=14859464013&path=";
//
//        String substring = StringUtils.substring(pageUrl, 0,pageUrl.indexOf(".com", 1)+4);
//
//        System.out.println(substring);

        CloseableHttpClient aDefault = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://lancome.tmall.com/i/asynSearch.htm?_ksTS=1543314435444_102&callback=jsonp103&mid=w-14640892225-0&wid=14640892225&path=/category.htm");
        httpGet.setHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36");
        httpGet.setHeader("referer","https://lancome.tmall.com/category.htm");
        httpGet.setHeader("cookie","cna=6KZmFH+sOWkCAXjAWe1mcgAH; uss=\"\"; otherx=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; x=__ll%3D-1%26_ato%3D0; lid=question0001; enc=BDgS2%2Be0BxJxe6UwrzvMyc%2ByCANaqitOdopxOQhqhWPGfFTfvO46sQT4TKJBZ%2FrEcjYhAe7MPjqOIxntKmrTPg%3D%3D; hng=CN%7Czh-CN%7CCNY%7C156; t=960894f2a131c5c60fd8e542b104ae32; tracknick=question0001; lgc=question0001; _tb_token_=ee753e8b593ea; cookie2=18fc6e424918eac6369715d4cc220e59; uc1=cookie16=Vq8l%2BKCLySLZMFWHxqs8fwqnEw%3D%3D&cookie21=U%2BGCWk%2F7p4mBoUyS4E9C&cookie15=V32FPkk%2Fw0dUvg%3D%3D&existShop=false&pas=0&cookie14=UoTYNclMD7a9Fg%3D%3D&tag=8&lng=zh_CN; uc3=vt3=F8dByR6kMQaO0sq8KpU%3D&id2=UU6nRR5oExz1%2FA%3D%3D&nk2=EuC2vKAXDBsbHsZN&lg2=URm48syIIVrSKA%3D%3D; _l_g_=Ug%3D%3D; ck1=\"\"; unb=2664822273; cookie1=BxBC6YironU4cwHoxYtETnLnKGTwedxq6kLi49rlsI8%3D; login=true; cookie17=UU6nRR5oExz1%2FA%3D%3D; _nk_=question0001; csg=f631b5d9; skt=897558ce244b959f; cq=ccp%3D0; pnm_cku822=; swfstore=293595; x5sec=7b2273686f7073797374656d3b32223a2237643935303465633561353265373834363666353137333439653236373031334350544a394e3846454d33306d62626a714c2b2b70514561444449324e6a51344d6a49794e7a4d374d773d3d227d; whl=-1%260%260%260; isg=BM3NGqYNqlHeyQ5tbknt4rez3OmHAjP66nZmsQ9SCWTTBu241_oRTBsQdNrFxhk0");
        try {
            CloseableHttpResponse response = aDefault.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String string = EntityUtils.toString(httpEntity, "utf-8");
            System.out.println(string);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
