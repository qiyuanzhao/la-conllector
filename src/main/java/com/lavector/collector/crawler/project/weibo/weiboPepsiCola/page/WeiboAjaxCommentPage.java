package com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page;

import com.lavector.collector.crawler.base.PageParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiboAjaxCommentPage implements PageParse {

    private static Logger logger = LoggerFactory.getLogger(WeiboAjaxCommentPage.class);

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
//        Project project = (Project) page.getRequest().getExtra("project");

        String url = page.getUrl().get();
        String pageNumber = url.substring(url.lastIndexOf("=") + 1);
        String selectable = null;
        try {
            selectable = page.getJson().jsonPath("$.data").get();
        } catch (Exception e) {
//            page.putField("project", project);
            logger.info("解析失败");
            return result;
        }
        String all = selectable.replace("\\", "");
        Html html = new Html(all);
        List<Selectable> nodes = html.xpath("//div[@class='list_li S_line1 clearfix']").nodes();

        List<Comment> comments = new ArrayList<>();
        if (!CollectionUtils.isEmpty(nodes)) {
            nodes.forEach(node -> {
                Comment comment = new Comment();
                String commentStr = node.xpath("//div[@class='list_con']/div[@class='WB_text']/allText()").get();
                String userName = node.xpath("//div[@class='list_con']/div[@class='WB_text']/a/text()").get();
                String userUrl = "https:" + node.xpath("//div[@class='list_con']/div[@class='WB_text']/a/@href").get();
                String time = node.xpath("//div[@class='WB_func clearfix']/div[@class='WB_from S_txt2']/text()").get();
                comment.setComment(commentStr);
                comment.setTime(time);
                comment.setUserName(userName);
                comment.setUserUrl(userUrl);
                comments.add(comment);
            });


//            String nexUrl = "https://weibo.com/aj/v6/comment/big?ajwvr=6&" + html.xpath("//div[@class='list_box']/div[@node-type='comment_list']//a[@action-type='click_more_comment']/@action-data").get();
            String nexUrl = "https://weibo.com/aj/v6/comment/big?ajwvr=6&id=4393283046514699&root_comment_max_id=168935869305550&root_comment_max_id_type=0&root_comment_ext_param=&filter=all&sum_comment_number=1435&filter_tips_before=0&from=singleWeiBo&__rnd=1562929075203=&page=" + Integer.parseInt(pageNumber) + 1;

            Request request = new Request();
            request.setUrl(nexUrl);
            page.addTargetRequest(request);
        }

        page.getRequest().putExtra("comments", comments);
        page.putField("comments", comments);
        return result;
    }


    @Override
    public String pageName() {
        return null;
    }

    @Override
    public boolean handleUrl(String url) {
        return false;
    }

}
