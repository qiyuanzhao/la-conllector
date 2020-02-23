package com.lavector.collector.crawler.project.article.weixin;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.article.weixin.page.SouGouWeiXinArticlePage;

/**
 * Created on 2018/1/8.
 *
 * @author zeng.zhao
 */
public class WeiXinPageProcessor extends BaseProcessor {

    public WeiXinPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new SouGouWeiXinArticlePage());
    }
}
