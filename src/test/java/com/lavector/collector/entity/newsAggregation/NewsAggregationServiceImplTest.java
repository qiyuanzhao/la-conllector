package com.lavector.collector.entity.newsAggregation;

import com.lavector.collector.entity.data.News;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * Created on 29/09/2017.
 *
 * @author seveniu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsAggregationServiceImplTest {
    @Autowired
    NewsAggregationService newsAggregationService;

    @Test
    public void getData() throws Exception {
        Map<String, List<List<News>>> data = newsAggregationService.getData();
        System.out.println(data);
    }

}