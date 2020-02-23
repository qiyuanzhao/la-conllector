package com.lavector.collector.entity.newsTop;

import com.lavector.collector.entity.newsAggregation.NewsAggregationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
@Repository
public interface NewsTopRepository extends JpaRepository<NewsTop, Long> {

    NewsTop findByCidAndTypeAndDate(Long cid, NewsAggregationType type, String date);
}
