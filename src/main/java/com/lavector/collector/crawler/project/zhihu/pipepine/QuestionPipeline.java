package com.lavector.collector.crawler.project.zhihu.pipepine;


import com.lavector.collector.crawler.project.babyTreeUserInfo.page.MyFilePipeline;
import com.lavector.collector.crawler.project.zhihu.entity.Question;
import com.lavector.collector.entity.readData.SkuData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuestionPipeline extends FilePipeline {


    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public QuestionPipeline(String path) {
        super(path);
    }


    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            Question question = (Question) resultItems.getRequest().getExtra("question");
            boolean falg = (boolean) resultItems.getRequest().getExtra("falg");


            if (question != null && !falg) {

                File file = getFile(path, "question.csv");
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "GBK"));


                FileInputStream fis = new FileInputStream(file);
                int available = fis.available();
                if (available == 0) {
                    List<String> headWords = skuData.getHeadWords();
                    String title = "";
                    for (String str : headWords) {
                        title += str + ",";
                    }
                    printWriter.println(title);
                }
                logger.info("开始写入文件");
                printWriter.print(skuData.getBrand() + ",");
                printWriter.print(skuData.getUrl() + ","); //id

                String replace = question.getTitle();
                String string = heanleString(replace);
                printWriter.print(string + ","); //问题

                Long created = question.getCreated();
                String date = handleDate(created);
                printWriter.print(date + ",");//时间

                printWriter.print(question.getFollowerCount() + ",");//关注者
                printWriter.print(question.getVisitCount() + ",");//被浏览

                String detail = question.getDetail();
                String newDetail = heanleString(detail);
                printWriter.print(newDetail + ","); //问题描述


                printWriter.print(question.getAuthor().getId() + ","); //用户id
                printWriter.print(question.getAuthor().getName() + ","); //用户姓名

                Integer gender = question.getAuthor().getGender();
                String sex = handleGender(gender);
                printWriter.print(sex + ","); //用户性别

                String headline = question.getAuthor().getHeadline();
                String newHeadline = heanleString(headline);
                printWriter.print(newHeadline + ","); //用户简介

                printWriter.println("https://www.zhihu.com/question/" + skuData.getUrl() + ",");
                logger.info("完成一条数据的写入");
                printWriter.close();
            } else {
                File file = getFile(path, "失败的url.txt");
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));


                FileInputStream fis = new FileInputStream(file);
                int available = fis.available();
                if (available == 0) {
                    List<String> headWords = skuData.getHeadWords();
                    String title = "";
                    for (String str : headWords) {
                        title += str + ",";
                    }
                    printWriter.println(title);
                }
                printWriter.print(skuData.getBrand() + ",");
                printWriter.println(skuData.getUrl());

            }
        } catch (IOException var10) {
            this.logger.warn("write file error", var10);
        }

    }

    private String handleGender(Integer gender) {
        switch (gender) {
            case 1:
                return "男";
            case 0:
                return "女";
            default:
                return "无";
        }
    }

    private String handleDate(Long create) {
        Date date = new Date(create * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }


    //检查是否存在
    private File getFile(String path, String name) {
        File file = new File(path, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    private String heanleString(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        return text.replace(",", "，").replace(" ", "").replace("\n", "");
    }


}
