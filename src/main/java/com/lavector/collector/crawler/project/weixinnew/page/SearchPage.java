package com.lavector.collector.crawler.project.weixinnew.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class SearchPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://news.sogou.com/news.*", url);
    }

    private static final String path = "G:\\text\\weixinnew\\data/weimeng.txt";

    @Override
    public Result parse(Page page) {
        String brand = page.getRequest().getExtra("brand").toString();
        List<Selectable> nodes = page.getHtml().xpath("//div[@class='wrap']//div[@class='results']/div[@class='vrwrap']").nodes();

        nodes.forEach(node -> {
            String title = node.xpath("//h3[@class='vrTitle']/a/text()").get();

            Selectable newInfo = node.xpath("//div[@class='news-detail']//div[@class='news-info']");

            String newsFrom = newInfo.xpath("//p[@class='news-from']/allText()").get();

            String from = "";
            String time = "";
            if (!StringUtils.isEmpty(newsFrom)) {

                String[] split = newsFrom.split(" ");

                from = split[0];

                time = split[1];

            }
            String newText = newInfo.xpath("//p[@class='news-txt']/span/allText()").get();

            if (!StringUtils.isEmpty(title)){
                try {
                    if (!new File(path).exists()) {
                        boolean newFile = new File(path).createNewFile();
                        if (newFile) {
                            FileUtils.writeStringToFile(new File(path), "关键词" + "," + "title" + "," + "來源" + "," + "时间" + "," + "content" + "\n", Charset.forName("UTF-8"), true);
                            System.out.println("写入一条");
                        }
                    }

                    FileUtils.writeStringToFile(new File(path), brand + "," + handleContent(title) + "," + from + "," + time + "," + handleContent(newText) +
                            "\n", Charset.forName("UTF-8"), true);
                    System.out.println("写入一条");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        String url = page.getRequest().getUrl();
        String pageNumber = url.substring(url.lastIndexOf("=") + 1);
        int newPageNumber = Integer.parseInt(pageNumber) + 1;

        if (nodes.size() > 0) {
            String newUrl = url.substring(0, url.lastIndexOf("=") + 1) + newPageNumber;
            page.addTargetRequest(new Request(newUrl).putExtra("brand", brand));
//            page.setSkip(true);
        }

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
