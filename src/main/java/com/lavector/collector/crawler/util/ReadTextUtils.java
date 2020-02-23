package com.lavector.collector.crawler.util;


import com.lavector.collector.entity.readData.SkuData;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadTextUtils {

    public static List<String> getText(String fileUrl) {
        File csv = new File(fileUrl);
        List<String> dataList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String line = "";
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }


    public static List<List<String>> getText(String fileUrl, String separatorChars) {
        List<List<String>> dataList = new ArrayList<>();
        List<String> skuData = getText(fileUrl);
        skuData.forEach(str -> {
            String[] strings = StringUtils.split(str, separatorChars);
            List<String> asList = Arrays.asList(strings);
            dataList.add(asList);
        });
        return dataList;
    }


    public static List<SkuData> getSkuData(String fileUrl, String separatorChars) {
        List<SkuData> skuDataList = new ArrayList<>();
        List<List<String>> listList = getText(fileUrl, separatorChars);
        for (List<String> list : listList) {
            SkuData skuData = new SkuData();
            if (list.size() > 0) {
                skuData.setBrand(list.get(0));
            }
            if (list.size() > 1) {
                skuData.setUrl(list.get(1));
            }
            if (list.size() > 2) {
//                String[] split = list.get(2).split("，");
//                List<String> asList = Arrays.asList(split);
                skuData.setWords(list.get(2));
            }
            if (list.size() > 3) {
                skuData.str4 = list.get(3);
            }
            if (list.size() > 4) {
                skuData.str5 = list.get(4);
            }
            if (list.size() > 5) {
                skuData.str6 = list.get(5);
            }
            skuDataList.add(skuData);
        }
        return skuDataList;
    }


}
