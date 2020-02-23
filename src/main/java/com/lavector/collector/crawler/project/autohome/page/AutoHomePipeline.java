package com.lavector.collector.crawler.project.autohome.page;

import com.lavector.collector.crawler.project.babyTreeUserInfo.page.TaoBaoPo;
import com.lavector.collector.entity.readData.SkuData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.*;
import java.util.List;


public class AutoHomePipeline extends FilePipeline {

    private Logger logger = LoggerFactory.getLogger(AutoHomePipeline.class);

    public AutoHomePipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            AutoHomeEntity autoHomeEntity = (AutoHomeEntity) resultItems.getRequest().getExtra("autoHomeEntity");
                File file = getFile(path, "autoHome.txt");
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
                    logger.info("开始写入文件");
                    printWriter.print(skuData.getBrand() + ",");
                    printWriter.print(autoHomeEntity.getUrl() + ",");
                    printWriter.print(autoHomeEntity.getUserName() + ",");
                    printWriter.print(autoHomeEntity.getDate() + ",");
                    printWriter.print(autoHomeEntity.getTitle() + ",");
                    printWriter.print(autoHomeEntity.getReplys() + ",");
                    printWriter.print(autoHomeEntity.getView() + ",");
                    printWriter.print(autoHomeEntity.getTitleUrl() + ",");
                    printWriter.println(autoHomeEntity.getContent() + ",");


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
