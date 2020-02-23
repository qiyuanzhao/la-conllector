package com.lavector.collector.crawler.project.tmall.page;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

/**
 * Created on 2018/11/23.
 *
 * @author zeng.zhao
 */
public interface TmallPageParse {

    void parse(Page page);

    boolean handlerRequest(Request request);
}
