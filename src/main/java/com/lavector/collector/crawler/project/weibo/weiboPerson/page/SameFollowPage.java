package com.lavector.collector.crawler.project.weibo.weiboPerson.page;


import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weibo.weiboStar.page.Person;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SameFollowPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://weibo.com/p.*", url) || RegexUtil.isMatch("https://weibo.com/.*", url);
    }

    @Override
    public Result parse(Page page) {
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        Html html = page.getHtml();
        List<String> scripts = html.xpath("script/html()").all();

        Optional<String> stringOptional = scripts.stream().filter(s -> s.contains("\"domid\":\"Pl_Official_HisRelation")).findFirst();

        List<Person> personList = new ArrayList<>();

        stringOptional.ifPresent(s -> {
            String json = new Json(s).regex("FM.view\\((.*)\\)").get();
            String selectable = JsonPath.read(json, "$.html");
            String all = selectable.replace("\\", "");
            Html newHtml = new Html(all);
            List<Selectable> nodes = newHtml.xpath("//div[@class='WB_cardwrap S_bg2']//div[@class='follow_box']/div[@class='follow_inner']//li[@class='follow_item S_line2']").nodes();

            for (Selectable node : nodes) {
                Person person = new Person();
                String userName = node.xpath("li[class='follow_item S_line2']//dt[@class='mod_pic']/a/@title").get();
                String url = "https://weibo.com" + node.xpath("li[class='follow_item S_line2']//dt[@class='mod_pic']/a/@href").get();
                person.setUrl(url);
                person.setName(userName);
                person.setId(skuData.getBrand());
                personList.add(person);
            }

        });

        page.getRequest().putExtra("personList", personList);
        page.putField("personList", personList);
        page.putField("skuData", skuData);

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
