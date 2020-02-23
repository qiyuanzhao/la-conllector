package com.lavector.collector.crawler.project.edu.zhihu.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.Keywords;
import com.lavector.collector.crawler.project.edu.WriteFile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2017/11/20.
 *
 * @author zeng.zhao
 */
public class AnswerListPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(AnswerListPage.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.zhihu.com/api/v4/questions");
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String site = page.getRequest().getExtra("site").toString();
        String keyword = page.getRequest().getExtra("keyword").toString();
        List<?> datas = JsonPath.read(page.getJson().get(), "$.data");
        List<Integer> createdTimes = JsonPath.read(page.getJson().toString(), "$.data[*].updated_time");
        List<Integer> likes = JsonPath.read(page.getJson().toString(), "$.data[*].voteup_count");
        List<Integer> comments = JsonPath.read(page.getJson().toString(), "$.data[*].comment_count");
        List<String> htmls = JsonPath.read(page.getJson().toString(), "$.data[*].content");
        //http://www.zhihu.com/api/v4/answers/57697318
        List<String> urls = JsonPath.read(page.getJson().toString(), "$.data[*].url");
        List<Integer> questionIds = JsonPath.read(page.getJson().toString(), "$.data[*].question.id");
        for (int i = 0; i < htmls.size(); i++) {
            Html html = new Html(htmls.get(i));
            String answerId = new Json(urls.get(i)).regex("http://www.zhihu.com/api/v4/answers/(\\d+)").get();
            Integer questionId = questionIds.get(i);
            String content = html.xpath("//body/allText()").get();
            Date timePublish = new Date(createdTimes.get(i) * 1000L);
            Integer like = likes.get(i);
            Integer comment = comments.get(i);
            if (timePublish.after(Keywords.targetDate)) {
                String interaction = "likes:" + like + ";comments:" + comment;
                String answerUrl = "https://www.zhihu.com/question/" + questionId + "/answer/" + answerId;
                String timePublishStr = parseTime(timePublish);
                String contentRep = content.replaceAll(",", "，");
                String writeContent = contentRep + "," + timePublishStr + "," + interaction + "," + answerUrl;
                executorService.execute(() -> {
                    WriteFile.write(writeContent, "/Users/zeng.zhao/Desktop/edu/" + site + "/" + keyword + ".csv");
                    logger.info("写入成功！{}, keyword : {}", answerUrl, keyword);
                });
            }

        }
        String nextUrl = JsonPath.read(page.getJson().toString(), "$.paging.next");
        if (CollectionUtils.isNotEmpty(datas)) {
            Request request = new Request(nextUrl);
            request.putExtra("site", site).putExtra("keyword", keyword);
            result.addRequest(request);
        }
        return result;
    }

    private String parseTime(Date time) {
        return simpleDateFormat.format(time);
    }

    @Override
    public String pageName() {
        return null;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://www.zhihu.com/api/v4/questions/20399468/answers?include=data%5B*%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cupvoted_followees%3Bdata%5B*%5D.mark_infos%5B*%5D.url%3Bdata%5B*%5D.author.follower_count%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics&offset=0&limit=20&sort_by=created";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .addHeader("authorization", "oauth c3cef7c66a1843f8b3a9e6a1e3160e20")
                .execute()
                .returnContent()
                .asString(Charset.forName("utf-8"));
        Page page = new Page();
        page.setRequest(new Request(url).putExtra("site", "知乎").putExtra("keyword", "兴趣班"));
        page.setUrl(new Json(url));
        page.setRawText(content);
        AnswerListPage answerListPage = new AnswerListPage();
        answerListPage.parse(page);
    }
}
