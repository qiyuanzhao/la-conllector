package com.lavector.collector.crawler.project.article.weixin.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.entity.wechatSmall.article.Article;
import com.lavector.collector.thirdpartyapi.weixin.entity.ArticleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

/**
 * Created on 2018/1/8.
 *
 * @author zeng.zhao
 */
public class SouGouWeiXinArticlePage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(SouGouWeiXinArticlePage.class);

    @Override
    public boolean handleUrl(String url) {
        return url.contains("mp.weixin.qq.com/s?__biz=");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        ArticleEntity articleEntity = (ArticleEntity) page.getRequest().getExtra("articleEntity");
        String content = html.xpath("//div[@id='js_content']/").get();
        String author = html.xpath("//a[@id='post-user']/text()").get();

        Article article = new Article();
        article.setUrl(page.getUrl().get());
        article.setTitle(articleEntity.getTitle());
        article.setDate(articleEntity.getPub_time());
        article.setAuthor(author);
        article.setContent(content.replaceAll("data-src=", "src="));
        article.setReadNum(articleEntity.getRead_num());
        article.setLikeNum(articleEntity.getLike_num());

        logger.info("title : " + article.getTitle());
        return result;
    }
}
