package com.lavector.collector.crawler.project.movie.iqiyi.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/2/28.
 *
 * @author zeng.zhao
 */
public class CategoryListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://list.iqiyi.com/www/20/-------------11-1-2-iqiyi-1-.html");
    }

    @Override
    public String pageName() {
        return null;
    }


    /**
     * 按行业划分
     */
    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        List<Selectable> nodes = html.xpath("//div[@class='mod_sear_list       ']").nodes();
        List<String> urls = new ArrayList<>();
        nodes.stream().filter(node -> {
            String title = node.xpath("//h3/text()").get();
            if (title.contains("行业")) {
                return true;
            }
            return false;
        }).forEach(node -> {
            List<String> urlList = node.xpath("//li/a/@href").regex(".*www.*").all();
            urls.addAll(urlList);
        });
        urls.stream().map(url -> {
            if (!url.contains("http")) {
                url = "http://list.iqiyi.com" + url;
            }
            return new Request(url);
        }).forEach(result::addRequest);
        return result;
    }
}
