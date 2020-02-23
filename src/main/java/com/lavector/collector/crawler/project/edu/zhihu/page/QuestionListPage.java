package com.lavector.collector.crawler.project.edu.zhihu.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.util.List;

/**
 * Created on 2017/11/20.
 *
 * @author zeng.zhao
 */
public class QuestionListPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(QuestionListPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.zhihu.com/r/search\\?q=.*&correction=1&type=content&offset=\\d+", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        List<String> htmls = JsonPath.read(page.getJson().toString(), "$.htmls");
        if (CollectionUtils.isEmpty(htmls)) {
            return result;
        }
        String site = page.getRequest().getExtra("site").toString();
        String keyword = page.getRequest().getExtra("keyword").toString();
        for (String str : htmls) {
            Html html = new Html(str);
            String dataType = html.xpath("//li/@data-type").get();
            if (!dataType.equals("Answer")) {
                continue;
            }
            String questionId = html.xpath("//div[@class='title']/a/@href").regex("/question/(\\d+)").get();
            String answerUrl = "https://www.zhihu.com/api/v4/questions/" + questionId + "/answers?include=data%5B*%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cupvoted_followees%3Bdata%5B*%5D.mark_infos%5B*%5D.url%3Bdata%5B*%5D.author.follower_count%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics&offset=0&limit=20&sort_by=created";
            us.codecraft.webmagic.Request request = new us.codecraft.webmagic.Request(answerUrl);
            request.putExtra("site", site).putExtra("keyword", keyword);
            result.addRequest(request);
        }

        String nextUrl;
        try {
            nextUrl = JsonPath.read(page.getJson().toString(), "$.paging.next");
        } catch (Exception e) {
            logger.error("没有更多数据！{}", keyword);
            return result;
        }
        us.codecraft.webmagic.Request nextRequest = new us.codecraft.webmagic.Request("https://www.zhihu.com" + nextUrl);
        nextRequest.putExtra("site", site).putExtra("keyword", keyword);
        result.addRequest(nextRequest);
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://www.zhihu.com/api/v4/questions/67087392/answers?sort_by=created&include=data%5B%2A%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cupvoted_followees%3Bdata%5B%2A%5D.mark_infos%5B%2A%5D.url%3Bdata%5B%2A%5D.author.follower_count%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics&limit=20&offset=20";
        String content = Request.Get(url)
                .addHeader("authorization", "oauth c3cef7c66a1843f8b3a9e6a1e3160e20")
                .execute()
                .returnContent()
                .asString();
        System.out.println(content);
//        Page page = new Page();
//        page.setRawText(content);
//        page.setRequest(new us.codecraft.webmagic.Request(url));
//        page.setUrl(new Json(url));
//        QuestionListPage listPage = new QuestionListPage();
//        listPage.parse(page);
    }
}
