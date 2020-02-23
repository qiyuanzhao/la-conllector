package com.lavector.collector.crawler.project.jd;

import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created on 26/06/2018.
 *
 * @author zeng.zhao
 */
public class JDDataExportExcel {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/tieba_filter(芬达).json"));
        JsonMapper mapper = JsonMapper.buildNormalBinder();
        String line = reader.readLine();
        String head = "userId,content,mid,time,type,url";
        FileUtils.write(head, "/Users/zeng.zhao/Desktop/tieba(芬达).csv");
        while (line != null) {
            NonceMessage message = mapper.fromJson(line, NonceMessage.class);
            if (StringUtils.isBlank(message.getContent())) {
                line = reader.readLine();
                continue;
            }
            StringBuilder stringBuilder = new StringBuilder();
            String content = message.getContent()
                    .replaceAll(",", "，")
                    .replaceAll("\n", "");
            stringBuilder.append(message.getUserId())
                    .append(",").append(content)
                    .append(",").append(message.getMid())
                    .append(",").append(message.getTime())
                    .append(",").append(message.getType())
                    .append(",").append(message.getUrl());
//                    .append(",").append(message.getSocialCount().getLikes())
//                    .append(",").append(message.getSocialCount().getComments())
//                    .append(",").append(message.getSocialCount().getCollects())
//                    .append(",").append(message.getType());
//            message.getParameter().forEach((k, v) ->
//                    stringBuilder.append(k.replaceAll(",", "，"))
//                            .append("=")
//                            .append(v.replaceAll(",", "，"))
//                            .append("，")
//            );
            FileUtils.write(stringBuilder.toString(), "/Users/zeng.zhao/Desktop/tieba(芬达).csv");
            line = reader.readLine();
        }
    }
}
