package com.lavector.collector.web;

import com.lavector.collector.entity.crawlerTask.CrawlerTaskService;
import com.lavector.collector.entity.data.News;
import com.lavector.collector.entity.data.NewsService;
import com.lavector.collector.entity.newsAggregation.NewsAggregationService;
import com.lavector.collector.entity.newsAggregation.NewsAggregationType;
import com.lavector.collector.entity.newsTop.NewsTop;
import com.lavector.collector.entity.newsTop.NewsTopService;
import com.lavector.collector.entity.source.OriginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 29/09/2017.
 *
 * @author seveniu
 */
@RestController
@RequestMapping("/api/news-top")
@CrossOrigin
public class NewsTopApi {
    @Autowired
    NewsAggregationService newsAggregationService;
    @Autowired
    CrawlerTaskService crawlerTaskService;
    @Autowired
    NewsTopService newsTopService;
    @Autowired
    NewsService newsService;
    @Autowired
    OriginService originService;

    @RequestMapping(value = "/{category}/24", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<List<News>> getNews(@PathVariable("category") String category) {
        return newsAggregationService.getData().get(category);
    }

    @RequestMapping(value = "/{category}/{type}/{date}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public NewsTop getTopNewsList(@PathVariable("category") Long cid, @PathVariable("type") String type, @PathVariable("date") String date) {
        return newsTopService.getByCategory(cid, NewsAggregationType.valueOf(type), date);
    }

    @RequestMapping(value = "/update/{topId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public NewsTop updateTopNews(@PathVariable("topId") Long topId, @RequestBody String data) {
        NewsTop newsTop = newsTopService.getById(topId);
        newsTop.setTopList(data);
        return newsTopService.save(newsTop);
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void send() {
        newsTopService.sendEmail();
    }
}
