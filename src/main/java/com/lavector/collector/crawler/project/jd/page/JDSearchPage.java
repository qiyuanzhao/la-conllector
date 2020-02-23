package com.lavector.collector.crawler.project.jd.page;

import com.lavector.collector.crawler.base.PageParse;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on 2018/1/17.
 *
 * @author zeng.zhao
 */
public class JDSearchPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("search.jd.com/search?keyword");
    }

    @Override
    public String pageName() {
        return null;
    }

    private static Set<String> urls = new HashSet<>();

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String keyword = page.getRequest().getExtra("keyword").toString();
        Html html = page.getHtml();
        List<String> itemUrls = html.xpath("//div[@class='gl-i-wrap']/div[@class='p-img']/a/@href").all();
        Set<Request> requests = itemUrls.stream()
                .filter(url -> url.contains("item.jd.com"))
                .map(url -> {
                    if (!url.contains("http:") || !url.contains("https")) {
                        url = "http:" + url;
                    }
                    return new Request(url).putExtra("keyword", keyword);
                })
                .collect(Collectors.toSet());

        if (itemUrls.size() >= 0) {
            Integer currentPage = Integer.parseInt(page.getUrl().regex("page=(\\d+)").get());
            List<String> scripts = html.xpath("script").all();
            String logParm = null;
            for (String script : scripts) {
                if (script.contains("LogParm =")) {
                    logParm = script;
                    break;
                }
            }
            if (logParm != null) {
                logParm = logParm.substring(0, logParm.indexOf("};") + 2);
                logParm = logParm.replace("<script>", "");
                ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
                ScriptEngine engine = scriptEngineManager.getEngineByName("nashorn");
                try {
                    engine.eval(logParm);
                } catch (ScriptException e) {
                    e.printStackTrace();
                }

                ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) engine.get("LogParm");
                String log_id = scriptObjectMirror.get("log_id").toString();
                if (StringUtils.isNotBlank(log_id)) {
                    Integer nextPage = currentPage + 1;
                    String url = "https://search.jd.com/s_new.php?keyword=" + keyword +
                            "&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&page=" + nextPage +
                            "&s=29&scrolling=y&log_id=" + log_id +
                            "&tpl=3_M";
                    try {
                        String content = org.apache.http.client.fluent.Request
                                .Get(url)
                                .addHeader("referer", page.getUrl().get())
                                .execute()
                                .returnContent()
                                .asString(Charset.forName("utf-8"));
                        Html childHtml = new Html(content);
                        List<String> childItemUrls = childHtml.xpath("//div[@class='gl-i-wrap']/div[@class='p-img']/a/@href").all();
                        childItemUrls.stream()
                                .filter(item -> item.contains("item.jd.com"))
                                .forEach(item -> {
                                    if (!(item.contains("http:") || item.contains("https"))) {
                                        item = "http:" + item;
                                    }
                                    Request request = new Request(item);
                                    request.putExtra("keyword", keyword);
                                    requests.add(request);
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            Integer nextPage = currentPage + 2;
            if (nextPage > 10) {
                return result;
            }
            String nextUrl = page.getUrl().get().replace("page=" + currentPage, "page=" + nextPage);
            Request request = new Request(nextUrl);
            request.putExtra("keyword", keyword);
            result.addRequest(request);
//            requests.forEach(request1 -> urls.add(request1.getUrl()));
//            System.out.println(requests.size());
        }
        result.addRequests(requests);
        return result;
    }

    public static void main(String[] args) throws Exception {
        String referer = "https://search.jd.com/Search?keyword=";
//        String url = "https://search.jd.com/s_new.php?keyword=3ds&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&page=2&s=29&scrolling=y&log_id=1516170785.94289&tpl=3_M";
        String url = "https://search.jd.com/Search?keyword=稻香村&enc=utf-8&page=1";
        String content = org.apache.http.client.fluent.Request
                .Get(url)
//                .addHeader("referer", referer)
                .execute()
                .returnContent()
                .asString(Charset.forName("utf-8"));
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url).putExtra("keyword", "keyword"));
        page.setUrl(new Json(url));
        JDSearchPage searchPage = new JDSearchPage();
        searchPage.parse(page);
    }
}
