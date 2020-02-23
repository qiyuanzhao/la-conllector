package com.lavector.collector.crawler.project.music;

import com.lavector.collector.crawler.project.music.MusicCommand;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created on 02/05/2018.
 *
 * @author zeng.zhao
 */
public class MusicProcessor implements PageProcessor {

    private Site site = Site.me().setSleepTime(2000);

    private MusicCommand[] musicCommands;

    public MusicProcessor(MusicCommand... musicCommands) {
        this.musicCommands = musicCommands;
    }

    @Override
    public void process(Page page) {
        for (MusicCommand musicCommand : musicCommands) {
            if (musicCommand.handler(page.getRequest())) {
                musicCommand.process(page);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

}
