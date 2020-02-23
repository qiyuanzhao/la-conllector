package com.lavector.collector.thirdpartyapi.weixin.entity;

import java.util.Set;

/**
 * Created on 2018/1/4.
 *
 * @author zeng.zhao
 */
public class AccountEntity {

    private String weixin_id; //微信账号id
    private String ori_weixin_id; //微信原始Id
    private String weixin_nick; //微信昵称
    private String description; //描述
    private Integer is_verified; //是否认证
    private String verified_info; //认证名称
    private String tags; //标签
    private String qr_url; //二维码地址
    private Integer estimate_fans_num; //预估粉丝数

    public String getWeixin_id() {
        return weixin_id;
    }

    public void setWeixin_id(String weixin_id) {
        this.weixin_id = weixin_id;
    }

    public String getOri_weixin_id() {
        return ori_weixin_id;
    }

    public void setOri_weixin_id(String ori_weixin_id) {
        this.ori_weixin_id = ori_weixin_id;
    }

    public String getWeixin_nick() {
        return weixin_nick;
    }

    public void setWeixin_nick(String weixin_nick) {
        this.weixin_nick = weixin_nick;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(Integer is_verified) {
        this.is_verified = is_verified;
    }

    public String getVerified_info() {
        return verified_info;
    }

    public void setVerified_info(String verified_info) {
        this.verified_info = verified_info;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getQr_url() {
        return qr_url;
    }

    public void setQr_url(String qr_url) {
        this.qr_url = qr_url;
    }

    public Integer getEstimate_fans_num() {
        return estimate_fans_num;
    }

    public void setEstimate_fans_num(Integer estimate_fans_num) {
        this.estimate_fans_num = estimate_fans_num;
    }
}
