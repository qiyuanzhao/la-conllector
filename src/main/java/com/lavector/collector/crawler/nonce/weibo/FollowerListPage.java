package com.lavector.collector.crawler.nonce.weibo;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.project.edu.WriteFile;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/10/18.
 *
 * @author zeng.zhao
 */
public class FollowerListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("follow?page=");
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        List<String> scripts = html.xpath("//script").all();
        List<String> contents = new ArrayList<>();
        String path = page.getRequest().getExtra("path").toString();
        String ownerId = page.getRequest().getExtra("ownerId").toString();
        String city = page.getRequest().getExtra("city").toString();
        String province = page.getRequest().getExtra("province").toString();
        scripts.forEach(script -> {
            if (script.contains("pl.content.followTab.index")) {
                String followJson = new Json(script).regex("FM.view\\((.*)\\)").get();
                String followHtmlStr = JsonPath.read(followJson, "$.html");
                Html followHtml = new Html(followHtmlStr);
                List<Selectable> nodes = followHtml.xpath("//li[@class='follow_item S_line2']").nodes();
                boolean[] isNext = new boolean[1];
                isNext[0] = true;
                nodes.stream().findFirst().ifPresent(node -> {
                    String follow = node.xpath("//div[@class='info_connect']/span[1]/em/a/text()").get();
                    if (follow == null) {
                        isNext[0] = false;
                    }
                });
                if (isNext[0]) {
                    nodes.forEach(node -> {
                        String follow = node.xpath("//div[@class='info_connect']/span[1]/em/a/text()").get();
                        String fans = node.xpath("//div[@class='info_connect']/span[2]/em/a/text()").get();
                        String post = node.xpath("//div[@class='info_connect']/span[3]/em/a/text()").get();
                        String userName = node.xpath("//a[@class='S_txt1']/text()").get();
                        String url = "https://weibo.com" + node.xpath("//a[@class='S_txt1']/@href").get();
                        String content = follow + "," + fans + ","
                                + post + "," + userName.replace(",", "，")
                                + "," + url + "," + ownerId + "," + city + "," + province;
                        contents.add(content);
                    });
                }
            }
        });

        // path 以斜杠结尾
        if (contents.size() > 0) {
            if (!new File(path + city + ".csv").exists()) {
                try {
                    boolean newFile = new File(path + city + ".csv").createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String head = "follow,fans,post,username,url,ownerId,city,province";
                FileUtils.write(head, path + city + ".csv");
            }
            contents.forEach(content -> {
                FileUtils.write(content, path + city + ".csv");
            });

            // 分页
            String currentPage = page.getUrl().regex("page=(\\d+)").get();
            Integer nextPage = Integer.parseInt(currentPage) + 1;
            String nextUrl = page.getUrl().get().replace("page=" + currentPage, "page=" + nextPage);
            us.codecraft.webmagic.Request request = new us.codecraft.webmagic.Request(nextUrl);
            request.putExtra("path", path);
            request.putExtra("ownerId", ownerId);
            request.putExtra("city", city);
            request.putExtra("province", province);
            result.addRequest(request);
        }

        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://weibo.com/p/1005056365009152/follow?page=1";
        String content = Request.Get(url)
                .addHeader("cookie", "SINAGLOBAL=638387246136.2206.1527731616813; un=13241300169; UOR=,,login.sina.com.cn; YF-Ugrow-G0=5b31332af1361e117ff29bb32e4d8439; YF-V5-G0=a53c7b4a43414d07adb73f0238a7972e; _s_tentry=login.sina.com.cn; Apache=2381017761437.592.1539914700415; YF-Page-G0=8fee13afa53da91ff99fc89cc7829b07; ULV=1539914700449:63:10:5:2381017761437.592.1539914700415:1539850993698; wb_view_log_5842303108=1920*10801; wb_view_log_5861853861=1920*10801; login_sid_t=2d9bd3bdb828514038942714fcf05843; cross_origin_proto=SSL; WBStorage=e8781eb7dee3fd7f|undefined; wb_view_log=1920*10801; WBtopGlobal_register_version=030d061db77a53e5; SCF=AvXliNK2E1oyuXpLHOqFJwmipzSDe8bW4H38H0zcNHJ4ixeIiVuddJT3kv02XBZDmCu8Vcqx0hu7fVErPmDLp7o.; SUB=_2A252zdgUDeRhGeNG71AS8C3NyzSIHXVVu07crDV8PUNbmtAKLUX3kW9NS0I5FFbfCZiLyYiw6MGRIDy1Qu_7zUU9; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFnzPQIAGZpce2AE4Ms3o7Q5JpX5K2hUgL.Fo-RShz0ehepehn2dJLoIEXLxK-LBKBLB-2LxKMLBK.LB.2LxKML1-2L1hBLxKqLBo5LBKMLxKqLBo5L1KBt; SUHB=0sqBRoEiOM3ayP; ALF=1540547268; SSOLoginState=1539942468")
                .execute()
                .returnContent()
                .asString();
        us.codecraft.webmagic.Request request = new us.codecraft.webmagic.Request(url);
        request.putExtra("path", "");
        request.putExtra("ownerId", "");
        request.putExtra("city", "");
        request.putExtra("province", "");
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(url));
        page.setRequest(request);
        new FollowerListPage().parse(page);
    }
}
