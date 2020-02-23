package com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage;

import com.lavector.collector.crawler.nonce.NonceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
            File file = getFile(path,  "娇兰.txt");
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));

            logger.info("开始写入文件");
            FileInputStream fis = new FileInputStream(file);
            int available = fis.available();
            if (available == 0) {
                String title = "品牌,转发数,评论数,点赞数,时间,内容";
                printWriter.println(title);
            }
            List<NonceMessage> messageAll = resultItems.get("messageAll");
            Map<String,Object> map = getNumCount(messageAll);

            printWriter.print(skuData.getBrand() + ",");
            printWriter.print(map.get("repost") + ",");
            printWriter.print(map.get("comment") + ",");
            printWriter.print(map.get("like") + ",");
            printWriter.print(map.get("time") + ",");
            printWriter.println(map.get("content") + ",");
            logger.info("完成一条数据的写入");
            printWriter.close();
        } catch (IOException var10) {
            this.logger.warn("write file error", var10);
        }
    }

    private Map<String,Object> getNumCount(List<NonceMessage> messageAll) {
        Map<String,Object> map = new HashMap<>();
        Integer comments = 0;
        Integer likes = 0;
        Integer reposts = 0;

        Integer sum = 0;
        for (NonceMessage nonceMessage : messageAll){
            NonceMessage.SocialCount socialCount = nonceMessage.getSocialCount();
            Integer comment = socialCount.getComments();
            Integer like = socialCount.getLikes();
            Integer repost = socialCount.getReposts();
            int newSum = comment + like + repost;
            if (sum<newSum){
                sum = newSum;
                String content = nonceMessage.getContent();
                String time = nonceMessage.getTime();
                map.put("comment",comment);
                map.put("like",like);
                map.put("repost",repost);
                map.put("content",content);
                map.put("time",time);
            }
//            comments+=comment;
//            likes+=like;
//            reposts+=repost;
        }
//        map.put("likes",likes);
//        map.put("reposts",reposts);
//        map.put("comments",comments);

        return map;
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
