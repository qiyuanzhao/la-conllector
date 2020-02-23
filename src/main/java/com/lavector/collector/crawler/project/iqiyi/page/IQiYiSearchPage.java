package com.lavector.collector.crawler.project.iqiyi.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.crawler.util.UrlUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


public class IQiYiSearchPage implements PageParse {

    private static final String path = "G:\\text\\iqiyi\\data/iqiyi.txt";

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://so.iqiyi.com/so.*", url);
    }

    @Override
    public Result parse(Page page) {
        String brand = page.getRequest().getExtra("brand").toString();
        Integer pageNumber = (Integer) page.getRequest().getExtra("pageNumber");

        List<Selectable> nodes = page.getHtml().xpath("//div[@class='qy-search-page']//div[@class='qy-search-result-con']//div[@class='qy-search-result-item']").nodes();

        nodes.forEach(node -> {
            String vidoUrl = node.xpath("//div[@class='result-figure']//a[@class='qy-mod-link']/@href").get();
            String title = node.xpath("//div[@class='result-figure']//a[@class='qy-mod-link']/img[@class='qy-mod-cover']/@title").get();
            String time = "";
            String sorce = "";
            List<Selectable> timeAndSources = node.xpath("//div[@class='result-right']/div[@class='qy-search-result-info half']").nodes();
            for (Selectable selectable : timeAndSources) {
                String infoLbl = selectable.xpath("//div[@class='qy-search-result-info half']/label[@class='info-lbl']/text()").get();
                if (!StringUtils.isEmpty(infoLbl)) {
                    if (infoLbl.contains("发布时间")) {
                        time = selectable.xpath("//div[@class='qy-search-result-info half']/span[@class='info-des']/text()").get();
                    }
                    if (infoLbl.contains("来源")) {
                        sorce = selectable.xpath("//div[@class='qy-search-result-info half']/span[@class='info-des']/text()").get();
                    }
                }
            }
            if (StringUtils.isEmpty(sorce)) {
                sorce = node.xpath("//div[@class='result-right']//div[@class='qy-search-result-info uploader']/span[@class='info-uploader']/a[@class='uploader-name']/text()").get();
            }

            String jianjie = node.xpath("//div[@class='result-right']/div[@class='qy-search-result-info multiple']/span[@class='info-des']/text()").get();


            try {
                if (!new File(path).exists()) {
                    boolean newFile = new File(path).createNewFile();
                    if (newFile) {
                        FileUtils.writeStringToFile(new File(path), "关键词" + "," + "title" + "," + "來源" + "," + "时间" + "," + "简介" + "," + "视频链接" + "\n", Charset.forName("UTF-8"), true);
                        System.out.println("写入一条");
                    }
                }

                FileUtils.writeStringToFile(new File(path), brand + "," + handleContent(title) + "," + sorce + "," + time + "," + handleContent(jianjie) + "," + vidoUrl +
                        "\n", Charset.forName("UTF-8"), true);
                System.out.println("写入一条");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pageNumber = pageNumber + 1;
        if (pageNumber < 20 && nodes.size() > 0) {
            String nextUrl = "https://so.iqiyi.com/so/q_" + UrlUtils.encode(brand) + "_ctg__t_0_page_" + pageNumber + "_p_1_qc_0_rd__site__m_4_bitrate_";
            page.addTargetRequest(new Request(nextUrl).putExtra("brand", brand).putExtra("pageNumber", pageNumber));
        }
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
