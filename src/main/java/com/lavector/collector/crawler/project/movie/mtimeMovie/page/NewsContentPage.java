package com.lavector.collector.crawler.project.movie.mtimeMovie.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
public class NewsContentPage implements PageParse {
    private static Logger logger = LoggerFactory.getLogger(NewsContentPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https*:\\/\\/news\\.mtime\\.com\\/\\d{4}\\/\\d{2}\\/\\d{2}\\/\\d+\\.html", url);
    }

    @Override
    public Result parse(Page page) {
        Html html = page.getHtml();
        Selectable header = html.xpath("//div[@class='newsheader ' or @class='newsheader newswhite']");
        String title = header.xpath("//div[@class='newsheadtit']/h2/text()").get();
        String time = header.xpath("//p/text()").get().trim();
        String originSource = header.xpath("//p/span[@class='ml15']/a/text()").get().trim().replaceAll("来源：", "");
        String content = html.xpath("//div[@id='newsContent']/allText()").get();
        String abstractText = html.xpath("//div[@class='newsnote']/allText()").get();
        News news = new News();
        news.setUrl(page.getRequest().getUrl());
        news.setTitle(title);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            news.setTimePublish(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        news.setOriginSource(originSource);
        news.setContent(content);
        news.setIntroduction(abstractText);
        news.setSource("时光网");
        news.setCategory(CategoryConstant.MOVIE);

        return Result.get().addData(news);
    }

    @Override
    public String pageName() {
        return null;
    }


    public static void main(String[] args) throws Exception {
        String url = "http://news.mtime.com/2017/09/28/1574010.html";
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
        NewsContentPage sportTop = new NewsContentPage();

        sportTop.parse(page);
    }
}
