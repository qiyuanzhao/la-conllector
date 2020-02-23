package com.lavector.collector.crawler.test;

/**
 * Created on 13/07/2018.
 *
 * @author zeng.zhao
 */
public class FindMaxNum {

    public static void main(String[] args) {
        findMaxNumber2();
    }

    private static void bubbleSort(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - 1; j++) {
                if (nums[j] < nums[j + 1]) {
                    nums[j] = nums[j] + nums[j + 1];
                    nums[j + 1] = nums[j] - nums[j + 1];
                    nums[j] = nums[j] - nums[j + 1];
                 }
            }
        }
    }

    private static void findMaxNumber1() {
        int[] nums = {5, 1, 3, 9 ,4, 6, 7, 2, 8};
        int k = 3;
        bubbleSort(nums);
        System.out.println(nums[k - 1]);
    }

    private static void findMaxNumber2() {
        int[] nums = {5, 1, 3, 9 ,4, 6, 7, 2, 8};
        int k = 3;
        int[] result = new int[k];
        System.arraycopy(nums, 0, result, 0, result.length);

        bubbleSort(result);

        for (int i =  k - 1; i < nums.length; i++) {
            find(result, nums[i], k - 1);
        }
        System.out.println(result[result.length - 1]);
    }

    private static void find(int[] result, int target, int index) {
        if (target < result[index]) {
            return;
        }

        int k = 0;
        for (int i = 0; i < result.length; i++) {
            if (target > result[i]) {
                k = i;
                break;
            }
        }

        if (k == result.length - 1) {
            result[k] = target;
        } else {
            System.arraycopy(result, k, result, k + 1, result.length - 1 - k);
            result[k] = target;
        }
    }
}
