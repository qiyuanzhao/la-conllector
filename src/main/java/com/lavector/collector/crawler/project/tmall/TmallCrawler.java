package com.lavector.collector.crawler.project.tmall;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.*;
import com.lavector.collector.crawler.project.tmall.page.shop.TmallShopHomePage;
import com.lavector.collector.crawler.project.tmall.page.shop.TmallShopPageProcessor;
import com.lavector.collector.crawler.project.tmall.page.shop.TmallShopProductListPage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.PlainText;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created on 2018/1/15.
 *
 * @author zeng.zhao
 */
public class TmallCrawler extends BaseCrawler {

    private String[] keywords = new String[]{
            "稻香村"
    };

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new TmallPageProcessor(crawlerInfo));
        spider.thread(3);
//        List<TaoBaoConfig> taoBaoConfigs = TaoBaoConfig.getTaoBaoConfigs();
//        for (TaoBaoConfig taoBaoConfig : taoBaoConfigs) {
//            for(String keyword : taoBaoConfig.getKeywords()) {
//                spider.addRequest(new Request("https://list.tmall.com/search_product.htm?q=" + keyword + "&type=p")
//                        .putExtra("brand", taoBaoConfig.getBrand())
//                        .putExtra("keyword", keyword)
//                        .putExtra("excludeKeywords", taoBaoConfig.getExcludeKeywords()));
//            }
//        }
        return spider;
    }

    // https://list.tmall.com/search_product.htm?q=%D6%DC%B4%F3%B8%A3&sort=d
    private List<Request> getStartRequestBySearch() {
        return Arrays.stream(keywords).map(keyword -> {
            Request request = new Request("https://list.tmall.com/search_product.htm?q=" + keyword + "&sort=d&s=0");
            request.putExtra("keyword", keyword);
            return request;
        }).collect(Collectors.toList());
    }


    private String[] itemIds = new String[]{
//      "562685311626", "561545049602"
            "563204268842"
    };

    //指定商品抓取评论
    private List<Request> getStartRequestByShopId() {
        String baseUrl = "https://detail.tmall.com/item.htm?id=";
        return Arrays.stream(itemIds).map(itemId -> {
            Request request = new Request(baseUrl + itemId);
            return request;
        }).collect(Collectors.toList());
    }


    private List<Request> appointUrls() {
        List<TmallConfig> configs = null;
        try {
            configs = TmallConfig.getTmallConfigs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert configs != null;
        return configs.stream().map(config -> {
            if (StringUtils.isBlank(config.getUrl())) {
                System.out.println(config);
            }
            Request request = new Request(config.getUrl());
            request.putExtra("config", config);
            return request;
        }).collect(Collectors.toList());
    }

    private List<Request> shopHome() {
        List<TmallConfig.TmallShopConfig> tmallShopConfigs = null;
        try {
            tmallShopConfigs = TmallConfig.getTmallShopConfigs();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert tmallShopConfigs != null;
        return tmallShopConfigs.stream().map(tmallShopConfig -> {
            Request request = new Request(tmallShopConfig.getUrl());
            request.putExtra("config", tmallShopConfig);
            request.putExtra(RequestExtraKey.KEY_PAGE_NAME, "home");
            return request;
        }).collect(Collectors.toList());

    }

    // 获取销量
    private List<Request> readFiles() {
        List<Request> requests = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try {
            Files.walkFileTree(Paths.get("/Users/zeng.zhao/Desktop/tmall/YSL.csv"), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String type = file.toFile().getName();
                    BufferedReader reader = Files.newBufferedReader(file, Charset.forName("utf-8"));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] lineArr = line.split(",");
                        executorService.execute(() -> {
                            String url = lineArr[lineArr.length - 1];
                            String product = lineArr[0];
                            try {
                                String content = getContent(new PlainText(url).regex("id=(\\d+)").get());
                                parseSellCount(content, url, product, type);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
//                        try {
//                            TimeUnit.MILLISECONDS.sleep(200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
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

        return requests;
    }


    private String getContent(String itemId) throws IOException {
        String url = "https://mdskip.taobao.com/core/initItemDetail.htm?isUseInventoryCenter=false&cartEnable=true&service3C=false&isApparel=true&isSecKill=false&tmallBuySupport=true&isAreaSell=false&tryBeforeBuy=false&offlineShop=false&itemId=" + itemId + "&showShopProm=false&isPurchaseMallPage=false&itemGmtModified=1543365295000&isRegionLevel=false&household=false&sellerPreview=false&queryMemberRight=true&addressLevel=2&isForbidBuyItem=false&callback=setMdskip&timestamp=1543376290545&isg=null&isg2=BIGB9ghxvi6CcNIZdrvcYylcksubrvWg5NawsuPWeQjsyqGcKP_7cr3BqL6MWY3Y&areaId=110100&cat_id=2&ref=https%3A%2F%2Flist.tmall.com%2Fsearch_product.htm%3Fq%3D%25D5%25EB%25D6%25AF%25C9%25C0%26click_id%3D%25D5%25EB%25D6%25AF%25C9%25C0%26from%3Dmallfp..pc_1.0_hq%26spm%3D875.7931836%252FB.a1z5h.1.66144265kmpZTd";
        String content;
        try (CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidating().build()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("cookie", "x5sec=7b226d616c6c64657461696c736b69703b32223a223862323839366235656361396565616661393630626164653633343466386430434c47642b4e3846454c757a35495770684f71466f51453d227d; _tb_token_=e7a73335e73ed; cookie2=1d602d046eed7094978200104f74ef4a; v=0; ucn=center; miid=1070885655337996300; isg=BAAA_3z6Tz5wMDMyKaXqpt3Y24jSieRTfZkRUXqRzJuu9aAfIpm049bGCd013pwr; _cc_=WqG3DMC9EA%3D%3D; lgc=%5Cu4F1A%5Cu98DE%5Cu7684%5Cu4E43%5Cu4F0A; mt=ci=0_1&np=; t=586326fdfc5c3df40b00f8973af166f6; tg=0; tracknick=%5Cu4F1A%5Cu98DE%5Cu7684%5Cu4E43%5Cu4F0A; uc3=vt3=F8dByR6leyTYc5nCFhg%3D&id2=UU269Z1KOpXG6g%3D%3D&nk2=2Dms%2FkGeTDPcWw%3D%3D&lg2=W5iHLLyFOGW7aA%3D%3D; hng=CN%7Czh-CN%7CCNY%7C156; thw=cn; enc=hW4R7ZkVbXi0TB2rLzmtkMNvULMPTudATNFKHCb4Omcro6wnNaY4lGf5YxANMicrzpGPEJYIihNy%2FsM%2Fql1gtQ%3D%3D; cna=JPF7FCjDSzICASv+W1Invvza");
            httpGet.addHeader("referer", "https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.1.7c913598LORqmM&id=26125852732&skuId=3642166092049&areaId=110100&user_id=908948999&cat_id=2&is_b=1&rn=9a76250c6a4c93b6a5a3ec60f89066c9");
            httpGet.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0.1 Safari/605.1.15");
            httpGet.addHeader("host", "mdskip.taobao.com");
            httpGet.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());

            RequestConfig requestConfig = RequestConfig
                    .custom()
                    .setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123))
                    .build();
            httpGet.setConfig(requestConfig);

            CloseableHttpResponse response = httpClient.execute(httpGet);
            content = EntityUtils.toString(response.getEntity());
        }
        return new PlainText(content).regex("\\((.*)\\)").get();
    }

    private String path = "/Users/zeng.zhao/Desktop/tmall2/";

    private void parseSellCount(String content, String url, String product, String brand) {
        String sellCount = JsonPath.read(content, "$.defaultModel.sellCountDO.sellCount");
        if (sellCount == null) {
            System.out.println("失败 ，" + product);
            return;
        }

        File file = new File(path + brand);
        try {
            boolean newFile = file.createNewFile();
            if (newFile) {
                String head = "商品,交易数,链接\n";
                FileUtils.writeStringToFile(file, head, Charset.forName("gbk"), true);
            }
            String writeContent = product + "," + sellCount + "," + url + "\n";
            FileUtils.writeStringToFile(file, writeContent, Charset.forName("gbk"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    public static void main(String[] args) throws IOException {
//        TmallCrawler crawler = new TmallCrawler();
//        crawler.readFiles();
//        HttpClientDownloader downloader = new HttpClientDownloader();
//        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("s2.proxy.mayidaili.com", 8123)));
//        Spider spider = Spider.create(new TmallShopPageProcessor(new TmallShopHomePage(), new TmallShopProductListPage()));
////        spider.startRequest(crawler.shopHome());
//        int index = 29;
//        spider.addRequest(crawler.shopHome().get(index));
////        spider.setDownloader(downloader);
//        spider.thread(1);
//        spider.start();
//        System.out.println(index);
//    }
}
