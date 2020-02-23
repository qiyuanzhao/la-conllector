package com.lavector.collector.crawler.nonce.douban;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created on 05/06/2018.
 *
 * @author zeng.zhao
 */
public class DouBanMessageListPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(DouBanMessageListPage.class);

    private String basePath = "/Users/zeng.zhao/Desktop/douban/";

    @Override
    public boolean handleUrl(String url) {
        return url.contains("https://www.douban.com/group/");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String groupName = page.getRequest().getExtra("groupName").toString();
        List<String> titles = html.xpath("//table[@class='olt']//td[@class='title']/a/text()").all();
        if (CollectionUtils.isNotEmpty(titles)) {
            titles.forEach(title -> write(title.replace(",", "，"), groupName));
        }
        String next = html.xpath("//span[@class='next']/a/@href").get();
        if (StringUtils.isNotBlank(next)) {
            result.addRequest(new Request(next).putExtra("groupName", groupName));
        } else {
            logger.error("finished. url : {}" + page.getUrl().get());
        }
        return result;
    }

    private void write (String content, String path) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            Files.asCharSink(Paths.get(basePath + path + ".csv").toFile(), StandardCharsets.UTF_8,
                    FileWriteMode.APPEND)
                    .write(content + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }
}
