package com.lavector.collector.crawler.project.jd.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/1/17.
 *
 * @author zeng.zhao
 */
public class JDItemPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("item.jd.com");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Map<String, String> parameter = new HashMap<>();
        String itemId = page.getUrl().regex(".*item.jd.com/(\\d+)\\.html.*").get();
        Html html = page.getHtml();
//        String shopName = html.xpath("//*[@id='crumb-wrap']/div/div[2]/div[2]/div[1]/div/a/text()").get();
//        String shopId = html.xpath("//*[@id='crumb-wrap']/div/div[2]/div[2]/div[5]/div/@data-vid").get();
        List<String> parameters = html.xpath("//div[@class='p-parameter']//li/allText()").all();
        parameters.forEach(para -> {
            String[] split = para.split("：");
            parameter.put(split[0], split[1]);
        });
        if (parameters.size() == 0) {
            result.addRequest(new Request(page.getUrl().get()));
            return result;
        }
        String commentUrl = "https://sclub.jd.com/comment/skuProductPageComments.action?callback=&productId=" +
                itemId +
                "&score=0&sortType=6&page=0&pageSize=10&isShadowSku=0&fold=1";
        Request request = new Request(commentUrl);
        request.putExtra("itemId", itemId).putExtra("parameter", parameter).putExtra("itemId", itemId);
        result.addRequest(request);
        return result;
    }

    public static void main(String[] args) throws IOException {
        String url = "https://item.jd.com/10279607159.html";
        String content = org.apache.http.client.fluent.Request
                .Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new Request(url));
        page.setRawText(content);
        new JDItemPage().parse(page);
    }
}
