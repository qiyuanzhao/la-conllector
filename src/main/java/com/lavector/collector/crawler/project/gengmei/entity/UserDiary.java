package com.lavector.collector.crawler.project.gengmei.entity;

/**
 * 日记
 */
public class UserDiary {

    private String post_id; //文章id

    private String uid; //用户id

    private String user_name; //用户名

    private HotProduct hot_product;

    private String create_date;  //创建时间

    private String group_id; //日記id

    private String url;


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public HotProduct getHot_product() {
        return hot_product;
    }

    public void setHot_product(HotProduct hot_product) {
        this.hot_product = hot_product;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
