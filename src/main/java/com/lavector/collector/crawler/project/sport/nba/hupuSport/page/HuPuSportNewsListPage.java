package com.lavector.collector.crawler.project.sport.nba.hupuSport.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.base.RequestExtraKey;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 2017/10/16.
 *
 * @author zeng.zhao
 */
public class HuPuSportNewsListPage implements PageParse {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public boolean handleUrl(String url) {
        boolean isMatch = RegexUtil.isMatch("https://voice.hupu.com/nba$", url);
        if (!isMatch) {
            isMatch = RegexUtil.isMatch("https://voice.hupu.com/nba/$", url);
        }
        if (!isMatch) {
            isMatch = RegexUtil.isMatch("https://voice.hupu.com/nba/\\d+", url);
        }
        return isMatch;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addRequests(parseListPage(page));
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private List<Request> parseListPage (Page page) {
        Date startTime = (Date) page.getRequest().getExtra(RequestExtraKey.KEY_BEGIN_DATE);

        List<Request> requests = new ArrayList<>();
        Html html = page.getHtml();
        List<String> urls = html.xpath("//div[@class='list-hd']/h4/a/@href").all();
        List<String> createdTimes = html.xpath("//div[@class='otherInfo']/span[@class='other-left']/a[@class='time']/@title").all();
        for (int i = 0; i < urls.size(); i++) {
            if (this.dateFormat(createdTimes.get(i)).after(startTime)) {
                requests.add(new Request(urls.get(i)));
            }
        }

        Date lastDate = this.dateFormat(createdTimes.get(createdTimes.size() - 1));
        if (lastDate.after(startTime)) {
            String nextPageUrl = html.xpath("//div[@class='voice-paging']/a[@class='page-btn-prev']/@href").get();
            if (!StringUtils.contains(nextPageUrl, "voice.hupu.com")) {
                nextPageUrl = "https://voice.hupu.com" + nextPageUrl;
            }
            requests.add(new Request(nextPageUrl).putExtra(RequestExtraKey.KEY_BEGIN_DATE, startTime));
        }
        return requests;
    }

    private Date dateFormat (String source) {
        try {
            return simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("date format error!" + source);
    }

    public static void main (String[] args) throws Exception {
        String url = "https://voice.hupu.com/nba";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setDownloadSuccess(true);
        page.setRequest(new Request(url).putExtra(RequestExtraKey.KEY_BEGIN_DATE, DateUtils.addDays(new Date(), -1)));
        page.setUrl(new Json(url));
        page.setRawText(content);
        HuPuSportNewsListPage listPage = new HuPuSportNewsListPage();
        listPage.parse(page);
    }
}
