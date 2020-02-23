package com.lavector.collector.entity.wechatSmall.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
public interface ArticleService {
    Article save(Article article);

    Page<Article> findByBrands(List<Long> brandIds, Pageable pageable);

    Page<Article> findByBrand(Long brandId, Pageable pageable);

    Article getById(Long id);

    Page<Article> findByScoreThan(int i, Pageable pageable);
}
