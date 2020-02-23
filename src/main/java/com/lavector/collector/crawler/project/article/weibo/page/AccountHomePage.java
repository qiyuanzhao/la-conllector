package com.lavector.collector.crawler.project.article.weibo.page;

import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created on 2017/12/22.
 *
 * @author zeng.zhao
 */
public class AccountHomePage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(AccountHomePage.class);

    @Override
    public boolean handleUrl(String url) {
        return false;
    }

    @Override
    public Result parse(Page page) {
        return null;
    }

    @Override
    public String pageName() {
        return null;
    }

    private Result parseHomePage(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        List<String> scripts = html.xpath("script").all();
        String messageDiv = null;
        for (String script :scripts) {
            if (script.contains("class=\\\"WB_detail\\\"")) {
                messageDiv = script;
                break;
            }
        }
        if (StringUtils.isBlank(messageDiv)) {
            logger.info("该用户没有帖子？{}", page.getUrl().toString());
            return result;
        }
        Html messageHtml = new Html(StringEscapeUtils.unescapeEcmaScript(messageDiv));
        List<Selectable> selectables = messageHtml.xpath("//div[@class='WB_detail']").nodes();
        System.out.println(messageHtml);
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://weibo.com/gucci?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1#feedtop";
        String content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36")
                .addHeader("cookie", "SUB=HELLO")
                .execute()
                .returnContent()
                .asString(Charset.forName("utf-8"));
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        AccountHomePage homePage = new AccountHomePage();
        homePage.parseHomePage(page);
    }
}
