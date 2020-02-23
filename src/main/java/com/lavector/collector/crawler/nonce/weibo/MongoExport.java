package com.lavector.collector.crawler.nonce.weibo;

import com.google.common.collect.Lists;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.lavector.collector.crawler.util.JsonMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.QueryOperators;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.*;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 14/05/2018.
 *
 * @author zeng.zhao
 */
public class MongoExport {

    private static JsonMapper mapper = JsonMapper.buildNormalBinder();

    //cachecache、cache、@cache、@cachecache
    public static void main(String[] args) {
        Bson condition = Filters.and(Filters.eq("site", "weibo"), Filters.in("tags.name", "@cachecache", "@cache", "cachecache", "cache"));
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("lavector");
        MongoCollection<Document> message = database.getCollection("message");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("site", "weibo");
//        basicDBObject.append("tags.name", new BasicDBObject(QueryOperators.IN, new String[]{"@cachecache", "@cache", "cachecache", "cache"}));
        FindIterable<Document> documents = message.find(basicDBObject);
        CharSink charSink = Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/mongo_export.json").toFile(),
                StandardCharsets.UTF_8, FileWriteMode.APPEND);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        MongoCursor<Document> mongoCursor = documents.iterator();
        while (mongoCursor.hasNext()) {
            Document document = mongoCursor.next();
            String s = mapper.toJson(document);
            try {
                charSink.write(s + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(atomicInteger.incrementAndGet());
        }
    }
}
