package com.lavector.collector.crawler.project.movie.iqiyi.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * Created on 2018/2/28.
 *
 * @author zeng.zhao
 */
public class SingleCategoryPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://list.iqiyi.com/www/20/--\\d+.*", url);
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        List<String> urls = html.xpath("//div[@class='site-piclist_pic']/a/@href").all();
        urls.stream().map(Request::new).forEach(result::addRequest);
        String nextUrl = html.xpath("//a[@data-key='down']/@href").get();
        if (StringUtils.isNotBlank(nextUrl)) {
            if (!nextUrl.contains("http:")) {
                nextUrl = "http://list.iqiyi.com" + nextUrl;
            }
            result.addRequest(new Request(nextUrl));
        }
        return result;
    }
}
