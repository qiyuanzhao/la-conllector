package com.lavector.collector.crawler.nonce.weibo;

import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 25/07/2018.
 *
 * @author zeng.zhao
 */
public class WeiBoDataProcess {

    private static void messageWrite() throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/weibo_message.json"));
        String line = reader.readLine();
        JsonMapper mapper = JsonMapper.buildNormalBinder();
        while (line != null) {
            NonceMessage message = null;
            try {
                message = mapper.fromJson(line, NonceMessage.class);
            } catch (Exception e) {

            }
            if (message == null) {
                line = reader.readLine();
                continue;
            }
//            String mid = MidToUrlConverter.Mid2Uid(message.getMid());
//            String url = "https://weibo.com/" + message.getUserId() + "/" + mid;
//
//            String content = message.getOwner() + "," + url + "," + (message.getContent().replaceAll(",", "，")) + "," + message.getTime()
//                    + "," + message.getSocialCount().getLikes() + "," + message.getSocialCount().getComments() + ","
//                    + message.getSocialCount().getReposts();

//            try {
//                File file = new File("/Users/zeng.zhao/Desktop/weibo-comment.csv");
//                CharSink charSink = com.google.common.io.Files.asCharSink(file,
//                        StandardCharsets.UTF_8, FileWriteMode.APPEND);
//                if (!file.exists()) {
//                    charSink.write("username,url,content,time,like,comment,repost\n");
//                } else {
//                    charSink.write(content + "\n");
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            line = reader.readLine();
        }
    }

    private static void commentProcess() throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/weibo_comment-2.json"));
        String line = reader.readLine();
        JsonMapper mapper = JsonMapper.buildNormalBinder();
        HashSet<String> mids = new HashSet<>();
        File file = new File("/Users/zeng.zhao/Desktop/weibo-comment-2.csv");
        CharSink charSink = com.google.common.io.Files.asCharSink(file,
                StandardCharsets.UTF_8, FileWriteMode.APPEND);
        charSink.write("content,time,like,username\n");
        while (line != null) {
            NonceMessage message = null;
            try {
                message = mapper.fromJson(line, NonceMessage.class);
            } catch (Exception e) {

            }
            boolean contains = mids.contains(message.getMid());
            if (!contains) {
                mids.add(message.getMid());
            }
            if (message == null || contains || StringUtils.isBlank(message.getContent())) {
                line = reader.readLine();
                continue;
            }
            String content = (message.getContent().replaceAll(",", "，").replace(" ：", "")) + "," + message.getTime()
                    + "," + message.getSocialCount().getLikes() + "," + message.getCommentUserName();


            charSink.write(content + "\n");
            line = reader.readLine();
        }
    }

    public static void main(String[] args) throws IOException {
//        messageWrite();
        commentProcess();
    }
}
