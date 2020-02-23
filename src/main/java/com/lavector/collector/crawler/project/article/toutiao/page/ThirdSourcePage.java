package com.lavector.collector.crawler.project.article.toutiao.page;

import com.lavector.collector.crawler.base.PageParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;

/**
 * Created on 2017/12/26.
 *
 * @author zeng.zhao
 */
public class ThirdSourcePage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(ThirdSourcePage.class);

    @Override
    public boolean handleUrl(String url) {
        return !url.contains("www.toutiao.com");
    }

    @Override
    public Result parse(Page page) {
        logger.info("第三方链接！{}", page.getUrl().get());
        return Result.get();
    }

    @Override
    public String pageName() {
        return null;
    }
}
