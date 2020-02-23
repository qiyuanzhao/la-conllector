package com.lavector.collector.crawler.project.weibo.weiboSearchContent.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

/**
 * Created by qyz on 2019/12/20.
 */
public class PersonInfoPage implements PageParse {


    private Logger logger = LoggerFactory.getLogger(PersonInfoPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://d.weibo.com.*", url);
    }

    @Override
    public Result parse(Page page) {

        Html html = page.getHtml();
        List<String> scripts = html.xpath("script/html()").all();

        Optional<String> optionalS = scripts.stream().filter(s -> s.contains("FM.view({\"ns\":\"pl.content.signInPeople.index\"")).findFirst();
        if (optionalS == null) {
            Request request = new Request();
            request.setUrl(page.getUrl().get());
            page.addTargetRequest(request);
            page.setSkip(true);
            return null;
        }
        optionalS.ifPresent((String s) -> {
            String json = new Json(s).regex("FM.view\\((.*)\\)").get();
            String htmlStr = JsonPath.read(json, "$.html");
            Html newHtml = new Html(htmlStr);

            List<Selectable> nodes = newHtml.xpath("//div[@class='WB_cardwrap S_bg2']//div[@class='follow_inner']//ul[@class='follow_list']/li[@class='follow_item S_line2']").nodes();


            String path = "G:/text/newWeibo/star/" + "KOL榜单" + ".csv";
            try {
                if (!new File(path).exists()) {
                    boolean newFile = new File(path).createNewFile();
                    if (newFile) {
                        String header = "用户id,姓名,粉丝数\n";
                        FileUtils.writeStringToFile(new File(path), header, Charset.forName("GBK"), true);
                    }
                }

                nodes.forEach(node -> {
                    String name = node.xpath("//dl[@class='clearfix']//div[@class='info_name W_fb W_f14']/a[@class='S_txt1']/strong/text()").get();
                    String idStr = node.xpath("//dl[@class='clearfix']//div[@class='info_name W_fb W_f14']/a[@class='S_txt1']/strong/@usercard").get();

                    String id = RegexUtil.findFirstGroup(idStr, "([0-9]+)");

                    String count = node.xpath("//dl[@class='clearfix']//div[@class='info_connect']/span[2]/em/text()").get();
                    String countNumber = "";
                    if (count.contains("万")) {
                        countNumber = RegexUtil.findFirstGroup(count, "([0-9]+)") + "0000";
                    }else {
                        countNumber = RegexUtil.findFirstGroup(count, "([0-9]+)");
                    }

                    if (!StringUtils.isEmpty(countNumber) && Integer.parseInt(countNumber) > 50000) {

                        String writeContent = id + "," + name + "," + countNumber + "\n";
                        try {
                            FileUtils.writeStringToFile(new File(path), writeContent, Charset.forName("GBK"), true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        logger.info("写入一条数据。。。");

                    }

                });

            } catch (IOException e) {
                e.printStackTrace();
            }

            String nextUrl = "https://d.weibo.com" + newHtml.xpath("//div[@class='WB_cardwrap S_bg2']//div[@class='W_pages']//a[@class='page next S_txt1 S_line1']/@href").get();

            page.addTargetRequest(new Request(nextUrl));

            page.setSkip(true);


        });


        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
