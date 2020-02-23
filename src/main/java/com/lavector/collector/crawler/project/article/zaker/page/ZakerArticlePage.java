package com.lavector.collector.crawler.project.article.zaker.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.article.Brand;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.crawler.util.TimeUtils;
import com.lavector.collector.entity.wechatSmall.article.Article;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created on 2018/1/2.
 *
 * @author zeng.zhao
 */
public class ZakerArticlePage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.myzaker.com/article/");
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
        String title = html.xpath("//div[@class='article_header']/h1/text()").get();
        String author = html.xpath("//span[@class='auther']/text()").get();
        String date = html.xpath("//span[@class='time']/text()").get();
        String content = html.xpath("//div[@class='article_content']/").get();
        if (StringUtils.contains(content, "data-original")) { // 图片懒加载
            content = content.replaceAll("data-original=", "src=");
        }
        List<String> comments = html.xpath("//div[@class='comment_item']/").all();
        Integer commentNum = comments == null ? 0 : comments.size();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String timePublish = simpleDateFormat.format(new Date());
        if (!date.contains("刚刚")) {
            timePublish = simpleDateFormat.format(TimeUtils.timeProcess(date));
        }
        Article article = new Article();
        article.setAuthor(author);
        article.setContent(content);
        article.setDate(timePublish);
        article.setTitle(title);
        article.setBrandId(Brand.brands.get(brand).longValue());
        article.setCommentNum(commentNum);
        article.setUrl(page.getUrl().get());
        result.addData(article);
        return result;
    }

    public static void main(String[] args) {
        String s = "1995-02-02";
        boolean match = RegexUtil.isMatch(".*[\\u4e00-\\u9fa5].*", s);
        System.out.println(match);
    }
}
