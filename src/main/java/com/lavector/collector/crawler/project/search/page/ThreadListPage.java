package com.lavector.collector.crawler.project.search.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.project.search.SearchCrawler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created on 2017/11/30.
 *
 * @author zeng.zhao
 */
public class ThreadListPage implements PageParse {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(ThreadListPage.class);

    @Override
    public boolean handleUrl(String url) {

        return url.contains("thread");
    }

    @Override
    public PageParse.Result parse(Page page) {
        Result result = Result.get();
        if (!page.isDownloadSuccess()) {
            result.addRequest(page.getRequest());
            return result;
        }
        Request nextRequest = parseThreadList(page);
        if (nextRequest != null) {
            result.addRequest(nextRequest);
        }
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private File file = new File("/Users/zeng.zhao/Desktop/autohome.csv");

    private String path = "/Users/zeng.zhao/Desktop/autohome.csv";

    private Request parseThreadList (Page page) {
        Html html = page.getHtml();
        if (page.getRequest().getExtra("first") != null) {
            String post = null; // 原贴内容
            try {
                post = html.xpath("//div[@class='w740']/allText()").all().get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            post = post.replaceAll(",", "，");
            String postTime = html.xpath("//span[@xname='date']/text()").all().get(0); //发布时间
            String views = html.xpath("//font[@id='x-views']/text()").get(); //点击数
            String reply = html.xpath("//font[@id='x-replys']/text()").get(); //回复数
            String postAuthor = html.xpath("//a[@xname='uname']/@title").all().get(0); // 楼主
            String city = html.xpath("//ul[@class='leftlist']/li[6]/a/text()").all().get(0); // 城市
            Date postTimeD = parseTime(postTime, page);
            if (postTimeD != null && postTimeD.after(SearchCrawler.targetDate)) {
                String postLine = "原贴," + postAuthor + "," + postTime + ",汽车之家," +
                        post + ",0," + reply + "," + views + ",,," + city + "," + page.getUrl().get() +  ",";
                WriteFile.write(postLine, path);
                System.out.println("原贴写入成功！");


                List<String> citys = html.xpath("//ul[@class='leftlist']/li[6]/a/text()").all();
                List<Selectable> nodes = html.xpath("//div[@class='w740']").nodes();//回复
                List<String> times = html.xpath("//span[@xname='date']/text()").all(); //回复时间
                List<String> replayAuthor = html.xpath("//a[@xname='uname']/@title").all(); //回复人
                for (int i = 1; i < nodes.size(); i++) {
                    String replayContent = nodes.get(i).xpath("//div[@class='w740']/text()").get();
                    if (StringUtils.isBlank(replayContent)) {
                        replayContent = nodes.get(i).xpath("//div[@class='yy_reply_cont']/text()").get();
                    }

                    if (StringUtils.isBlank(replayContent)) {
                        replayContent = nodes.get(i).xpath("//div[@class='w740']/allText()").get();
                    }
                    Date time = parseTime(times.get(i), page);
                    try {
                        replayContent = replayContent.replaceAll(",", "，");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (time != null) {
                        String replyLine = "回复," + replayAuthor.get(i) + "," + times.get(i) + ",汽车之家," +
                                replayContent + ",0,0,0,,," + citys.get(i) + "," + page.getUrl().get() +  ",";
                        WriteFile.write(replyLine, path);
                        System.out.println("回复写入成功！");
                    }
                }

                Date lastDate = parseTime(times.get(times.size() - 1), page);
                if (lastDate != null && lastDate.after(SearchCrawler.targetDate)) {
                    String nextPage = html.xpath("//div[@id='x-pages2']/a[@class='afpage']/@href").get();
                    if (StringUtils.isNotBlank(nextPage)) {
                        return new Request("https://club.autohome.com.cn/bbs/" + nextPage);
                    }
                }
            }
        } else {

            List<String> citys = html.xpath("//ul[@class='leftlist']/li[6]/a/text()").all();//城市
            List<Selectable> nodes = html.xpath("//div[@class='w740']").nodes();//回复
            List<String> times = html.xpath("//span[@xname='date']/text()").all(); //回复时间
            List<String> replayAuthor = html.xpath("//a[@xname='uname']/@title").all(); //回复人
            for (int i = 0; i < nodes.size(); i++) {
                String replayContent = nodes.get(i).xpath("/div[@class='w740']/text()").get();
                if (StringUtils.isBlank(replayContent)) {
                    replayContent = nodes.get(i).xpath("//div[@class='yy_reply_cont']/text()").get();
                }
                Date time = parseTime(times.get(i), page);
                if (time != null && time.after(SearchCrawler.targetDate)) {
                    String replyLine = "回复," + replayAuthor.get(i) + "," + times.get(i) + ",汽车之家," +
                            replayContent + ",0,0,0,,," + citys.get(i) + "," + page.getUrl().get() +  ",";
                    WriteFile.write(replyLine, path);
                    System.out.println("回复写入成功！");
                }
            }


            Date lastDate = parseTime(times.get(times.size() - 1), page);
            if (lastDate != null && lastDate.after(SearchCrawler.targetDate)) {
                String nextPage = html.xpath("//div[@id='x-pages2']/a[@class='afpage']/@href").get();
                if (StringUtils.isNotBlank(nextPage)) {
                    return new Request("https://club.autohome.com.cn/bbs/" + nextPage);
                }
            }
        }


        return null;
    }

    private Date parseTime (String time, Page page) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            logger.error("解析时间失败! time = {},  {}", time, page.getUrl().get());
        }
        return null;
    }

    public static void main (String[] args) throws Exception {
        String url = "https://club.autohome.com.cn/bbs/thread-c-117-30237385-1.html";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                .addHeader("host", "club.autohome.com.cn")
                .addHeader("cookie", "CYHTooltip=1; fvlid=1511148456623h6ReOHU6k5; sessionid=9B3AE573-755F-4A36-AC94-86D5B7126FF8%7C%7C2017-11-20+11%3A27%3A36.945%7C%7C0; ahpau=1; sessionuid=9B3AE573-755F-4A36-AC94-86D5B7126FF8%7C%7C2017-11-20+11%3A27%3A36.945%7C%7C0; historybbsName4=c-505%7C%E9%9B%85%E5%8A%9B%E5%A3%AB%2Cc-4480%7C%E5%A5%94%E5%A5%94mini-e%2Cc-538%7C%E5%A5%A5%E8%BF%AAA5; sessionfid=3629974514; sessionip=221.218.29.82; ahpvno=99; CNZZDATA1262640694=72599395-1512011116-https%253A%252F%252Fwww.autohome.com.cn%252F%7C1512042490; Hm_lvt_9924a05a5a75caf05dbbfb51af638b07=1512012012,1512029812; Hm_lpvt_9924a05a5a75caf05dbbfb51af638b07=1512045400; ref=www.baidu.com%7C0%7C0%7C0%7C2017-11-30+20%3A36%3A44.087%7C2017-11-30+11%3A20%3A11.515; sessionvid=9865998D-3885-469E-A7DA-BBCB17E627BF; area=110199; cn_1262640694_dplus=%7B%22distinct_id%22%3A%20%2215fd7778d321a-0960bb8a4f8996-31627c01-13c680-15fd7778d337f0%22%2C%22sp%22%3A%20%7B%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201512045407%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201512045407%7D%7D; UM_distinctid=15fd7778d321a-0960bb8a4f8996-31627c01-13c680-15fd7778d337f0")
                .execute()
                .returnContent()
                .asString();
        System.out.println(content);
//        Page page = new Page();
//        page.setRawText(content);
//        page.setRequest(new Request(url).putExtra("first", "first"));
//        page.setUrl(new Json(url));
//        ThreadListPage listPage = new ThreadListPage();
//        listPage.parseThreadList(page);

//        String s = org.apache.commons.lang.StringEscapeUtils.unescapeHtml("A5为什么没&#xec78;人买探戈红，我预定&#xec3b;探戈红，会");

//        System.out.println(s);
    }
}
