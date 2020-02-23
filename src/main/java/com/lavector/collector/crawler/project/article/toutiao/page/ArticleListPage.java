package com.lavector.collector.crawler.project.article.toutiao.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.collections.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.List;

/**
 * Created on 2017/12/22.
 *
 * @author zeng.zhao
 */
public class ArticleListPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.toutiao.com/search_content/");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        if (CollectionUtils.isEmpty(JsonPath.read(json, "$.data"))) {
            return result;
        }
        String brand = page.getRequest().getExtra("brand").toString();
        List<String> sourceUrls = JsonPath.read(json, "$.data[*].share_url");
        List<Integer> articleComments = JsonPath.read(json, "$.data[*].comments_count");
        List<String> articleTitles = JsonPath.read(json, "$.data[*].title");
        List<String> articleAuthors = JsonPath.read(json, "$.data[*].source");
        for (int i = 0; i < sourceUrls.size(); i++) {
            if (articleTitles.get(i).contains(brand) && !sourceUrls.get(i).contains("comsslocal")) {
                Request articleRequest = new Request(sourceUrls.get(i));
                articleRequest.putExtra("comment", articleComments.get(i));
                articleRequest.putExtra("author", articleAuthors.get(i));
                articleRequest.putExtra("brand", brand);
                result.addRequest(articleRequest);
            }
        }

        String url = page.getUrl().get();
        String offset = page.getUrl().regex("offset=(\\d+)&format").get();
        Integer nextOffset = Integer.parseInt(offset) + 20;
        String nextUrl = url.replace("offset=" + offset, "offset=" + nextOffset);
        Request nextRequest = new Request(nextUrl);
        nextRequest.putExtra("brand", brand);
        result.addRequest(nextRequest);
        return result;
    }
}
