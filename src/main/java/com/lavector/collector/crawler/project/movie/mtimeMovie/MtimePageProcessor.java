package com.lavector.collector.crawler.project.movie.mtimeMovie;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.movie.mtimeMovie.page.NewsContentPage;
import com.lavector.collector.crawler.project.movie.mtimeMovie.page.NewsTop;

/**
 * Created on 26/09/2017.
 *
 * @author seveniu
 */
public class MtimePageProcessor extends BaseProcessor {
    public MtimePageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NewsTop(), new NewsContentPage());
    }

}
