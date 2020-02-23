package com.lavector.collector.crawler.project.gengmei.entity.question;


public class AnswerInfo {

    private String create_date; //时间

    private String content; //文本

    private String view_cnt;//浏览量

    private Integer up_cnt; //点赞数

    private String comment_cnt; //评论

    private String post_id; //文章id

    public String getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(String comment_cnt) {
        this.comment_cnt = comment_cnt;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getView_cnt() {
        return view_cnt;
    }

    public void setView_cnt(String view_cnt) {
        this.view_cnt = view_cnt;
    }

    public Integer getUp_cnt() {
        return up_cnt;
    }

    public void setUp_cnt(Integer up_cnt) {
        this.up_cnt = up_cnt;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
