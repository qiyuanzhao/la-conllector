package com.lavector.collector.crawler.project.weibo.weixinTitle.page;

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
            Project project = (Project) resultItems.getRequest().getExtra("project");
            File file = getFile(path, project.getKeyWord() + ".txt");
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));

            logger.info("开始写入文件");
            FileInputStream fis = new FileInputStream(file);
            int available = fis.available();
            if (available == 0) {
                String title = "关键词,时间,链接,内容";
                printWriter.println(title);
            }
            printWriter.print(project.getKeyWord() + ",");
            printWriter.print(project.getDate() + ",");
            printWriter.print(project.getUrl() + ",");
            printWriter.println(project.getTitle() + ",");

            logger.info("完成一条数据的写入");
            printWriter.close();
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
