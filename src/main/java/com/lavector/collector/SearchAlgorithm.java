package com.lavector.collector;

/**
 * Created on 21/10/2017.
 *
 * @author seveniu
 */
public class SearchAlgorithm {
    private static final int[] array = new int[]{1, 2, 3, 4, 5};

    /**
     * 递归实现
     *
     * @param array  目标数组
     * @param low    起始位
     * @param high   截止位
     * @param target 目标数
     */
    public static int binarySearch(int[] array, int low, int high, int target) {
        if (low > high) {
            return -1;
        }
        int middle = (low + high) / 2;
        int middleNum = array[middle];
        if (middleNum == target) {
            return middle;
        }
        if (middleNum > target) {
            return binarySearch(array, low, middle - 1, target);
        } else {
            return binarySearch(array, middle + 1, high, target);
        }
    }

    /**
     * 非递归实现
     *
     * @param array  目标数组
     * @param low    起始位
     * @param high   截止位
     * @param target 目标数
     */
    public static int binarySearch2(int[] array, int low, int high, int target) {
        while (true) {
            int middle = (low + high) / 2;
            if (low > high) {
                return -1;
            }
            int middleNum = array[middle];
            if (middleNum == target) {
                return middle;
            }
            if (middleNum > target) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(binarySearch(array, 0, array.length - 1, 6));
        System.out.println(binarySearch2(array, 0, array.length - 1, 3));
    }

}
