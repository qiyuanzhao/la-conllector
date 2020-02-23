package com.lavector.collector.crawler.project.yanying.bilibili.page;

import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

/**
 * Created on 2018/2/8.
 *
 * @author zeng.zhao
 */
public class BiliVideoPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(BiliVideoPage.class);

    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.bilibili.com/video/");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String cid = html.regex("cid=(\\d+)").get();
        if (StringUtils.isBlank(cid)) {
            logger.warn("该页面没有找到cid。{}", page.getUrl().get());
            return result;
        }
        String mid = page.getUrl().regex("av(\\d+)").get();
        String danmuUrl = "http://comment.bilibili.com/" + cid + ".xml";
        result.addRequest(new Request(danmuUrl).putExtra("mid", mid));
        return result;
    }
}
