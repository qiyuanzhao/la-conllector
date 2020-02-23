package com.lavector.collector.crawler.project.weibo.weiboStar.page;


import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.Optional;

public class UserDetilPage implements PageParse {


    @Override
    public Result parse(Page page) {
        Person person = (Person) page.getRequest().getExtra("person");

        Html html = page.getHtml();
        List<String> scripts = html.xpath("script/html()").all();

        Optional<String> stringOptional = scripts.stream().filter(s -> s.contains("\"domid\":\"Pl_Official_PersonalInfo")).findFirst();

        stringOptional.ifPresent(s -> {
            String json = new Json(s).regex("FM.view\\((.*)\\)").get();

            String selectable = JsonPath.read(json, "$.html");

            String all = selectable.replace("\\", "");
            Html newHtml = new Html(all);


            List<Selectable> nodes = newHtml.xpath("//div[@class='WB_cardwrap S_bg2']").nodes();
            for (Selectable selectable1 : nodes) {
                String text = selectable1.xpath("//div[@class='WB_cardtitle_b S_line2']/div/h2/text()").get();
                if (text.contains("基本信息")) {
                    List<Selectable> selectables = selectable1.xpath("//li[@class='li_1 clearfix']").nodes();
                    for (Selectable sel : selectables) {

                        String string = sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_title S_txt2']/text()").get();

                        if (string!=null){
                            if (string.contains("昵称")){
                                person.setName(sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_detail']/text()").get());
                            }else if (string.contains("所在地")){
                                person.setCity(sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_detail']/text()").get());
                            }else if (string.contains("性别")){
                                person.setSex(sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_detail']/text()").get());
                            }else if (string.contains("生日")){
                                person.setAge(sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_detail']/text()").get());
                            }
                        }
                    }

                }
            }

        });
        page.putField("person",person);
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
