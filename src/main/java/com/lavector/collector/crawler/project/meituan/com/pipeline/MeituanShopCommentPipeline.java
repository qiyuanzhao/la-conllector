package com.lavector.collector.crawler.project.meituan.com.pipeline;


import com.lavector.collector.crawler.project.babyTreeUserInfo.page.MyFilePipeline;
import com.lavector.collector.crawler.project.meituan.com.entity.MeituanComment;
import com.lavector.collector.crawler.project.meituan.com.entity.ShopComment;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MeituanShopCommentPipeline extends FilePipeline {

    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public MeituanShopCommentPipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            List<MeituanComment> shopComments = (List<MeituanComment>) resultItems.getRequest().getExtra("meituanComments");
            File file = getFile(path, "shopComments.txt");
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

            for (MeituanComment shopComment : shopComments) {
                logger.info("开始写入文件");

                printWriter.print(skuData.getBrand() + ",");

                printWriter.print(shopComment.wmComment.user_id + ",");

                if (shopComment.wmComment.username != null) {
                    printWriter.print(shopComment.wmComment.username + ",");
                } else {
                    printWriter.print(" ");
                }

                printWriter.print(shopComment.wmComment.id + ",");

                printWriter.print(handleContent(shopComment.wmComment.comment) + ",");

                printWriter.println(shopComment.commentTime + ",");


                logger.info("完成一条数据的写入");
            }


            printWriter.close();
        } catch (IOException var10) {
            this.logger.warn("write file error", var10);
        }
    }

    private String handleTime(String commentTime) {
        String formatDate = "";
        if (!StringUtils.isEmpty(commentTime)){
            long timeLong = Long.parseLong(commentTime);
            Date date = new Date(timeLong);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatDate = simpleDateFormat.format(date);
        }
        return formatDate;
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

    private String handleContent(String content) {
        String replace = content.replace("\n", "").replace(",", "，");
        return replace;
    }


}
