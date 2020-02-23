//package com.lavector.collector.entity.message;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.lavector.collector.crawler.util.StringToDateConverter;
//import com.lavector.collector.entity.BaseEntity;
//
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import java.util.Date;
//import java.util.Map;
//
///**
// * Created on 25/09/2017.
// *
// * @author seveniu
// */
//@Entity
//public class DianPingMessage extends BaseEntity {
//
//    @JsonProperty("mId")
//    protected String messageId;
//    @JsonProperty("pU")
//    protected UserInfo postingUser;
//    @Enumerated(EnumType.STRING)
//    protected ContentType type;
//    /**
//     * 回复帖子的id
//     */
//    @JsonProperty("qmId")
//    protected String quoteMessageId;
//    /**
//     * 回复的那个用户信息
//     */
//    @JsonProperty("qU")
//    protected UserInfo quotedUser;
//    /**
//     * 发帖客户端
//     */
//    protected String client;
//
//    /**
//     * 发帖/回帖内容
//     */
//    protected String content;
//
//    /**
//     * 阅读数
//     */
//    protected Integer reads;
//    @JsonIgnore
//    protected Map<String, Integer> historyReads;
//    /**
//     * 点赞数
//     */
//    protected Integer likes;
//    @JsonIgnore
//    protected Map<String, Integer> historyLikes;
//    /**
//     * 点差数
//     */
//    protected Integer dislikes;
//    @JsonIgnore
//    protected Map<String, Integer> historyDislikes;
//
//    protected Integer reposts;
//    @JsonIgnore
//    protected Map<String, Integer> historyReposts;
//    protected Integer comments;
//    @JsonIgnore
//    protected Map<String, Integer> historyComments;
//    /**
//     * source site
//     */
//    protected String site;
//    protected String url;
//
//    @JsonProperty("tC")
//    @JsonDeserialize(using = StringToDateConverter.class)
//    protected Date timeCreated;
//    @JsonProperty("tF")
//    @JsonDeserialize(using = StringToDateConverter.class)
//    protected Date timeFetched;
//    /**
//     * what client/class fetched the data
//     */
//    protected String clientFetched;
//    protected Date timeProcessed;
//    protected Date timeUpdated;
//
//    private String province;
//    private String city;
//
//    private String subjectId;
//
//    public String getMessageId() {
//        return messageId;
//    }
//
//    public void setMessageId(String messageId) {
//        this.messageId = messageId;
//    }
//
//    public UserInfo getPostingUser() {
//        return postingUser;
//    }
//
//    public void setPostingUser(UserInfo postingUser) {
//        this.postingUser = postingUser;
//    }
//
//    public String getQuoteMessageId() {
//        return quoteMessageId;
//    }
//
//    public void setQuoteMessageId(String quoteMessageId) {
//        this.quoteMessageId = quoteMessageId;
//    }
//
//    public UserInfo getQuotedUser() {
//        return quotedUser;
//    }
//
//    public void setQuotedUser(UserInfo quotedUser) {
//        this.quotedUser = quotedUser;
//    }
//
//    public String getClient() {
//        return client;
//    }
//
//    public void setClient(String client) {
//        this.client = client;
//    }
//
//    public ContentType getType() {
//        return type;
//    }
//
//    public void setType(ContentType type) {
//        this.type = type;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public Integer getReads() {
//        return reads;
//    }
//
//    public void setReads(Integer reads) {
//        this.reads = reads;
//    }
//
//    public Map<String, Integer> getHistoryReads() {
//        return historyReads;
//    }
//
//    public void setHistoryReads(Map<String, Integer> historyReads) {
//        this.historyReads = historyReads;
//    }
//
//    public Integer getLikes() {
//        return likes;
//    }
//
//    public void setLikes(Integer likes) {
//        this.likes = likes;
//    }
//
//    public Map<String, Integer> getHistoryLikes() {
//        return historyLikes;
//    }
//
//    public void setHistoryLikes(Map<String, Integer> historyLikes) {
//        this.historyLikes = historyLikes;
//    }
//
//    public Integer getDislikes() {
//        return dislikes;
//    }
//
//    public void setDislikes(Integer dislikes) {
//        this.dislikes = dislikes;
//    }
//
//    public Map<String, Integer> getHistoryDislikes() {
//        return historyDislikes;
//    }
//
//    public void setHistoryDislikes(Map<String, Integer> historyDislikes) {
//        this.historyDislikes = historyDislikes;
//    }
//
//    public Integer getReposts() {
//        return reposts;
//    }
//
//    public void setReposts(Integer reposts) {
//        this.reposts = reposts;
//    }
//
//    public Map<String, Integer> getHistoryReposts() {
//        return historyReposts;
//    }
//
//    public void setHistoryReposts(Map<String, Integer> historyReposts) {
//        this.historyReposts = historyReposts;
//    }
//
//    public Integer getComments() {
//        return comments;
//    }
//
//    public void setComments(Integer comments) {
//        this.comments = comments;
//    }
//
//    public Map<String, Integer> getHistoryComments() {
//        return historyComments;
//    }
//
//    public void setHistoryComments(Map<String, Integer> historyComments) {
//        this.historyComments = historyComments;
//    }
//
//    public String getProvince() {
//        return province;
//    }
//
//    public void setProvince(String province) {
//        this.province = province;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getSubjectId() {
//        return subjectId;
//    }
//
//    public void setSubjectId(String subjectId) {
//        this.subjectId = subjectId;
//    }
//
//    public String getSite() {
//        return site;
//    }
//
//    public void setSite(String site) {
//        this.site = site;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public Date getTimeCreated() {
//        return timeCreated;
//    }
//
//    public void setTimeCreated(Date timeCreated) {
//        this.timeCreated = timeCreated;
//    }
//
//    public Date getTimeFetched() {
//        return timeFetched;
//    }
//
//    public void setTimeFetched(Date timeFetched) {
//        this.timeFetched = timeFetched;
//    }
//
//    public String getClientFetched() {
//        return clientFetched;
//    }
//
//    public void setClientFetched(String clientFetched) {
//        this.clientFetched = clientFetched;
//    }
//
//    public Date getTimeProcessed() {
//        return timeProcessed;
//    }
//
//    public void setTimeProcessed(Date timeProcessed) {
//        this.timeProcessed = timeProcessed;
//    }
//
//    public Date getTimeUpdated() {
//        return timeUpdated;
//    }
//
//    public void setTimeUpdated(Date timeUpdated) {
//        this.timeUpdated = timeUpdated;
//    }
//}
