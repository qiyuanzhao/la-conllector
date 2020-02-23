package com.lavector.collector.web;

import com.lavector.collector.entity.data.News;
import com.lavector.collector.entity.data.NewsService;
import com.lavector.collector.entity.newsAggregation.NewsAggregationService;
import com.lavector.collector.entity.newsAggregation.NewsAggregationType;
import com.lavector.collector.entity.newsTop.NewsTopService;
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
@RequestMapping("/api/news")
@CrossOrigin
public class NewsApi {
    @Autowired
    NewsAggregationService newsAggregationService;
    @Autowired
    NewsService newsService;
    @Autowired
    NewsTopService newsTopService;

//    @RequestMapping(value = "/{category}/24", method = RequestMethod.GET)
//    @ResponseStatus(HttpStatus.OK)
//    public List<List<News>> getNews(@PathVariable("category") String category) {
//        return newsAggregationService.getData().get(category);
//    }

//    @RequestMapping(value = "/{category}/24", method = RequestMethod.GET)
//    @ResponseStatus(HttpStatus.OK)
//    public List<List<News>> getNews(@PathVariable("category") String category) {
//        return newsService.getLastByCategoryAndType();
//    }

    @RequestMapping(value = "/{category}/{type}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<List<News>> getNews(@PathVariable("category") Long category, @PathVariable("type") String type) {
        return newsAggregationService.multiCategoryAggregation(category, NewsAggregationType.valueOf(type));

    }

}
