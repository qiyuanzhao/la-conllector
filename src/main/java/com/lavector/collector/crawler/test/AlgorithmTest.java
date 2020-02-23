package com.lavector.collector.crawler.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created on 2017/12/11.
 *
 * @author zeng.zhao
 */
public class AlgorithmTest {

    private static int gcd(int a, int b) {
        if (b == 0) return a;
        int r = a % b;
        return gcd(b, r);
    }

    private static int rank(int key, int[] arrays) {
        return rank(key, arrays, 0, arrays.length);
    }

    private static int rank(int key, int[] arrays, int start, int end) {
        int mid = (start + end) / 2;
        if (arrays[mid] == key) {
            return mid;
        } else if (mid == 0 || mid == arrays.length - 1) {
            return -1;
        }

        if (arrays[mid] < key) {
            start = mid;
            return rank(key, arrays, start, end);
        } else if (arrays[mid] > key) {
            end = mid;
            return rank(key, arrays, start, end);
        }
        return -1;
    }

    private static void sort(int[] arrays) {
        for (int i = 0; i < arrays.length - 1; i++) {
            for (int j = 0; j < arrays.length - 1; j++) {
                if (arrays[j] > arrays[j + 1]) {
                    int temp = arrays[j];
                    arrays[j] = arrays[j + 1];
                    arrays[j + 1] = temp;
                }
            }
        }
    }

    private static int find(int key, int[] arrays) {
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] == key) {
                return i;
            }
        }
        return -1;
    }

    private static int[] randomIntArr(int count, int range) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < count; i++) {
            set.add((int) (Math.random() * range));
        }
        int[] nums = new int[set.size()];
        Iterator<Integer> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            nums[i] = iterator.next();
            i++;
        }
        return nums;
    }

    public static void main(String[] args) {
//        int[] nums = randomIntArr(10, 100);
//        sort(nums);
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 47};
        long start = System.currentTimeMillis();
        int find = rank(5, nums);
        long end = System.currentTimeMillis() - start;
        System.out.println("time : " + end + "ms.");
//        sort(nums);
//        int rank = rank(47, nums);
        System.out.println(find);
    }
}
