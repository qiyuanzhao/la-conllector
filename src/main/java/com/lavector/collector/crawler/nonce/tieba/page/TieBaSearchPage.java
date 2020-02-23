package com.lavector.collector.crawler.nonce.tieba.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.tieba.TieBaPageProcessor;
import com.lavector.collector.crawler.util.StringToDateConverter;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 23/06/2018.
 *
 * @author zeng.zhao
 */
public class TieBaSearchPage implements PageParse {

    private StringToDateConverter converter = new StringToDateConverter();

    @Override
    public boolean handleUrl(String url) {
        return url.contains("http://tieba.baidu.com/f/search");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        List<Selectable> divs = html.xpath("//div[@class='s_post']").nodes();
        List<us.codecraft.webmagic.Request> requests = new ArrayList<>();
        divs.stream()
                .filter(div -> {
                    String time = div.xpath("//font[@class='p_green p_date']/text()").get();
                    Date date = converter.convert(time);
                    return date != null && date.after(TieBaPageProcessor.startTime);
                })
                .forEach(div -> {
                    String threadId = div.xpath("//span[@class='p_title']/a/@href").regex("p/(\\d+)").get();
                    String url = "http://tieba.baidu.com/p/" + threadId + "?pn=1";
                    requests.add(new us.codecraft.webmagic.Request(url));
                });
        result.addRequests(requests);
        if (requests.size() > 0)

        {
            String next = html.xpath("//a[@class='next']/@href").get();
            if (!next.contains("tieba.baidu.com")) {
                next = "http://tieba.baidu.com" + next;
            }
            result.addRequest(new us.codecraft.webmagic.Request(next));
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String url = "http://tieba.baidu.com/f/search/res?isnew=1&kw=&qw=%C3%C0%C4%EA%B4%EF&rn=10&un=&only_thread=1&sm=1&sd=&ed=&pn=1";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setUrl(new Json(url));
        new TieBaSearchPage().parse(page);
    }
}
