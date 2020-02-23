package com.lavector.collector.thirdpartyapi.weixin.entity;

/**
 * Created on 2018/1/4.
 *
 * @author zeng.zhao
 */
public class ArticleEntity {

    private String mid; //微信批量发送id
    private String title; //标题
    private String digest; //简介
    private String content_url; //文章url
    private String source_url; //原文链接
    private String cover; //封面
    private Integer is_multi; //是否多图文
    private Integer is_top; //是否是头条
    private Integer idx; //文章位置
    private String pub_time; //发布时间
    private Integer read_num; //阅读数
    private Integer like_num; //点赞数
    private String author; //作者
    private Integer copyright; //是否原创
    private String wechat_id; //微信账号id
    private String collect_time; //采集时间
    private AccountEntity account_info; //账号信息
    private Integer last_page;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getIs_multi() {
        return is_multi;
    }

    public void setIs_multi(Integer is_multi) {
        this.is_multi = is_multi;
    }

    public Integer getIs_top() {
        return is_top;
    }

    public void setIs_top(Integer is_top) {
        this.is_top = is_top;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getPub_time() {
        return pub_time;
    }

    public void setPub_time(String pub_time) {
        this.pub_time = pub_time;
    }

    public Integer getRead_num() {
        return read_num;
    }

    public void setRead_num(Integer read_num) {
        this.read_num = read_num;
    }

    public Integer getLike_num() {
        return like_num;
    }

    public void setLike_num(Integer like_num) {
        this.like_num = like_num;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getCopyright() {
        return copyright;
    }

    public void setCopyright(Integer copyright) {
        this.copyright = copyright;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    public String getCollect_time() {
        return collect_time;
    }

    public void setCollect_time(String collect_time) {
        this.collect_time = collect_time;
    }

    public AccountEntity getAccount_info() {
        return account_info;
    }

    public void setAccount_info(AccountEntity account_info) {
        this.account_info = account_info;
    }

    public Integer getLast_page() {
        return last_page;
    }

    public void setLast_page(Integer last_page) {
        this.last_page = last_page;
    }
}
