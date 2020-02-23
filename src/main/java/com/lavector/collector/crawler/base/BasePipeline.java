package com.lavector.collector.crawler.base;

import com.google.common.base.CharMatcher;
import com.lavector.collector.entity.crawlerTask.CrawlerTask;
import com.lavector.collector.entity.data.News;
import com.lavector.collector.entity.data.NewsService;
import com.lavector.collector.entity.wechatSmall.article.Article;
import com.lavector.collector.entity.wechatSmall.article.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.LinkedList;
import java.util.List;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BasePipeline implements Pipeline {
    @Autowired
    NewsService newsService;

    @Autowired
    private ArticleService articleService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        PageParse.Result result = resultItems.get(ContextKey.RESULT);
        MySpider mySpider;
        if (task instanceof MySpider) {
            mySpider = (MySpider) task;
        } else {
            throw new IllegalArgumentException("spider is not MySpider ");
        }
        CrawlerInfo crawlerInfo = mySpider.getCrawlerInfo();
        CrawlerTask crawlerTask = mySpider.getCrawler().getCrawlerTask();
        List<News> datas = result.getDatas(News.class);
        if (datas != null) {
            for (News data : datas) {
                if (data.getContent() != null) {
                    String content = data.getContent();
                    content = content.replaceAll("[ 　]", "\n");
                    content = removeEmptyLine(content);
                    data.setContent(content);
                    data.setTitle(data.getTitle().trim());
                    data.setTaskId(crawlerTask.getId());
                }
                newsService.save(data);
            }
        }

        List<Article> articles = result.getDatas(Article.class);
        if (articles != null) {
            for (Article article : articles) {
                if (StringUtils.isNotBlank(article.getContent())) {
                    articleService.save(article);
                }
            }
        }
    }

    private static String removeEmptyLine(String s) {
        String[] array = s.split("\n");
        List<String> strings = new LinkedList<>();
        for (String s1 : array) {
            s1 = CharMatcher.whitespace().removeFrom(s1);
            if (s1.trim().length() > 0) {
                strings.add(s1);
            }
        }
        return String.join("\n", strings);
    }
}
