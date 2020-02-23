package com.lavector.collector.crawler.test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 04/07/2018.
 *
 * @author zeng.zhao
 */
public class Test1 {

    public static void main(String[] args) {
//        System.out.println(f(2));
        printOut(87);
    }

    private static int f(int x) {
        if (x == 1) {
            return 1;
        }

        return 2 * f(x - 1) + x * x;
    }

    private static void printOut(int n) {
        if (n >= 10) {
            printOut(n / 10);
        }
        System.out.println(n % 10);
    }

}
