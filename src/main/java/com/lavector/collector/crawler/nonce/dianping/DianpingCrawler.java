package com.lavector.collector.crawler.nonce.dianping;

import com.google.common.collect.Lists;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.nonce.dianping.entity.DianPingMessage;
import com.lavector.collector.crawler.nonce.dianping.entity.Shop;
import com.lavector.collector.crawler.project.food.NewDianpingDownloader;
import com.lavector.collector.crawler.util.JsonMapper;
import net.minidev.json.JSONArray;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 11/05/2018.
 *
 * @author zeng.zhao
 */
public class DianpingCrawler extends BaseCrawler {

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static final String[] shops = {
//            "http://www.dianping.com/shop/82787136",
//            "http://www.dianping.com/shop/24282755",
//            "http://www.dianping.com/shop/27504284",
//            "http://www.dianping.com/shop/11318552",
//            "http://www.dianping.com/shop/92045711",
//            "http://www.dianping.com/shop/24278532",
//            "http://www.dianping.com/shop/21739379",
//            "http://www.dianping.com/shop/57295445",
//            "http://www.dianping.com/shop/33492971",
//            "http://www.dianping.com/shop/92045630",
//            "http://www.dianping.com/shop/19685442",
//            "http://www.dianping.com/shop/90319021",
//            "http://www.dianping.com/shop/48666300",
//            "http://www.dianping.com/shop/6415804",
//            "http://www.dianping.com/shop/32894434",
//            "http://www.dianping.com/shop/83565126",
//            "http://www.dianping.com/shop/70622114",
//            "http://www.dianping.com/shop/92045555",
//            "http://www.dianping.com/shop/71943198",
//            "http://www.dianping.com/shop/32385668",
//            "http://www.dianping.com/shop/11555896",
//            "http://www.dianping.com/shop/21352223",
//            "http://www.dianping.com/shop/21386847",
//            "http://www.dianping.com/shop/27221314",
//            "http://www.dianping.com/shop/21635246",
//            "http://www.dianping.com/shop/82097713",
//            "http://www.dianping.com/shop/22583820",
//            "http://www.dianping.com/shop/92045463",
//            "http://www.dianping.com/shop/92045645",
//            "http://www.dianping.com/shop/83565455",
//            "http://www.dianping.com/shop/92045632",
//            "http://www.dianping.com/shop/83565452",
//            "http://www.dianping.com/shop/83560236",
//            "http://www.dianping.com/shop/92045479",
//            "http://www.dianping.com/shop/91026428",
//            "http://www.dianping.com/shop/93558377",
//            "http://www.dianping.com/shop/82114868",
//            "http://www.dianping.com/shop/92045743",
//            "http://www.dianping.com/shop/74696609",
//            "http://www.dianping.com/shop/6231580",
//            "http://www.dianping.com/shop/23530338",
//            "http://www.dianping.com/shop/21568402",
//            "http://www.dianping.com/shop/3884632",
//            "http://www.dianping.com/shop/74851662",
//            "http://www.dianping.com/shop/92045438",
//            "http://www.dianping.com/shop/92045815",
//            "http://www.dianping.com/shop/92045815",
//            "http://www.dianping.com/shop/5394212",
//            "http://www.dianping.com/shop/11228428",
//            "http://www.dianping.com/shop/92045536",
//            "http://www.dianping.com/shop/17690171",
//            "http://www.dianping.com/shop/24846018",
//            "http://www.dianping.com/shop/15180246",
//            "http://www.dianping.com/shop/92045496",
//            "http://www.dianping.com/shop/92045478",
//            "http://www.dianping.com/shop/57593401",
//            "http://www.dianping.com/shop/4726761",
//            "http://www.dianping.com/shop/21860415",
//            "http://www.dianping.com/shop/91887611",
//            "http://www.dianping.com/shop/91604674",
//            "http://www.dianping.com/shop/73148303",
//            "http://www.dianping.com/shop/24845950",
//            "http://www.dianping.com/shop/9974944",
//            "http://www.dianping.com/shop/4103375",
//            "http://www.dianping.com/shop/72462776",
//            "http://www.dianping.com/shop/3673837",
//            "http://www.dianping.com/shop/24358115",
//            "http://www.dianping.com/shop/66409208",
//            "http://www.dianping.com/shop/19609435",
//            "http://www.dianping.com/shop/103270476",
//            "http://www.dianping.com/shop/67332329",
//            "http://www.dianping.com/shop/23894234",
//            "http://www.dianping.com/shop/4019845",
//            "http://www.dianping.com/shop/92045811",
//            "http://www.dianping.com/shop/56632953",
//            "http://www.dianping.com/shop/92045808",
//            "http://www.dianping.com/shop/74664642",
//            "http://www.dianping.com/shop/90316039",
//            "http://www.dianping.com/shop/91636978",
//            "http://www.dianping.com/shop/5546744",
//            "http://www.dianping.com/shop/22997547",
//            "http://www.dianping.com/shop/17786279",
//            "http://www.dianping.com/shop/4096018",
//            "http://www.dianping.com/shop/5138801",
//            "http://www.dianping.com/shop/4054684",
//            "http://www.dianping.com/shop/4107641",
//            "http://www.dianping.com/shop/6270334",
//            "http://www.dianping.com/shop/26205722",
//            "http://www.dianping.com/shop/83574847",
//            "http://www.dianping.com/shop/92045529",
//            "http://www.dianping.com/shop/92045909",
//            "http://www.dianping.com/shop/92045459",
//            "http://www.dianping.com/shop/5871385",
//            "http://www.dianping.com/shop/32405791",
//            "http://www.dianping.com/shop/16832341",
//            "http://www.dianping.com/shop/10665668",
//            "http://www.dianping.com/shop/5247641",
//            "http://www.dianping.com/shop/92045442",
//            "http://www.dianping.com/shop/92045553",
//            "http://www.dianping.com/shop/83565122",
//            "http://www.dianping.com/shop/50660388",
//            "http://www.dianping.com/shop/92045618",
//            "http://www.dianping.com/shop/5161279",
//            "http://www.dianping.com/shop/91887719",
//            "http://www.dianping.com/shop/12595842",
//            "http://www.dianping.com/shop/18085388",
//            "http://www.dianping.com/shop/21017858",
//            "http://www.dianping.com/shop/2499451",
//            "http://www.dianping.com/shop/3917995",
//            "http://www.dianping.com/shop/15774246",
//            "http://www.dianping.com/shop/2583169",
//            "http://www.dianping.com/shop/18576872",
//            "http://www.dianping.com/shop/2058388",
//            "http://www.dianping.com/shop/16072988",
//            "http://www.dianping.com/shop/8870208",
//            "http://www.dianping.com/shop/3646192",
//            "http://www.dianping.com/shop/22991384",
//            "http://www.dianping.com/shop/4292113",
//            "http://www.dianping.com/shop/92045592",
//            "http://www.dianping.com/shop/17723600",
//            "http://www.dianping.com/shop/2679262",
//            "http://www.dianping.com/shop/9978457",
//            "http://www.dianping.com/shop/16072987",
//            "http://www.dianping.com/shop/16072987",
//            "http://www.dianping.com/shop/92045607",
//            "http://www.dianping.com/shop/2274835",
//            "http://www.dianping.com/shop/4657614",
//            "http://www.dianping.com/shop/18813445",
//            "http://www.dianping.com/shop/83565620",
//            "http://www.dianping.com/shop/21884649",
//            "http://www.dianping.com/shop/22991726",
//            "http://www.dianping.com/shop/4273978",
//            "http://www.dianping.com/shop/4273978",
//            "http://www.dianping.com/shop/2645068",
//            "http://www.dianping.com/shop/22992056",
//            "http://www.dianping.com/shop/92045429",
//            "http://www.dianping.com/shop/17226865",
//            "http://www.dianping.com/shop/22991303",
//            "http://www.dianping.com/shop/92045500",
//            "http://www.dianping.com/shop/18333636",
//            "http://www.dianping.com/shop/7918184",
//            "http://www.dianping.com/shop/16072985",
//            "http://www.dianping.com/shop/22991410",
//            "http://www.dianping.com/shop/17245057",
//            "http://www.dianping.com/shop/22992217",
//            "http://www.dianping.com/shop/56581234",
//            "http://www.dianping.com/shop/22992020",
//            "http://www.dianping.com/shop/2370550",
//            "http://www.dianping.com/shop/22992119",
//            "http://www.dianping.com/shop/2645074",
//            "http://www.dianping.com/shop/92045745",
//            "http://www.dianping.com/shop/4673786",
//            "http://www.dianping.com/shop/22991225",
//            "http://www.dianping.com/shop/8864067",
//            "http://www.dianping.com/shop/6015039",
//            "http://www.dianping.com/shop/45307564",
//            "http://www.dianping.com/shop/20887519",
//            "http://www.dianping.com/shop/9029506",
//            "http://www.dianping.com/shop/16035328",
//            "http://www.dianping.com/shop/21875378",
//            "http://www.dianping.com/shop/92045703",
//            "http://www.dianping.com/shop/17245051",
//            "http://www.dianping.com/shop/22991540",
//            "http://www.dianping.com/shop/56581510",
//            "http://www.dianping.com/shop/90363863",
//            "http://www.dianping.com/shop/68159393",
//            "http://www.dianping.com/shop/92555999",
//            "http://www.dianping.com/shop/65689974",
//            "http://www.dianping.com/shop/92045550",
//            "http://www.dianping.com/shop/66872217",
//            "http://www.dianping.com/shop/77060528",
//            "http://www.dianping.com/shop/23693012",
//            "http://www.dianping.com/shop/76885724",
//            "http://www.dianping.com/shop/70121005",
//            "http://www.dianping.com/shop/77405258",
//            "http://www.dianping.com/shop/76759716",
//            "http://www.dianping.com/shop/95053309",
//            "http://www.dianping.com/shop/19085405",
//            "http://www.dianping.com/shop/83368021",
//            "http://www.dianping.com/shop/19584208",
//            "http://www.dianping.com/shop/92045816",
//            "http://www.dianping.com/shop/16072981",
//            "http://www.dianping.com/shop/70597307",
//            "http://www.dianping.com/shop/92045727",
//            "http://www.dianping.com/shop/6004207",
//            "http://www.dianping.com/shop/92390418",
//            "http://www.dianping.com/shop/70393780",
//            "http://www.dianping.com/shop/3528760",
//            "http://www.dianping.com/shop/92555463",
//            "http://www.dianping.com/shop/97525372",
//            "http://www.dianping.com/shop/83565072",
//            "http://www.dianping.com/shop/11461037",
//            "http://www.dianping.com/shop/3632452",
//            "http://www.dianping.com/shop/83564354",
//            "http://www.dianping.com/shop/80998883",
//            "http://www.dianping.com/shop/47457847",
//            "http://www.dianping.com/shop/16987662",
//            "http://www.dianping.com/shop/17897159",
//            "http://www.dianping.com/shop/5466557",
//            "http://www.dianping.com/shop/79527120",
//            "http://www.dianping.com/shop/8983102",
//            "http://www.dianping.com/shop/33304497",
//            "http://www.dianping.com/shop/5679272",
//            "http://www.dianping.com/shop/98983547",
//            "http://www.dianping.com/shop/4174884",
//            "http://www.dianping.com/shop/26218785",
//            "http://www.dianping.com/shop/75171662",
//            "http://www.dianping.com/shop/83570846",
//            "http://www.dianping.com/shop/74904607",
//            "http://www.dianping.com/shop/18090042",
//            "http://www.dianping.com/shop/23945642",
//            "http://www.dianping.com/shop/16970287",
//            "http://www.dianping.com/shop/8054425",
//            "http://www.dianping.com/shop/22008090",
//            "http://www.dianping.com/shop/69140044",
//            "http://www.dianping.com/shop/90905137",
//            "http://www.dianping.com/shop/5437949",
//            "http://www.dianping.com/shop/92045684",
//            "http://www.dianping.com/shop/48915176",
//            "http://www.dianping.com/shop/5438085",
//            "http://www.dianping.com/shop/4099171",
//            "http://www.dianping.com/shop/79573273",
//            "http://www.dianping.com/shop/92045573",
//            "http://www.dianping.com/shop/92045720",
//            "http://www.dianping.com/shop/14887434",
//            "http://www.dianping.com/shop/50774002",
//            "http://www.dianping.com/shop/90363836",
//            "http://www.dianping.com/shop/96671566",
//            "http://www.dianping.com/shop/58255045",
//            "http://www.dianping.com/shop/21723572",
//            "http://www.dianping.com/shop/97550461",
//            "http://www.dianping.com/shop/100904531",
//            "http://www.dianping.com/shop/14172269",
//            "http://www.dianping.com/shop/71781545",
//            "http://www.dianping.com/shop/92045617",
//            "http://www.dianping.com/shop/92045539",
//            "http://www.dianping.com/shop/81941239",
//            "http://www.dianping.com/shop/22510499",
//            "http://www.dianping.com/shop/90319021",
//            "http://www.dianping.com/shop/48666300",
//            "http://www.dianping.com/shop/32894434",
//            "http://www.dianping.com/shop/6415804",
//            "http://www.dianping.com/shop/57151786",
//            "http://www.dianping.com/shop/45825480",
//            "http://www.dianping.com/shop/33501857",
//            "http://www.dianping.com/shop/23311459",
//            "http://www.dianping.com/shop/27405820",
//            "http://www.dianping.com/shop/21994575",
//            "http://www.dianping.com/shop/92045564",
//            "http://www.dianping.com/shop/15127530",
//            "http://www.dianping.com/shop/83565478",
//            "http://www.dianping.com/shop/93373812",
//            "http://www.dianping.com/shop/72425078",
//            "http://www.dianping.com/shop/21885705",
//            "http://www.dianping.com/shop/83570784",
////            "http://www.dianping.com/brands/b4205694",
//            "http://www.dianping.com/shop/92045566",
//            "http://www.dianping.com/shop/21852586",
//            "http://www.dianping.com/shop/18244998",
//            "http://www.dianping.com/shop/11355316",
//            "http://www.dianping.com/shop/92045462",
//            "http://www.dianping.com/shop/6002978",
//            "http://www.dianping.com/shop/70313826",
//            "http://www.dianping.com/shop/24281094",
//            "http://www.dianping.com/shop/71784392",
//            "http://www.dianping.com/shop/82787136",
//            "http://www.dianping.com/shop/24282755"


            "http://www.dianping.com/shop/19348893"
    };

    /*
        在抓取店铺信息的时候，3线程
        店铺评论 5 线程
        签到评论 8 - 10
     */
    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new DianPingPageProcessor(crawlerInfo));
        HttpClientDownloader downloader = new NewDianpingDownloader();
        spider.setDownloader(downloader);
//        this.addRequests(spider);
        spider.thread(20);
//        this.incrementByUserId(spider);
//        this.addShopUrl(spider);
//        spider.addRequest(new Request("http://www.dianping.com/search/keyword/5/0_%E5%90%BE%E6%82%A6%E5%B9%BF%E5%9C%BA/p1").putExtra("referer", "http://www.dianping.com/nanjing")
//            .putExtra("keyword", "吾悦广场"));
        this.addRequestsByShopCommentFile(spider);
//        this.addRequestByError(spider);
//        spider.setSpiderListeners(Lists.newArrayList(new SpiderListener() {
//            @Override
//            public void onSuccess(Request request) {
//            }
//
//            @Override
//            public void onError(Request request) {
//                write(request);
//            }
//
//            private synchronized void write (Request request) {
//                CharSink charSink = Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/dianping_error_member_1.txt").toFile(),
//                        StandardCharsets.UTF_8, FileWriteMode.APPEND);
//                String memberId = new PlainText(request.getUrl()).regex("memberId=(\\d+)").get();
//                try {
//                    charSink.write(memberId + "\n");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("错误ID 写入成功.");
//            }
//        }));
        spider.start();
        return spider;
    }

    private void addRequests(MySpider spider) {
        for (String shop : shops) {
            spider.addRequest(new Request(shop + "/review_all/p1?queryType=sortType&queryVal=latest")
                    .putExtra("referer", shop + "/review_all"));
        }
//        Request request = new Request("http://www.dianping.com/ajax/member/checkin/checkinList?memberId=2259014&page=174")
//                .putExtra("referer", "http://www.dianping.com/member/2259014")
//                .putExtra("userId", "2259014");
//        request.setMethod(HttpConstant.Method.POST);
//        HttpRequestBody requestBody = new HttpRequestBody();
//        requestBody.setContentType("application/x-www-form-urlencoded");
//        requestBody.setEncoding("utf-8");
//        request.setRequestBody(requestBody);
//        spider.addRequest(request);
    }

    private void addRequestsByShopCommentFile(MySpider mySpider) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/dianping_message.json"));
            String line = reader.readLine();
            while (line != null) {
                List<String> userIds = JsonPath.read(line, "$.[*].person.userId");
//                List<String> shopIds = JsonPath.read(line, "$.[*].shopId");
                for (String userId : userIds) {
//                     用户签到
//                    Request request = new Request("http://www.dianping.com/ajax/member/checkin/checkinList?memberId=" + userId + "&page=5")
//                            .putExtra("referer", "http://www.dianping.com/member/" + userId)
//                            .putExtra("userId", userId);
//                    request.setMethod(HttpConstant.Method.POST);
//                    HttpRequestBody requestBody = new HttpRequestBody();
//                    requestBody.setContentType("application/x-www-form-urlencoded");
//                    requestBody.setEncoding("utf-8");
//                    request.setRequestBody(requestBody);

//                     用户点评
                    Request request = new Request("http://www.dianping.com/member/" + userId + "/reviews")
                            .putExtra("referer", "http://www.dianping.com/member/" + userId);

                    mySpider.addRequest(request);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addRequestByError(MySpider spider) {
        Set<String> successIds = new HashSet<>();
        Set<String> errorIds = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/dianping_sign.json"));
            String line = reader.readLine();
            while (line != null) {
                JSONArray persons = JsonPath.read(line, "$.[*]");
                persons.forEach(person -> {
                    String type = JsonPath.read(person, "$.type");
                    if (type.equals("SIGN_IN")) {
                        String userId = JsonPath.read(person, "$.person.userId");
                        successIds.add(userId);
                    }
                });
                line = reader.readLine();
            }
            reader.close();

            BufferedReader reader1 = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/Error_Users.txt"));
            String line1 = reader1.readLine();
            while (line1 != null) {
                String userId = new Json(line1).regex("member/(\\d+)/checkin").get();
                errorIds.add(userId);
                line1 = reader1.readLine();
            }
            reader1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AtomicInteger count = new AtomicInteger();
        errorIds.forEach(errorId -> {
            if (!successIds.contains(errorId)) {
                spider.addRequest(new Request("http://www.dianping.com/member/" + errorId + "/checkin")
                        .putExtra("referer", "http://www.dianping.com/member/" + errorId));
                count.getAndIncrement();
            }
        });
        System.out.println("error Id size : " + count.get());
    }

    private void addShopUrl(Spider spider) {
        try {
            JsonMapper jsonMapper = JsonMapper.buildNormalBinder();
            BufferedReader reader = java.nio.file.Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/dianping_shop_1.json"));
            String line = reader.readLine();
            while (line != null) {
                Shop shop = jsonMapper.fromJson(line, Shop.class);
                spider.addRequest(new Request(shop.getUrl() + "/review_all/p1?queryType=sortType&queryVal=latest").putExtra("referer", shop.getUrl() + "/review_all"));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void incrementByUserId(Spider spider) {
        Set<String> total = new HashSet<>();
        try {
            BufferedReader reader = java.nio.file.Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/dianping_message.json"));
            String line = reader.readLine();
            while (line != null) {
                List<String> userIds = JsonPath.read(line, "[*].person.userId");
                total.addAll(userIds);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<String> success = new HashSet<>();
        try {
            BufferedReader reader = java.nio.file.Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/dianping_message_1.json"));
            String line = reader.readLine();
            while (line != null) {
                List<String> userIds = JsonPath.read(line, "[*].person.userId");
                success.addAll(userIds);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<String> result = new HashSet<>();
        for (String s : total) {
            if (!success.contains(s)) {
                result.add(s);
            }
        }

        for (String userId : result) {
            Request request = new Request("http://www.dianping.com/member/" + userId + "/reviews")
                    .putExtra("referer", "http://www.dianping.com/member/" + userId);

            spider.addRequest(request);
        }
    }

    public static void main(String[] args) throws Exception {
        DianpingCrawler dianpingCrawler = new DianpingCrawler();
        dianpingCrawler.createSpider(new CrawlerInfo());
    }
}
