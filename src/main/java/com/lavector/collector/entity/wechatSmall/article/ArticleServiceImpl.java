package com.lavector.collector.entity.wechatSmall.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Override
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public Page<Article> findByBrands(List<Long> brandIds, Pageable pageable) {
        return articleRepository.findByBrandIdIn(brandIds, pageable);
    }

    @Override
    public Page<Article> findByBrand(Long brandId, Pageable pageable) {
        return articleRepository.findByBrandId(brandId, pageable);
    }

    @Override
    public Article getById(Long id) {
        return articleRepository.getOne(id);
    }

    @Override
    public Page<Article> findByScoreThan(int i, Pageable pageable) {
        return articleRepository.findByScoreGreaterThanEqual(i, pageable);
    }
}
