package com.lavector.collector.crawler.project.article.tianya.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.crawler.util.StringToDateConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/12/26.
 *
 * @author zeng.zhao
 */
public class SearchListPage implements PageParse {

    private static Date targetTime = DateUtils.addDays(new Date(), -90);

    private StringToDateConverter converter = new StringToDateConverter();

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch(".*search.tianya.cn/bbs\\?q=.*", url);
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String brand = page.getRequest().getExtra("brand").toString();
        Html html = page.getHtml();
        List<Request> requests = html.xpath("//div[@class='searchListOne']/ul/li/div/h3/a/@href").all().stream()
                .map(url -> {
                    Request request = new Request(url);
                    request.putExtra("brand", brand);
                    return request;
                }).collect(Collectors.toList());
        result.addRequests(requests);


        List<String> publishTimes = html.xpath("//div[@class='searchListOne']/ul/li/p/span[1]/text()").all();
        if (CollectionUtils.isEmpty(publishTimes)) {
            return result;
        }
        String lastTime = publishTimes.get(publishTimes.size() - 1);
        Date lastDate = converter.convert(lastTime);
        if (CollectionUtils.isNotEmpty(requests) && (lastDate != null && lastDate.after(targetTime))) {
            String url = page.getUrl().get();
            String pageNum = page.getUrl().regex("&pn=(\\d+)").get();
            Integer nextPageNum = Integer.parseInt(pageNum) + 1;
            String nextUrl = url.replace("&pn=" + pageNum, "&pn=" + nextPageNum);
            Request nextRequest = new Request(nextUrl);
            nextRequest.putExtra("brand", brand);
            result.addRequest(nextRequest);
        }
        return result;
    }
}
