package com.lavector.collector.crawler.project.douyin;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.food.NewDianpingDownloader;
import com.lavector.collector.crawler.util.JsonMapper;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2018/4/4.
 *
 * @author zeng.zhao
 */
public class DouYinCrawler extends BaseCrawler {

    public static final String[] keywords = {
            "巧克力",
            "乳酪", "黄油", "奶油", "牛油", "芝士", "马苏", "乳脂", "干酪",
            "奶酪", "切达", "抹茶", "肉松", "草莓千层", "草莓班戟", "草莓奶昔", "蛋糕",
            "奶盖", "月饼", "意面", "焗饭", "意大利面", "披萨", "甜点", "西点", "甜品",
            "奶茶", "果茶", "茶饮", "柠檬茶", "三明治", "吐司", "面包", "欧包", "布丁",
            "慕斯", "泡芙", "荷花酥", "雪花酥", "桃花酥", "榴莲酥", "蛋黄酥", "绿豆酥",
            "凤梨酥", "蔓越莓酥", "梨酥", "菠萝酥", "核桃酥", "饼干", "蛋挞", "挞", "下午茶"
    };

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new DouYinPageProcessor(crawlerInfo));
        HttpClientDownloader downloader = new NewDianpingDownloader();
        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("s2.proxy.mayidaili.com", 8123)));
        spider.setDownloader(downloader);
        spider.thread(5);
        return spider;
    }

    private void addStartRequestBySearchUser(MySpider spider) {
        Arrays.asList(keywords).forEach(keyword -> {
            spider.addRequest(new Request("https://api.amemv.com/aweme/v1/discover/search/?cursor=0&keyword=" + keyword +
                    "&count=10&type=1&retry_type=no_retry&iid=17900846586&device_id=34692364855&ac=wifi&channel=xiaomi&aid=1128&app_name=aweme&version_code=162&version_name=1.6.2&device_platform=android&ssmix=a&device_type=MI+5&device_brand=Xiaomi&os_api=24&os_version=7.0&uuid=861945034132187&openudid=dc451556fc0eeadb&manifest_version_code=162&resolution=1080*1920&dpi=480&update_version_code=1622"));
        });
    }

    private void addStartRequestByUserHome(MySpider spider) {
        JsonMapper mapper = JsonMapper.buildNonNullBinder();
        Path path = Paths.get("/Users/zeng.zhao/Desktop/douyin_user_top.json");
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                DouYinAccount account = mapper.fromJson(line, DouYinAccount.class);
                spider.addRequest(new Request("https://www.douyin.com/aweme/v1/aweme/post/?user_id=" + account.getDouyinId() +
                        "&max_cursor=0&count=" + account.getVideoCount()).putExtra("userId", account.getDouyinId()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DouYinCrawler crawler = new DouYinCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
//        crawler.addStartRequestBySearchUser(spider);
        crawler.addStartRequestByUserHome(spider);
        spider.start();

    }
}
