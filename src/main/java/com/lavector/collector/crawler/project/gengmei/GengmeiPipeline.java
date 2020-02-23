package com.lavector.collector.crawler.project.gengmei;


import com.lavector.collector.crawler.project.babyTreeUserInfo.page.MyFilePipeline;
import com.lavector.collector.crawler.project.weibo.weiboStar.page.Person;
import com.lavector.collector.entity.readData.SkuData;
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
import java.util.List;

public class GengmeiPipeline extends FilePipeline {


    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public GengmeiPipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            Person person = (Person) resultItems.getRequest().getExtra("person");
            File file = getFile(path, skuData.getBrand() + ".txt");
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
//            printWriter.println(person.getUrl() + ",");
            if(person.getSex()!=null){
                printWriter.print(person.getSex() + ",");
            }else {
                printWriter.print("null");
            }

//            printWriter.print(person.getAge() + ",");
//            printWriter.print(person.getName() + ",");
            if (person.getCity()!=null){
                printWriter.println(person.getCity());
            }else {
                printWriter.println("null");
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
