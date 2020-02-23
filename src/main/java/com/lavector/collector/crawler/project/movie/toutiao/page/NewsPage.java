package com.lavector.collector.crawler.project.movie.toutiao.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.def.Site;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

import java.text.ParseException;
import java.util.List;

/**
 * Created on 2017/10/25.
 *
 * @author zeng.zhao
 */
public class NewsPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.toutiao.com/a\\d+/", url);
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

    private News parseNewsPage(Page page) {
        News news = new News();
        Html html = page.getHtml();
        if (page.getRawText().contains("chineseTag: '图片'")) {
            return null;
        }
        String title = page.getRequest().getExtra("title").toString();
        String content = html.xpath("//div[@class='article-content']/div/allText()").get();
        if (StringUtils.isBlank(content)) {
            String baseData = getBaseData(html);
            String contentCode = StringUtils.substring(baseData, baseData.indexOf("content") + 10, baseData.indexOf("'.replace"));
            String contentInfo = transferCode(contentCode);
            Html contentHtml = createHtml(contentInfo);
            content = contentHtml.xpath("//body/allText()").get();
        }
        String originSource = html.xpath("//div[@class='articleInfo']/span[@class='original']/text()").get();
        String author = html.xpath("//div[@class='articleInfo']/span[@class='src']/text()").get();
        if (StringUtils.isBlank(author)) {
            author = html.regex("isOriginal:.*source: '(.*)',.*time:").get();
        }
        String timePublish = html.xpath("//div[@class='articleInfo']/span[@class='time']/text()").get();
        if (StringUtils.isBlank(timePublish)) {
            timePublish = html.regex("isOriginal:.*source:.*time: '(\\d+-\\d+-\\d+ \\d+:\\d+)'").get();
        }
        news.setContent(content);
        news.setTitle(title);
        news.setAuthor(author);
        try {
            news.setTimePublish(DateUtils.parseDate(timePublish, "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            throw new IllegalStateException("parse timePublish error : " + timePublish);
        }
        news.setOriginSource(originSource);
        news.setUrl(page.getUrl().get());
        news.setCategory(CategoryConstant.MOVIE);
        news.setSource("今日头条电影频道");
        news.setSite(Site.TOUTIAO);
        return news;
    }

    /**
     * 页面结构改变
     *
     * @param html
     * @return
     */
    private String getBaseData(Html html) {
        StringBuilder builder = new StringBuilder();
        List<String> scripts = html.xpath("//script").all();
        for (String script : scripts) {
            if (StringUtils.contains(script, "var BASE_DATA")) {
                builder.append(script);
                break;
            }
        }
        return builder.toString();
    }

    private String transferCode(String content) {
        String var1 = StringUtils.replace(content, "&lt;", "<");
        String var2 = StringUtils.replace(var1, "&gt;", ">");
        String var3 = StringUtils.replace(var2, "&quot;", "\"");
        return var3;
    }

    private Html createHtml(String htmlCode) {
        return new Html(htmlCode);
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.toutiao.com/a6480662659350921742/";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new PlainText(url));
        page.setRawText(content);
        page.setRequest(new Request(url).putExtra("title", ""));
        NewsPage newsPage = new NewsPage();
        newsPage.parse(page);
    }
}
