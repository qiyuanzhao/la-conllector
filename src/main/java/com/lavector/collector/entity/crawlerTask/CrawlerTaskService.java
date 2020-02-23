package com.lavector.collector.entity.crawlerTask;

import java.util.List;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
public interface CrawlerTaskService {
    List<CrawlerTask> findAllByCrawlerType(String type);

    List<CrawlerTask> findDueTask();

    void runTask(CrawlerTask task);

    void runDueTask();

    CrawlerTask getById(Long id);
}
