package com.lavector.collector.crawler.project.article.yidian.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import net.minidev.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created on 2017/12/27.
 *
 * @author zeng.zhao
 */
public class SearchListPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.contains("http://www.yidianzixun.com/home/q/news_list_for_keyword");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        String brand = page.getRequest().getExtra("brand").toString();
        JSONArray jsonArray = JsonPath.read(json, "$.result");
        if (CollectionUtils.isEmpty(jsonArray)) {
            return result;
        }

        for (Object obj : jsonArray) {
            if (obj instanceof LinkedHashMap) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (LinkedHashMap<String, Object>) obj;
                Object contentType = map.get("content_type");
                if (Objects.isNull(contentType)) {
                    continue;
                }
                if (!contentType.toString().contains("news")) {
                    continue;
                }

                String docId = map.get("docid").toString();
                String date = map.get("date").toString();
                String author = map.get("source").toString();
                Object commentNum = map.get("comment_count");
                Object likeNum = map.get("like");
                Request articleRequest = new Request("http://www.yidianzixun.com/article/" + docId);
                articleRequest.putExtra("date", date);
                articleRequest.putExtra("author", author);
                articleRequest.putExtra("brand", brand);
                if (Objects.nonNull(commentNum)) {
                    articleRequest.putExtra("commentNum", commentNum.toString());
                }
                if (Objects.nonNull(likeNum)) {
                    articleRequest.putExtra("likeNum", likeNum.toString());
                }
                result.addRequest(articleRequest);
            }

        }

        String url = page.getUrl().get();
        String start = page.getUrl().regex("cstart=(\\d+)").get();
        String end = page.getUrl().regex("cend=(\\d+)").get();
        Integer nextEnd = Integer.parseInt(end) + 10;
        String urlReplace = url.replace("cstart=" + start, "cstart=" + end);
        String nextUrl = urlReplace.replace("cend=" + end, "cend=" + nextEnd);
        Request nextRequest = new Request(nextUrl);
        nextRequest.putExtra("brand", brand);
        result.addRequest(nextRequest);
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.yidianzixun.com/home/q/news_list_for_keyword?display=%E9%9F%A9%E6%97%B6%E7%83%A4%E8%82%89&cstart=30&cend=40&word_type=token";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url).putExtra("brand", "GUCCI"));
        page.setUrl(new Json(url));
        SearchListPage listPage = new SearchListPage();
        listPage.parse(page);
    }
}
