package com.lavector.collector.crawler.project.music.neteasy;

import com.lavector.collector.crawler.project.music.MusicCommand;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

/**
 * Created on 02/05/2018.
 *
 * @author zeng.zhao
 */
public class NetEasyMusicPage implements MusicCommand {

    @Override
    public void process(Page page) {
        page.getHtml().links().all().forEach(url -> {
            if (!url.contains("music.163.com")) {
                url = "http://music.163.com" + url;
            }
            page.addTargetRequest(url);
        });
        System.out.println(page.getUrl().get());
    }

    @Override
    public boolean handler(Request request) {
        return true;
    }
}
