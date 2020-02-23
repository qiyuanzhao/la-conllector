package com.lavector.collector.crawler.project.article;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created on 2017/12/22.
 *
 * @author zeng.zhao
 */
public class Brand {

    public static Map<String, Integer> brands = Maps.newHashMap();

    static {
        brands.put("GUCCI", 7);
        brands.put("ZARA", 8);
        brands.put("欧舒丹", 10);
        brands.put("SEPHORA", 11);
        brands.put("韩时烤肉", 13);
        brands.put("西堤厚牛排", 14);
        brands.put("火宴山", 15);
        brands.put("和府捞面", 16);
        brands.put("金吉鸟健身", 17);
        brands.put("innisfree", 18);
        brands.put("外婆家", 19);
        brands.put("帝豪斯健身", 20);
    }
}
