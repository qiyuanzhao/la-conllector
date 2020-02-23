package com.lavector.collector.crawler.nonce.weibo;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created on 11/05/2018.
 *
 * @author zeng.zhao
 */
public class FansListPage implements PageParse {

    private JsonMapper jsonMapper = JsonMapper.buildNonDefaultBinder();

    @Override
    public boolean handleUrl(String url) {
        return url.contains("weibo.com/2709931352/fans");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        List<String> scripts = html.xpath("//script/html()").all();
        Optional<String> scriptOptional = scripts.stream()
                .filter(s -> s.contains("pl.relation.fans.index"))
                .findFirst();
        scriptOptional.ifPresent(script -> {
            String json = new Json(script).regex("FM.view\\((.*)\\)").get();
            String htmlStr = JsonPath.read(json, "$.html");
            Html fansHtml = new Html(htmlStr);
            List<Selectable> nodes = fansHtml.xpath("//li[@class='follow_item S_line2']").nodes();
            List<Map<String, String>> lists= new ArrayList<>();
            nodes.forEach(node -> {
                Map<String, String> map = new HashMap<>();
                String fansName = node.xpath("//a[@class='S_txt1']/text()").get();
                String userId = node.xpath("//a[@class='S_txt1']/@usercard").regex("id=(\\d+)").get();
                String desc = node.xpath("//div[@class='info_intro']/span/text()").get();
                String follows = node.xpath("//div[@class='info_connect']/span[1]/em/a/text()").get();
                String fans = node.xpath("//div[@class='info_connect']/span[2]/em/a/text()").get();
                String posts = node.xpath("//div[@class='info_connect']/span[3]/em/a/text()").get();
                map.put("name", fansName);
                map.put("desc", desc);
                map.put("follows", follows);
                map.put("fans", fans);
                map.put("posts", posts);
                map.put("userId", userId);
                lists.add(map);
            });
            String s = jsonMapper.toJson(lists);
            try {
                Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/fans.json").toFile(),
                        StandardCharsets.UTF_8, FileWriteMode.APPEND)
                        .write(s + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
//            if (lists.size() > 0) {
//                String currentPageNo = page.getUrl().regex("&Pl_Official_RelationFans__67_page=(\\d+)").get();
//                Integer nextPageNo = Integer.parseInt(currentPageNo) + 1;
//                String nextPage = page.getUrl()
//                        .get()
//                        .replace("&Pl_Official_RelationFans__67_page=" + currentPageNo, "&Pl_Official_RelationFans__67_page=" + nextPageNo);
//                result.addRequest(new us.codecraft.webmagic.Request(nextPage));
//            }
        });
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://weibo.com/2709931352/fans?cfs=600&relate=fans&t=1&f=1&type=&Pl_Official_RelationFans__67_page=37";
        String content = Request.Get(url)
                .addHeader("cookie", "SINAGLOBAL=9906983875306.838.1525775389863; UOR=,,login.sina.com.cn; YF-Ugrow-G0=56862bac2f6bf97368b95873bc687eef; YF-V5-G0=3d0866500b190395de868745b0875841; _s_tentry=login.sina.com.cn; Apache=5336305725481.104.1526024747750; ULV=1526024747760:4:4:4:5336305725481.104.1526024747750:1525948429976; YF-Page-G0=046bedba5b296357210631460a5bf1d2; login_sid_t=79fe52381a13808adba77384de959628; cross_origin_proto=SSL; WBStorage=5548c0baa42e6f3d|undefined; wb_view_log=1440*9002; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWTiZl3e6TbEwdXZq_5Qha35JpX5K2hUgL.FozNeh.4e020SKz2dJLoIpfhqgSQ9CfhqgSQ9CH8SFHF1FHWxFH8SC-4BbHFx5tt; SUHB=04W_dZZNg6hJXz; ALF=1557561867; SSOLoginState=1526025868; SCF=AjSsGNZHel-pUVITqhsDkD-KcZohKM--251X_qbCliPl1Lu3FDgqbA-Pkk4zjxMtH494gK4QXjw4Zq_WJoclkjI.; SUB=_2A2538T7cDeRhGeRJ61sY8y_Pzj6IHXVUhxcUrDV8PUNbmtBeLRiskW9NUtugU0tHPZCkOpxWu394aRnbEkucNviY; un=marketingwb@cache-cache.com.cn")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        FansListPage fansListPage = new FansListPage();
        fansListPage.parse(page);
    }
}
