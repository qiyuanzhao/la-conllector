package com.lavector.collector.crawler.test;

import java.util.Arrays;

/**
 * Created on 2018/11/6.
 *
 * @author zeng.zhao
 */
public class ArrayTest {

    public static void main(String[] args) {

        int range = 3;
        int[] nums = new int[]{0, -1, -2, 3 ,1, -100, -1, 0, 2, -5, 1, -2};
        int cycleNum = nums.length - range + 1;
        int[] target = new int[range];
        int min = 0;
        for (int i = 0; i < cycleNum; i++) {
            int x = 0;
            for (int j = i; j < i + range; j++) {
                x += nums[j];
            }
            if (x < min) {
                min = x;
                int index = 0;
                for (int j = i; j < i + range; j++) {
                    target[index] = nums[j];
                    index++;
                }
            }
        }
        System.out.println(Arrays.toString(target));
    }
}
