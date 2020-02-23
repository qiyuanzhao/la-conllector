package com.lavector.collector.crawler.project.weibo.weiboStar.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.CookieUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

public class WeiboAjaxCommentPage implements PageParse {


    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Person person = (Person) page.getRequest().getExtra("person");
        person.setComment(null);
        String url = page.getUrl().get();
//        String mid = url.substring(url.indexOf("id=") + 3, url.lastIndexOf("&"));
        String pageNumber = url.substring(url.lastIndexOf("=") + 1);

        String selectable = page.getJson().jsonPath("$.data").get();
        String all = selectable.replace("\\", "");
        Html html = new Html(all);
        List<Selectable> nodes = html.xpath("//div[@class='list_li S_line1 clearfix']").nodes();
        List<Person> personList = new ArrayList<>();
        nodes.forEach(node -> {
            Person newPerson = new Person();
//            String newUrl = node.xpath("//div[@class='list_con']/div[@class='WB_text']/a/@href").get();
//            String time = node.xpath("//div[@class='list_con']/div[@class='WB_func clearfix']/div[@class='WB_from S_txt2']/text()").get();
            List<String> comment = node.xpath("//div[@class='list_con']/div[@class='WB_text']/text()").all();
            newPerson.setComment(comment.get(0));
//            newPerson.setTime(time);
            newPerson.setStarName(person.getStarName());
            newPerson.setUrl(person.getUrl());
            personList.add(newPerson);
//            Request newRequestUrl = new Request();
//            newRequestUrl.setUrl("https:" + newUrl);
//            newRequestUrl.addHeader("cookie",CookieUtils.getCookie());
//            newRequestUrl.putExtra("person",newPerson);
//            page.addTargetRequest(newRequestUrl);
        });



//        List<Selectable> selectableList = html.xpath("//div[@class='WB_cardpage S_line1']/div[@class='W_pages']/a[@class='page S_txt1']").nodes();
//        String number =null;
//        if (selectableList.size()>1){
//            number = selectableList.get(selectableList.size() - 1).xpath("//a/text()").get();
//        }

//        String text = html.xpath("//div[@class='empty_con clearfix']/p/text()").get();
//        String more = html.xpath("//a[@action-type='click_more_comment']//span[@class='more_txt']/text()").get();

        if (Integer.parseInt(pageNumber)<=10/*(text != null && text.contains("正在加载")) || (more!=null && more.contains("查看更多"))*/){
            Request newRequest = new Request();
            newRequest.setUrl("https://weibo.com/aj/v6/comment/big?from=singleWeiBo&id=" + person.getId() + "&page=" + (Integer.parseInt(pageNumber) + 1)).putExtra("person", person);
            newRequest.addHeader("cookie",CookieUtils.getCookie());
            newRequest.putExtra("person", person);
            page.addTargetRequest(newRequest);
        }

//        if (number != null && Integer.parseInt(number) > Integer.parseInt(pageNumber)) {
//            Request request = new Request();
//            request.setUrl("https://weibo.com/aj/v6/comment/big?from=singleWeiBo&id=" + mid + "&page=" + (Integer.parseInt(pageNumber) + 1)).putExtra("person", person);
//            request.addHeader("cookie",CookieUtils.getCookie());
//            page.addTargetRequest(request.putExtra("person",person));
//        }
//        page.setSkip(true);
        Request request1 = page.getRequest();
        request1.putExtra("personList",personList);

        page.putField("personList",personList);

        return result;
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
