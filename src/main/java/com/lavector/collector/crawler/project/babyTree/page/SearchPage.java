package com.lavector.collector.crawler.project.babyTree.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.entity.readData.SkuData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchPage implements PageParse {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    private Logger logger = LoggerFactory.getLogger(SearchPage.class);


    @Override
    public boolean handleUrl(String url) {
        boolean b = url.contains("http://www.babytree.com/s.php?q=") && url.contains("&c=community&cid=0&range=&pg=");
        return b;
    }

    @Override
    public Result parse(Page page) {
        String url = page.getUrl().get();
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        Result result = Result.get();
        List<Selectable> all = new LinkedList<>();
        try {
            all = page.getHtml().xpath("//div[@class='search_col_2']/div[@class='search_item_area']/div[@class='search_item']").nodes();
        } catch (Exception e) {
            logger.info("解析异常");
        }

        List<BabyPrams> babyPramsList = new ArrayList<>();
        all.forEach((Selectable node) -> {
            BabyPrams babyPrams = new BabyPrams();

            String detileUrl = node.xpath("//div[@class='search_item_tit']/a/@href").get();
            List<String> stringList = node.xpath("//div[@class='search_item_info']/span[@class='search_num']/text()").regex("[0-9]+").all();
            List<Selectable> listSe = node.xpath("//div[@class='search_item_info']/span[@class='search_date']").nodes();
            String date = listSe.get(0).xpath("//span[@class='search_date']/text()").get();
            String fromWhere = listSe.get(1).xpath("//span[@class='search_date']/a/text()").get();
            String userName = node.xpath("//div[@class='search_item_info']/a[@class='search_category']/text()").get();

            babyPrams.setUrl(detileUrl);
            babyPrams.setDate(date);
            babyPrams.setFromWhere(fromWhere);
            babyPrams.setUserName(userName);
            if (stringList.size() > 0) babyPrams.setBrowseNumber(stringList.get(0));
            if (stringList.size() > 1) babyPrams.setCommentNumber(stringList.get(1));

            rabbitTemplate.convertAndSend("test",detileUrl);
//            babyPramsList.add(babyPrams);
//            result.addRequest(request);
//            Request request = new Request();
//            request.setUrl(detileUrl);
//            request.putExtra("babyPrams", babyPrams);
//            request.putExtra("skuData", skuData);
//            result.addRequest(request);
        });

        //分页
        int count = Integer.parseInt(page.getHtml().xpath("//div[@class='pagejump']/span[@class='page-number']/text()").regex("[0-9]+").get());
        int current = Integer.parseInt(url.substring(url.lastIndexOf("=") + 1));
        if (current < count) {
            String newUrl = url.replace("&pg=" + current, "&pg=" + (current + 1));
            result.addRequest(new Request(newUrl).putExtra("skuData", skuData));
        }

//        Request request = new Request();
//        request.putExtra("babyPrams", babyPramsList);
//        request.putExtra("skuData", skuData);
//        page.setRequest(request);
//        page.putField("babyPrams",babyPramsList);
//        page.putField("skuData",skuData);
        page.setSkip(true);
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }


    public static void main(String[] args) {

        String st = "view-source:http://www.babytree.com/s.php?q=%E7%BB%B4%E7%94%9F%E7%B4%A0&c=community&cid=0&range=&pg=1";

        String substring = st.substring(st.lastIndexOf("=") + 1);
        System.out.println(substring);
    }

}
