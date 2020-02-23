package com.lavector.collector.crawler.project.edu.mama.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/11/16.
 *
 * @author zeng.zhao
 */
public class BBSListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://so.mama.cn/search\\?q=.*&source=mamaquan&csite=all&size=15&sortMode=1&page=\\d+", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String site = page.getRequest().getExtra("site").toString();
        String keyword = page.getRequest().getExtra("keyword").toString();
        List<String> urls = html.xpath("//h1[@class='result-com__title']/a/@href").all();
        List<Request> requests = urls.stream().map(url -> {
            String var1 = url.replaceAll("&#x3D;", "=");
            Request request = new Request(var1);
            request.putExtra("site", site).putExtra("keyword", keyword);
            return request;
        }).collect(Collectors.toList());
        result.addRequests(requests);
        if (requests.size() == 15) {
            String pageNo = page.getUrl().regex("page=(\\d+)").get();
            Integer nextPageNo = Integer.parseInt(pageNo) + 1;
            String nextUrl = page.getUrl().get().replace("page=" + pageNo, "page=" + nextPageNo);
            Request nextRequest = new Request(nextUrl);
            nextRequest.putExtra("site", site);
            nextRequest.putExtra("keyword", keyword);
            result.addRequest(nextRequest);
        } else {
            System.out.println("末页？" + page.getUrl().get());
        }
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    public static void main (String[] args) {
        String url = "http://so.mama.cn/search?q=幼儿园毕业&source=mamaquan&csite=all&size=15&sortMode=1&page=1";
        BBSListPage listPage = new BBSListPage();
        boolean b = listPage.handleUrl(url);
        System.out.println(b);
    }
}
