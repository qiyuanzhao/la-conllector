package com.lavector.collector.crawler.project.babyTreeUserInfo.page;

import com.lavector.collector.crawler.project.babyTree.page.BabyPrams;
import com.lavector.collector.entity.readData.SkuData;
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
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            BabyPrams babyPrams = (BabyPrams) resultItems.getRequest().getExtra("babyPrams");
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
//            if (babyPrams.getContent()==null){
//                printWriter.print(babyPrams.getBabyAge() + ",");
//                printWriter.print(babyPrams.getAddress() + ",");
//                printWriter.print(babyPrams.getCollectionNumber() + ",");
//                printWriter.println(babyPrams.getContent() + ",");
//            }else {
            printWriter.print(skuData.getBrand() + ",");
            printWriter.print(skuData.getUrl() + ",");
            printWriter.print(babyPrams.getBabyAge() + ",");
            printWriter.print(babyPrams.getAddress() + ",");
            printWriter.print(babyPrams.getCollectionNumber() + ",");
            printWriter.print(babyPrams.getContent() + ",");
            printWriter.print(babyPrams.getConmentUser().getName() + ",");
            printWriter.print(babyPrams.getConmentUser().getAge() + ",");
            printWriter.print(babyPrams.getConmentUser().getAddress() + ",");
            printWriter.print(babyPrams.getConmentUser().getDate() + ",");
            printWriter.println(babyPrams.getConmentUser().getContent() + ",");
//            }
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
