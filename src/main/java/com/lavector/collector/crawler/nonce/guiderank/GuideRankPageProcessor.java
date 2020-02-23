package com.lavector.collector.crawler.nonce.guiderank;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.PageParse;

/**
 * Created on 12/07/2018.
 *
 * @author zeng.zhao
 */
public class GuideRankPageProcessor extends BaseProcessor {

    public GuideRankPageProcessor() {
        super(new CrawlerInfo(), new CategoryListPage(), new CategoryRankinkPage());
    }
}
