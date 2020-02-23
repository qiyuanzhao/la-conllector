package com.lavector.collector.entity.newsAggregation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created on 29/09/2017.
 *
 * @author seveniu
 */
public interface NewsAggregationRepository extends JpaRepository<NewsAggregation, Long> {
    NewsAggregation findFirstByOrderByIdDesc();

    @Query(nativeQuery = true, value = "select id from news_aggregation order by id desc limit 1")
    Long getLastId();

//    @Query(nativeQuery = true, value = "select id from news_aggregation order by id desc limit 1")
    NewsAggregation findTopByCidAndTypeOrderByTimeCreatedDesc(Long cid, NewsAggregationType type);
}
