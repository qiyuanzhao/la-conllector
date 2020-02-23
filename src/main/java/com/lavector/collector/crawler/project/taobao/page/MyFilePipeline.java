package com.lavector.collector.crawler.project.taobao.page;


import com.lavector.collector.crawler.project.babyTree.page.BabyPrams;
import com.lavector.collector.crawler.project.babyTreeUserInfo.page.TaoBaoPo;
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
            List<TaoBaoPo> taoBaoPoList = (List<TaoBaoPo>) resultItems.getRequest().getExtra("taoBaoPoList");
            if (taoBaoPoList != null && taoBaoPoList.size() > 0) {
                File file = getFile(path,  "taobao.csv");
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
//            if (babyPrams.getContent()==null){
//                printWriter.print(babyPrams.getBabyAge() + ",");
//                printWriter.print(babyPrams.getAddress() + ",");
//                printWriter.print(babyPrams.getCollectionNumber() + ",");
//                printWriter.println(babyPrams.getContent() + ",");
//            }else {
                for (TaoBaoPo taoBaoPo : taoBaoPoList) {
                    logger.info("开始写入文件");
//                    printWriter.print(skuData.getBrand() + ",");
//                    printWriter.print(taoBaoPo.getUserName() + ",");
//                    printWriter.print(taoBaoPo.getCommentTime() + ",");
//                    printWriter.print(taoBaoPo.getDetailUrl() + ",");
//                    printWriter.print(taoBaoPo.getComment() + ",");


                    printWriter.print(skuData.getBrand() + ",");
                    printWriter.print(taoBaoPo.getTitle() + ",");
                    printWriter.print(taoBaoPo.getDetailUrl() + ",");
                    printWriter.print(taoBaoPo.nick + ",");
                    printWriter.print(taoBaoPo.getViewSales() + ",");
                    printWriter.print(taoBaoPo.getCommentCount() + ",");
                    printWriter.print(taoBaoPo.getViewPrice() + ",");
                    printWriter.println(taoBaoPo.type + ",");
                    logger.info("完成一条数据的写入");
                }

//            }

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

