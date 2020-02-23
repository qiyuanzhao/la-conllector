package com.lavector.collector.crawler.project.wqc.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;


public class WqcSearchPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.lemanarc.com/grid-2/", url);
    }

    @Override
    public Result parse(Page page) {

        List<String> all = page.getHtml().xpath("//div[@class='content']//div[@class='grid-items js-isotope js-grid-items']//div[@class='project-item item-shadow']/a/@href").all();

        all.forEach(url->{
            Request request = new Request(url);
            page.addTargetRequest(request);
        });

//        String path = "G:/text/newWeibo/star/四.csv";
//
//        try {
//            if (!new File(path).exists()) {
//                boolean newFile = new File(path).createNewFile();
//                if (newFile) {
//
//                    for (String url : all) {
//                        FileUtils.writeStringToFile(new File(path), name + "," + url + "\n", Charset.forName("UTF-8"), true);
//                    }
//
//                }
//            }else {
//                for (String url : all) {
//                    FileUtils.writeStringToFile(new File(path), name + "," + url + "\n", Charset.forName("UTF-8"), true);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        String url = page.getHtml().xpath("//div[@class='plan_nav_btn']/a[@class='next']/@href").get();
//
//        page.addTargetRequest(new Request("http://chca.zhuyitai.com/" + url));

        page.setSkip(true);
        return null;
    }


    private void pasePage(Page page) {
        Html html = page.getHtml();

        Selectable xpath = html.xpath("//div[@id=main]//div[@class='catalog']");

        List<Selectable> nodes = xpath.xpath("//ul/li/a").nodes();

        nodes.forEach(node -> {
            String url = node.xpath("//a/@href").get();

            String newUrl = "https://www.kancloud.cn/owenwangwen/open-capacity-platform/" + url;

//            String name = node.xpath("//a/text()").get();
//            name = name.replace("\\", "_");

            page.addTargetRequest(new Request(newUrl).putExtra("name", url));

        });


    }

    @Override
    public String pageName() {
        return null;
    }
}
