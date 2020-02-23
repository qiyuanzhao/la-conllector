package com.lavector.collector.crawler.project.weibo.weiboStar.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.CookieUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPage implements PageParse {

    //https://weibo.com/p/1003061087770692/info?mod=pedit_more

    @Override
    public Result parse(Page page) {

        Request request = page.getRequest();
        Person person = (Person) request.getExtra("person");

        Html html = page.getHtml();
        List<String> scripts = html.xpath("script/html()").all();
        Map<String, String> params = new HashMap<>();
        scripts.stream().filter(s -> s.contains("var $CONFIG = {}")).findFirst().ifPresent(config -> {
            String pageId = new Json(config).regex("\\$CONFIG\\['page_id'\\]='(\\d+)';").get();
            params.put("pageId", pageId);
        });
        Request request1 = new Request();
        request1.setUrl("https://weibo.com/p/"+params.get("pageId")+"/info?mod=pedit_more");
        request1.addHeader("cookie", CookieUtils.getCookie());
        page.addTargetRequest(request1.putExtra("person",person));

        page.setSkip(true);
        return null;


    }


    @Override
    public String pageName() {
        return null;
    }

    @Override
    public boolean handleUrl(String url) {
        return false;
    }

}
