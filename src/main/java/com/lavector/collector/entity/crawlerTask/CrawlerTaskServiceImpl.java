package com.lavector.collector.entity.crawlerTask;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.entity.EntityStatus;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
@Service
@EnableScheduling
public class CrawlerTaskServiceImpl implements CrawlerTaskService, ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(CrawlerTaskServiceImpl.class);
    @Autowired
    CrawlerTaskRepository crawlerTaskRepository;
    private ApplicationContext applicationContext;

    @Override
    public List<CrawlerTask> findAllByCrawlerType(String type) {
        return crawlerTaskRepository.findAllByCrawlerType(type);
    }


    @Override
    public List<CrawlerTask> findDueTask() {
        return crawlerTaskRepository.findAllByStatusAndTaskStatusAndNextRunTimeBefore(EntityStatus.ACTIVE, CrawlerTaskStatus.STOP, DateUtils.addDays(new Date(), 1));
    }

    public CrawlerTask save(CrawlerTask crawlerTask) {
        if (crawlerTask.getId() != null) {
            crawlerTask.setTimeUpdated(new Date());
        } else {
            crawlerTask.setTimeCreated(new Date());
        }
        return crawlerTaskRepository.save(crawlerTask);
    }

    @Override
    public void runTask(CrawlerTask task) {
        logger.info("run task id : {}, type : {}", task.getId(), task.getCrawlerType());
        BaseCrawler baseCrawler = applicationContext.getBean(task.getCrawlerType(), BaseCrawler.class);
        baseCrawler.setStartTime(task.getLastRunTime());
        baseCrawler.start(task);
        if (task.getCycle() > 0) {
            task.setLastRunTime(new Date());
            task.setNextRunTime(new Date(new Date().getTime() + task.getCycle() * 1000));
            this.save(task);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2999, Calendar.JANUARY, 1);
            task.setNextRunTime(calendar.getTime());
        }
    }

    @Override
//    @Scheduled(fixedRate = 10 * 1000, initialDelay = 5000)
    @Scheduled(fixedRate = 60 * 60 * 1000, initialDelay = 5000)
    public void runDueTask() {
        findDueTask().forEach(this::runTask);
    }

    @Override
    public CrawlerTask getById(Long id) {
        return crawlerTaskRepository.getOne(id);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
