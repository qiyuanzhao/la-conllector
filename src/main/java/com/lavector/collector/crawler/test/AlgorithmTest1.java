package com.lavector.collector.crawler.test;

/**
 * Created on 2017/12/12.
 *
 * @author zeng.zhao
 */
public class AlgorithmTest1 {

    private static int rank(int key, int[] nums) {
        sort(nums);
        int start = 0, end = nums.length, mid = 0;
        while(mid >= 0) {
            mid = (start + end) / 2;
            if (nums[mid] == key) {
                break;
            }
            if (mid == 0) {
                mid = -1;
                break;
            }

            if (nums[mid] < key) {
                start = mid;
            } else if (nums[mid] > key) {
                end = mid;
            }
        }
        return mid;
    }

    private static void sort(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                if (nums[i] > nums[j]) {
                    int temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
//        int[] nums = {6, 8, 3, 9, 5, 1, 7, 2, 4, 10};
//        int index = rank(10, nums);
//        System.out.println(index);
        int a = 10;
        int b = a + (a << 1);
        System.out.println(b);
    }
}
