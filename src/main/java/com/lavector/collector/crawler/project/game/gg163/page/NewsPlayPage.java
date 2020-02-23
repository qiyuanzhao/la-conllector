package com.lavector.collector.crawler.project.game.gg163.page;

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
public class NewsPlayPage implements PageParse {
    private static Logger logger = LoggerFactory.getLogger(NewsPlayPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https*:\\/\\/play\\.163\\.com\\/\\d{2}\\/\\d{4}\\/\\d{2}\\/\\w+\\.html", url)
                || RegexUtil.isMatch("https*:\\/\\/gg\\.163\\.com\\/\\d{2}\\/\\d{4}\\/\\d{2}\\/\\w+\\.html", url);
    }

    @Override
    public Result parse(Page page) {
        if (page.getHtml().xpath("//div[@class='article-wrap']").match()) {
            Html html = page.getHtml();
            Selectable header = html.xpath("//div[@class='article-wrap']");
            String title = header.xpath("//h1/text()").get();
            String author = header.xpath("//div[@class='article-about']/span[1]/text()").get().trim().replaceAll("作者[:：]", "").trim();
            String originSource = header.xpath("//div[@class='article-about']/span[2]/allText()").get().replaceAll("来源[:：]", "").trim();
            String time = header.xpath("//div[@class='article-about']/span[3]/text()").get().trim();
            String content = html.xpath("//div[@id='endText']/tidyText()").get();
            News news = new News();
            news.setUrl(page.getRequest().getUrl());
            news.setTitle(title);
            news.setAuthor(author);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = simpleDateFormat.parse(time);
                news.setTimePublish(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            news.setOriginSource(originSource);
            news.setContent(content);
            news.setSource("易竞技");
            news.setCategory(CategoryConstant.EGAME);
            return Result.get().addData(news);

        } else if (page.getHtml().xpath("//div[@class='muban-spec']").match()) {
            Html html = page.getHtml();
            String title = html.xpath("//h1[@class='article-h1']/text()").get();
            String author = html.xpath("//span[@id='aut']/text()").get();
            String originSource = "视界";
            String time = html.xpath("//span[@class='time']/text()").get().replaceAll("时间：", "").trim();
            String content = html.xpath("//div[@id='endText']/tidyText()").get();
            News news = new News();
            news.setUrl(page.getRequest().getUrl());
            news.setTitle(title);
            news.setAuthor(author);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = simpleDateFormat.parse(time);
                news.setTimePublish(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            news.setOriginSource(originSource);
            news.setContent(content);
            news.setSource("爱玩网");
            news.setCategory(CategoryConstant.EGAME);
            return Result.get().addData(news);
        } else if (page.getHtml().xpath("//ul[@class='game-navigation']").match()) {

            Html html = page.getHtml();
            Selectable contentS = html.xpath("//div[@class='article-content']");
            String title = contentS.xpath("//h1/text()").get();
            String author = contentS.xpath("//div[@class='article-metadata']/span[1]/text()").get().trim().replaceAll("作者[:：]", "").trim();
            String originSource = contentS.xpath("//div[@class='article-metadata']/span[2]/allText()").get().replaceAll("来源[:：]", "").trim();
            String time = contentS.xpath("//div[@class='article-metadata']/span[3]/text()").get().trim();
            String content = html.xpath("//div[@id='endText']/tidyText()").get();
            News news = new News();
            news.setUrl(page.getRequest().getUrl());
            news.setTitle(title);
            news.setAuthor(author);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(time);
                news.setTimePublish(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            news.setOriginSource(originSource);
            news.setContent(content);
            news.setSource("爱玩网");
            news.setCategory(CategoryConstant.EGAME);
            return Result.get().addData(news);
        } else {
            throw new IllegalArgumentException("unknow page parse");
        }
    }

    @Override
    public String pageName() {
        return null;
    }


    public static void main(String[] args) throws Exception {
        String url = "http://play.163.com/17/0925/15/CV6IOQFB00318QCD.html";
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
        NewsPlayPage sportTop = new NewsPlayPage();

        sportTop.parse(page);
    }
}
