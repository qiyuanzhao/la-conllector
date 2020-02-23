package com.lavector.collector.crawler.project.sport.ufc.cn.page;

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
import java.util.List;

/**
 * Created on 2017/10/19.
 *
 * @author zeng.zhao
 */
public class UFCNewsPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://ufc.cn/news/\\d+-\\d+-\\d+/news.*.shtml", url);
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
        String title = html.xpath("//div[@class='arti-hgroup']/h2/text()").get();
        String timePublishInfo = html.xpath("//div[@class='arti-hgroup']/h3/text()").get();
        String timePublishReplace1 = timePublishInfo.replace("年", "-");
        String timePublishReplace2 = timePublishReplace1.replace("月", "-");
        String timePublish = timePublishReplace2.replace("日", " ");
        String content = html.xpath("//div[@class='arti-p']/allText()").get();
        List<String> contentInfos = html.xpath("//div[@class='arti-p']/p/text()").all();
        String author = null;
        String originSource = null;
        for (int i = 0; i < contentInfos.size(); i++) {
            if (i == contentInfos.size() - 2) {
                String authorInfo = contentInfos.get(i)
                        .substring(contentInfos.get(i).lastIndexOf("（") + 1, contentInfos.get(i).lastIndexOf("）"));
                if (StringUtils.isNotBlank(authorInfo)) {
                    originSource = authorInfo.split(" ")[0];
                    author = authorInfo.split(" ")[1];
                    break;
                }
            }
        }
        news.setTitle(title);
        news.setContent(content);
        news.setAuthor(author);
        news.setOriginSource(originSource);
        news.setCategory(CategoryConstant.SPORT);
        news.setUrl(page.getUrl().get());
        news.setSource("UFC中国");
        try {
            news.setTimePublish(DateUtils.parseDate(timePublish, "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            throw new IllegalStateException("parse timePublish error: " + timePublish);
        }
        return news;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://ufc.cn/news/2017-10-09/news-ifymrcmm9516394.shtml";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString(Charset.forName("utf-8"));
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setUrl(new Json(url));
        UFCNewsPage newsPage = new UFCNewsPage();
        newsPage.parse(page);
    }
}
