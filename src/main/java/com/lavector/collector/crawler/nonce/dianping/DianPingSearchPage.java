package com.lavector.collector.crawler.nonce.dianping;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

/**
 * Created on 20/07/2018.
 *
 * @author zeng.zhao
 */
public class DianPingSearchPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.dianping.com/search/keyword");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String keyword = page.getRequest().getExtra("keyword").toString();
        html.xpath("//div[@class='txt']").nodes()
                .stream()
                .filter(node -> {
                    String commentStr = node.xpath("//a[@class='review-num']/b/text()").get();
                    String tag = node.xpath("//div[@class='tag-addr']/a[2]/span/text()").get();
                    String name = node.xpath("//div[@class='tit']/a/h4/text()").get();
                    String address = node.xpath("//div[@class='tag-addr']/span[@class='addr']/text()").get();
                    return commentStr != null && Integer.parseInt(commentStr) > 0 && (tag.contains(keyword) || name.contains(keyword) || address.contains(keyword));
                })
                .forEach(node -> {
                    String shopId = node.xpath("//a[@class='review-num']/@href").regex("shop/(\\d+)").get();
                    result.addRequest(new Request("http://www.dianping.com/shop/" + shopId).putExtra("referer", page.getUrl().get())
                        .putExtra("keyword", keyword)
                    );
                });

        String next = html.xpath("//div[@class='page']/a[@class='next']/@href").get();
        if (next != null) {
            result.addRequest(new Request(next).putExtra("referer", page.getUrl().get())
                .putExtra("keyword", keyword)
            );
        }
        return result;
    }
}
