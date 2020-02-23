package com.lavector.collector.crawler.project.article.toutiao.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

/**
 * Created on 2017/12/26.
 *
 * @author zeng.zhao
 */
public class DefaultArticlePage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.toutiao.com/group/\\d+/", url) || RegexUtil.isMatch("https://www.toutiao.com/group/\\d+/", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        if (page.getRawText().contains("articleInfo")) {
            String articleId = page.getUrl().regex("\\d+").get();
            Request request = page.getRequest();
            request.setUrl("https://www.toutiao.com/a" + articleId + "/");
            result.addRequest(request);
        }
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }
}
