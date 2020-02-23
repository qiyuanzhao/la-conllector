package com.lavector.collector.crawler.project.meituan.com.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.utils.HttpConstant;

/**
 * Created by qyz on 2019/9/4.
 */
public class ReportPage implements PageParse {


    @Override
    public boolean handleUrl(String url) {

        return RegexUtil.isMatch("https://i.waimai.meituan.com/openh5/order/myuncompleteorder.*", url);
    }

    @Override
    public Result parse(Page page) {

        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");

        if (page.getStatusCode() == 200) {
            page.addTargetRequest(new Request(skuData.getUrl()).putExtra("skuData", skuData).setMethod(HttpConstant.Method.POST));
        }

        page.setSkip(true);
        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
