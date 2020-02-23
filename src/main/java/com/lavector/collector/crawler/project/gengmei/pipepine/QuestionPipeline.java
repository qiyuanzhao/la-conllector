package com.lavector.collector.crawler.project.gengmei.pipepine;


import com.lavector.collector.crawler.project.gengmei.entity.ArrProduct;
import com.lavector.collector.crawler.project.gengmei.entity.question.Question;
import com.lavector.collector.crawler.project.taobao.page.MyFilePipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class QuestionPipeline extends FilePipeline {


    private Logger logger = LoggerFactory.getLogger(QuestionPipeline.class);

    public QuestionPipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            List<Question> questionList = (List<Question>) resultItems.getRequest().getExtra("questionList");
            if (questionList != null && questionList.size() > 0) {
                File file = getFile(path, "玻尿酸" + ".txt");
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
                FileInputStream fis = new FileInputStream(file);
                int available = fis.available();
                if (available == 0) {
                    List<String> headWords = new ArrayList<>();
                    headWords.add("问题");
                    headWords.add("时间");
                    headWords.add("查阅数");
                    headWords.add("评论数");
                    headWords.add("点赞");
                    headWords.add("回答");
                    headWords.add("链接");

                    String title = "";
                    for (String str : headWords) {
                        title += str + ",";
                    }
                    printWriter.println(title);
                }
                for (Question arrProduct : questionList) {
                    logger.info("开始写入文件");
                    printWriter.print(arrProduct.getQuestion_info().getQuestion_content() + ",");
                    printWriter.print(arrProduct.getAnswer_info().getCreate_date() + ",");
                    printWriter.print(arrProduct.getAnswer_info().getView_cnt() + ",");
                    printWriter.print(arrProduct.getAnswer_info().getComment_cnt() + ",");
                    printWriter.print(arrProduct.getAnswer_info().getUp_cnt() + ",");
                    printWriter.print(arrProduct.getAnswer_info().getContent() + ",");
                    printWriter.println(arrProduct.getQuestion_info().getUrl() + ",");
                    logger.info("完成一条数据的写入");
                }
                printWriter.close();
            }
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


}
