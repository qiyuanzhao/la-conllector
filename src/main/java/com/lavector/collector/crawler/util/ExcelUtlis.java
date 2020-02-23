package com.lavector.collector.crawler.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.*;
import cn.hutool.poi.excel.ExcelUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qyz on 2019/9/11.
 */
public class ExcelUtlis {


    public static void main(String[] args) {

//        ExcelReader reader = ExcelUtil.getReader(FileUtil.file("G:\\text\\tmall\\data\\Herbalife_天猫抓取结果_9.11_戚元昭.xlsx"));
//
//        Map<String, String> headerAlias = reader.getHeaderAlias();
//
//        List<Map<String, Object>> maps = reader.readAll();
//
//        System.out.println("...");


//        List<String> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd");
//        List<String> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1");
//        List<String> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2");
//        List<String> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3");

//        Map<String, Object> row1 = new LinkedHashMap<>();
//        row1.put("姓名", "张三");
//        row1.put("年龄", 23);
//        row1.put("成绩", 88.32);
//        row1.put("是否合格", true);
//        row1.put("考试日期", DateUtil.date());
//        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1);

//        List<String> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4");
//
//        List<String> strings = CollUtil.newArrayList("关键词","分类","商品","交易数","评论数","价格","链接");
//
//        List<List<String>> list = new ArrayList();
//
//        list.add(strings);
//        list.add(row5);
//
//        ArrayList<List<String>> lists = CollUtil.newArrayList(list);
//        List<List<String>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);
        File file = FileUtil.file("G:\\text\\tmall\\data\\Herbalife.xlsx");

        ExcelWriter writer = ExcelUtil.getWriter(file);
        List<String> strings = CollUtil.newArrayList("关键词", "分类", "商品", "交易数", "评论数", "价格", "链接");
        writer.writeHeadRow(strings);


        for (int i = 0; i < 4; i++) {
            List<String> row5 = CollUtil.newArrayList("aa7", "bb7", "cc7", "dd7");
//            writer.writeRow(row5);
            writer.writeCellValue(i, 11, row5.get(i));
            writer.flush();
        }

        writer.close();


    }


}
