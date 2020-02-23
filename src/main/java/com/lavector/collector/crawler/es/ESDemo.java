package com.lavector.collector.crawler.es;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.lavector.collector.crawler.base.Message;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 03/07/2018.
 *
 * @author zeng.zhao
 */
public class ESDemo {

    private static final String INDEX = "lavector-main2";

    public static void main(String[] args) throws Exception {
        search();
    }

    private static void aggregate() throws Exception {
        Settings settings = Settings.builder().put("cluster.name", "lavector-es-cluster").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        SearchResponse response = client.prepareSearch()
                .addAggregation(AggregationBuilders.range("tC"))
                .get();
        System.out.println(response);
    }

    private static void indices() throws Exception {
        Settings settings = Settings.builder().put("cluster.name", "lavector-es-cluster").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        IndicesAdminClient indices = client.admin().indices();
        GetSettingsResponse response = indices.prepareGetSettings().get();
        for (ObjectObjectCursor<String, Settings> cursor : response.getIndexToSettings()) {
            String index = cursor.key;
            System.out.println("index : " + index);
        }
    }

    private static void search() throws Exception {
        Settings settings = Settings.builder().put("cluster.name", "lavector-es-cluster").build();
        Client client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("site", "weibo"))
                .must(QueryBuilders.nestedQuery("tags", QueryBuilders.termsQuery("tags.name", "美年达"), ScoreMode.Max));

        SearchResponse searchResponse = client.prepareSearch(INDEX)
                .setScroll(new TimeValue(15 * 1000))
                .setQuery(queryBuilder)
                .setTypes("message")
                .get();

        JsonMapper mapper = JsonMapper.buildNormalBinder();

        AtomicInteger atomicInteger = new AtomicInteger(0);
        scroll(client, searchResponse, 100, hit -> {
            String messageStr = hit.getSourceAsString();
        });
    }

    private static void scroll(Client client, SearchResponse searchResponse, int maxCount, HitConsumer hitConsumer) {
        int count = 0;
        do {
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                hitConsumer.process(hit);
                count++;
                if (count >= maxCount) {
                    return;
                }
            }
            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId()).setScroll(new TimeValue(15 * 1000)).get();
        } while (searchResponse.getHits().getHits().length > 0);

    }
}
