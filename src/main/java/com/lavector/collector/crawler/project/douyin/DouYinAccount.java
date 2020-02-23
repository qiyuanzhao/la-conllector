package com.lavector.collector.crawler.project.douyin;

/**
 * Created on 2018/4/4.
 *
 * @author zeng.zhao
 */
public class DouYinAccount {
    private String douyinId;
    private String nickName;
    private String description;
    private Integer gender;
    private Integer followerCount;
    private Integer followingCount;
    private Integer likes;
    private Integer videoCount;

    public DouYinAccount() {
    }

    public DouYinAccount(String douyinId, String nickName, String description, Integer gender, Integer followerCount, Integer followingCount, Integer likes, Integer videoCount) {
        this.douyinId = douyinId;
        this.nickName = nickName;
        this.description = description;
        this.gender = gender;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.likes = likes;
        this.videoCount = videoCount;
    }

    public String getDouyinId() {
        return douyinId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getDescription() {
        return description;
    }

    public Integer getGender() {
        return gender;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public Integer getLikes() {
        return likes;
    }

    public Integer getVideoCount() {
        return videoCount;
    }

    public static class Builder {
        private String douyinId;
        private String nickName;
        private String description;
        private Integer gender;
        private Integer followerCount;
        private Integer followingCount;
        private Integer likes;
        private Integer videoCount;

        public Builder setDouyinId(String douyinId) {
            this.douyinId = douyinId;
            return this;
        }

        public Builder setNickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setGender(Integer gender) {
            this.gender = gender;
            return this;
        }

        public Builder setFollowerCount(Integer followerCount) {
            this.followerCount = followerCount;
            return this;
        }

        public Builder setFollowingCount(Integer followingCount) {
            this.followingCount = followingCount;
            return this;
        }

        public Builder setLikes(Integer likes) {
            this.likes = likes;
            return this;
        }

        public Builder setVideoCount(Integer videoCount) {
            this.videoCount = videoCount;
            return this;
        }
        
        public DouYinAccount build() {
            return new DouYinAccount(douyinId, nickName, description, gender, followerCount, followingCount, likes, videoCount);
        }
    }
    
    public static DouYinAccount.Builder custom() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "DouYinAccount{" +
                "douyinId='" + douyinId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", description='" + description + '\'' +
                ", gender=" + gender +
                ", followerCount=" + followerCount +
                ", followingCount=" + followingCount +
                ", likes=" + likes +
                ", videoCount=" + videoCount +
                '}';
    }
}
