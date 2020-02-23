package com.lavector.collector.crawler.project.article.tianya.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.article.Brand;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.wechatSmall.article.Article;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

/**
 * Created on 2017/12/26.
 *
 * @author zeng.zhao
 */
public class ArticlePage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch(".*bbs.tianya.cn/post.*.shtml", url);
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String brand = page.getRequest().getExtra("brand").toString();
        String title = html.xpath("//h1[@class='atl-title']/span[@class='s_title']/span/text()").get();
        if (StringUtils.isBlank(title)) {
            title = html.xpath("//div[@class='q-title']/h1/span/text()").get();
        }
        String author = html.xpath("//div[@class='atl-info']/span[1]/a/text()").get();
        if (StringUtils.isBlank(author)) {
            author = html.xpath("//div[@class='q-info']/span[@class='ml5']/a/text()").get();
        }
        String date = html.xpath("//div[@class='atl-info']/span[2]/text()").regex("\\d+-\\d+-\\d+ \\d+:\\d+:\\d+").get();
        if (StringUtils.isBlank(date)) {
            date = html.xpath("//div[@class='q-info']/span[@class='ml5']/text()").regex("\\d+-\\d+-\\d+ \\d+:\\d+:\\d+").get();
        }
        String readNum = html.xpath("//div[@class='atl-info']/span[3]/text()").regex("\\d+").get();
        if (StringUtils.isBlank(readNum)) {
            readNum = html.xpath("//div[@class='q-info']/span[@class='ml5']/text()").regex("点击：(\\d+)").get();
        }
        String commentNum = html.xpath("div[@class='atl-info']/span[4]/text()").regex("\\d+").get();
        if (StringUtils.isBlank(commentNum)) {
            commentNum = html.xpath("//div[@class='q-info']/span[@class='ml5']/text()").regex("回复：(\\d+)").get();
        }
        String content = html.xpath("//div[@class='bbs-content clearfix']/text()").get();
        if (StringUtils.isBlank(content)) {
            content = html.xpath("//div[@class='text']/text()").get();
        }

        Article article = new Article();
        article.setAuthor(author);
        article.setBrandId(Brand.brands.get(brand).longValue());
        article.setCommentNum(Integer.parseInt(commentNum));
        article.setContent(content);
        article.setReadNum(Integer.parseInt(readNum));
        article.setDate(date);
        article.setTitle(title);
        article.setUrl(page.getUrl().get());
        result.addData(article);
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://bbs.tianya.cn/post-feeling-4322830-1-1.shtml";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRequest(new us.codecraft.webmagic.Request(url).putExtra("brand", "brand"));
        page.setRawText(content);
        page.setUrl(new Json(url));
        ArticlePage articlePage = new ArticlePage();
        articlePage.parse(page);
    }
}
