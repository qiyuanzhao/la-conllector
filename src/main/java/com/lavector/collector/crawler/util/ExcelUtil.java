package com.lavector.collector.crawler.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created on 2018/11/18.
 *
 * @author zeng.zhao
 */
public class ExcelUtil {

    public static List<Map<String, String>> readExcelBySheel(File file, String sheetName) throws IOException {
        List<Map<String, String>> list = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheetAt = workbook.getSheet(sheetName);
        XSSFRow titleRow = sheetAt.getRow(sheetAt.getFirstRowNum());
        String[] title = new String[titleRow.getLastCellNum()];
        for (int i = titleRow.getFirstCellNum(); i < titleRow.getLastCellNum(); i++) {
            title[i] = titleRow.getCell(i).getStringCellValue();
        }

        for (int i = sheetAt.getFirstRowNum() + 1; i < sheetAt.getLastRowNum() + 1; i++) {
            XSSFRow row = sheetAt.getRow(i);
            if (row == null) {
                continue;
            }
            Map<String, String> map = new HashMap<>();
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                map.put(title[j], row.getCell(j).getStringCellValue());
            }
            list.add(map);
        }

        inputStream.close();
        return list;
    }

    public static List<Map<String, List<Map<String, Object>>>> readExcel(InputStream inputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        List<Map<String, List<Map<String, Object>>>> lists = new ArrayList<>();
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Map<String, List<Map<String, Object>>> sheetMap = new HashMap<>();
            List<Map<String, Object>> rowList = new LinkedList<>();
            Sheet sheet = sheetIterator.next();
            Row titleRow = sheet.getRow(sheet.getFirstRowNum());
            String[] title = new String[titleRow.getLastCellNum()];
            for (int i = titleRow.getFirstCellNum(); i < titleRow.getLastCellNum(); i++) {
                title[i] = titleRow.getCell(i).getStringCellValue();
            }

            for (int i = sheet.getFirstRowNum() + 1; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                if (row.getCell(0) == null) {
                    break;
                }
                Map<String, Object> map = new HashMap<>();
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    try {
                        if (row.getCell(j) == null) {
                            map.put(title[j], "");
                        } else {
                            map.put(title[j], row.getCell(j));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                rowList.add(map);//一行
            }
            sheetMap.put(sheet.getSheetName(), rowList);
            lists.add(sheetMap);
        }

        inputStream.close();

        return lists;
    }
    public static List<Map<String, List<Map<String, Object>>>> readExcel(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        return readExcel(inputStream);
    }


    public static void main(String[] args) throws IOException {
        File file = new File("/Users/zeng.zhao/Desktop/试点广场优惠券清单(1)(3).xlsx");
        List<Map<String, List<Map<String, Object>>>> maps = readExcel(file);
        System.out.println(maps);
    }

}
