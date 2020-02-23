package com.lavector.collector.crawler.nonce.weibo;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * Created on 21/05/2018.
 *
 * @author zeng.zhao
 */
public class Test111 {

    public static void main(String[] args) throws Exception {
        test22();
    }

    private static void test22() throws Exception {
        java.nio.file.Files.walkFileTree(Paths.get("/Users/zeng.zhao/Desktop/weibo_message.json"), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return null;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                return null;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return null;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return null;
            }
        });
    }

    private static void test11() throws IOException {
        JsonMapper mapper = JsonMapper.buildNormalBinder();
        BufferedReader reader = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/cache/weibo/weibo_message_4.json"));
        String line = reader.readLine();
        int i = 0;
        while (line != null) {
            if (StringUtils.isNotBlank(line)) {
                i++;
//                List<String> types = JsonPath.read(line, "$.[*].type");
//                for (String type : types) {
//                    if (type.equals("SIGN_IN")) {
//                    }
//                }
            }
            line = reader.readLine();
        }
        reader.close();
        System.out.println(i);
    }
}
