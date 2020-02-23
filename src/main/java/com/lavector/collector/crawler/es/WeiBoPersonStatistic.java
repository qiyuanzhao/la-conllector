package com.lavector.collector.crawler.es;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created on 2018/9/29.
 *
 * @author zeng.zhao
 */
public class WeiBoPersonStatistic {

    private static String[] keywords = {

            // 子品牌 -> 头部护理
//            "AQUAIR",
//            "水之密语",
//            "丝蓓绮",
//            "玛馨妮",
//            "Macherie",

            // 子品牌 -> 面部护理
//            "SENKA",
//            "珊珂",
//            "洗颜专科",
//            "吾诺",
//            "资生堂 UNO",

//             竞品 -> 头部护理
//            "施华蔻",
//            "斐丝丽",
//            "schwarzkopf",
//            "滋源",
//            "SEEYOUNG",
//            "RYOE吕",
//            "syoss",
//            "丝蕴",
//            "阿道夫",
//            "Adolph",
//            "黛丝恩",
//            "Moist Diane",

            // 竞品 -> 面部护理
            "芙丽芳丝",
            "freeplus",
            "碧柔",
            "Biore",
            "妮维雅 精华",
            "妮维雅 洁面",
            "妮维雅 爽肤水",
            "妮维雅 面霜",
            "妮维雅 洗面",
            "妮维雅 乳液",
//            "妮维雅 男士",
//            "nivea 男士",
            "曼秀雷敦 男士",
//            "欧莱雅 男士",
            "高夫 控油",
            "高夫 须后",
            "高夫 面膜",
            "高夫 洗护",
            "高夫 洗面",
            "高夫 发蜡",
            "高夫 防晒",
            "高夫 唇膏",
            "高夫 沐浴",
            "高夫 喷雾",
            "高夫 洁面",
            "高夫 保湿",
            "高夫 乳液",
            "高夫 洗发",
            "高夫 爽肤水",
            "高夫 眼霜",
            "高夫 面霜"
    };

    private static ZoneId zoneId = ZoneId.systemDefault();
    private static LocalDate from = LocalDate.of(2018, 6, 1);
    private static ZonedDateTime fromZone = from.atStartOfDay(zoneId);
    private static Date startTime = Date.from(fromZone.toInstant());

    private static LocalDateTime to = LocalDateTime.of(2018, 8, 31, 23, 59);
    private static ZonedDateTime toZone = to.atZone(zoneId);
    private static Date endTime = Date.from(toZone.toInstant());

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final String INDEX = "lavector-main2";

    private static JsonMapper jsonMapper = JsonMapper.buildNormalBinder();

    private static String basePath = "/Users/zeng.zhao/Desktop/fans/竞品/面部护理/";

    private static void start() throws Exception {
        Settings settings = Settings.builder().put("cluster.name", "lavector-es-cluster").build();
        Client client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        BoolQueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("site", "weibo"))
                .must(QueryBuilders.nestedQuery("tags", QueryBuilders.termsQuery("tags.name", keywords), ScoreMode.Max))
                .must(QueryBuilders.rangeQuery("tC").gte(simpleDateFormat.format(startTime)).lte(simpleDateFormat.format(endTime)));


        SearchResponse searchResponse = client.prepareSearch(INDEX)
                .setSize(100)
                .setScroll(new TimeValue(2 * 60 * 1000))
                .setQuery(queryBuilder)
                .setTypes("message")
                .get();


        Set<String> userIds = new HashSet<>();

        String head = "userId,displayName,province,url";
        scroll(client, searchResponse, hit -> {
            JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
            JSONObject user = jsonObject.getJSONObject("user");
            String city = user.getString("city");
            if (userIds.contains(user.getString("userId"))) {
                return;
            }
            userIds.add(user.getString("userId"));
//            if (count <= 124500) {
//                return;
//            }
            String content = user.getString("userId") + ","
                    + user.getString("displayName") + ","
                    + user.getString("province") + ","
                    + "https://weibo.com/u/" + user.getString("userId");
            if (!new File(basePath + city + ".csv").exists()) {
                WriteFile.write(head, basePath + city + ".csv");
            }
            WriteFile.write(content, basePath + city + ".csv");
        });

        System.out.println(userIds.size());
    }

    private static Integer count = 0;

    private static void scroll(Client client, SearchResponse searchResponse, HitConsumer hitConsumer) {
        do {
            for (SearchHit hit : searchResponse.getHits().getHits()) {
//                if (count > 124500) {
                    hitConsumer.process(hit);
//                }
                count++;
                if (count % 1000 == 0) {
                    System.out.println("process length. " + count);
                }
            }
            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId()).setScroll(new TimeValue(2 * 60 * 1000)).get();
        } while (searchResponse.getHits().getHits().length > 0);

    }

    public static void main(String[] args) {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(count);
    }
}
