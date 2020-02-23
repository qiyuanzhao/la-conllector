package com.lavector.collector.crawler.project.music;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

/**
 * Created on 02/05/2018.
 *
 * @author zeng.zhao
 */
public interface MusicCommand {

    void process(Page page);

    boolean handler(Request request);
}
