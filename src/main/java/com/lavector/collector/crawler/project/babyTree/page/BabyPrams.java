package com.lavector.collector.crawler.project.babyTree.page;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BabyPrams implements Serializable {

    private String userName;
    private String date;
    private String content;
    private String browseNumber;
    private String commentNumber;
    private String collectionNumber;
    private String fromWhere;
    private String url;
    private String landlordName;
    private String babyAge;
    private String address;
    private ConmentUser conmentUser;
    private boolean userFalg = false;



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBrowseNumber(String browseNumber) {
        this.browseNumber = browseNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }

    public void setCollectionNumber(String collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getUserName() {
        return userName;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getBrowseNumber() {
        return browseNumber;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public String getCollectionNumber() {
        return collectionNumber;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public String getLandlordName() {
        return landlordName;
    }

    public String getBabyAge() {
        return babyAge;
    }

    public void setLandlordName(String landlordName) {
        this.landlordName = landlordName;
    }

    public void setBabyAge(String babyAge) {
        this.babyAge = babyAge;
    }

    public ConmentUser getConmentUser() {
        return conmentUser;
    }

    public void setConmentUser(ConmentUser conmentUser) {
        this.conmentUser = conmentUser;
    }

    public boolean getUserFalg() {
        return userFalg;
    }

    public void setUserFalg(boolean userFalg) {
        this.userFalg = userFalg;
    }
}
