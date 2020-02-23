package com.lavector.collector.crawler.project.tieba.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class TiebaListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://tieba.baidu.com/f/search/res.*", url);
    }

    @Override
    public Result parse(Page page) {

        List<Selectable> selectables = page.getHtml().xpath("//div[@class='wrap1']//div[@class='s_post_list']/div[@class='s_post']").nodes();

        if (!CollectionUtils.isEmpty(selectables)) {

        }

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
