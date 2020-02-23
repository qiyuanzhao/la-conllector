package com.lavector.collector.crawler.project.movie.qqMovie.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
public class MovieNewsContent implements PageParse {
    private static Logger logger = LoggerFactory.getLogger(MovieNewsContent.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https*:\\/\\/ent\\.qq\\.com\\/a\\/\\d{8}\\/\\d+\\.htm.*", url);
    }

    @Override
    public Result parse(Page page) {
        Selectable article = page.getHtml().xpath("//div[@class='qq_article']");
        String title = article.xpath("//div[@class='hd']/h1/text()").get();
        if (title == null) {
            title = page.getHtml().xpath("//meta[@name='Description']/@content").get();
        }
        String time = article.xpath("//div[@class='hd']//div[@class='a_Info']/span[@class='a_time']/text()").get();
        String originSource = article.xpath("//div[@class='hd']//div[@class='a_Info']/span[@class='a_source']/allText()").get();
        String content = article.xpath("//div[@id='Cnt-Main-Article-QQ']/tidyText()").get();

        Result result = new Result();
        News news = new News();
        news.setTitle(title);
        try {
            Date date;
            if (time == null) {
                time = RegexUtil.findFirstGroup(page.getRawText(), "pubtime:'(\\d{4}年\\d{2}月\\d{2}日\\d{2}:\\d{2})'");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
                date = simpleDateFormat.parse(time);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                date = simpleDateFormat.parse(time);
            }
            news.setTimePublish(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        news.setOriginSource(originSource);
        news.setContent(parseContent(content));
        news.setCategory(CategoryConstant.MOVIE);
        news.setSource("腾讯娱乐");
        news.setUrl(page.getRequest().getUrl());
        result.addData(news);
        return result;
    }

    private String parseContent(String content) {
        if (content == null) {
            return null;
        }
        content = content.replaceAll("自动播放开关", "");
        content = content.replaceAll("自动播放", "");
        content = content.replaceAll("正在加载\\.\\.\\.", "");
        content = content.replaceAll(" ", "");
        content = content.replaceAll("", "");
        content = content.replaceAll("<.*>", "");
        Pattern pattern = Pattern.compile("([^\\n])[\\n]([^\\n])");
        Matcher m = pattern.matcher(content);
        return m.replaceAll("$1$2");
    }

    @Override
    public String pageName() {
        return null;
    }


    public static void main(String[] args) throws Exception {
        String url = "http://ent.qq.com/a/20170920/019936.htm";
        String content = org.apache.http.client.fluent.Request.Get(url)
//                .addHeader("referer", referer)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
//                .addHeader("cookie", "UM_distinctid=15b8efcb50f1-0453e4c1a78ff6-f333168-1fa400-15b8efcb510143; uuid=\"w:7bc393ed442e4c0c91e1b04137afe76d\"; sso_login_status=0; _ba=BA0.2-20170421-51d9e-lUjPTRpy6vyjzeSe2aDJ; tt_webid=57974723281; utm_source=toutiao; __utma=24953151.454786688.1492752578.1497855275.1498203009.4; __utmb=24953151.5.10.1498203009; __utmc=24953151; __utmz=24953151.1493801814.2.2.utmcsr=toutiao.com|utmccn=(referral)|utmcmd=referral|utmcct=/search_content/; csrftoken=8f3d44c18a9ceb56d75800e3362477e7; WEATHER_CITY=%E5%8C%97%E4%BA%AC; _ga=GA1.2.454786688.1492752578; _gid=GA1.2.184813642.1498014394; CNZZDATA1259612802=885820200-1492750758-null%7C1498202969; __tasessionId=l8hjwvols1498202251217")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRequest(new Request(url));
        page.setRawText(content);
        MovieNewsContent sportTop = new MovieNewsContent();

        sportTop.parse(page);
    }
}
