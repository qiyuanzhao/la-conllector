package com.lavector.collector.crawler.project.article.weixin;

/**
 * Created on 2018/1/8.
 *
 * @author zeng.zhao
 */
public class WeiXinAccount {

    private String brand;
    private String accountID;
    private String pageNo;
    private int pageSize = 20;
    private int index;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
