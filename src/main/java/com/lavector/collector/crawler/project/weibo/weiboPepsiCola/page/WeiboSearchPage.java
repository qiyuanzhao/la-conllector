package com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page;

import com.lavector.collector.crawler.base.PageParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WeiboSearchPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(WeiboSearchPage.class);

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String url = "https://weibo.com/aj/v6/comment/big?id=";
    private String endUrl = "&from=singleWeiBo";


    @Override
    public boolean handleUrl(String url) {
        return url.contains("https://s.taobao.com/search");
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        logger.info("获取到列表页面");
        Project newProject = (Project) page.getRequest().getExtra("project");

        String pageUrl = page.getUrl().get();
        String pageNumber = pageUrl.substring(pageUrl.lastIndexOf("=") + 1);

        List<Selectable> nodes = page.getHtml().xpath("//div[@class='card-wrap']").xpath("//div[@action-type='feed_list_item']").nodes();

        List<Request> requests = new ArrayList<>();
        nodes.forEach(node -> {
            Project project = new Project();
            String reportRel = "0";
            String commentRel = "0";
            String likeRel = "0";
            Request newRequest = new Request();

            String name = node.xpath("//a[@class='name']/text()").get();

            //微博内容
            String content = node.xpath("//p[@node-type='feed_list_content_full']/text()").get();
            if (content == null) {
                content = node.xpath("//p[@node-type='feed_list_content']/text()").get();
            }

            String time = node.xpath("//p[@class='from']/a[1]/text()").get();
            String urlDetile = node.xpath("//p[@class='from']/a[1]/@href").get();

            String mid = node.xpath("//div[@class='card-wrap']/@mid").get();
            String report = node.xpath("//div[@class='card-act']/ul/li[2]/a/text()").get();
            String comment = node.xpath("//div[@class='card-act']/ul/li[3]/a/text()").get();

            //点赞数
            String like = node.xpath("//div[@class='card-act']/ul/li[4]/a/em/text()").get();

            if (time != null && report != null && comment != null && like != null) {


                if (!like.equals("")) {
                    likeRel = like;
                }

                //转发数
                if (report.length() > 3) {
                    String substring = report.substring(report.indexOf(" ") + 1);
                    if (substring != null) {
                        reportRel = substring;
                    }
                }
                //评论数
                if (comment.length() > 3) {
                    String substring = comment.substring(comment.indexOf(" ") + 1);
                    if (substring != null) {
                        commentRel = substring;
                    }
                }
//                if (checkTime(time)) {
                    project.setName(name);
                    project.setLike(likeRel);
                    project.setReport(reportRel);
                    project.setConent(content.replace(",", "，"));
                    project.setId(mid);
                    project.setKeyWord(newProject.getKeyWord());
                    project.setCommentNumber(commentRel);
                    project.setDate(time);
                    project.setUrl("https:" + urlDetile);
                    newRequest.setUrl(this.url + mid + endUrl)
                            .putExtra("project", project);
                    requests.add(newRequest);
                    result.setRequests(requests);
                    page.setSkip(true);
//                }
            }
        });
        List<Selectable> urlNodes = page.getHtml().xpath("//div[@class='m-page']//ul[@class='s-scroll']/li").nodes();
        if (Integer.parseInt(pageNumber) == 1) {
            for (int i = 1; i < urlNodes.size(); i++) {
                String newUrl = pageUrl.replace("&page=1", "&page=" + (i + 1));
                result.addRequest(new Request().setUrl(newUrl).putExtra("project", newProject));
                page.setSkip(true);
            }
        }

        return result;
    }

    private boolean checkTime(String time) {
        String start = "2018-05-30";
        String end = "2018-08-02";
        boolean flag = false;
        Date date = null;
        if (time.contains("今天")) {
            date = new Date(System.currentTimeMillis());
        } else if (!time.contains("年") && time.contains("月") && time.contains("日")) {
            Pattern pattern = Pattern.compile("([0-9]+)");
            Matcher matcher = pattern.matcher(time);
            int i = 0;
            String[] times = new String[2];
            while (matcher.find()) {
                String group = matcher.group();
                times[i] = group;
                i++;
                if (i == 2) {
                    break;
                }
            }
            String newTime = "2018-" + times[0] + "-" + times[1];
            try {
//                System.out.println(newTime);
                date = simpleDateFormat.parse(newTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }
        try {
            Date startTime = simpleDateFormat.parse(start);
            Date endTime = simpleDateFormat.parse(end);
            if (startTime.before(date) && endTime.after(date)) {
                flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }


    @Override
    public String pageName() {
        return null;
    }

    @Override
    public <T> T getRequestExtra(String key, Page page) {
        return null;
    }

    @Override
    public boolean handleRequest(Request request) {
        return false;
    }
}
