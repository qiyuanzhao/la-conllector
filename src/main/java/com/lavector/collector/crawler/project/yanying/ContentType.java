package com.lavector.collector.crawler.project.yanying;

/**
 * Created on 2018/2/7.
 *
 * @author zeng.zhao
 */
public enum ContentType {
    POST("post"),
    COMMENT("comment"),
    REPLY("reply"),
    VIDEO("video"),
    DANMU("danmu");

    private String type;

    ContentType(String type) {
        this.type = type;
    }

    public String get() {
        return type;
    }
}
