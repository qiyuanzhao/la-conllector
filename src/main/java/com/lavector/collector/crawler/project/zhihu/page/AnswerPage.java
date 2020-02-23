package com.lavector.collector.crawler.project.zhihu.page;


import com.alibaba.fastjson.JSON;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.zhihu.entity.Answer;
import com.lavector.collector.crawler.project.zhihu.entity.Paging;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

public class AnswerPage implements PageParse {


    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.zhihu.com/api/v4/questions/[0-9]+/answers.*", url);
    }

    @Override
    public Result parse(Page page) {
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        String rawText = page.getRawText();
        if (StringUtils.isEmpty(rawText)) {
            throw new RuntimeException("《《《页面下载错误》》》");
        }

        String pagingStr = page.getJson().jsonPath("$.paging").get();
        Paging paging = JSON.parseObject(pagingStr, Paging.class);

        List<String> list = page.getJson().jsonPath("$.data").all();

        List<Answer> answerList = new ArrayList<>();
        for (String detile : list){
            Answer answer = JSON.parseObject(detile, Answer.class);
            String content = answer.getContent();
            if (!StringUtils.isEmpty(content)){
                String s = new Html(content).xpath("//body/allText()").get();
                answer.setContent(s);
            }
            answerList.add(answer);
        }
        if (!paging.is_end) {
            page.addTargetRequest(new Request(paging.next).putExtra("skuData", skuData));
        }
        page.getRequest().putExtra("answerList",answerList);
        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
