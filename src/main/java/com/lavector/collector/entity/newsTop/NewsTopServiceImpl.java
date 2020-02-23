package com.lavector.collector.entity.newsTop;

import com.alibaba.fastjson.JSON;
import com.lavector.collector.email.EmailSenderService;
import com.lavector.collector.email.EmailTemplateService;
import com.lavector.collector.entity.category.Category;
import com.lavector.collector.entity.category.CategoryService;
import com.lavector.collector.entity.config.ConfigName;
import com.lavector.collector.entity.config.ConfigService;
import com.lavector.collector.entity.crawlerTask.CrawlerTaskService;
import com.lavector.collector.entity.data.News;
import com.lavector.collector.entity.data.NewsService;
import com.lavector.collector.entity.newsAggregation.NewsAggregationService;
import com.lavector.collector.entity.newsAggregation.NewsAggregationType;
import com.lavector.collector.entity.source.OriginService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 27/09/2017.
 *
 * @author seveniu
 */
@Service
public class NewsTopServiceImpl implements NewsTopService {
    @Autowired
    NewsAggregationService newsAggregationService;
    @Autowired
    NewsTopRepository newsTopRepository;
    @Autowired
    CrawlerTaskService crawlerTaskService;
    @Autowired
    NewsService newsService;
    @Autowired
    OriginService originService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    EmailTemplateService templateService;
    @Autowired
    ConfigService configService;
    @Value("${email.news.template:/templates/news.vm}")
    private String newsEmailTemplate;

    @Override
    public NewsTop save(NewsTop news) {
        if (news.getId() == null) {
            news.setTimeCreated(new Date());
        } else {
            news.setTimeUpdated(new Date());
        }
        try {
            return newsTopRepository.save(news);
        } catch (DataIntegrityViolationException e) {
//            e.printStackTrace();
        }
        return news;
    }

    @Override
    public synchronized NewsTop getByCategory(Long cid, final NewsAggregationType type, String date) {
        NewsTop newsTop = newsTopRepository.findByCidAndTypeAndDate(cid, type, date);
        if (newsTop != null) {
            return newsTop;
        } else {
            List<List<News>> news;
            int size;
            if (type == NewsAggregationType.HOT || type == NewsAggregationType.DISCOVER) {
                size = JSON.parseObject(configService.findByName(ConfigName.SEND_NUM).getConfigData()).getIntValue("hotChooseNum");
                news = newsAggregationService.multiCategoryAggregation(cid, NewsAggregationType.HOT).stream().limit(size).collect(Collectors.toList());
            } else if (type == NewsAggregationType.TREND) {
                size = JSON.parseObject(configService.findByName(ConfigName.SEND_NUM).getConfigData()).getIntValue("trendChooseNum");
                news = newsAggregationService.multiCategoryAggregation(cid, type).stream().limit(size).collect(Collectors.toList());
            } else {
                throw new IllegalArgumentException("not handle type : " + type);
            }
            List<NewsTop.NewsSimple> newsSimples = news.stream().limit(size).map(news1 -> {
                News n = news1.get(0);
                return new NewsTop.NewsSimple(n, originService.getById(crawlerTaskService.getById(n.getTaskId()).getOriginId()));
            }).collect(Collectors.toList());
            newsTop = new NewsTop();
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
            newsTop.setDate(DATE_FORMAT.format(new Date()));
            newsTop.setTopNewsList(newsSimples);
            newsTop.setType(type);
            newsTop.setCid(cid);
            return newsTopRepository.save(newsTop);
        }
    }

    @Override
    public NewsTop getById(Long topId) {
        return newsTopRepository.getOne(topId);
    }


    @Scheduled(cron = "0 0 9 * * ?")
    @Override
    public void sendEmail() {
        List<Category> firstLevel = categoryService.getFirstLevel();

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
        Map<String, List<NewsTop.NewsSimple>> dataMap = new HashMap<>();
        int size = JSON.parseObject(configService.findByName(ConfigName.SEND_NUM).getConfigData()).getIntValue("hotSendNum");
        for (Category category : firstLevel) {
            NewsTop newsTop = getByCategory(category.getId(), NewsAggregationType.HOT, DATE_FORMAT.format(new Date()));
            dataMap.put(category.getName(), newsTop.getTopNewsList().stream().filter(NewsTop.NewsSimple::isHot).limit(size).collect(Collectors.toList()));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", dataMap);
        map.put("title", "菱歌BrandBuzz™智能系统为您发现，品牌的热点话题如下：");
        String emailBody = templateService.mergeIntoTemplate(newsEmailTemplate, map);
//        emailSenderService.sendEmailToUser(Lists.newArrayList("lei.feng@lavector.com", "dongyue.liu@lavector.com"), "品牌头条", emailBody);
//        System.out.println(emailBody);
        emailSenderService.sendEmailToUser(Lists.newArrayList("dongyue.liu@lavector.com"), "万达热点", emailBody);
    }

}
