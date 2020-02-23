package com.lavector.collector.crawler.project.movie.iqiyi.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.List;
import java.util.Map;

/**
 * Created on 2018/1/24.
 *
 * @author zeng.zhao
 */
public class PostSearchPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("so.iqiyi.com/intent?if=video&type=list");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        String data = JsonPath.read(json, "$.data");
        Html html = new Html(data);
        int totalCount = Integer.parseInt(JsonPath.read(json, "$.totalNum"));
        List<String> videoUrls = html.xpath("//li[@class='intent-item-twoline']/div[1]/a/@href").all();
        videoUrls.stream().map(Request::new).forEach(result::addRequest);

        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) page.getRequest().getExtra("params");
        Integer currentPage = (Integer) params.get("pageNum");
        if (currentPage * 10 < totalCount) {
            Integer nextPage = currentPage + 1;
            params.put("pageNum", nextPage);

            Request postRequest = new Request("http://so.iqiyi.com/intent?if=video&type=list");
            HttpRequestBody requestBody = HttpRequestBody.form(params, "utf-8");
            postRequest.setMethod(HttpConstant.Method.POST);
            postRequest.setRequestBody(requestBody);
            postRequest.putExtra("params", params);
            result.addRequest(postRequest);

        }
        return result;
    }
}
