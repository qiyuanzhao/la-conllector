package com.lavector.collector.crawler.base;

import java.util.Date;

/**
 * Created on 2018/4/16.
 *
 * @author zeng.zhao
 */
public class Message {

    private String userId;
    private String site;
    private Integer likes;
    private String title;
    private String mid;
    private Integer comments;
    private String content;
    private String time;
    private String qmid;
    private String type;
    private Integer reposts;
    private Integer playCount;

    public Message() {
    }

    public Message(String userId, String site, Integer likes, String title, String mid, Integer comments, String content, String time, String qmid, String type, Integer reposts, Integer playCount) {
        this.userId = userId;
        this.site = site;
        this.likes = likes;
        this.title = title;
        this.mid = mid;
        this.comments = comments;
        this.content = content;
        this.time = time;
        this.qmid = qmid;
        this.type = type;
        this.reposts = reposts;
        this.playCount = playCount;
    }

    public String getUserId() {
        return userId;
    }

    public String getSite() {
        return site;
    }

    public Integer getLikes() {
        return likes;
    }

    public String getTitle() {
        return title;
    }

    public String getMid() {
        return mid;
    }

    public Integer getComments() {
        return comments;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getQmid() {
        return qmid;
    }

    public String getType() {
        return type;
    }

    public Integer getReposts() {
        return reposts;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public static Message.Builder custom() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private String site;
        private Integer likes;
        private String title;
        private String mid;
        private Integer comments;
        private String content;
        private String time;
        private String qmid;
        private String type;
        private Integer reposts;
        private Integer playCount;

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setSite(String site) {
            this.site = site;
            return this;
        }

        public Builder setLikes(Integer likes) {
            this.likes = likes;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMid(String mid) {
            this.mid = mid;
            return this;
        }

        public Builder setComments(Integer comments) {
            this.comments = comments;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setTime(String time) {
            this.time = time;
            return this;
        }

        public Builder setQmid(String qmid) {
            this.qmid = qmid;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setReposts(Integer reposts) {
            this.reposts = reposts;
            return this;
        }

        public Builder setPlayCount(Integer playCount) {
            this.playCount = playCount;
            return this;
        }

        public Message build() {
            return new Message(userId, site, likes, title, mid, comments, content, time, qmid, type, reposts, playCount);
        }
    }
}
