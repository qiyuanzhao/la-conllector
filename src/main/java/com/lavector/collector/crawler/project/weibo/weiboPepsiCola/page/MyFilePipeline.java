package com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
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
            List<Comment> comments = (List<Comment>) resultItems.getRequest().getExtra("comments");
            if (!CollectionUtils.isEmpty(comments)) {
                File file = getFile(path, "评论4.txt");
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));


                FileInputStream fis = new FileInputStream(file);
                int available = fis.available();
                if (available == 0) {
                    String title = "用户,用户链接,时间,评论内容";
                    printWriter.println(title);
                }
                for (Comment comment : comments) {
                    logger.info("开始写入文件");
                    if (!StringUtils.isEmpty(comment.getUserName())) {
                        printWriter.print(comment.getUserName() + ",");
                    } else {
                        printWriter.print(" " + ",");
                    }
                    if (!StringUtils.isEmpty(comment.getUserName())) {
                        printWriter.print(comment.getUserUrl() + ",");
                    } else {
                        printWriter.print(" " + ",");
                    }
                    if (!StringUtils.isEmpty(comment.getUserName())) {
                        printWriter.print(comment.getTime() + ",");
                    } else {
                        printWriter.print(" " + ",");
                    }
                    if (!StringUtils.isEmpty(comment.getUserName())) {
                        printWriter.println(comment.getComment() + ",");
                    } else {
                        printWriter.println(" " + ",");
                    }
                    logger.info("完成一条数据的写入");
                }

                logger.info("*********完成" + comments.size() + "条数据的写入*********");
//                printWriter.print(project.getKeyWord() + ",");
//                printWriter.print(project.getType() + ",");
//                printWriter.print(project.getUserName() + ",");
//                printWriter.print(project.getDate() + ",");
//                printWriter.print(project.getConent() + ",");
//                printWriter.print(project.getReport() + ",");
//                printWriter.print(project.getCommentNumber() + ",");
//                printWriter.print(project.getLike() + ",");
//                printWriter.print(project.getUrl() + ",");
//                if (project.getComment() != null && project.getComment().size() > 0) {
//                    String commentString = getCommentString(project.getComment());
//                    printWriter.println(commentString + ",");
//                } else {
//                    printWriter.println("");
//                }

                printWriter.close();
            }
        } catch (IOException var10) {
            this.logger.warn("write file error", var10);
        }
    }

    private String getCommentString(List<String> comment) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : comment) {
            stringBuilder.append(str + "{{{{{}}}}");
        }
        return stringBuilder.toString();
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
