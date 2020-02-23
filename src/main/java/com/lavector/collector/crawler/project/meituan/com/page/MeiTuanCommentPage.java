package com.lavector.collector.crawler.project.meituan.com.page;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.meituan.com.entity.MeituanComment;
import com.lavector.collector.crawler.project.meituan.com.entity.ShopComment;
import com.lavector.collector.crawler.project.meituan.com.entity.ShopEntity;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.List;

public class MeiTuanCommentPage implements PageParse {


    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://waimai.meituan.com/ajax/comment.*", url);
    }

    @Override
    public Result parse(Page page) {
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");

        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText);
        String commentListStr = jsonObject.getJSONObject("data").getJSONArray("wmCommentVo").toJSONString();

        List<MeituanComment> meituanComments = JSON.parseArray(commentListStr, MeituanComment.class);

        page.getRequest().putExtra("meituanComments", meituanComments);

        page.putField("meituanComments", meituanComments);

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
