package com.lavector.collector.crawler.project.zhihu.pipepine;


import com.lavector.collector.crawler.project.babyTreeUserInfo.page.MyFilePipeline;
import com.lavector.collector.crawler.project.weibo.weiboStar.page.Person;
import com.lavector.collector.crawler.project.zhihu.entity.Question;
import com.lavector.collector.entity.readData.SkuData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
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
import java.util.List;

public class ZhihuPipeline extends FilePipeline {


    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public ZhihuPipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            List<Question> questionList = (List<Question>) resultItems.getRequest().getExtra("questionList");
            List<Question> articleList = (List<Question>) resultItems.getRequest().getExtra("articleList");
            File file = getFile(path, "search.csv");
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

            if (!CollectionUtils.isEmpty(questionList)) {
                questionList.forEach(question -> {
                    logger.info("开始写入文件");
                    printWriter.print(skuData.getBrand() + ",");
                    printWriter.print(question.id + ",");

                    String replace = question.getName();
                    String newReplace = heanleString(replace);
                    printWriter.print(newReplace + ",");

                    printWriter.print("" + ",");

                    printWriter.println("https://www.zhihu.com/question/" + question.id + ",");
                    logger.info("完成一条数据的写入");

                });
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(articleList)) {
                articleList.forEach(question -> {
                    logger.info("开始写入文件");
                    printWriter.print(skuData.getBrand() + ",");
                    printWriter.print(question.id + ",");

                    printWriter.print(heanleString(question.getTitle()) + ",");

                    printWriter.print(heanleString(question.getContent()) + ",");

                    printWriter.println("https://zhuanlan.zhihu.com/p/" + question.id + ",");
                    logger.info("完成一条数据的写入");

                });
            }


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

    private String heanleString(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        return text.replace(",", "，").replace(" ", "").replace("\n", " ").replace("\r\n", " ");
    }

}
