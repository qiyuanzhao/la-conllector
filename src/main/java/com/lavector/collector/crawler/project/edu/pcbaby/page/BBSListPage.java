package com.lavector.collector.crawler.project.edu.pcbaby.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/11/15.
 *
 * @author zeng.zhao
 */
public class BBSListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://ks.pcbaby.com.cn/kids_bbs\\.shtml.*", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String keyword = page.getRequest().getExtra("keyword").toString();
        String site = page.getRequest().getExtra("site").toString();
        List<String> urls = html.xpath("//p[@class='aList-title']/a/@href").all();
        List<Request> requests = urls.stream().map(url -> {
            if (!url.contains("http")) {
                url = "http:" + url;
            }
            return new Request(url).putExtra("site", site).putExtra("keyword", keyword);
        })
                .collect(Collectors.toList());
        result.addRequests(requests);
        String nextUrl = html.xpath("//div[@id='pcbaby_page']/a[@class='next']/@HREF").get();
        if (StringUtils.isNoneBlank(nextUrl)) {
            result.addRequest(new Request(nextUrl).putExtra("site", site).putExtra("keyword", keyword));
        }
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }
}
