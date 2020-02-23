package com.lavector.collector.crawler.nonce.tieba;

import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.util.JsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on 25/06/2018.
 *
 * @author zeng.zhao
 */
public class TieBaDataProcessor {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/tieba(芬达).json"));
        String line = reader.readLine();
        List<NonceMessage> messages = new ArrayList<>();
        JsonMapper mapper = JsonMapper.buildNormalBinder();
        while (line != null) {
            NonceMessage message = mapper.fromJson(line, NonceMessage.class);
            messages.add(message);
            line = reader.readLine();
        }

        Set<NonceMessage> postMessages = messages.stream()
                .filter(message -> message.getType().equals("Post"))
                .filter(message -> message.getContent().contains("芬达"))
                .collect(Collectors.toSet());

        postMessages
                .forEach(post ->
                        messages.stream()
                                .filter(message -> !message.getType().equals(post.getType()))
                                .filter(message -> message.getQmid().equals(post.getMid()))
                                .forEach(message -> {
                                    String s = mapper.toJson(message);
                                    FileUtils.write(s, "/Users/zeng.zhao/Desktop/tieba_filter(芬达).json");
                                })
                );

        postMessages.forEach(post -> {
            String s = mapper.toJson(post);
            FileUtils.write(s, "/Users/zeng.zhao/Desktop/tieba_filter(芬达).json");

        });
    }

}
