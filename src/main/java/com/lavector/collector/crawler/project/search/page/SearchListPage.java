package com.lavector.collector.crawler.project.search.page;

import com.google.common.escape.UnicodeEscaper;
import com.google.common.net.PercentEscaper;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/11/30.
 *
 * @author zeng.zhao
 */
public class SearchListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return !url.contains("thread");
    }

    @Override
    public PageParse.Result parse(Page page) {
        Result result = Result.get();
        List<Request> requests = parseMessageList(page);
        result.addRequests(requests);
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private List<Request> parseMessageList (Page page) {
        Html html = page.getHtml();
        List<String> urls = html.xpath("//dl[@class='list-dl']/dt/a/@href").all();
//        System.out.println(urls.size() + "===" + page.getUrl().get());
//        List<Request> requests1 = new ArrayList<>();
//        if (urls.size() == 0) {
//            requests1.add(page.getRequest());
//        }
//        if (true) {
//            return requests1;
//        }

        List<Request> requests = urls.stream()
                .filter(url -> url.contains("thread"))
                .filter(url -> !RegexUtil.isMatch(".*club.autohome.com.cn/bbs/thread$", url))
                .map(url -> {
                    Request request = new Request(url);
                    request.putExtra("first", "first");
                    return request;
                }).collect(Collectors.toList());
        String nextPage = html.xpath("//div[@class='page']/a[@class='page-item-next']/@href").get();
        if (StringUtils.isNotBlank(nextPage)) {
            requests.add(new Request("https://sou.autohome.com.cn/luntan" + nextPage));
        }
        return requests;
    }

    public static void main(String[] args) {
        String s = "db.message.find({'$and': [{'tags.name': {'$in': [u'\\u5965\\u8fea A5 \\u5185\\u9970', u'\\u5965\\u8fea A5 \\u52a8\\u529b', u'\\u5965\\u8fea A5 \\u6cb9\\u8017', u'\\u5965\\u8fea A5 \\u5916\\u89c2', u'\\u5965\\u8fea A5 \\u7a7a\\u95f4', u'\\u5965\\u8fea A5 \\u9500\\u91cf', u'\\u5965\\u8fea A5 \\u53d1\\u52a8\\u673a', u'\\u5965\\u8fea A5 \\u4ef7\\u683c', u'\\u5965\\u8fea A5 \\u6027\\u4ef7\\u6bd4', u'\\u5965\\u8fea A5 \\u5b9e\\u62cd', u'\\u5965\\u8fea A5 \\u4e0a\\u5e02', u'\\u5965\\u8fea A5 \\u5b89\\u5168', u'\\u5965\\u8fea A5 \\u989c\\u503c', u'\\u5965\\u8fea A5 \\u5b89\\u5168\\u6027\\u80fd', u'\\u5965\\u8fea A5 \\u9a7e\\u4e58\\u8212\\u9002', u'\\u5965\\u8fea A5 \\u8212\\u9002', u'\\u5965\\u8fea A5 \\u611f\\u53d7', u'\\u5965\\u8fea A5 \\u63a8\\u8350', u'\\u5965\\u8fea A5 \\u9020\\u578b', u'\\u5965\\u8fea A5 \\u989c\\u8272', u'\\u5965\\u8fea A5 \\u706f', u'\\u5965\\u8fea A5 \\u8f6e\\u6bc2', u'\\u5965\\u8fea A5 \\u52a0\\u901f', u'\\u5965\\u8fea A5 \\u53d8\\u901f\\u7bb1', u'\\u5965\\u8fea A5 MMI', u'\\u5965\\u8fea A5 \\u5ea7\\u8231', u'\\u5965\\u8fea A5 \\u5c3a\\u5bf8', u'\\u5965\\u8fea A5 \\u843d\\u5730\\u4ef7', u'\\u5965\\u8fea A5 \\u88f8\\u8f66\\u4ef7', u'\\u5965\\u8fea A5 \\u4f18\\u60e0', u'\\u5965\\u8fea A5 \\u914d\\u7f6e']}}, {'tags.name': {'$nin': []}}], 'site': {'$in': ['weibo', 'zhihu', 'autohome', 'tieba']}, 'tC': {'$gte': datetime.datetime(2017, 11, 12, 0, 0), '$lt': datetime.datetime(2017, 11, 30, 0, 0)}}).count();";
        String format = String.format(s);
        System.out.println(format);
    }
}
