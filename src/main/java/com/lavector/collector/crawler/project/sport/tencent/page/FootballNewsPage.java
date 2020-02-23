package com.lavector.collector.crawler.project.sport.tencent.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created on 2017/10/18.
 *
 * @author zeng.zhao
 */
public class FootballNewsPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://sports.qq.com/a/\\d+/\\d+\\.htm", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        News news = parseNewsPage(page);
        if (news != null) {
            result.addData(news);
        }
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private News parseNewsPage (Page page) {
        News news = new News();
        Html html = page.getHtml();
        List<String> contentList = html.xpath("//p[@class='text']/text()").all();
        if (CollectionUtils.isEmpty(contentList)) {
            return null;
        }
        String content = contentList.stream().reduce((s1, s2) -> s1 + s2).get();
        String author = html.xpath("//div[@id='QQeditor']/text()").regex("：(.*)").get();
        String title = html.xpath("//h2[@class='rv-title']/a/text()").get();
        String originSource = html.xpath("//div[@class='a_Info']/span[@class='a_source']/allText()").get();
        String timePublishStr = html.xpath("//div[@class='a_Info']/span[@class='a_time']/text()").get();
        news.setContent(content);
        news.setTitle(title);
        news.setAuthor(author);
        news.setOriginSource(originSource);
        try {
            news.setTimePublish(DateUtils.parseDate(timePublishStr, "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            throw new IllegalStateException("parse timePublish error: " + timePublishStr);
        }
        news.setCategory(CategoryConstant.SPORT);
        news.setSource("腾讯体育");
        news.setUrl(page.getUrl().get());
        return news;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://sports.qq.com/a/20171016/035316.htm";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setUrl(new Json(url));
        FootballNewsPage newsPage = new FootballNewsPage();
        newsPage.parseNewsPage(page);
    }
}
