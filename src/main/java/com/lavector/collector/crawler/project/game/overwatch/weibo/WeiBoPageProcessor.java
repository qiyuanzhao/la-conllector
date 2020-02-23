package com.lavector.collector.crawler.project.game.overwatch.weibo;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.game.overwatch.weibo.page.WeiBoHomePage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/10/24.
 *
 * @author zeng.zhao
 */
public class WeiBoPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .addHeader("cookie", "SUB=_2AkMutMcZf8NxqwJRmP4TyG_lZIlzyAHEieKY6DbCJRMxHRl-yT83qkIrtRApj2jQyjYXa7K1fhefC2Q_lZ631Q..");

    public WeiBoPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new WeiBoHomePage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
