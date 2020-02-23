package com.lavector.collector.entity.wechatSmall.article;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Created on 21/12/2017.
 *
 * @author seveniu
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Article extends BaseAuditableSqlEntity {
    private Long brandId;
    private String title;
    private String date;
    private String author;
    @Lob
    private String content;
    private Integer likeNum; // 点赞
    private Integer readNum;
    private Integer commentNum;
    private Integer favoriteNum; // 收藏
    private String url;
    private String image;
    private Integer score;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(Integer favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
