package com.lavector.collector.crawler.project.tmall.page.shop;

import com.lavector.collector.crawler.base.RequestExtraKey;
import com.lavector.collector.crawler.project.tmall.TmallConfig;
import com.lavector.collector.crawler.project.tmall.page.TmallPageParse;
import com.lavector.collector.crawler.util.RequestUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.IOException;

/**
 * Created on 2018/11/23.
 *
 * @author zeng.zhao
 */
public class TmallShopHomePage implements TmallPageParse {


    @Override
    public void parse(Page page) {
        TmallConfig.TmallShopConfig tmallShopConfig = (TmallConfig.TmallShopConfig) page.getRequest().getExtra("config");
        String wid = page.getJson().regex("s.w4011-(\\d+)\\.").get(); // 获取店铺内商品列表所需id
        if (wid == null) {
            wid = page.getJson().regex("s.w4010-(\\d+)\\.").get();
        }
        if (wid == null) {
            wid = page.getJson().regex("4011-(\\d+)").get();
        }
        if (wid == null) {
            System.out.println();
            if (page.getUrl().get().contains("loreal.tmall.com")) {
                wid = "18407891890";
            } else if (page.getUrl().get().contains("anessa.tmall.com")) {
                wid = "15018618371";
            }
        }

        if (tmallShopConfig.getBrand().equals("YSL")) {
            wid = "17857459356";
        }

        String brandBaseUrl = page.getHtml().xpath("//meta[@property='og:url']/@content").get();
        if (brandBaseUrl == null) {
            brandBaseUrl = page.getHtml().xpath("//a[@class='slogo-shopname']/@href").get();
        }
        if (brandBaseUrl == null) {
            System.out.println(page);
        }

        String listUrl = brandBaseUrl + "i/asynSearch.htm?mid=w-" + wid + "-0&wid=" + wid + "&pageNo=1";
        page.addTargetRequest(new Request(listUrl).putExtra("config", tmallShopConfig).putExtra(RequestExtraKey.KEY_PAGE_NAME, "list"));
    }

    @Override
    public boolean handlerRequest(Request request) {
        return request.getExtra(RequestExtraKey.KEY_PAGE_NAME).toString().contains("home");
    }

    public static void main(String[] args) throws IOException {
        String url = "https://shiseido.tmall.com/?spm=a1z10.3-b-s.1997427721.d4918089.1f8c4d47jfzon8";
        new TmallShopHomePage().parse(RequestUtil.getPage(url));
    }
}
