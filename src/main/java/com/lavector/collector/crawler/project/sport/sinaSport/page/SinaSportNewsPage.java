package com.lavector.collector.crawler.project.sport.sinaSport.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
public class SinaSportNewsPage implements PageParse {
    private static Logger logger = LoggerFactory.getLogger(SinaSportNewsPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http:\\/\\/sports\\.sina\\.com\\.cn\\/.+\\.shtml", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = new Result();
//        List<Selectable> titles = html.xpath("//div[@class='phdnews_hdline']//a").nodes();
//        titles.forEach(v -> {
//            News news = new News();
//            news.setTitle(v.xpath("//a/text()").get());
//            news.setUrl(v.xpath("//a/@href").get());
//            news.setCategory(CategoryConstant.SPORT);
//            //TODO:时间比较麻烦
//            logger.info("get news : {}", news);
//            result.addData(news);
//            result.addRequest(new Request());
//        });

        News news = new News();
        news.setUrl(page.getRequest().getUrl());
        Html html = page.getHtml();
        news.setTitle(html.xpath("//article/h1/allText()").get());
        // date
        String dateStr = html.xpath("//article//span[@class='article-a__time']/text()").get();
        if (dateStr != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
            try {
                news.setTimePublish(dateFormat.parse(dateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // origin source
        news.setOriginSource(html.xpath("//article//span[@class='article-a__source']/allText()").get());
        news.setSource("新浪体育");
        news.setContent(html.xpath("//div[@id='artibody']/tidyText()").get());
        news.setCategory(CategoryConstant.SPORT);
        result.addData(news);
        logger.info("get news : {}", news.getTitle());
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }


//    public static void main(String[] args) throws Exception {
//        String url = "http://sports.sina.com.cn/cba/2017-09-27/doc-ifymesii5911379.shtml";
//        String content = org.apache.http.client.fluent.Request.Get(url)
////                .addHeader("referer", referer)
//                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
////                .addHeader("cookie", "UM_distinctid=15b8efcb50f1-0453e4c1a78ff6-f333168-1fa400-15b8efcb510143; uuid=\"w:7bc393ed442e4c0c91e1b04137afe76d\"; sso_login_status=0; _ba=BA0.2-20170421-51d9e-lUjPTRpy6vyjzeSe2aDJ; tt_webid=57974723281; utm_source=toutiao; __utma=24953151.454786688.1492752578.1497855275.1498203009.4; __utmb=24953151.5.10.1498203009; __utmc=24953151; __utmz=24953151.1493801814.2.2.utmcsr=toutiao.com|utmccn=(referral)|utmcmd=referral|utmcct=/search_content/; csrftoken=8f3d44c18a9ceb56d75800e3362477e7; WEATHER_CITY=%E5%8C%97%E4%BA%AC; _ga=GA1.2.454786688.1492752578; _gid=GA1.2.184813642.1498014394; CNZZDATA1259612802=885820200-1492750758-null%7C1498202969; __tasessionId=l8hjwvols1498202251217")
//                .execute()
//                .returnContent()
//                .asString(Charset.forName("utf-8"));
//        Page page = new Page();
//        page.setRequest(new Request(url));
//        page.setRawText(content);
//        SinaSportNewsPage sportTop = new SinaSportNewsPage();
//
//        sportTop.parse(page);
//    }

    public static void main(String[] args) throws ParseException {
        String url = "http://sports.sina.com.cn/china/afccl/2017-09-27/doc-ifymfcih6379686.shtml";
        System.out.println(RegexUtil.isMatch("http:\\/\\/sports\\.sina\\.com\\.cn\\/.+\\.shtml", url));
        String dateS = "2017年09月27日07:53";
//        dateS = dateS.replace("年","-");
//        dateS = dateS.replace("月","-");
//        dateS = dateS.replace("日"," ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
        Date date = dateFormat.parse(dateS);
        System.out.println(date);
    }
}
