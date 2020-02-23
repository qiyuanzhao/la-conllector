package com.lavector.collector.crawler.project.sport.nba.nbaChina.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/10/17.
 *
 * @author zeng.zhao
 */
public class NBAChinaNewsListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        boolean isMatch = RegexUtil.isMatch("http://china.nba.com/news/$", url);
        if (!isMatch) {
            isMatch = RegexUtil.isMatch("http://china.nba.com/news$", url);
        }
        return isMatch;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addRequests(parseNewsList(page));
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private List<Request> parseNewsList (Page page) {
        Html html = page.getHtml();
        List<String> newsUrls = html.xpath("//a[@class='no-margin']/@href").all();
        return newsUrls.stream().map(Request::new).collect(Collectors.toList());
    }
}
