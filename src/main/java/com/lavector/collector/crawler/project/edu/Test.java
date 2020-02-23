package com.lavector.collector.crawler.project.edu;

import org.apache.http.client.fluent.Request;

/**
 * Created on 2017/11/17.
 *
 * @author zeng.zhao
 */
public class Test {

    public int function (int i, int res) {
        if (i == 1) {
            return res;
        }
        return function(i - 1, res) * i;
    }

    private static void reverseInt (int input) {
        int length = String.valueOf(input).length();
        int result = 0;
        if (input > 0) {
            while (length > 0) {
                if (length == 1) {
                    result += input;
                    break;
                }
                result = result + input % 10 * (int) Math.pow(10, length - 1);
                input = input / 10;
                length = length - 1;
            }
        } else if (input < 0) {
            length--;
            input = Math.abs(input);
            while (length > 0) {
                if (length == 1) {
                    result += input;
                    break;
                }
                result = result + input % 10 * (int) Math.pow(10, length - 1);
                length = length - 1;
                input = input / 10;
            }
            result = result - result * 2;
        } else {
            result = input;
        }
        System.out.println(result);
    }

    public static void main (String[] args) throws Exception {
        reverseInt(-123);
//        System.out.println(123 % (int) Math.pow(10, 2));
    }
}
