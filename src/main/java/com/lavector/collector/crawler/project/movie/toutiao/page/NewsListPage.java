package com.lavector.collector.crawler.project.movie.toutiao.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.base.RequestExtraKey;
import com.lavector.collector.crawler.project.movie.qqMovie.page.MovieNews;
import com.lavector.collector.crawler.util.RegexUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 2017/10/25.
 *
 * @author zeng.zhao
 */
public class NewsListPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.toutiao.com/api/pc/feed/\\?category=movie&utm_source=toutiao&widen=\\d+&max_behot_time=\\d+&max_behot_time_tmp=\\d+&tadrequire=true", url);
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
        Date startTime = (Date) page.getRequest().getExtra(RequestExtraKey.KEY_BEGIN_DATE);
        String json = page.getJson().get();
        List<String> groupIds = JsonPath.read(json, "$.data[*].group_id");
        List<String> titles = JsonPath.read(json, "$.data[*].title");
        List<String> tags = JsonPath.read(json, "$.data[*].chinese_tag");
        Integer nextTime = JsonPath.read(json, "$.next.max_behot_time");
        for (int i = 0; i < groupIds.size(); i++) {
            if (!tags.get(i).equals("图片") && !tags.get(i).equals("视频")) {
                String url = "http://www.toutiao.com/a" + groupIds.get(i) + "/";
                Request request = new Request(url);
                request.putExtra("title", titles.get(i));
                requests.add(request);
            }
        }

        if (new Date(nextTime * 1000).after(startTime)) {
            String currentlyUrl = page.getUrl().get();
            String currentlyTime = page.getUrl().regex("max_behot_time=(\\d+)").get();
            String replace1 = currentlyUrl.replace("max_behot_time=" + currentlyTime, "max_behot_time=" + nextTime);
            String nextUrl = replace1.replace("max_behot_time_tmp=" + currentlyTime, "max_behot_time_tmp=" + nextTime);
            requests.add(new Request(nextUrl).putExtra(RequestExtraKey.KEY_BEGIN_DATE, startTime));
        }
        return requests;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.toutiao.com/api/pc/feed/?category=movie&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true";
        String content = org.apache.http.client.fluent.Request.Get(url)
//                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRequest(new Request(url).putExtra(RequestExtraKey.KEY_BEGIN_DATE, new Date()));
        page.setRawText(content);
        NewsListPage newsListPage = new NewsListPage();
        newsListPage.parse(page);
    }
}
