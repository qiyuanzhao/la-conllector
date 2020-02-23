package com.lavector.collector.crawler.project.game.sinaEgame.page;

import com.lavector.collector.crawler.base.PageParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
public class EgameNewsTop implements PageParse {
    private static Logger logger = LoggerFactory.getLogger(EgameNewsTop.class);

    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://dj.sina.com.cn/information") || url.equals("http://dj.sina.com.cn/information/");
    }

    @Override
    public Result parse(Page page) {
        List<String> links = page.getHtml().xpath("//div[@class='E_sports_news_list']//h3//a/@href").all();
        return Result.get().addRequests(links.stream().map(Request::new).collect(Collectors.toList()));
    }

    @Override
    public String pageName() {
        return null;
    }


    public static void main(String[] args) throws Exception {
        String url = "http://dj.sina.com.cn/information";
        String content = org.apache.http.client.fluent.Request.Get(url)
//                .addHeader("referer", referer)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
//                .addHeader("cookie", "UM_distinctid=15b8efcb50f1-0453e4c1a78ff6-f333168-1fa400-15b8efcb510143; uuid=\"w:7bc393ed442e4c0c91e1b04137afe76d\"; sso_login_status=0; _ba=BA0.2-20170421-51d9e-lUjPTRpy6vyjzeSe2aDJ; tt_webid=57974723281; utm_source=toutiao; __utma=24953151.454786688.1492752578.1497855275.1498203009.4; __utmb=24953151.5.10.1498203009; __utmc=24953151; __utmz=24953151.1493801814.2.2.utmcsr=toutiao.com|utmccn=(referral)|utmcmd=referral|utmcct=/search_content/; csrftoken=8f3d44c18a9ceb56d75800e3362477e7; WEATHER_CITY=%E5%8C%97%E4%BA%AC; _ga=GA1.2.454786688.1492752578; _gid=GA1.2.184813642.1498014394; CNZZDATA1259612802=885820200-1492750758-null%7C1498202969; __tasessionId=l8hjwvols1498202251217")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRequest(new Request(url));
        page.setRawText(content);
        EgameNewsTop sportTop = new EgameNewsTop();

        sportTop.parse(page);
    }
}
