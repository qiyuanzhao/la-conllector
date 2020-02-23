package com.lavector.collector.crawler.project.tmall;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.project.tmall.page.AllBrandPage;
import com.lavector.collector.crawler.project.tmall.page.TmallCommentPage;
import com.lavector.collector.crawler.project.tmall.page.TmallProductPage;
import com.lavector.collector.crawler.project.tmall.page.TmallSearchListPage;
import com.lavector.collector.crawler.project.tmall.page.shop.TmallShopProductListPage;
import us.codecraft.webmagic.Site;

import java.util.Random;

/**
 * Created on 2018/1/15.
 *
 * @author zeng.zhao
 */
public class TmallPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setRetrySleepTime(6000)
            .setCycleRetryTimes(5)
            .setTimeOut(15 * 1000);

    public TmallPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new TmallSearchListPage(), new TmallProductPage(), new TmallCommentPage(), new TmallShopProductListPage(), new AllBrandPage());
    }

    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(5000);
        site.setSleepTime(/*10000 +*/ time);
//        site.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
        site.addHeader("Cookie", "cna=6KZmFH+sOWkCAXjAWe1mcgAH; otherx=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; _med=dw:1920&dh:1080&pw:1920&ph:1080&ist:0; _uab_collina=154417877062282690616723; hng=CN%7Czh-CN%7CCNY%7C156; lid=question0001; enc=JZ9CJvydKPduBpqtdalkAhQUD43N9p0De5DwnHNowwZjDXzdTLWnXLufHF2dGw9trxZ%2FI0xYRdSWeQWuMszTXw%3D%3D; OZ_1U_2061=vid=vdef0ba1a0a8f5.0&ctime=1575947169&ltime=0; sm4=330100; csa=0_0_0_0_0_0_0_0_0_0_0; _m_h5_tk=edee55383fd18a2d7a398a95fc9e7cfd_1577162431871; _m_h5_tk_enc=d4be6ecf3da701fec300a747146ebd6d; cq=ccp%3D1; t=3d2ddf1a611d4b6ad4b94275966f138e; uc3=id2=UU6nRR5oExz1%2FA%3D%3D&vt3=F8dByuqkagjoXlkR%2FbQ%3D&lg2=WqG3DMC9VAQiUQ%3D%3D&nk2=EuC2vKAXDBsbHsZN; tracknick=question0001; uc4=id4=0%40U2xqITEc2o4r8f9cMWc%2F5s4XbWXw&nk4=0%40EJ8nxZHL9p6QYwqM3TlbZkfAZXzimKQ%3D; lgc=question0001; _tb_token_=e35573ed15e78; cookie2=196c3e08ce301b73cc81ad37aea12dbe; x5sec=7b22746d616c6c7365617263683b32223a223831666462646635653135373862636335346663383838626161383162333331434a4b316b504146454f54386e502b4a2f61666a4a786f4d4d6a59324e4467794d6a49334d7a7378227d; pnm_cku822=098%23E1hvCQvUvbpvUpCkvvvvvjiPRsSWAj1RRsqp1jEUPmPU1jnhRFzvtjn2nLqh1ji8n8wCvvBvpvpZRphvChCvvvvPvpvhMMGvvvyCvh12m6QvITl1B3693E7rejvrfvc6D40OaAiMoWFZVCl1B5Eb%2BnezrmphwhKn3feAOHH1oGexRdIAcVvHfwpXd56OVADl%2Bb8rwkYYVVzhKphv8bYvvRLMMqytphvwv9vvpoavpCQmvvChNhCvjvUvvhBZphvwv9vvBHoEvpvVpyUUCEKwuphvmhCvCb69CVVhvphvCyCUvvvvvv%3D%3D; res=scroll%3A1903*5466-client%3A1903*358-offset%3A1903*5466-screen%3A1920*1080; l=cBQi_XIuvqhLKZtWBOfanurza779nIRb8sPzaNbMiICP995e5elcWZc-LoTwCnGVK65HR3PBVv7HBcYejyCqi7T2kX98ERC..; isg=BL29TEr1v9hx0hmNRZEYNrYKzBm3WvGsyyaoHH8Ce5RDttzoR6vVfIYkYKpVNglk");
//        site.addHeader("User-Agent", randomUserAgent());
        site.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
//        site.addHeader("Referer", "https://list.tmall.com/search_product.htm?spm=a220m.1000858.1000724.4.233c45d7PoCGBo&cat=55154008&sort=s&style=g&search_condition=23&from=sn_1_cat-qp&industryCatId=55154008");
//        site.addHeader("Host", "list.tmall.com");
//        site.addHeader("Sec-Fetch-Mode", "no-cors");
//        site.addHeader("sec-fetch-mode", "navigate");
//        site.addHeader("sec-fetch-site", "same-origin");
//        site.addHeader("sec-fetch-user", "?1");
        return site;
    }


    public static void main(String[] args) {
        String name = "@hahha";
        String substring = name.substring(1);
        System.out.println(substring);

    }

}
