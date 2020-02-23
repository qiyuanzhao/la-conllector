package com.lavector.collector.crawler.nonce;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Map;

/**
 * Created on 16/05/2018.
 *
 * @author zeng.zhao
 */
public class NonceMessage {

    public static class SocialCount{
        private Integer playCount = 0;
        private Integer likes = 0;
        private Integer comments = 0;
        private Integer reposts = 0;
        private Integer collects = 0;

        public Integer getPlayCount() {
            return playCount;
        }

        public void setPlayCount(Integer playCount) {
            this.playCount = playCount;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }

        public Integer getComments() {
            return comments;
        }

        public void setComments(Integer comments) {
            this.comments = comments;
        }

        public Integer getReposts() {
            return reposts;
        }

        public void setReposts(Integer reposts) {
            this.reposts = reposts;
        }

        public Integer getCollects() {
            return collects;
        }

        public void setCollects(Integer collects) {
            this.collects = collects;
        }
    }

    private String content;
    private SocialCount socialCount;
    private String type;
    private String time;
    private String qmid;
    private String mid;
    private String title;
    private String userId;
    private String site;
    private String url;
    private String commentUserName;
    private Map<String, String> parameter;

    private String owner; // 微博账号名

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SocialCount getSocialCount() {
        return socialCount;
    }

    public void setSocialCount(SocialCount socialCount) {
        this.socialCount = socialCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQmid() {
        return qmid;
    }

    public void setQmid(String qmid) {
        this.qmid = qmid;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Map<String, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NonceMessage message = (NonceMessage) o;

        return new EqualsBuilder()
                .append(content, message.content)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(content)
                .toHashCode();
    }
}
