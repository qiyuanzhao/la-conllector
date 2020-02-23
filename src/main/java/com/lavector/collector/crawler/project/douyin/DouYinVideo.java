package com.lavector.collector.crawler.project.douyin;

import java.io.Serializable;

/**
 * Created on 2018/4/9.
 *
 * @author zeng.zhao
 */
public class DouYinVideo implements Serializable {

    private static final long serialVersionUID = 3365705259013898914L;
    private String userId;
    private String playCount;
    private String commentCount;
    private String shareCount;
    private String url;
    private String description;
    private String likeCount;
    private Integer createTime;

    public DouYinVideo() {}

    public DouYinVideo(String userId, String playCount, String commentCount, String shareCount, String url, String description, String likeCount, Integer createTime) {
        this.userId = userId;
        this.playCount = playCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.url = url;
        this.description = description;
        this.likeCount = likeCount;
        this.createTime = createTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlayCount() {
        return playCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getShareCount() {
        return shareCount;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public static class Builder {
        private String userId;
        private String playCount;
        private String commentCount;
        private String shareCount;
        private String url;
        private String description;
        private String likeCount;
        private Integer createTime;

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setPlayCount(String playCount) {
            this.playCount = playCount;
            return this;
        }

        public Builder setCommentCount(String commentCount) {
            this.commentCount = commentCount;
            return this;
        }

        public Builder setShareCount(String shareCount) {
            this.shareCount = shareCount;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setLikeCount(String likeCount) {
            this.likeCount = likeCount;
            return this;
        }

        public Builder setCreateTime(Integer createTime) {
            this.createTime = createTime;
            return this;
        }

        public DouYinVideo build() {
            return new DouYinVideo(userId, playCount, commentCount, shareCount, url, description, likeCount, createTime);
        }
    }

    public static DouYinVideo.Builder custom() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "DouYinVideo{" +
                "userId='" + userId + '\'' +
                ", playCount='" + playCount + '\'' +
                ", commentCount=" + commentCount +
                ", shareCount=" + shareCount +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", likeCount=" + likeCount +
                ", createTime=" + createTime +
                '}';
    }
}
