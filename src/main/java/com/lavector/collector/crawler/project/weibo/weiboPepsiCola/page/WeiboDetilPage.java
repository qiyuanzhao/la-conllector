package com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page;

import com.lavector.collector.crawler.base.PageParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WeiboDetilPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(WeiboDetilPage.class);


    @Override
    public boolean handleUrl(String url) {
        return false;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Request request = page.getRequest();
        Project project = (Project) request.getExtra("project");
        String selectable = null;
        try{
            selectable = page.getJson().jsonPath("$.data").get();
        }catch (Exception e){
            page.putField("project", project);
            return result;
        }
        String all = selectable.replace("\\", "");
        Html html = new Html(all);
        List<Selectable> nodes = html.xpath("//div[@node-type='root_comment']").nodes();
        nodes.forEach(node -> {

            String comment = node.xpath("//div[@class='list_con']/div[@class='WB_text']/text()").get();
            String userName = node.xpath("//div[@class='list_con']/div[@class='WB_text']/a/text()").get();
            String time = node.xpath("//div[@class='WB_func clearfix']/div[@class='WB_from S_txt2']/text()").get();
            Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5 ]+");
            Matcher matcher = pattern.matcher(comment);
            String sum = "";
            if (comment != null) {
                while (matcher.find()) {
                    String group = matcher.group();
                    sum += group;
                }
            }
            project.getComment().add("用户名称:"+userName+"时间："+time + sum.trim().replace(",","，"));
        });

        String text = html.xpath("//div[@node-type='comment_loading']/div/div/div/p/text()").get();
        if (text != null && text.contains("正在加载")) {
            Request newRequest = new Request();
            newRequest.setUrl("https://weibo.com/aj/v6/comment/big?id=" + project.getId() + "&page=1");
            newRequest.putExtra("project", project);

            result.addRequest(newRequest);
            page.setSkip(true);
        } else {
            page.setSkip(false);
            page.putField("project", project);
        }
        return result;
    }


    @Override
    public String pageName() {
        return null;
    }


}
