package com.lavector.collector.crawler.project.article.huxiu.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.article.Brand;
import com.lavector.collector.entity.wechatSmall.article.Article;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

/**
 * Created on 2017/12/26.
 *
 * @author zeng.zhao
 */
public class ArticlePage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.huxiu.com/article/");
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
        String title = html.xpath("//h1[@class='t-h1']/text()").get().trim();
        String author = html.xpath("//span[@class='author-name']/a/text()").get();
        String date = html.xpath("//span[@class='article-time pull-left']/text()").get();
        if (StringUtils.isBlank(date)) {
            date = html.xpath("//span[@class='article-time']/text()").get();
        }
        String favoriteCount = html.xpath("//span[@class='article-share pull-left']/text()").regex("\\d+").get();
        if (StringUtils.isBlank(favoriteCount)) {
            favoriteCount = html.xpath("//span[@class='article-share']/text()").regex("\\d+").get();
        }
        String commentCount = html.xpath("//span[@class='article-pl pull-left']/text()").regex("\\d+").get();
        if (StringUtils.isBlank(commentCount)) {
            commentCount = html.xpath("//span[@class='article-pl']/text()").regex("\\d+").get();
        }
        String content = html.xpath("//div[@class='article-content-wrap']").get();
        String likeNum = html.xpath("//div[@class='Qr-code']/div/span[@class='num']/text()").get();

        Document document = Jsoup.parse(content);
        document.select(".neirong-shouquan").remove();
        document.select(".neirong-shouquan-public").remove();

        String articleContent = document.body().child(0).toString();

        Article article = new Article();
        article.setTitle(title);
        article.setAuthor(author);
        article.setDate(date);
        article.setCommentNum(Integer.parseInt(commentCount));
        article.setContent(articleContent);
        article.setFavoriteNum(Integer.parseInt(favoriteCount));
        article.setLikeNum(StringUtils.isBlank(likeNum) ? 0 : Integer.parseInt(likeNum));
        article.setUrl(page.getUrl().get());
        article.setBrandId(Brand.brands.get(brand).longValue());
        result.addData(article);
        return result;
    }

}
