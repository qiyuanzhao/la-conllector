package com.lavector.collector.crawler.nonce;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created on 16/05/2018.
 *
 * @author zeng.zhao
 */
public class FileUtils {

    public static void write(String content, String path) {
        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
//            Files.asCharSink(Paths.get(path).toFile(), StandardCharsets.UTF_8, FileWriteMode.APPEND)
//                    .write(content + "\n");

            BufferedWriter writer = java.nio.file.Files.newBufferedWriter(Paths.get(path), Charset.forName("gbk"), StandardOpenOption.APPEND);
            writer.write(content);
            writer.newLine();
            writer.flush();
            writer.close();


            lock.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
