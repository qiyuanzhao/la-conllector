package com.lavector.collector.crawler.project.zhihu.page;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.zhihu.entity.Question;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class QuestionPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(QuestionPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.zhihu.com/question/[0-9]+", url);
    }

    @Override
    public Result parse(Page page) {
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        String html = page.getHtml().xpath("//script[@id='js-initialData']/html()").get();
        JSONObject jsonObject = JSON.parseObject(html);
        JSONObject state = jsonObject.getJSONObject("initialState");
        if (state != null) {
            JSONObject entities = state.getJSONObject("entities");
            if (entities != null) {
                JSONObject questions = entities.getJSONObject("questions");
                if (questions != null) {
                    String brand = skuData.getUrl();
                    Object o = questions.get(brand);
                    if (o != null) {
                        String string = o.toString();
                        if (!StringUtils.isEmpty(string)) {
                            Question question = JSON.parseObject(string, Question.class);
                            Long created = question.getCreated();
                            boolean flag = checkTime(created);
                            if (flag) {
                                String name = question.getTitle();
                                if (!StringUtils.isEmpty(name)) {
                                    String s = new Html(name).xpath("//body/allText()").get();
                                    question.setName(s);
                                }
                                String detail = question.getDetail();
                                if (!StringUtils.isEmpty(detail)){
                                    String s = new Html(detail).xpath("//body/allText()").get();
                                    question.setDetail(s);
                                }
                                page.getRequest().putExtra("question", question);
                                page.getRequest().putExtra("falg", false);
                            } else {
                                page.setSkip(true);
                            }
                        }
                    } else {
                        page.getRequest().putExtra("falg", true);
                    }
                }
            }
        }
        return null;
    }

    private boolean checkTime(Long created) {
        Instant instant = Instant.ofEpochMilli(created * 1000);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDateTime.ofInstant(instant, zoneId).toLocalDate();
        LocalDate parse = LocalDate.parse("2018-05-01", DateTimeFormatter.ISO_LOCAL_DATE);
        if (localDate.isBefore(parse)) {
            return false;
        }
        return true;
    }

    @Override
    public String pageName() {
        return null;
    }
}
