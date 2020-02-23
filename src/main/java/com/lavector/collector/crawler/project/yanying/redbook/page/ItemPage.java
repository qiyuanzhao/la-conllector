package com.lavector.collector.crawler.project.yanying.redbook.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.project.yanying.ContentType;
import com.lavector.collector.crawler.project.yanying.Message;
import com.lavector.collector.crawler.project.yanying.redbook.RedBookCrawler;
import com.lavector.collector.crawler.project.yanying.redbook.RedBookPageProcessor;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.StringToDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created on 2018/2/7.
 *
 * @author zeng.zhao
 */
public class ItemPage implements PageParse {

    private JsonMapper mapper = JsonMapper.buildNonNullBinder();

    private Logger logger = LoggerFactory.getLogger(ItemPage.class);

    private StringToDateConverter converter = new StringToDateConverter();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("http://www.xiaohongshu.com/discovery/item");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
//        List<String> contents = html.xpath("//div[@class='content']/p/text()").all();
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String con : contents) {
//            if (con.contains("class=\"cube-icon\"")) {
//                break;
//            }
//            String text = new Json(con).regex("<p.*?>(.*?)</p>").get();
//            if (text != null) {
//                stringBuilder.append(text);
//            }
//        }
        String keyword = page.getRequest().getExtra("keyword").toString();
        String content = html.xpath("//div[@class='content']/allText()").get();
        if (!content.contains(keyword)) {
            return result;
        }
        List<String> scripts = html.xpath("//script").all();
        String contentInfo = null;
        for (String script : scripts) {
            if (script.contains("window.__INITIAL_SSR_STATE__=")) {
                String scriptObj = new Json(script).regex("<script.*?>(.*?)</script>").get()
                        .replaceAll("\r\n", "");
                contentInfo = scriptObj.substring(scriptObj.indexOf("{"), scriptObj.lastIndexOf("}") + 1);
                break;
            }
        }

        Integer collects = null;
        Integer comments = null;
        Integer likes = null;
        String time = null;
        String userId = null;
        if (contentInfo != null) {
            collects = JsonPath.read(contentInfo, "$.NoteView.noteInfo.collects");
            comments = JsonPath.read(contentInfo, "$.NoteView.noteInfo.comments");
            likes = JsonPath.read(contentInfo, "$.NoteView.noteInfo.likes");
            time = JsonPath.read(contentInfo, "$.NoteView.noteInfo.time");
            userId = JsonPath.read(contentInfo, "$.NoteView.noteInfo.user.id");
        }
        String type = ContentType.POST.get();
        String mid = page.getUrl().regex("item/(.*)?").get();

        NonceMessage message = new NonceMessage();
        message.setMid(mid);
        message.setContent(content);
        message.setType(type);
        message.setSite("redBook");
        message.setUserId(userId);
        message.setTime(format.format(converter.convert(time)));
        NonceMessage.SocialCount socialCount = new NonceMessage.SocialCount();
        socialCount.setLikes(likes == null ? 0 : likes);
        socialCount.setComments(comments == null ? 0 : comments);
        socialCount.setCollects(collects == null ? 0 : collects);
        message.setSocialCount(socialCount);

        if (converter.convert(time).after(RedBookPageProcessor.startTime)) {
            String commentUrl = "http://www.xiaohongshu.com/web_api/sns/v1/note/" + mid + "/comment";
            result.addRequest(new Request(commentUrl)
                    .putExtra("mid", mid));
            String messageJson = mapper.toJson(message);
            WriteFile.write(messageJson, RedBookCrawler.OUT_FILE);
            logger.info("原贴写入成功！");
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String url = "http://www.xiaohongshu.com/discovery/item/5a54b8e4aac7cb64b6823bfe";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url));
        page.setUrl(new Json(url));
        ItemPage itemPage = new ItemPage();
        itemPage.parse(page);
    }
}
