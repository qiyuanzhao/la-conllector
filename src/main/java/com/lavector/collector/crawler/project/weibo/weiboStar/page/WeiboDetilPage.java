package com.lavector.collector.crawler.project.weibo.weiboStar.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;


public class WeiboDetilPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(WeiboDetilPage.class);


    @Override
    public boolean handleUrl(String url) {
        return false;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Request request = page.getRequest();
        Person person = (Person) request.getExtra("person");
        String selectable = page.getJson().jsonPath("$.data").get();
        String all = selectable.replace("\\", "");
        Html html = new Html(all);
        List<Selectable> nodes = html.xpath("//div[@class='list_li S_line1 clearfix']").nodes();

        List<Request> list = new ArrayList<>();
        List<Person> personList = new ArrayList<>();
        nodes.forEach(node -> {
            Person person1 = new Person();
            person1.setStarName(person.getStarName());
//            String url = node.xpath("//div[@class='list_con']/div[@class='WB_text']/a/@href").regex("(weibo.com/[0-9 a-z]+)").get();
//            Request newRequestUrl = new Request();
//            String time = node.xpath("//div[@class='list_con']/div[@class='WB_func clearfix']/div[@class='WB_from S_txt2']/text()").get();
            List<String> comment = node.xpath("//div[@class='list_con']/div[@class='WB_text']/text()").all();
            person1.setComment(comment.get(0));
            person1.setUrl(person.getUrl());
//            person1.setTime(time);
            personList.add(person1);
//            newRequestUrl.setUrl("https://" + url);
//            newRequestUrl.addHeader("cookie",CookieUtils.getCookie());
//            newRequestUrl.putExtra("person",person1);
//            list.add(newRequestUrl);
        });
        String text = html.xpath("//div[@class='empty_con clearfix']/p/text()").get();
        if (text != null && text.contains("正在加载")) {
            Request newRequest = new Request();
            newRequest.setUrl("https://weibo.com/aj/v6/comment/big?from=singleWeiBo&id=" + person.getId() + "&page=1");
//            newRequest.addHeader("cookie",CookieUtils.getCookie());
            newRequest.putExtra("person", person);
            list.add(newRequest);
        }
//        List<Request> targetRequests = page.getTargetRequests();
        result.setRequests(list);

        Request request1 = page.getRequest();
        request1.putExtra("personList",personList);
        page.putField("personList",personList);

        return result;
    }


    @Override
    public String pageName() {
        return null;
    }


}
