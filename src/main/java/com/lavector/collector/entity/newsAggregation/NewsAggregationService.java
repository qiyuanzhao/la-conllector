package com.lavector.collector.entity.newsAggregation;

import com.lavector.collector.entity.data.News;

import java.util.List;
import java.util.Map;

/**
 * Created on 29/09/2017.
 *
 * @author seveniu
 */
public interface NewsAggregationService {
    NewsAggregation getByLast();

    NewsAggregation getLastOneByCategoryAndType(Long cid, NewsAggregationType type);

    Map<String, List<List<News>>> getData();

    List<List<News>> getLastByCategoryAndType(Long cid, NewsAggregationType type);

    List<List<News>> multiCategoryAggregation(Long cid, NewsAggregationType type);

    Map<String, List<List<Long>>> getLastDataMap();

}
