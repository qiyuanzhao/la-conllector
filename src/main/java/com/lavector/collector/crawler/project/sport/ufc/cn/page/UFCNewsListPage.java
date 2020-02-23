package com.lavector.collector.crawler.project.sport.ufc.cn.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/10/19.
 *
 * @author zeng.zhao
 */
public class UFCNewsListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://interface.sina.cn/sports/get_ufc_news.d.json\\?pageNum=\\d+&pageSize=\\d+", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addRequests(parseNewsListPage(page));
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private List<Request> parseNewsListPage (Page page) {
        List<Request> requests = new ArrayList<>();
        String json = page.getJson().get();
        List<String> newsUrls = JsonPath.read(json, "$.result.data[*].url");
        List<String> topNews = JsonPath.read(json, "$.result.top[*].url");
        newsUrls.forEach(url -> requests.add(new Request(url)));
        topNews.forEach(url -> requests.add(new Request(url)));
        return requests;
    }
}
