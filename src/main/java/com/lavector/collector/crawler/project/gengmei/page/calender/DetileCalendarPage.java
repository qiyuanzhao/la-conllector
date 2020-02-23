package com.lavector.collector.crawler.project.gengmei.page.calender;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.gengmei.entity.UserDiary;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

public class DetileCalendarPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.soyoung.com/p[0-9]+/", url);
    }

    @Override
    public Result parse(Page page) {

        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");

        String url = (String) page.getRequest().getExtra("url");
        Selectable selectable = page.getHtml().xpath("//div[@id='bd']");
        Selectable selectable1 = selectable.xpath("//div[@class='column']");
        Selectable selectable2 = selectable1.xpath("//div[@class='content']");

        StringBuilder stringBuilder = new StringBuilder();
        List<Selectable> nodes = selectable2.xpath("//div[@class='c']/p[@class='text']").nodes();
        List<Selectable> ps = selectable2.xpath("//div[@class='c']/p").nodes();
        String test = selectable2.xpath("//div[@class='c']/text()").get();


        if (nodes.size() > 0) {
            nodes.forEach(node -> {
                String title = node.xpath("//p/text()").get();
                stringBuilder.append(title);
            });
        } else if (ps.size() > 0) {
            ps.forEach(se -> {
                String title = se.xpath("//p/allText()").get();
                stringBuilder.append(title);
            });
        } else if (!StringUtils.isEmpty(test)) {
            stringBuilder.append(test);
        } else {
            String title = selectable2.xpath("//div[@class='c']/p/text()").get();
            stringBuilder.append(title);
        }
        String string = stringBuilder.toString();


        String content = "";
        List<Selectable> images = selectable2.xpath("//div[@class='c']/p[@class='image']").nodes();
        if (images.size() > 0) {
            Selectable selectable3 = images.get(0);
            content = selectable3.xpath("//p/img/@title").get();
        }


        UserDiary userDiary = new UserDiary();
        if (!StringUtils.isEmpty(stringBuilder)) {
            userDiary.setUrl(url);
            userDiary.setGroup_id(stringBuilder.toString());
            userDiary.setCreate_date(content);
        }

        List<UserDiary> list = new ArrayList<>();
        list.add(userDiary);
        page.getRequest().putExtra("userDiary", list);
        page.getRequest().putExtra("skuData", skuData);

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
