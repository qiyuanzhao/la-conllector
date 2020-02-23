package com.lavector.collector.crawler.project.yanyuewang.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import us.codecraft.webmagic.Page;

/**
 * Created by qyz on 2019/8/26.
 */
public class YanyuewangSearchPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://bbs.yanyue.cn/search.php?mod=forum&searchid=398&orderby=lastpost&ascdesc=desc&searchsubmit=yes&kw=", url);
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
