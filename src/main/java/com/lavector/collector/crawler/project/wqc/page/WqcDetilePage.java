package com.lavector.collector.crawler.project.wqc.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class WqcDetilePage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.lemanarc.com/projects/.*", url);
    }

    @Override
    public Result parse(Page page) {

        String path = "G:/text/newWeibo/star/医院.csv";
        //标题
        String contentStr = page.getHtml().xpath("//div[@class='content']/p/text()").get();
        //http://www.lemanarc.com/wp-content/uploads/2017/05/best-shot-1-1.jpg

        List<String> all = page.getHtml().xpath("//div[@class='content']//div[@id='rev_slider2']//rs-slides/rs-slide/img/@src").all();

        Selectable projectContent = page.getHtml().xpath("//div[@class='content']//div[@class='project-detail-info']//div[@class='project-detail-content']");

        String name = projectContent.xpath("//h3[@class='project-detail-title']/text()").get();
        String jianjieStr = projectContent.xpath("//p[@class='project-detail-text]/text()").get();
        List<Selectable> nodes = projectContent.xpath("//ul[@class='project-detail-list text-dark']/li").nodes();

        String kehu = nodes.get(0).xpath("//span[@class='right']/text()").get();
        String shjian = nodes.get(1).xpath("//span[@class='right']/text()").get();
        String guimoStr = nodes.get(2).xpath("//span[@class='right']/text()").get();
        String dizhi = nodes.get(3).xpath("//span[@class='right']/text()").get();


        String content = handleContent(contentStr);
        String jianjie = handleContent(jianjieStr);
        String guimo = guimoStr.replace(",", "");


        try {
            if (!new File(path).exists()) {
                boolean newFile = new File(path).createNewFile();
                if (newFile) {

                    for (String picurl : all) {
                        FileUtils.writeStringToFile(new File(path), name + "," + content + "," + jianjie + ","
                                + kehu + "," + shjian + "," + guimo + "," + dizhi + "," + picurl + ","
                                + page.getUrl().get() + "\n", Charset.forName("UTF-8"), true);
                    }

                }
            } else {
                for (String picurl : all) {
                    FileUtils.writeStringToFile(new File(path), name + "," + content + "," + jianjie + ","
                            + kehu + "," + shjian + "," + guimo + "," + dizhi + "," + picurl + ","
                            + page.getUrl().get() + "\n", Charset.forName("UTF-8"), true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        page.setSkip(true);
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

    @Override
    public String pageName() {
        return null;
    }
}
