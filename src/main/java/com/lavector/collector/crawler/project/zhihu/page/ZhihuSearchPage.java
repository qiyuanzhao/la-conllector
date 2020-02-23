package com.lavector.collector.crawler.project.zhihu.page;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.zhihu.entity.Paging;
import com.lavector.collector.crawler.project.zhihu.entity.Question;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import javax.swing.text.DateFormatter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZhihuSearchPage implements PageParse {


    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.zhihu.com/api/v4/search_v3.*", url) || RegexUtil.isMatch("https://api.zhihu.com/search_v3.*", url);
    }

    @Override
    public Result parse(Page page) {
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        String rawText = page.getRawText();
        if (StringUtils.isEmpty(rawText)) {
            throw new RuntimeException("《《《页面下载错误》》》");
        }

        List<String> list = page.getJson().jsonPath("$.data").all();
        String pagingStr = page.getJson().jsonPath("$.paging").get();
        Paging paging = JSON.parseObject(pagingStr, Paging.class);
        List<Question> questionList = new ArrayList<>();
        List<Question> articleList = new ArrayList<>();
        for (String selectable : list) {
            JSONObject jsonObject = JSON.parseObject(selectable);
            JSONObject object = jsonObject.getJSONObject("object");
            if (object != null) {
                JSONObject question1 = object.getJSONObject("question");
                if (question1 != null) {
                    String string = question1.toJSONString();
                    Question question = JSON.parseObject(string, Question.class);
                    String name = question.getName();
                    if (StringUtils.isEmpty(name)) {
                        String s = new Html(name).xpath("//body/allText()").get();
                        question.setName(s);
                    }
                    questionList.add(question);
                }else {
                    String jsonString = object.toJSONString();
                    Question question = JSON.parseObject(jsonString, Question.class);
                    articleList.add(question);
                }
            }
        }
        if (!paging.is_end) {
            page.addTargetRequest(new Request(paging.next).putExtra("skuData", skuData));
        }
        page.getRequest().putExtra("questionList", questionList);
        page.getRequest().putExtra("articleList", articleList);
        return null;
    }



    @Override
    public String pageName() {
        return null;
    }
}
