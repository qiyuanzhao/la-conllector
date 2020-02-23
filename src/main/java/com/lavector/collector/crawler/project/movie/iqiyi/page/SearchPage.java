package com.lavector.collector.crawler.project.movie.iqiyi.page;

import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/1/24.
 *
 * @author zeng.zhao
 */
public class SearchPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("so.iqiyi.com/so/q_");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        List<Selectable> nodes = html.xpath("//li[@class='list_item']").nodes();

        //如果有数据框，数据是post请求，获取参数，构建post请求
        if (nodes.size() >= 21) {
            //先处理数据框
            Selectable head = nodes.get(0);
            String termParams = head.xpath("//div[@class='mod-discernList mb10 mt10']/@data-intentrecognition-termparams").get();
            String totalCount = head.xpath("//div[@class='mod-discernList mb10 mt10']/@data-intentrecognition-totalcount").get();
            //第一页随网页静态加载，后续页面动态加载
            List<String> firstPageUrls = head.xpath("//p[@class='site-piclist_info_title']/a/@href").all();
            firstPageUrls.stream().map(Request::new).forEach(result::addRequest);

            if (Integer.parseInt(totalCount) > firstPageUrls.size()) {
                Map<String, Object> params = new HashMap<>();
                params.put("p", 10);
                params.put("p1", 101);
                params.put("mode", 11);
                params.put("threeCategory", "");
                params.put("platform", "web");
                params.put("pageNum", 2);
                params.put("intentActionType", 0);
                params.put("pageSize", 10);
                params.put("ctgName", "");
                params.put("dataType", "json");
                params.put("method", "POST");
                params.put("firstFilter", "");
                params.put("secondFilter", "");
                params.put("termParams", termParams);
                params.put("pos", 1);

                Request postRequest = new Request("http://so.iqiyi.com/intent?if=video&type=list");
                HttpRequestBody requestBody = HttpRequestBody.form(params, "utf-8");
                postRequest.setMethod(HttpConstant.Method.POST);
                postRequest.setRequestBody(requestBody);
                postRequest.putExtra("params", params);
                result.addRequest(postRequest);
            }

            //后处理页面列表
            for (int i = 1; i < nodes.size(); i++) {
                String videoUrl = nodes.get(i).xpath("/li/a/@href").get();
                List<String> multiItems = nodes.get(i).xpath("//li[@class='album_item']/a/@href").all();
                if (CollectionUtils.isNotEmpty(multiItems)) {
                    multiItems.stream().map(Request::new).forEach(result::addRequest);
                }
                result.addRequest(new Request(videoUrl));
            }

        } else {
            nodes.forEach(node -> {
                String videoUrl = node.xpath("/li/a/@href").get();
                List<String> multiItems = node.xpath("//li[@class='album_item']/a/@href").all();
                if (CollectionUtils.isNotEmpty(multiItems)) {
                    multiItems.stream().map(Request::new).forEach(result::addRequest);
                }
                result.addRequest(new Request(videoUrl));
            });
        }


        //分页
        String nextPage = html.xpath("//div[@class='mod-page']/a[@data-key='down']/@href").get();
        if (StringUtils.isNotBlank(nextPage)) {
            if (!nextPage.contains("so.iqiyi.com")) {
                nextPage = "http://so.iqiyi.com" + nextPage;
            }

            result.addRequest(new Request(nextPage));
        }


        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://so.iqiyi.com/so/q_%E6%B1%BD%E8%BD%A6%E5%B9%BF%E5%91%8A?source=input&sr=1426538130286";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url));
        page.setUrl(new Json(url));
        SearchPage searchPage = new SearchPage();
        searchPage.parse(page);

    }
}
