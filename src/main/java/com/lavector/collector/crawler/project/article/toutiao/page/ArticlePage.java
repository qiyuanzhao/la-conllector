package com.lavector.collector.crawler.project.article.toutiao.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.article.Brand;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.wechatSmall.article.Article;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created on 2017/12/22.
 *
 * @author zeng.zhao
 */
public class ArticlePage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(ArticlePage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch(".*www.toutiao.com/a\\d+.*", url) || RegexUtil.isMatch(".*toutiao.com/group/\\d+/.*", url);
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        try {
            if (page.getRawText().contains("chineseTag: '图片'") || page.getRawText().contains("chineseTag: '问答'") || page.getRawText().contains("chineseTag: '视频'")) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Html html = page.getHtml();
        List<String> scripts = html.xpath("script").all();
        String articleData = null;
        for (String script : scripts) {
            if (script.contains("var BASE_DATA")) {
//                String var = script.substring(script.indexOf("BASE_DATA") + 12, script.length() - 10);
                String var = script.substring(script.indexOf("var BASE"), script.length() - 10);
                String var1 = var.replaceAll("\\n      ", "");
                articleData = var1.replaceAll("\\n    ", "");
                break;
            }
        }
        if (StringUtils.isBlank(articleData)) {
            if (html.xpath("//title/text()").get().equals("阳光宽频网")) {
                logger.info("视频链接：{}", page.getUrl());
                return result;
            }
            logger.info("该文章没有内容？{}", page.getUrl());
            return result;
        }

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByName("nashorn");
        try {
            engine.eval(articleData);
        } catch (ScriptException e) {
            logger.error("该页面结构不一样？{}", page.getUrl().get());
            return result;
        }

        String brand = page.getRequest().getExtra("brand").toString();
        String title = null;
        String content = null;
        String publishTime = null;
        String author = page.getRequest().getExtra("author").toString();
        String comment = page.getRequest().getExtra("comment").toString();
        ScriptObjectMirror base_datas = (ScriptObjectMirror) engine.get("BASE_DATA");
        Set<Map.Entry<String, Object>> entries = base_datas.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if (entry.getKey().equals("articleInfo")) {
                ScriptObjectMirror value = (ScriptObjectMirror) entry.getValue();
                title = value.get("title").toString();
                content = value.get("content").toString();
                publishTime = ((ScriptObjectMirror) value.get("subInfo")).get("time").toString();
                break;
            }
        }

        Article article = new Article();
        article.setAuthor(author);
        article.setContent(transferCode(content));
        article.setDate(publishTime);
        article.setTitle(title);
        article.setCommentNum(Integer.parseInt(comment));
        article.setBrandId(Brand.brands.get(brand).longValue());
        article.setUrl(page.getUrl().get());
        result.addData(article);
        return result;
    }


    //替换特殊字符
    private String transferCode(String content) {
        String var1 = StringUtils.replace(content, "&lt;", "<");
        String var2 = StringUtils.replace(var1, "&gt;", ">");
        String var3 = StringUtils.replace(var2, "&quot;", "\"");
        return StringUtils.replace(var3, "&#x3D;", "=");
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.dianping.com/shop/4548709/review_more_latest?pageno=1";
        String content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setUrl(new Json(url));
        ArticlePage articlePage = new ArticlePage();
        articlePage.parse(page);
    }
}
