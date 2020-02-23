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

import java.io.*;
import java.util.List;

public class PersonPipeline extends FilePipeline {


    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public PersonPipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            Person person = (Person) resultItems.getRequest().getExtra("person");
            File file = getFile(path, "汉堡王用户信息.csv");
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "GBK"));

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

            printWriter.print("https://weibo.com/u/" + skuData.getBrand() + "?refer_flag=1028035010_,");
//            printWriter.println(person.getUrl() + ",");
            if (person.getSex() != null) {
                printWriter.print(person.getSex() + ",");
            } else {
                printWriter.print(" ");
            }

            printWriter.print(person.getAge() + ",");
            printWriter.print(person.getName() + ",");
            if (person.getCity() != null) {
                printWriter.print(person.getCity() + ",");
            } else {
                printWriter.print(" " + ",");
            }
            if (person.introduction != null) {
                printWriter.print(person.introduction + ",");
            } else {
                printWriter.print(" " + ",");
            }

            if (!StringUtils.isEmpty(person.getClearfix())) {
                printWriter.println(person.getClearfix());
            } else {
                printWriter.println(" ");
            }

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
