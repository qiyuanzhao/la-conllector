package com.lavector.collector.crawler.project.food;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 2017/11/6.
 *
 * @author zeng.zhao
 */
@Service
public class DianpingCrawler extends BaseCrawler {

    public static final List<String> targetCitys = new ArrayList<>(3);

    private static final String[] cityIds = new String[]{
            "4", //广州
            "2", //北京
            "1", //上海
            "7"  //深圳
    };

    private static final Map<String, String> category = new HashMap<>();

    static {
        targetCitys.add("北京");
        targetCitys.add("上海");
        targetCitys.add("广州");
        targetCitys.add("深圳");

//        category.put("面包甜点", "g117");
//        category.put("咖啡厅", "g132");
//        category.put("西餐", "g116");
//        category.put("小吃快餐", "g112");
//
//
//        category.put("快吃简餐", "g210");
    }

    private static int[] completeCityIds = {1, 2, 3, 11, 5, 6, 13, 21, 22, 10, 160, 18, 19, 79, 4, 7, 8, 9, 17, 14, 15, 16, 110, 344, 94, 98, 104, 105, 101, 145, 148, 149, 219, 92, 99,
            108, 150, 152, 155, 213, 385, 35, 93, 208, 24, 102, 416, 12, 70, 258, 267, 151, 62, 26, 23, 146, 27, 162, 206, 220, 345, 147, 59, 80, 44, 299, 180,
            179, 196, 194, 111, 46, 167, 103, 209, 224, 25, 71, 97, 58, 106, 107, 95, 42, 184, 260, 313, 225, 226, 47, 33, 134, 242, 217, 210, 119, 137, 153, 120,
            143, 112, 161, 192, 1009, 292, 197, 218, 113, 321, 211, 116, 133, 96, 325, 129, 207, 127, 29, 166, 163, 84, 241, 115, 109, 277, 130, 200, 114, 157, 132};


    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new DianpingPageProcessor(crawlerInfo));
        HttpClientDownloader downloader = new NewDianpingDownloader();
        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("s2.proxy.mayidaili.com", 8123)));
        spider.setDownloader(downloader);
        spider.thread(20);
        return spider;
    }


    /**
     * 按城市、按类别获取店铺list
     *
     * @return
     */
    private List<Request> getStartRequest() {
        List<Request> requests = new ArrayList<>();
        for (String city : cityIds) {
            String baseUrl = "http://www.dianping.com/search/category/" + city + "/10/";
            for (Map.Entry<String, String> entry : category.entrySet()) {
                String startUrl = baseUrl + entry.getValue();
                Request request = new Request(startUrl).putExtra("category", entry.getKey());
                requests.add(request);
            }
        }
        return requests;
    }

    /**
     * 根据id抓取评论
     *
     * @return
     */
    private Set<Request> getStartRequestByShopId() {
        Set<Request> requests = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/file_list_1.txt"));
            String line = reader.readLine();
            while (line != null) {
                String[] shopIds = line.split(",");
                String url = "http://www.dianping.com/shop/" + shopIds[0] + "/" + shopIds[1];
                Request request = new Request(url);
                request.putExtra("shopId", shopIds[0]).putExtra("dishId", shopIds[1]);
                requests.add(request);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return requests;
    }

    /**
     * 指定菜名抓取评论
     *
     * @return
     */
    private List<Request> getStartRequestByDishName() {
        List<Request> requests = new ArrayList<>();
        //初雪炸鸡
        requests.add(
                new Request("http://www.dianping.com/shop/17623634/review_search_%E5%88%9D%E9%9B%AA%E7%82%B8%E9%B8%A1")
                        .putExtra("shopId", 17623634)
                        .putExtra("dishId", 4689019)
        );
        //榴莲芝士汉堡
        requests.add(
                new Request("http://www.dianping.com/shop/92876078/review_search_%E6%A6%B4%E8%8E%B2%E8%8A%9D%E5%A3%AB%E6%B1%89%E5%A0%A1")
                        .putExtra("shopId", 92876078)
                        .putExtra("dishId", 101781037)
        );
        //茉莉大汉堡
        requests.add(
                new Request("http://www.dianping.com/shop/18264824/review_search_%E8%8C%89%E8%8E%89%E5%A4%A7%E6%BC%A2%E5%A0%A1")
                        .putExtra("shopId", 18264824)
                        .putExtra("dishId", 6779734)
        );
        //茉莉大汉堡
        requests.add(
                new Request("http://www.dianping.com/shop/3317554/review_search_%E8%8C%89%E8%8E%89%E5%A4%A7%E6%B1%89%E5%A0%A1")
                        .putExtra("shopId", 3317554)
                        .putExtra("dishId", 8716625)
        );
        //竹炭鸡肉鹅肝汉堡
        requests.add(
                new Request("http://www.dianping.com/shop/76066928/review_search_%E7%AB%B9%E7%82%AD%E9%B8%A1%E8%82%89%E9%B9%85%E8%82%9D%E6%B1%89%E5%A0%A1")
                        .putExtra("shopId", 76066928)
                        .putExtra("dishId", 14579787)
        );
        //拉面汉堡
        requests.add(
                new Request("http://www.dianping.com/shop/90984413/review_search_%E6%8B%89%E9%9D%A2%E6%B1%89%E5%A0%A1")
                        .putExtra("shopId", 90984413)
                        .putExtra("dishId", 39294381)
        );
        //大力水手汉堡
        requests.add(
                new Request("http://www.dianping.com/shop/58203180/review_search_%E5%A4%A7%E5%8A%9B%E6%B0%B4%E6%89%8B")
                        .putExtra("shopId", 58203180)
                        .putExtra("dishId", 7981796)
        );

        //店铺：Kyochon
        requests.add(new Request("https://www.dianping.com/search/keyword/1/0_Kyochon"));
//        //店铺：Fat Cow
//        requests.add(new Request("https://www.dianping.com/search/keyword/1/0_Fat%20Cow"));
        //店铺：Co. Cheese Melt Bar
        requests.add(new Request("https://www.dianping.com/search/keyword/1/0_Co.%20Cheese%20Melt%20Bar"));
        return requests;
    }

    /**
     * 根据菜品进行检索
     * @return List<Request></>
     */
    private List<Request> getStartRequestByCityAndShopAndDishName() {
        List<Request> requests = new ArrayList<>(completeCityIds.length);
        String shopName = "Blue Frog";
        String dishName = "迷你小汉堡";
        for (int cityId : completeCityIds) {
            Request request = new Request("http://www.dianping.com/search/keyword/" + cityId + "/0_" + shopName
                    + "/p1");
            request.putExtra("dishName", dishName);
            requests.add(request);
        }
        return requests;
    }

    /**
     * 指定城市，指定菜名
     * @return List<Request></>
     */
    private List<Request> getStartRequestByCityAndDishName() {
        String dishName = "鸡排";
        return Arrays.stream(cityIds).map(cityId -> {
            Request request = new Request("http://www.dianping.com/search/keyword/" + cityId + "/0_" +
                    dishName
                    + "/p1");
            request.putExtra("dishName", dishName);
            return request;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        DianpingCrawler dianpingCrawler = new DianpingCrawler();
        MySpider spider = dianpingCrawler.createSpider(new CrawlerInfo());
        spider.startRequest(dianpingCrawler.getStartRequestByCityAndDishName());
        spider.start();
    }


}
