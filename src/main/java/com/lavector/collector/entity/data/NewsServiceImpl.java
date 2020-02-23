package com.lavector.collector.entity.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created on 27/09/2017.
 *
 * @author seveniu
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    NewsRepository newsRepository;

    @Override
    public void save(News news) {
//        boolean exist = newsRepository.existsByUrl(news.getUrl());
//        if (exist) {
//            return;
//        }
        boolean exists = newsRepository.existsByContent(news.getContent());
        if (exists) {
            return;
        }
        if (news.getId() == null) {
            news.setTimeCreated(new Date());
        } else {
            news.setTimeUpdated(new Date());
        }
        try {
            newsRepository.save(news);
        } catch (DataIntegrityViolationException e) {
//            e.printStackTrace();

        }
    }

    @Override
    public Optional<News> getOne(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public List<News> getNewsByIds(List<Long> ids) {
        return newsRepository.findAllById(ids);
    }
}
