package com.lavector.collector.crawler.project.gengmei.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.gengmei.entity.Person;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class PersonPage implements PageParse {


    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.soyoung.com/u[0-9]+", url);

    }

    @Override
    public Result parse(Page page) {
        Selectable selectable = page.getHtml().xpath("//div[@id='bd']//div[@class='column']//li[@class='first']//div[@class='con']");

        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        Person person = new Person();

        if (selectable != null) {
            List<Selectable> nodes = selectable.xpath("//div/p").nodes();

            for (Selectable node : nodes) {
                String s = node.xpath("//p/text()").get();
                if (!StringUtils.isEmpty(s)) {
                    if (s.contains("性别")) {
                        String sex = s.substring(s.indexOf("：") + 1);
                        person.setSex(sex);
                    } else if (s.contains("年龄")) {
                        String age = s.substring(s.indexOf("：") + 1);
                        person.setAge(age);
                    } else if (s.contains("地区")) {
                        String region = s.substring(s.indexOf("：") + 1);
                        person.setRegion(region);
                    } else if (s.contains("氧分")) {
                        String oxygenContent = s.substring(s.indexOf("：") + 1);
                        person.setOxygenContent(oxygenContent);
                    } else if (s.contains("经验值")) {
                        String empiricalValue = s.substring(s.indexOf("：") + 1);
                        person.setEmpiricalValue(empiricalValue);
                    }
                }
            }
        }

        person.setUrl(page.getUrl().get());
        page.getRequest().putExtra("person", person);
        page.getRequest().putExtra("skuData", skuData);

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
