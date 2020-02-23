package com.lavector.collector.crawler.util;

/**
 * Created on 2018/3/2.
 *
 * @author zeng.zhao
 */
public class ExcelTest {

    public static void main(String[] args) throws Exception {
//        ExcelPlus excelPlus = new ExcelPlus();
//        ExcelResult<Map> objectExcelResult = excelPlus
//                .read(Paths.get("/Users/zeng.zhao/Desktop/获取的公众号品牌-已分工.xlsx").toFile(), Map.class)
//                .asResult();
//
//        List<Map> rows = objectExcelResult.rows();
//
//        System.out.println(rows);


        String mid = "4213138691222829";
        String result = "";
        for (int i = mid.length() - 4; i > -4; i -= 4) {
            int offset = i < 0 ? 0 : i;
//            int len = i < 0 ? 0 : mid.length() / 4;
//            if (offset == 0 && len == 0) {
//                break;
//            }

            String str = intToEnode62(Integer.parseInt(mid.substring(offset, mid.length())));
            if (mid.length() >= 4) {
                mid = mid.substring(0, mid.length() - 4);
            }
            result += str;
        }
        System.out.println(result);
    }

    private static String[] str62keys = {"0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
            "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z"};

    public static String intToEnode62(Integer int10) {
        String s62 = "";
        int r = 0;
        while (int10 != 0) {
            r = int10 % 62;
            s62 = str62keys[r] + s62;
            int10 = (int) Math.floor(int10 / 62.0);
        }
        return s62;
    }

    //62进制转成10进制
    public static String str62toInt(String str62) {
        long i64 = 0;
        for (int i = 0; i < str62.length(); i++) {
            long Vi = (long) Math.pow(62, (str62.length() - i - 1));
            String t = str62.substring(i, i + 1);

            i64 += Vi * findindex(t);
        }
        // System.out.println(i64);
        return Long.toString(i64);
    }

    public static int findindex(String t) {
        int index = 0;
        for (int i = 0; i < str62keys.length; i++) {
            if (str62keys[i].equals(t)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
