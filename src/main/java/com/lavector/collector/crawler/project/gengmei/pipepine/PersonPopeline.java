package com.lavector.collector.crawler.project.gengmei.pipepine;


import com.lavector.collector.crawler.project.gengmei.entity.ArrProduct;
import com.lavector.collector.crawler.project.gengmei.entity.Person;
import com.lavector.collector.crawler.project.taobao.page.MyFilePipeline;
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
import java.util.ArrayList;
import java.util.List;

public class PersonPopeline extends FilePipeline {

    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public PersonPopeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            Person person = (Person) resultItems.getRequest().getExtra("person");
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            if (person != null) {
                File file = getFile(path, skuData.getBrand() + ".txt");
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
                FileInputStream fis = new FileInputStream(file);
                int available = fis.available();
                if (available == 0) {
                    List<String> headWords = new ArrayList<>();
                    headWords.add("性别");
                    headWords.add("年龄");
                    headWords.add("地区");
                    headWords.add("氧气");
                    headWords.add("经验值");
                    headWords.add("url");
                    String title = "";
                    for (String str : headWords) {
                        title += str + ",";
                    }
                    printWriter.println(title);
                }
                logger.info("开始写入文件");
                printWriter.print(person.getSex() + ",");
                printWriter.print(person.getAge() + ",");
                printWriter.print(person.getRegion() + ",");
                printWriter.print(person.getOxygenContent() + ",");
                printWriter.print(person.getEmpiricalValue() + ",");
                printWriter.println(person.getUrl() + ",");
                logger.info("完成一条数据的写入");
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
