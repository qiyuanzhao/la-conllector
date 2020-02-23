package com.lavector.collector.crawler.project.sport.ufc.weiboufc;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.sport.ufc.weiboufc.page.WeiBoUFCHomePage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/10/19.
 *
 * @author zeng.zhao
 */
public class WeiBoUFCPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .addHeader("cookie", "SUB=_2AkMutMcZf8NxqwJRmP4TyG_lZIlzyAHEieKY6DbCJRMxHRl-yT83qkIrtRApj2jQyjYXa7K1fhefC2Q_lZ631Q..");

    public WeiBoUFCPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new WeiBoUFCHomePage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
