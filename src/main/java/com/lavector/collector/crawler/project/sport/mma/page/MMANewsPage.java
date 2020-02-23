package com.lavector.collector.crawler.project.sport.mma.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.nio.charset.Charset;
import java.text.ParseException;

/**
 * Created on 2017/10/18.
 *
 * @author zeng.zhao
 */
public class MMANewsPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.vs.cm/news/\\d+/", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addData(parseNewsPage(page));
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private News parseNewsPage (Page page) {
        News news = new News();
        Html html = page.getHtml();
        String title = html.xpath("//div[@class='list']/h1/text()").get();
        String timePublishStr = html.xpath("//div[@class='list']/div[@class='content']/div[1]/text()").regex("\\d+年\\d+月\\d+日").get();
        String timePublish = null;
        if (StringUtils.isNoneBlank(timePublishStr)) {
            String timePublish1 = timePublishStr.replace("年", "-");
            String timePublish2 = timePublish1.replace("月", "-");
            timePublish = timePublish2.replace("日", "");
        }
        String author = html.xpath("//div[@class='list']/div[@class='content']/div[1]/text()").regex("作者：(.*)").get();
        String contentInfo = html.xpath("//div[@class='content']/allText()").get().trim();
        String content = contentInfo;
        if (content.contains("点击：加载中")) {
            content = new Json(contentInfo).regex("(.*)点击：加载中").get();
        }
        news.setUrl(page.getUrl().get());
        news.setSource("MMA/UFC");
        news.setCategory(CategoryConstant.SPORT);
        news.setAuthor(author);
        try {
            news.setTimePublish(DateUtils.parseDate(timePublish, "yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        news.setTitle(title);
        news.setContent(content);
        return news;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://www.vs.cm/news/14368/" +
                "";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString(Charset.forName("gbk"));
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setRawText(content);
        MMANewsPage newsPage = new MMANewsPage();
        newsPage.parseNewsPage(page);
    }
}
