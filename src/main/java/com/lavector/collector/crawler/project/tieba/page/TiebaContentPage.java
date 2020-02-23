package com.lavector.collector.crawler.project.tieba.page;


import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;

public class TiebaContentPage implements PageParse {



    @Override
    public boolean handleUrl(String url) {
        return false;
    }

    @Override
    public Result parse(Page page) {
        return null;
    }

    @Override
    public String pageName() {
        return null;
    }
}
