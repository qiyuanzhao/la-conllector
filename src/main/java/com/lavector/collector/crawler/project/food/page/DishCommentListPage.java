package com.lavector.collector.crawler.project.food.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created on 2017/12/22.
 *
 * @author zeng.zhao
 */
public class DishCommentListPage implements PageParse {

    private static final Map<String, String> starMap = new HashMap<>();

    private static final Map<String, String> reviewRank = new HashMap<>();

    static {
        starMap.put("sml-rank-stars sml-str50 star", "5.0");
        starMap.put("sml-rank-stars sml-str45 star", "4.5");
        starMap.put("sml-rank-stars sml-str40 star", "4.0");
        starMap.put("sml-rank-stars sml-str35 star", "3.5");
        starMap.put("sml-rank-stars sml-str30 star", "3.0");
        starMap.put("sml-rank-stars sml-str25 star", "2.5");
        starMap.put("sml-rank-stars sml-str20 star", "2.0");
        starMap.put("sml-rank-stars sml-str15 star", "1.5");
        starMap.put("sml-rank-stars sml-str10 star", "1.0");
        starMap.put("sml-rank-stars sml-str5 star", "0.5");
        starMap.put("sml-rank-stars sml-str0 star", "0.0");

        reviewRank.put("非常好", "5");
        reviewRank.put("很好", "4");
        reviewRank.put("好", "3");
        reviewRank.put("一般", "2");
        reviewRank.put("差", "1");
    }

    private Logger logger = LoggerFactory.getLogger(DishCommentListPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.dianping.com/shop/\\d+/review_search_.*", url);
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String shopId = page.getRequest().getExtra("shopId").toString();
        String dishId = page.getRequest().getExtra("dishId").toString();
        Html html = page.getHtml();
        String shopName = html.xpath("//a[@class='shop-name']/text()").get();
        String dishName = html.xpath("//div[@class='reviews-search-wrap']/p/span/text()").get();
        dishName = dishName.replaceAll("”", "");
        List<Selectable> commentInfo = html.xpath("//div[@class='main-review']").nodes();
        for (Selectable node : commentInfo) {
            String userId = node.xpath("//div[@class='dper-info']/a/@href").regex("\\d+").get();
            String star = node.xpath("//div[@class='review-rank']/span[1]/@class").get();
            String starRating = starMap.get(star);
            if (StringUtils.isBlank(starRating)) {
                logger.error("没有评星？{}", page.getUrl().get());
                return result;
            }
            String kouwei = node.xpath("//div[@class='review-rank']/span[2]/span[1]/text()").get();
            String huanjing = node.xpath("//div[@class='review-rank']/span[2]/span[2]/text()").get();
            String fuwu = node.xpath("//div[@class='review-rank']/span[2]/span[3]/text()").get();
            String content = node.xpath("//div[@class='review-words']/text()").get();
            String date = node.xpath("//span[@class='time']/text()").regex("\\d+-\\d+-\\d+ \\d+:\\d+").get();

            String writeLine = shopName + "," + dishId + "," + dishName.replace("“", "") + ","
                    + userId + "," +
                    (starRating == null ? "0" : starRating) + "," + content.replaceAll(",", "，")
                    + ",";

            if (StringUtils.isNotBlank(kouwei)) {
                writeLine = writeLine + ShopCommentListPage.processReviewRank(kouwei) + ",";
            }

            if (StringUtils.isNotBlank(huanjing)) {
                writeLine = writeLine + ShopCommentListPage.processReviewRank(huanjing) + ",";
            }

            if (StringUtils.isNotBlank(fuwu)) {
                writeLine = writeLine + ShopCommentListPage.processReviewRank(fuwu) + ",";
            }

            writeLine += page.getUrl().get();
            writeLine = writeLine + "," + date;
            wirte(writeLine, "/Users/zeng.zhao/Desktop/dianping/dish_comment/" + shopId + ".csv");
            logger.info("写入成功！{}", shopName);
        }

        String nextUrl = html.xpath("//div[@class='reviews-pages']/a[@class='NextPage']/@href").get();
        if (StringUtils.isNotBlank(nextUrl)) {
            if (!nextUrl.contains("http://www")) {
                nextUrl = "http://www.dianping.com" + nextUrl;
            }
            us.codecraft.webmagic.Request request = new us.codecraft.webmagic.Request(nextUrl);
            request.putExtra("shopId", shopId).putExtra("dishId", dishId);
            result.addRequest(request);
        }
        return result;
    }


    public static void wirte(String content, String filePath) {
//        ExecutorService executorService = getFixedExecutorService(10000, 2);
//        executorService.execute(() -> {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
                writer.write(content);
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        });
    }

    public static ExecutorService getFixedExecutorService(int queueSize, int threadNum) {
        BlockingQueue<Runnable> blockingQueue;
        if (threadNum < 1) {
            throw new IllegalArgumentException("thread number is illegal : " + threadNum);
        }
        if (queueSize < 0) {
            throw new IllegalArgumentException("queue size is illegal : " + threadNum);
        }
        if (queueSize == 0) {
            blockingQueue = new SynchronousQueue<>();
        } else {
            blockingQueue = new LinkedBlockingDeque<>();
        }
        return new ThreadPoolExecutor(threadNum, threadNum,
                0L, TimeUnit.MILLISECONDS,
                blockingQueue, Executors.defaultThreadFactory(), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                if (!executor.isShutdown()) {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        // should not be interrupted
                    }
                }
            }
        });
    }


    public static void main(String[] args) throws Exception {
        String url = "http://www.dianping.com/shop/21040337/review_search_%E7%BA%A2%E7%83%A7%E4%BB%94%E6%8E%92";
        String content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url).putExtra("shopid", 231).putExtra("dishid", 2423));
        page.setUrl(new Json(url));
        DishCommentListPage listPage = new DishCommentListPage();
        listPage.parse(page);
    }
}
