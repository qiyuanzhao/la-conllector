package com.lavector.collector.entity.wechatSmall.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByBrandIdIn(List<Long> brandIds, Pageable pageable);

    Page<Article> findByBrandId(Long brandId, Pageable pageable);

    Page<Article> findByScoreGreaterThanEqual(Integer score, Pageable pageable);
}
