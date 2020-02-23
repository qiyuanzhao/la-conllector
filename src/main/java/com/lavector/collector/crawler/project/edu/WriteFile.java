package com.lavector.collector.crawler.project.edu;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created on 2017/11/15.
 *
 * @author zeng.zhao
 */
public class WriteFile {

//    private static final ExecutorService service = Executors.newFixedThreadPool(3);

    public static synchronized void write(String content, String path) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
            bw.write(content);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (service.getActiveCount() == 0) {
//            service.shutdown();
//        }
    }

}
