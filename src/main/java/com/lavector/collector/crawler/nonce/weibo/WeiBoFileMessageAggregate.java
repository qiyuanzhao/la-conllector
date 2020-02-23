package com.lavector.collector.crawler.nonce.weibo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 2018/10/9.
 *
 * @author zeng.zhao
 */
public class WeiBoFileMessageAggregate {

    private static void start() throws IOException {
        long[] totalCount = new long[1];
        int[] fileCount = new int[1];
        Set<String> set = new HashSet<>();
        Files.walkFileTree(Paths.get("/Users/zeng.zhao/Desktop/follow"), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    Files.lines(file, Charset.forName("iso-8859-1")).forEach(line -> {
                        String[] split = line.split(",");
                        totalCount[0]++;
                        set.add(split[split.length - 3]);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fileCount[0]++;
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
        System.out.println(totalCount[0]);
        System.out.println(fileCount[0]);
    }

    public static void main(String[] args) throws IOException {
        start();
    }
}
