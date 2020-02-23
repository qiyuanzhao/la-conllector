package com.lavector.collector.crawler.project.weibo.weiboSearchContent.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by qyz on 2019/12/20.
 */
public class SearchContentPersonPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(SearchContentPersonPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://s.weibo.com/user\\?q.*", url);
    }

    @Override
    public Result parse(Page page) {

        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");

        Html html = page.getHtml();

        String id = "";
        List<Selectable> nodes = html.xpath("div[@class='m-main']//div[@class='m-wrap']//div[@class='card card-user-b s-pg16 s-brt1']").nodes();
        if (nodes.size()>0){
            Selectable selectable = nodes.get(0);
            String url = selectable.xpath("//div[@class='info']/p/span[@class='s-nobr']/a/@href").get();
            System.out.println(url);
            id = RegexUtil.findFirstGroup(url, "([0-9]+)");
            System.out.println(id);
        }


        String path = "G:/text/newWeibo/star/" + "明星id_KOL" + ".csv";
        try {
            if (!new File(path).exists()) {
                boolean newFile = new File(path).createNewFile();
                if (newFile) {
                    String header = "用户id,姓名\n";
                    FileUtils.writeStringToFile(new File(path), header, Charset.forName("GBK"), true);
                }
            }
            String writeContent = id + "," + skuData.getBrand() + "\n";
            FileUtils.writeStringToFile(new File(path), writeContent, Charset.forName("GBK"), true);
            logger.info("写入一条数据。。。");
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
