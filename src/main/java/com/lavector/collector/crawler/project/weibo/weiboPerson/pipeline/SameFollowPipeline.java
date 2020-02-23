package com.lavector.collector.crawler.project.weibo.weiboPerson.pipeline;


import com.lavector.collector.crawler.project.babyTreeUserInfo.page.MyFilePipeline;
import com.lavector.collector.crawler.project.weibo.weiboStar.page.Person;
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
import java.util.List;

public class SameFollowPipeline extends FilePipeline {


    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public SameFollowPipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            List<Person> personList = (List<Person>) resultItems.getRequest().getExtra("personList");
            File file = getFile(path, "person.txt");
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));

            logger.info("开始写入文件");
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

            printWriter.print(skuData.getUrl() + ",");

            printWriter.print(skuData.getWords());

            StringBuilder stringBuilder = new StringBuilder();
            for (Person person : personList) {
                String name = person.getName();
                stringBuilder.append(name).append("，");
            }
            printWriter.println(stringBuilder);

            logger.info("完成一条数据的写入");
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
}
