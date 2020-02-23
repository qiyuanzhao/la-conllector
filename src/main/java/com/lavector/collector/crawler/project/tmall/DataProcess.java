package com.lavector.collector.crawler.project.tmall;

import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.util.JsonMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created on 2018/11/19.
 *
 * @author zeng.zhao
 */
public class DataProcess {

    private static class TmallObject{
        String product;
        String brand;
        String category;
        String url;
        String content;
        String time;

        public void setProduct(String product) {
            this.product = product;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public static void main(String[] args) throws IOException {
        start();
    }

    private static void start() throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/tmall1.json"));
        File file = new File("/Users/zeng.zhao/Desktop/tmall.csv");
        String line;
        while((line = reader.readLine()) != null) {
            TmallObject tmallObject = JsonMapper.buildNormalBinder().fromJson(line, TmallObject.class);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    String header = "category,brand,product,url,content,time\n";
                    org.apache.commons.io.FileUtils.writeStringToFile(file, header, Charset.forName("utf-8"), true);
                }
            } else {
                String content = tmallObject.category + "," + tmallObject.brand + "," + tmallObject.product + ","
                        + tmallObject.url + "," + tmallObject.content + "," + tmallObject.time + "\n";
                org.apache.commons.io.FileUtils.writeStringToFile(file, content, Charset.forName("utf-8"), true);
            }
        }
    }
}
