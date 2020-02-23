package com.lavector.collector.crawler.project.weibo.weiboStar.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class WeiboSearchPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(WeiboSearchPage.class);

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String url = "https://weibo.com/aj/v6/comment/big?id=";
    private String endUrl = "&page=1";


    @Override
    public boolean handleUrl(String url) {
        return url.contains("https://s.taobao.com/search");
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        logger.info("获取到列表页面");
        Person person = (Person) page.getRequest().getExtra("person");

        Html html = page.getHtml();
        List<String> scripts = html.xpath("script/html()").all();
        List<Request> requests = new ArrayList<>();
        Optional<String> optionalS = scripts.stream().filter(s -> s.contains("\"pl.content.homeFeed.index\",\"domid\":\"Pl_Official_MyProfileFeed")).findFirst();
        optionalS.ifPresent(s -> {
            String json = new Json(s).regex("FM.view\\((.*)\\)").get();
            String htmlStr = JsonPath.read(json, "$.html");
            if (htmlStr != null) {
                Html newHtml = new Html(htmlStr);
                List<Selectable> nodes = newHtml.xpath("//div[@action-type='feed_list_item']").nodes();
                if (nodes != null && nodes.size() > 5) {
                    for (int i = 0; i < 5; i++) {
                        Request newRequest = new Request();
                        String mid = nodes.get(i).xpath("//div[@action-type='feed_list_item']/@mid").get();
                        person.setId(mid);
                        newRequest.setUrl(url + mid + endUrl)
                                .putExtra("person", person);
                        requests.add(newRequest);
                    }
                }else {
                    for (int i = 0; i < nodes.size(); i++) {
                        Request newRequest = new Request();
                        String mid = nodes.get(i).xpath("//div[@action-type='feed_list_item']/@mid").get();
                        person.setId(mid);
                        newRequest.setUrl(url + mid + endUrl)
                                .putExtra("person", person);
                        requests.add(newRequest);
                    }
                }
            }
        });
        result.setRequests(requests);
        page.setSkip(true);
        return result;
    }


    @Override
    public String pageName() {
        return null;
    }

    @Override
    public <T> T getRequestExtra(String key, Page page) {
        return null;
    }

    @Override
    public boolean handleRequest(Request request) {
        return false;
    }
}
