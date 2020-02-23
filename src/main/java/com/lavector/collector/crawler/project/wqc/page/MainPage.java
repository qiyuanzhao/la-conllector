package com.lavector.collector.crawler.project.wqc.page;

import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.io.FileUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        //https://www.aedas.com/en/what-we-do/architecture

//        return url.contains("https://www.aedas.com/en/what-we-do");
        return false;
    }

    @Override
    public Result parse(Page page) {

        String brand = page.getRequest().getExtra("brand").toString();


        Selectable xpath = page.getHtml().xpath("//div[@class='cssMasterPanel']//div[@class='cssContentPanel']//div[@class='cssLhsContent']");

        List<Selectable> nodes = xpath.xpath("//div[@class='cssLinks']//li").nodes();

        List<String> list = new ArrayList<>();
        List<String> names = new ArrayList<>();
        nodes.forEach(node -> {
//            String endUrl = node.xpath("//a/@href").get();

//            String url = "https://www.aedas.com" + endUrl;
//            Request request = new Request(url).putExtra("name", name);
//            page.addTargetRequest(request);

            String code = node.xpath("//li/@data-url").get();
            String name = node.xpath("//a/span/text()").get();
//
            String url = "https://www.aedas.com/feedcall.php?type=project&lang=en&sort_by=field_completion_year1_value&sort_order=DESC&field_services_nid[]=" + code;


            list.add(url);
            names.add(name);

//            Request request = new Request(url);
//            page.addTargetRequest(request);


        });


        String path = "G:/text/newWeibo/star/all.csv";
        try {
            if (!new File(path).exists()) {
                boolean newFile = new File(path).createNewFile();
                if (newFile) {

                    for (int i = 0; i < list.size(); i++) {
                        FileUtils.writeStringToFile(new File(path), brand + "," + names.get(i) + "," + list.get(i) + ","
                                + page.getUrl().get() + "\n", Charset.forName("UTF-8"), true);
                        System.out.println("写入一条");
                    }

                }
            } else {
                for (int i = 0; i < list.size(); i++) {
                    FileUtils.writeStringToFile(new File(path), brand + "," + names.get(i) + "," + list.get(i) + ","
                            + page.getUrl().get() + "\n", Charset.forName("UTF-8"), true);
                    System.out.println("写入一条");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        page.setSkip(true);

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
