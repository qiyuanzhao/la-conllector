package com.lavector.collector.crawler.project.weibo.weiboPepsiCola;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.project.weibo.weiboPepsiCola.entity.Status;
import com.lavector.collector.crawler.project.weibo.weiboPepsiCola.entity.StatusWapper;
import com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page.MyFilePipeline;
import com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page.Project;
import com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page.ReadCsv;
import com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page.SkuData;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.util.Base62Util;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WeiboPepsiColaCrawler {

    private static String startUrl = "https://weibo.com/aj/v6/comment/big?id=";
    private static String endUrl = "&from=singleWeiBo";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static Logger logger = LoggerFactory.getLogger(WeiboPepsiColaCrawler.class);

    public static void main(String[] args) {
//        List<SkuData> skuDatas = ReadCsv.getSkuData();

        Spider spider = Spider.create(new WeibolPepsiColaPageProcessor())
                .addPipeline(new MyFilePipeline("G:/text/newWeibo/data"));

//        for (SkuData skudata : skuDatas) {
//            for (String keyword : skudata.getWords()) {
//                List<StatusWapper> statusWappers = getWeiBo(keyword);
//
//                for (StatusWapper statusWapper : statusWappers) {
//                    if (statusWapper!=null){
//                        List<Status> statuses = statusWapper.getStatuses();
//                        for (Status status : statuses) {
//                            Project project = new Project();
//                            Request request = new Request();
//                            String mid = status.getMid();
//
//                            if (status.getRetweeted_status() != null) {
//                                project.setType("转发");
//                            } else {
//                                project.setType("原贴");
//                            }
//                            //微博日期
//                            Date created_at = status.getCreated_at();
//                            String format = simpleDateFormat.format(created_at);
//                            project.setDate(format);
//
//                            project.setId(mid);
//                            project.setKeyWord(keyword);
//                            project.setUserName(status.getUser().getName());
//                            project.setLike(status.getAttitudes_count() + "");
//                            project.setReport(status.getReposts_count() + "");
//                            //内容
//                            Pattern pattern = Pattern.compile("\\s*|\\t|\\r|\\n");
//                            Matcher matcher = pattern.matcher(status.getText());
//                            String replaceAll = matcher.replaceAll("");
//                            project.setConent(replaceAll);
//
//                            project.setCommentNumber(status.getComments_count() + "");
//                            project.setUrl("http://weibo.com/" + status.getUser().getId() + "/" + Base62Util.mid2url(status.getId()) + "?type=comment");
//                            request.setUrl(startUrl + mid + endUrl);
//                            request.putExtra("project", project);
//                            spider.addRequest(request);
//                        }
//                    }
//                }
//            }
//        }

        Request request = new Request("https://weibo.com/aj/v6/comment/big?ajwvr=6&id=4393283046514699&root_comment_max_id=168935869305550&root_comment_max_id_type=0&root_comment_ext_param=&filter=all&sum_comment_number=1435&filter_tips_before=0&from=singleWeiBo&__rnd=1562929075203&page=1");
        spider.addRequest(request);

        Downloader downloader = new SougouWeixinDownloader();
        spider.setDownloader(downloader);
        spider.thread(1);
        spider.start();
    }

    //一个关键词所有的微博
    private static List<StatusWapper> getWeiBo(String keyword) {

        String url = "https://c.api.weibo.com/2/search/statuses/limited.json?source=674587526&access_token=2.00Byjh5G0IqI9v98fc9582ccrGseQD&starttime=1535644800&endtime=1545806599&count=100&dup=0&antispam=1&page=1&q=" + keyword;

        List<StatusWapper> statusWappers = new ArrayList<>();
        try {
            String responseStr = getData(url);
            if (responseStr != null) {
                JsonMapper jsonMapper = JsonMapper.buildNormalBinder();
                jsonMapper.setDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                StatusWapper statusWapper = jsonMapper.fromJson(responseStr, StatusWapper.class);
                statusWappers.add(statusWapper);
                List<Status> statusList = statusWapper.getStatuses();
                int count = 1;
                while (statusWapper != null && statusWapper.getStatuses().size() != 0) {
                    statusWapper = new StatusWapper();
                    count++;
                    String newUrl = url.replace("&page=1", "&page=" + count);
                    String data = getData(newUrl);
                    statusWapper = jsonMapper.fromJson(data, StatusWapper.class);
                    if (statusWapper == null) {
                        break;
                    }
                    statusWappers.add(statusWapper);
                    logger.info("已获得一页...");
//                    Thread.sleep(3000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusWappers;
    }


    private static String getData(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpGet);
        String entity_str = (response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : null);
        return entity_str;
    }


}
