package com.lavector.collector.entity.data;

import com.lavector.collector.entity.newsAggregation.NewsAggregationType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
@Service
public interface NewsService {
    void save(News message);

    Optional<News> getOne(Long id);

    List<News> getNewsByIds(List<Long> ids);

}
