package com.lavector.collector.crawler.util;

import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.PlainText;

import java.io.IOException;
import java.util.Map;

/**
 * Created on 2018/11/23.
 *
 * @author zeng.zhao
 */
public class RequestUtil {

    public static String getContent(String url) throws IOException {
        return getContent(url, null);
    }

    public static String getContent(String url, Map<String, String> header) throws IOException {
        Request request = Request.Get(url);
        if (header != null && header.size() > 0) {
            header.forEach(request::addHeader);
        }
        return request.execute().returnContent().asString();
    }

    public static Page getPage(String url) throws IOException {
        return getPage(url, null);
    }

    public static Page getPage(String url, Map<String, String> header) throws IOException {
        String content = getContent(url, header);
        Page page = new Page();
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setUrl(new PlainText(url));
        page.setRawText(content);
        return page;
    }
}
