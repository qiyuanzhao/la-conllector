package com.lavector.collector.crawler.project.tmall;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 2018/1/18.
 *
 * @author zeng.zhao
 */
public class FileComment {

    public static void main(String[] args) throws Exception {
        Set<String> set = new HashSet<>();
        File file = new File("/Users/zeng.zhao/Desktop/tmall_comment.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            set.add(line);
            line = reader.readLine();
        }

        File writeFile = new File("/Users/zeng.zhao/Desktop/tmall_comment_final.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile));
        for (String ln : set) {
            writer.write(ln);
            writer.newLine();
        }

        writer.close();
        reader.close();
    }

}
