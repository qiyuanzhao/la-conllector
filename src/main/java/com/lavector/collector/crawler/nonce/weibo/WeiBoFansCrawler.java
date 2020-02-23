package com.lavector.collector.crawler.nonce.weibo;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.food.NewDianpingDownloader;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.commons.exec.Executor;
import org.omg.CORBA.OBJ_ADAPTER;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Json;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created on 11/05/2018.
 *
 * @author zeng.zhao
 */
public class WeiBoFansCrawler extends BaseCrawler {

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        for (int i = 1; i < 48302; i++) {
            final int index = i;
            executorService.execute(() -> {
                MySpider spider = MySpider.create(new WeiBoFansPageProcessor(crawlerInfo));
                HttpClientDownloader downloader = new NewDianpingDownloader();
                downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("s2.proxy.mayidaili.com", 8123)));
                spider.setDownloader(downloader);
                spider.thread(3);
                spider.addUrl("https://weibo.com/2709931352/fans?pids=Pl_Official_RelationFans__67&cfs=600&relate=fans&t=1&f=1&type=&Pl_Official_RelationFans__67_page=" + index + "&ajaxpagelet=1&ajaxpagelet_v6=1&__ref=%2F2709931352%2Ffans%3Fcfs%3D600%26relate%3Dfans%26t%3D1%26f%3D1%26type%3D%26Pl_Official_RelationFans__67_page%3D36%23Pl_Official_RelationFans__67&_t=FM_152602817917937");
                spider.start();
            });
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return MySpider.create(new WeiBoFansPageProcessor(crawlerInfo));
    }

    private void start() {
        Downloader downloader = new WeiBoDownloader();
        MySpider spider = MySpider.create(new WeiBoFansPageProcessor(new CrawlerInfo()));
        spider.setDownloader(downloader);
        spider.thread(20);
//        readPersonId().forEach(personId -> {
//        spider.addUrl(personUrls);
//        });
//        this.addCommentRequest(spider);
        this.projectFans(spider);
        spider.start();


    }

    private String[] personUrls = {
            "http://weibo.com/Yyymakeup?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "http://weibo.com/u/5829592694?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "http://weibo.com/sueleehom?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/5137030071?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/deerstuar?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/1956671355?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/1951359172?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/1910326415?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/1790206477?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/bubblezyx?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "http://weibo.com/u/5492607158?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/272219922?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/misskinn?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/2033221695?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/lmyyml171?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/5245132696?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/5341782190?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/5328588061?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/2164467785?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/1765069550?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/xixixixixixiaoxi?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/523489618?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/floridbaobao?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/baby0923?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "http://weibo.com/u/1806836937?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/1813469601?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/2036677281?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/5126473825?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "http://weibo.com/u/1737252082?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "https://weibo.com/u/3148699507?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "http://weibo.com/babyakiramisstokyo?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "http://weibo.com/shaoyufei?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "http://weibo.com/u/3174829200?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
            "http://weibo.com/shenyunfei?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1",
    };

    private List<String> readPersonId() {
        List<String> personIds = new ArrayList<>();
        JsonMapper mapper = JsonMapper.buildNormalBinder();
        try {
            List<String> lines = Files.readAllLines(Paths.get("/Users/zeng.zhao/Desktop/person.txt"));
            lines.forEach(line -> {
//                List<LinkedHashMap> list = mapper.fromJson(line, List.class);
//                list.forEach((LinkedHashMap user) -> personIds.add(user.get("userId").toString()));
                String personId = new Json(line).regex("weibo_(\\d+)").get();
                personIds.add(personId);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return personIds;
    }


    private void addCommentRequest(Spider spider) {
        //4265760425546209   1
        //4265030620932819   2
        spider.addRequest(new Request("https://weibo.com/aj/v6/comment/big?ajwvr=6&id=4265760425546209&from=singleWeiBo"));
//        spider.addRequest(new Request("https://weibo.com/aj/v6/comment/big?ajwvr=6&id=4265030620932819&root_comment_max_id=4265047938877175&root_comment_max_id_type=1&root_comment_ext_param=&page=148&filter=hot&sum_comment_number=1223&filter_tips_before=1&from=singleWeiBo"));
    }


    private void projectFans(Spider spider) {
        String basePath = "/Users/zeng.zhao/Desktop/follow/";
        try {
            Files.walkFileTree(Paths.get("/Users/zeng.zhao/Desktop/fans"), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String type = file.toFile().getName();
                    BufferedReader reader = Files.newBufferedReader(file);
                    String line = reader.readLine();
                    while ((line = reader.readLine()) != null) {
                        String[] lineArr = line.split(",");
                        String url = lineArr[lineArr.length - 1];
                        if (url.contains("url")) {
                            continue;
                        }
                        spider.addRequest(new Request(url + "?is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1")
                                .putExtra("path", basePath + type.substring(0, type.indexOf(".")) + "/")
                                .putExtra("userId", lineArr[0])
                                .putExtra("province", lineArr[2])
                        );
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
//        new WeiBoFansCrawler().createSpider(new CrawlerInfo());
        new WeiBoFansCrawler().start();
//        new WeiBoFansCrawler().readPersonId();
//        String s = MidToUrlConverter.Uid2Mid("GxGdRbR1d");
//        System.out.println(s); 香水,男香,Chanel 自助找券,女香,香奈儿 自助找券

//        new WeiBoFansCrawler().projectFans(null);
    }
}
