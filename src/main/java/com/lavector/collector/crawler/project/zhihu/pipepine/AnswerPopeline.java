package com.lavector.collector.crawler.project.zhihu.pipepine;


import com.lavector.collector.crawler.project.babyTreeUserInfo.page.MyFilePipeline;
import com.lavector.collector.crawler.project.zhihu.entity.Answer;
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

public class AnswerPopeline extends FilePipeline {
    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public AnswerPopeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            if (skuData==null){
                return;
            }
            List<Answer> answerList = (List<Answer>) resultItems.getRequest().getExtra("answerList");
            File file = getFile(path, "answer.csv");
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
            answerList.forEach(answer -> {
                logger.info("开始写入文件");
                printWriter.print(answer.getQuestion().id + ","); //问题id
                printWriter.print(answer.getId() + ","); //回答id
                printWriter.print(answer.getAuthor().getId() + ",");//回答人id
                printWriter.print(answer.getAuthor().getName() + ",");//回答人姓名

                String sex = handleGender(answer.getAuthor().getGender());
                printWriter.print(sex + ",");//回答人性别

                String text = answer.getAuthor().getHeadline();
                String string = heanleString(text);
                printWriter.print(string + ",");//回答人描述

                Long time = answer.getCreated_time();
                String date = handleDate(time * 1000);
                printWriter.print(date + ",");//回答时间

                printWriter.print(answer.getComment_count() + ",");//回答评论数
                printWriter.print(answer.getVoteup_count() + ",");//回答赞同数

                String content = answer.getContent();
                String newContent = heanleString(content);
                printWriter.print(newContent + ",");//回答内容

                printWriter.println(answer.getUrl() + ",");//链接
                logger.info("完成一条数据的写入");

            });

            printWriter.close();
        } catch (IOException var10) {
            this.logger.warn("write file error", var10);
        }
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
        Date date = new Date(create + 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    private String heanleString(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        return text.replace(",", "，").replace(" ", "").replace("\n", "");
    }

}
