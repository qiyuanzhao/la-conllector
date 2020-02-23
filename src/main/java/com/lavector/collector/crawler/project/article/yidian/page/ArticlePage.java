package com.lavector.collector.crawler.project.article.yidian.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.article.Brand;
import com.lavector.collector.entity.wechatSmall.article.Article;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

/**
 * Created on 2017/12/29.
 *
 * @author zeng.zhao
 */
public class ArticlePage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.yidianzixun.com/article/") || url.contains("www.yidianzixun.com/mp/content");
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
        String author = page.getRequest().getExtra("author").toString();
        String date = page.getRequest().getExtra("date").toString();
        String commentNum = page.getRequest().getExtra("commentNum") == null ? "0" : page.getRequest().getExtra("commentNum").toString();
        String likeNum = page.getRequest().getExtra("likeNum") == null ? "0" : page.getRequest().getExtra("likeNum").toString();
        String content = html.xpath("//div[@class='content-bd]").get();
        String title = html.xpath("//h2/text()").get();

        if (StringUtils.isBlank(content)) {
            return result;
        }

        Article article = new Article();
        article.setUrl(page.getUrl().get());
        article.setTitle(title);
        article.setDate(date);
        article.setContent(content);
        article.setCommentNum(Integer.parseInt(commentNum));
        article.setAuthor(author);
        article.setFavoriteNum(Integer.parseInt(likeNum));
        article.setBrandId(Brand.brands.get(brand).longValue());
        result.addData(article);
        return result;
    }
}
