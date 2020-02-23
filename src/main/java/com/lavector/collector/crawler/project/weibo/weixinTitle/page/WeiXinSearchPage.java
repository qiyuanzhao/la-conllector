package com.lavector.collector.crawler.project.weibo.weixinTitle.page;


import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class WeiXinSearchPage implements PageParse {

    private static String startUrl = "https://weixin.sogou.com/weixin?type=2&page=1&query=";


    @Override
    public Result parse(Page page) {

        Project project = (Project) page.getRequest().getExtra("project");

        String id = page.getUrl().regex("&page=[0-9]+").regex("[0-9]+").get();

        List<Selectable> nodes = page.getHtml().xpath("//div[@class='txt-box']").nodes();

        for (Selectable node : nodes) {
            String detileUrl = node.xpath("//h3/a/@href").get();
            Request request = new Request();
            request.setUrl(detileUrl);
            request.putExtra("project", project);
            page.addTargetRequest(request);
        }
        List<Selectable> nodes1 = page.getHtml().xpath("//div[@class='p-fy']/a[@id!='sogou_next']").nodes();
        if (nodes1 != null && nodes1.size() > 0) {
            for (Selectable selectable : nodes1) {
                Request request = new Request();
                String newUrl = selectable.xpath("//a/@href").get();
                request.setUrl("https://weixin.sogou.com/weixin" + newUrl);
                request.addHeader("Referer", startUrl + project.getKeyWord());
                request.putExtra("project", project);
                page.addTargetRequest(request);
            }
        }

        page.setSkip(true);
        return null;
    }


    @Override
    public boolean handleUrl(String url) {
        return false;
    }


    @Override
    public String pageName() {
        return null;
    }
}
