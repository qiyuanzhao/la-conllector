package com.lavector.collector.crawler.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 16/07/2018.
 *
 * @author zeng.zhao
 */
public class Main {

    public static void main(String[] args) {
//        System.out.println(Main.name);
        char a = '是';
        System.out.println(a);
    }
    public static String name;
    static {
        name = getName();
    }
    private static String getName() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add("a" + i);
        }
        datas = datas.stream()
                .filter(s -> s.equalsIgnoreCase("a9"))
                .collect(Collectors.toList());
        return datas.get(0);
    }
}
