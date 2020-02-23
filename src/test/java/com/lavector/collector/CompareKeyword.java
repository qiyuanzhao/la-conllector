package com.lavector.collector;

import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CompareKeyword {


    public static void main(String[] args) {
        List<SkuData> keywords = ReadTextUtils.getSkuData("G:/text/newWeibo/search.txt", ",");
        List<SkuData> crawlerKeywords = ReadTextUtils.getSkuData("G:/text/newWeibo/qqq.txt", ",");

        for (SkuData crawlerSku : crawlerKeywords) {
            String crawlerKeyword = crawlerSku.getBrand();
            if (StringUtils.isEmpty(crawlerKeyword)){
                continue;
            }
            for (SkuData skuData : keywords) {
                String keyword = skuData.getBrand();
                if (StringUtils.isEmpty(keyword)){
                    continue;
                }
                if (keyword.equalsIgnoreCase(crawlerKeyword)) {
                    keywords.remove(skuData);
                    break;
                }
            }
        }
        keywords.forEach(k->{
            String brand = k.getBrand();
            System.out.println(brand);
        });
    }


}
