package com.lavector.collector.crawler.project.music.neteasy;

import com.lavector.collector.crawler.project.music.MusicProcessor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Json;

import java.util.concurrent.TimeUnit;

/**
 * Created on 02/05/2018.
 *
 * @author zeng.zhao
 */
public class NetEasyMusicCrawler implements Downloader {

    public static void main(String[] args) {
        NetEasyMusicCrawler crawler = new NetEasyMusicCrawler();
        String url = "http://music.163.com/#/discover/artist";
        MusicProcessor processor = new MusicProcessor(new NetEasyMusicPage());
        Spider.create(processor)
                .setDownloader(crawler)
                .addUrl(url)
                .start();
    }

    @Override
    public Page download(Request request, Task task) {
//        WebDriver driver = DriverUtils.getDriver();
//        try {
//            TimeUnit.MILLISECONDS.sleep(task.getSite().getSleepTime());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        driver.get(request.getUrl());
//        String source = driver.switchTo().frame(driver.findElement(By.id("g_iframe"))).getPageSource();
        Page page = new Page();
//        page.setRawText(source);
        page.setRequest(request);
        page.setUrl(new Json(request.getUrl()));
        page.setStatusCode(200);
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }
}
