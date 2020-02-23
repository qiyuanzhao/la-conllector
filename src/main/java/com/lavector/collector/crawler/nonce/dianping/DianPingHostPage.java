package com.lavector.collector.crawler.nonce.dianping;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import net.minidev.json.JSONArray;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 18/05/2018.
 *
 * @author zeng.zhao
 */
public class DianPingHostPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://www.dianping.com");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addRequest(new Request("http://www.dianping.com/shop/507576").putExtra("referer", page.getUrl().get()));
        return result;
    }
}
