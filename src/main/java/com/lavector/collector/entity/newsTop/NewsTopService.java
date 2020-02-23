package com.lavector.collector.entity.newsTop;

import com.lavector.collector.entity.newsAggregation.NewsAggregationType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
@Service
public interface NewsTopService {
    NewsTop save(NewsTop message);

    NewsTop getByCategory(Long id, NewsAggregationType type, String date);

    NewsTop getById(Long topId);

    void sendEmail();
}
