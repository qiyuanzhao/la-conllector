package com.lavector.collector.crawler.project.sport.tencent.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/10/18.
 *
 * @author zeng.zhao
 */
public class FootballNewsListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        boolean isMatch = RegexUtil.isMatch("http://sports.qq.com/csocce/cft/$", url);
        if (!isMatch) {
            isMatch = RegexUtil.isMatch("http://sports.qq.com/csocce/cft$", url);
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
        List<String> allLinks = html.links().all();
        return allLinks.stream()
                .filter((link) -> RegexUtil.isMatch("http://sports.qq.com/a/\\d+/\\d+\\.htm", link))
                .map(Request::new)
                .collect(Collectors.toList());
    }

    public static void main (String[] args) throws Exception {
        String url = "http://sports.qq.com/csocce/cft/";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new Request(url));
        page.setRawText(content);
        FootballNewsListPage newsListPage = new FootballNewsListPage();
        List<Request> requests = newsListPage.parseNewsList(page);
        for (Request request : requests) {
            System.out.println(request.getUrl());
        }
    }
}
