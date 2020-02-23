package com.lavector.collector.crawler.es;

import org.elasticsearch.search.SearchHit;

/**
 * Created on 2018/9/29.
 *
 * @author zeng.zhao
 */
public interface HitConsumer {
    void process(SearchHit hit);
}
