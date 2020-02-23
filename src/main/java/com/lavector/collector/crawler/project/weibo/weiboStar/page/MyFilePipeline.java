package com.lavector.collector.crawler.project.weibo.weiboStar.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.io.*;
import java.util.List;


public class MyFilePipeline extends FilePipeline {

    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public MyFilePipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            List<Person> personList = (List<Person>) resultItems.getRequest().getExtra("personList");
            File file = getFile(path, "comment.txt");
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));

//            if (person.getCity() != null || person.getSex() != null || person.getAge() != null) {
            for (Person person: personList){

                logger.info("开始写入文件");
                FileInputStream fis = new FileInputStream(file);
                int available = fis.available();
                if (available == 0) {
//                    String title = "明星,名称,时间,年龄,性别,城市,评论";
                    String title = "微博,url,评论";
                    printWriter.println(title);
                }
                if (person.getStarName() != null) {
                    printWriter.print(person.getStarName() + ",");
                }
                else {
                    printWriter.print("null" + ",");
                }
                printWriter.print(person.getUrl());
                /*
                if (person.getName() != null) {
                    printWriter.print(person.getName() + ",");
                } else {
                    printWriter.print("null" + ",");
                }
                if (person.getTime()!=null){
                    printWriter.print(person.getTime() + ",");
                }else {
                    printWriter.print("null" + ",");
                }
                if (person.getAge() != null) {
                    printWriter.print(person.getAge() + ",");
                } else {
                    printWriter.print("null" + ",");
                }
                if (person.getSex() != null) {
                    printWriter.print(person.getSex() + ",");
                } else {
                    printWriter.print("null" + ",");
                }
                if (person.getCity() != null) {
                    printWriter.print(person.getCity() + ",");
                } else {
                    printWriter.print("null" + ",");
                }*/
                if (person.getComment() != null) {
                    printWriter.println(person.getComment() + ",");
                } else {
                    printWriter.println("null" + ",");
                }
                logger.info("完成一条数据的写入");
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


}
