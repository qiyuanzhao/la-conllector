package com.lavector.collector.entity.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Site用户id
     */
    private String id;
    /**
     * 用户登录邮箱(暂时不用)
     */
    @JsonProperty("un")
    private String userName;
    /**
     * 用户昵称
     */
    @JsonProperty("dn")
    private String displayName;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 粉丝数
     */
    @JsonProperty("fc")
    private int followersCount;


    public UserInfo() {
    }

    public UserInfo(String id, String userName, String displayName) {
        super();
        this.id = id;
        this.userName = userName;
        this.displayName = displayName;
    }

    public UserInfo(String id, String userName, String displayName, String avatar) {
        super();
        this.id = id;
        this.userName = userName;
        this.displayName = displayName;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    //	@JsonProperty(value = "_id", access = JsonProperty.Access.WRITE_ONLY)
    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

}
