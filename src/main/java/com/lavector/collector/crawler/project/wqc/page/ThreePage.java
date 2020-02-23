package com.lavector.collector.crawler.project.wqc.page;

import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qyz on 2019/10/24.
 */
public class ThreePage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.contains("https://www.aedas.com/en/what-we-do");
    }


    @Override
    public Result parse(Page page) {

        String name12 = page.getRequest().getExtra("name1").toString();
        String name22 = page.getRequest().getExtra("name2").toString();
        String name32 = page.getRequest().getExtra("name3").toString();


        String name1 = handleContent(name12);
        String name2 = handleContent(name22);
        String name3 = handleContent(name32);

        Selectable cssLayer1 = page.getHtml().xpath("//div[@class='cssMasterPanel']//div[@class='cssContentPanel']//div[@class='cssLayer1']");
        Selectable cssLayer2 = page.getHtml().xpath("//div[@class='cssMasterPanel']//div[@class='cssContentPanel']//div[@class='cssLayer2']");

        List<String> picList = new ArrayList<>();
        String pic1 = cssLayer1.xpath("//div[@class='cssLayer1 ']//div[@class='cssCr']/img/@src").get();
        picList.add(pic1);
        List<Selectable> nodes = cssLayer2.xpath("//div[@class='cssWrapper']//div[@class='cssGalleryFrame']//div[@class='cssBg cssZoom']").nodes();

        nodes.forEach(node -> {
            String src = node.xpath("//div[@class='cssBg cssZoom']/@data-src").get();
            picList.add(src);
        });

        System.out.println("一共：" + picList.size() + "条");
        String path = "G:/text/newWeibo/star/Architecture.csv";


        try {
            if (!new File(path).exists()) {
                boolean newFile = new File(path).createNewFile();
                if (newFile) {

                    for (String picurl : picList) {
                        FileUtils.writeStringToFile(new File(path), name1 + "," + name2 + "," + name3 + "," + picurl + ","
                                + page.getUrl().get() + "\n", Charset.forName("UTF-8"), true);
                        System.out.println("写入一条");
                    }

                }
            } else {
                for (String picurl : picList) {
                    FileUtils.writeStringToFile(new File(path), name1 + "," + name2 + "," + name3 + "," + picurl + ","
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

    public String handleContent(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        String replace = content.replace("\n", "").replace(",", "，").replace("\t", "");
        if (replace.length() > 30000) {
            replace = replace.substring(0, 30000);
        }
        return replace;
    }
}
