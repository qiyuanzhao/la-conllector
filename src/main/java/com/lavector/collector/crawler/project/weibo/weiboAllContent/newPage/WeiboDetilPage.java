package com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage;

import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.lavector.crawler.tianmao.TmallPageProcessor;

public class WeiboDetilPage{

    private Logger logger = LoggerFactory.getLogger(WeiboDetilPage.class);

    private final String dtileUrl = "https://detail.tmall.com/item.htm?id=";



    @SuppressWarnings("unchecked")
    public void parse(Page page) {
        Map<String, Object> map = new HashMap<>();
        logger.info("开始解析详情页面");
        String url = page.getUrl().get();
        Request request = page.getRequest();

        //获取月销量
        String number = this.getStock(page);
        //获取标题
        String title = page.getHtml().xpath("//meta[@name='keywords']/@content").get();

        //排除关键词
        boolean falg = this.removeTitle(title, (SkuData) request.getExtra("skuData"));
        if (falg){
            page.setSkip(true);
        }

        map.put("number", number);
        map.put("url", url);
        map.put("title",title);

        page.putField("map", map);
        page.putField("skuData", request.getExtra("skuData"));

    }

    //排除关键词
    private boolean removeTitle(String title, SkuData skuData) {
        if (skuData.getWords()!=null){
            for(String word:skuData.getWords()){
                if (title.contains(word)){
                    return true;
                }
            }
        }
        return false;
    }


    //获取月销量
    private String getStock(Page page) {
        logger.info("获取月销量");
        String itemId = "";
        String url = page.getUrl().get();
        Pattern pattern = Pattern.compile("(id=[0-9]+)");// 匹配的模式
        Matcher m = pattern.matcher(url);
        while (m.find()){
            String group = m.group();
            itemId = group.substring(group.indexOf("=",1));
        }

        String urls = "https://mdskip.taobao.com/core/initItemDetail.htm?itemId"+itemId;

        String string = "";
        try {
            org.apache.http.client.fluent.Request request = org.apache.http.client.fluent.Request.Get(urls);
            request.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36");
            request.setHeader("referer", "https://detail.tmall.com/item.htm?id="+itemId);
            request.setHeader("cookie","cna=6KZmFH+sOWkCAXjAWe1mcgAH; otherx=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; x=__ll%3D-1%26_ato%3D0; lid=question0001; enc=BDgS2%2Be0BxJxe6UwrzvMyc%2ByCANaqitOdopxOQhqhWPGfFTfvO46sQT4TKJBZ%2FrEcjYhAe7MPjqOIxntKmrTPg%3D%3D; hng=CN%7Czh-CN%7CCNY%7C156; t=960894f2a131c5c60fd8e542b104ae32; tracknick=question0001; lgc=question0001; _tb_token_=ee753e8b593ea; cookie2=18fc6e424918eac6369715d4cc220e59; uc1=cookie16=Vq8l%2BKCLySLZMFWHxqs8fwqnEw%3D%3D&cookie21=U%2BGCWk%2F7p4mBoUyS4E9C&cookie15=V32FPkk%2Fw0dUvg%3D%3D&existShop=false&pas=0&cookie14=UoTYNclMD7a9Fg%3D%3D&tag=8&lng=zh_CN; uc3=vt3=F8dByR6kMQaO0sq8KpU%3D&id2=UU6nRR5oExz1%2FA%3D%3D&nk2=EuC2vKAXDBsbHsZN&lg2=URm48syIIVrSKA%3D%3D; ck1=\"\"; csg=f631b5d9; skt=897558ce244b959f; pnm_cku822=; swfstore=293595; whl=-1%260%260%260; cq=ccp%3D1; x5sec=7b2273686f7073797374656d3b32223a223964663932343732306539653932616666353434616237383237396134393261434e54782b4e3846454c47342f4f57447736486b3577453d227d; isg=BDc32H0yIPDsn6RvaF_XQKltxitBVDnYhHiMM4nkUIZtOFd6kc-Fr3-eHtjD0OPW");
            string = request.execute().returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String number = JsonPath.read(string,"$.defaultModel.sellCountDO.sellCount");
        return number;
    }

}
