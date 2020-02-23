package com.lavector.collector.crawler.project.yanying.redbook.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import net.minidev.json.JSONArray;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2018/2/7.
 *
 * @author zeng.zhao
 */
public class SearchPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.xiaohongshu.com/web_api/sns/v2/search/note?keyword");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        JSONArray jsonArray = JsonPath.read(json, "$.data");
        if (jsonArray.size() == 0) {
            return result;
        }

        String keyword = page.getRequest().getExtra("keyword").toString();

        List<String> itemIds = JsonPath.read(json, "$.data[*].id");
        result.addRequests(itemIds.stream()
                .map(id -> new Request("http://www.xiaohongshu.com/discovery/item/" + id).putExtra("keyword", keyword))
                .collect(Collectors.toList())
        );

        String currentPage = page.getUrl().regex("page=(\\d+)").get();
        Integer nextPage = Integer.parseInt(currentPage) + 1;
        String nextUrl = page.getUrl().get().replace("page=" + currentPage, "page=" + nextPage);
        result.addRequest(new Request(nextUrl).putExtra("keyword", keyword));
        return result;
    }
}
