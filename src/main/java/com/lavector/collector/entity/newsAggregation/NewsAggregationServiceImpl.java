package com.lavector.collector.entity.newsAggregation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.entity.category.Category;
import com.lavector.collector.entity.category.CategoryService;
import com.lavector.collector.entity.data.News;
import com.lavector.collector.entity.data.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 29/09/2017.
 *
 * @author seveniu
 */
@Service
public class NewsAggregationServiceImpl implements NewsAggregationService {
    @Autowired
    NewsAggregationRepository newsAggregationRepository;
    @Autowired
    NewsService newsService;
    @Autowired
    CategoryService categoryService;

    private Long lastId;
    private Map<String, List<List<News>>> dataMap;

    @Override
    public NewsAggregation getByLast() {
        return newsAggregationRepository.findFirstByOrderByIdDesc();
    }

    @Override
    public NewsAggregation getLastOneByCategoryAndType(Long cid, NewsAggregationType type) {
        return newsAggregationRepository.findTopByCidAndTypeOrderByTimeCreatedDesc(cid, type);
    }

    @Override
    public Map<String, List<List<News>>> getData() {
        Long lastId = newsAggregationRepository.getLastId();
        if (!Objects.equals(lastId, this.lastId)) {
            this.dataMap = getDataMap();
        }
        return dataMap;
    }

    @Override
    public List<List<News>> getLastByCategoryAndType(Long cid, NewsAggregationType type) {
        NewsAggregation newsAggregation = getLastOneByCategoryAndType(cid, type);
        List<NewsAggregation.SortAggregationData> aggregationDataListList = newsAggregation.getAggregationData();
        return aggregationDataListList.stream().map(
                newsAggregationDataList -> newsService.getNewsByIds(newsAggregationDataList.getIds())
        ).collect(Collectors.toList());
    }

    @Override
    public List<List<News>> multiCategoryAggregation(Long cid, NewsAggregationType type) {
        NewsAggregation newsAggregation = getLastOneByCategoryAndType(cid, type);
        List<NewsAggregation.SortAggregationData> aggregationDataList = new LinkedList<>();
        if (newsAggregation != null) {
            aggregationDataList.addAll(newsAggregation.getAggregationData());
        }
        List<Category> children = categoryService.getAllChildren(cid);
        for (Category child : children) {
            NewsAggregation childAggregation = getLastOneByCategoryAndType(child.getId(), type);
            aggregationDataList.addAll(childAggregation.getAggregationData());
        }
        List<List<Long>> idss = aggregationDataList.stream().sorted((h1, h2) -> h1.getScore() > h2.getScore() ? 1 : 0).map(d -> d.getIds()).collect(Collectors.toList());
        return idss.stream().map(ids -> ids.stream().map(id -> newsService.getOne(id)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList())).collect(Collectors.toList());
    }


    @Override
    public Map<String, List<List<Long>>> getLastDataMap() {
        NewsAggregation newsAggregation = getByLast();
        String aggregation = newsAggregation.getAggregation();
        ObjectMapper objectMapper = JsonMapper.buildNonNullBinder().getMapper();
        try {
            return objectMapper.readValue(aggregation, new TypeReference<Map<String, List<List<Long>>>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, List<List<News>>> getDataMap() {
        NewsAggregation newsAggregation = getByLast();
        lastId = newsAggregation.getId();
        String aggregation = newsAggregation.getAggregation();
        ObjectMapper objectMapper = JsonMapper.buildNonNullBinder().getMapper();
        try {
            Map<String, List<List<Long>>> typeIdss = objectMapper.readValue(aggregation, new TypeReference<Map<String, List<List<Long>>>>() {
            });
            Map<String, List<List<News>>> dataMap = new HashMap<>();
            for (String category : typeIdss.keySet()) {
                dataMap.put(category, typeIdss.get(category).stream().map(
                        ids -> ids.stream().map(id -> {
                            Optional<News> news = newsService.getOne(id);
                            return news.orElse(null);
                        }).collect(Collectors.toList())
                ).collect(Collectors.toList()));
            }
            return dataMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
