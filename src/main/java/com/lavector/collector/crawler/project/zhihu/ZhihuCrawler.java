package com.lavector.collector.crawler.project.zhihu;


import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.project.zhihu.pipepine.AnswerPopeline;
import com.lavector.collector.crawler.project.zhihu.pipepine.QuestionPipeline;
import com.lavector.collector.crawler.project.zhihu.pipepine.ZhihuPipeline;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.crawler.util.UrlUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.LinkedList;
import java.util.List;

public class ZhihuCrawler {
    //https://www.zhihu.com/search?type=content&q=%E5%B0%8F%E5%B7%A8%E5%A3%B0
    //https://www.zhihu.com/search?type=content&q=LS%2050

    private static String startUrl = "https://www.zhihu.com/api/v4/search_v3?advert_count=0&correction=1&limit=20&offset=0&q=";
    private static String endUrl = "&search_hash_id=95bede296896571d5622234773c07c87&t=general&vertical_info=0%2C0%2C1%2C0%2C0%2C0%2C0%2C0%2C0%2C1";

    private static String startAnswerUrl = "https://www.zhihu.com/api/v4/questions/";
    private static String endAnswerUrl = "/answers?include=data%5B*%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_labeled%2Cis_recognized%2Cpaid_info%3Bdata%5B*%5D.mark_infos%5B*%5D.url%3Bdata%5B*%5D.author.follower_count%2Cbadge%5B*%5D.topics&offset=0&limit=5&sort_by=default&platform=desktop";

    private static String questionUrl = "https://www.zhihu.com/question/";

    public static void main(String[] args) {
        //keyword.txt
        //question.txt
        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/zhihu/keyword.txt", ",");

        Spider spider = Spider.create(new ZhihuProcesser(new CrawlerInfo()))
                .addPipeline(new ZhihuPipeline("G:/text/zhihu/data/search"));
//                .addPipeline(new QuestionPipeline("G:/text/zhihu/data/question"));
//                .addPipeline(new ZhihuPipeline("G:/text/zhihu/data/search"));

        for (SkuData skuData : skuDatas) {
            Request request = new Request(startUrl + UrlUtils.encodeStr(skuData.getBrand()) + endUrl);

//            Request request = new Request(questionUrl + skuData.getUrl());

//            Request request = new Request(startAnswerUrl + skuData.getUrl() + endAnswerUrl);

//            Request request = new Request(skuData.getUrl());
            skuData.setHeadWords(getList());
            request.putExtra("skuData", skuData);
            spider.addRequest(request);
        }
        spider.setDownloader(new SougouWeixinDownloader());
        spider.thread(10);
        spider.start();
    }


    public static List<String> getList() {
        List<String> list = new LinkedList<>();
        list.add("关键词");
        list.add("问题Id");
        list.add("问题名字");
        list.add("文章内容");
        list.add("url");
        return list;
    }


    public static List<String> getQuestionList() {
        List<String> list = new LinkedList<>();
        list.add("问题Id");
        list.add("问题Id");
        list.add("问题名字");
        list.add("时间");
        list.add("问题关注者");
        list.add("问题被浏览");
        list.add("问题描述");
        list.add("用户id");
        list.add("用户姓名");
        list.add("用户性别");
        list.add("用户简介");
        list.add("url");
        return list;
    }

    public static List<String> getAnswerList() {
        List<String> list = new LinkedList<>();
        list.add("问题id");
        list.add("回答id");
        list.add("回答人id");
        list.add("回答人姓名");
        list.add("回答人性别");
        list.add("回答人描述");
        list.add("回答时间");
        list.add("回答评论数");
        list.add("回答赞同数");
        list.add("回答内容");
        list.add("url");
        return list;
    }


}
