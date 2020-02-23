package com.lavector.collector.crawler.project.sport.yidianzixun.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.base.RequestExtraKey;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 2017/10/17.
 *
 * @author zeng.zhao
 */
public class ZiXunNewsListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return StringUtils.contains(url, "home/q/news_list_for_channel");
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
        Date startTime = (Date) page.getRequest().getExtra(RequestExtraKey.KEY_BEGIN_DATE);
        List<Request> requests = new ArrayList<>();
        String json = page.getJson().get();
        List<String> urls = JsonPath.read(json, "$.result[*].url");
        List<String> timePublishs = JsonPath.read(json, "$.result[*].date");
        for (int i = 0; i < urls.size(); i++) {
            if (urls.get(i).contains("yidianzixun.com")) {
                requests.add(new Request(urls.get(i)).putExtra(RequestExtraKey.KEY_BEGIN_DATE, timePublishs.get(i)));
            }
        }

        if (urls.size() >= 10 && ZiXunNewsPage.parseTimePublish(timePublishs.get(timePublishs.size() - 1)).after(startTime)) {
            String cStart = page.getUrl().regex("&cstart=(\\d+)").get();
            String cEnd = page.getUrl().regex("&cend=(\\d+)").get();
            Integer nextEnd = Integer.parseInt(cEnd) + 10;
            String nextUrl1 = page.getUrl().get().replace("&start=" + cStart, "&start=" + cEnd);
            String nextUrl2 = nextUrl1.replace("&cend=" + cEnd, "&cend=" + nextEnd);
            requests.add(new Request(nextUrl2));
        }
        return requests;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://www.yidianzixun.com/home/q/news_list_for_channel?channel_id=sc4&cstart=20&cend=30&infinite=true&refresh=1&__from__=pc&multi=5&appid=web_yidian&_=1508223123302";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new Request(url));
        page.setRawText(content);
        ZiXunNewsListPage newsListPage = new ZiXunNewsListPage();
        newsListPage.parse(page);
    }
}
