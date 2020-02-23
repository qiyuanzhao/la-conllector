package com.lavector.collector.entity.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    boolean existsByUrl(String url);

    boolean existsByContent(String content);
}
