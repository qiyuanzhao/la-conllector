package com.lavector.collector.crawler.project.meituan.com.entity;

import java.util.List;

/**
 * Created by qyz on 2019/9/3.
 */
public class MeituanShopEntity {

    public String id;

    //店铺名称
    public String name;

    //月售
    public String monthSalesTip;

    //评分
    public String wmPoiScore;

    //人均
    public String averagePriceTip;

    //产品列表
    public List<MeituanProduct> productList;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl() {
        this.url = "https://waimai.meituan.com/comment/" + this.id;
    }
}
