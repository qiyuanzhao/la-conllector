package com.lavector.collector.crawler.project.zhihu.entity;


public class Answer {

    private Long id;

    private Question question;

    private Author author;

    private Long created_time;  //回答时间

    private Integer voteup_count; //赞同

    private Integer comment_count;//评论

    private String content; //内容

    private String url;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Long getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Long created_time) {
        this.created_time = created_time;
    }

    public Integer getVoteup_count() {
        return voteup_count;
    }

    public void setVoteup_count(Integer voteup_count) {
        this.voteup_count = voteup_count;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String excerpt) {
        this.content = excerpt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
