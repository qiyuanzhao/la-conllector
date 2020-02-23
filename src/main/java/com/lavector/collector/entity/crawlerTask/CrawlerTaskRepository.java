package com.lavector.collector.entity.crawlerTask;

import com.lavector.collector.crawler.base.CrawlerType;
import com.lavector.collector.entity.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
@Repository
public interface CrawlerTaskRepository extends JpaRepository<CrawlerTask, Long> {
    List<CrawlerTask> findAllByCrawlerType(String crawlerType);

    List<CrawlerTask> findAllByStatusAndTaskStatusAndNextRunTimeBefore(EntityStatus entityStatus, CrawlerTaskStatus taskStatus, Date date);
}
