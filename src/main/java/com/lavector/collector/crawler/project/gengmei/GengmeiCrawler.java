package com.lavector.collector.crawler.project.gengmei;


import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.gengmei.entity.question.Question;
import com.lavector.collector.crawler.project.gengmei.pipepine.ArrProductPipeline;
import com.lavector.collector.crawler.project.gengmei.pipepine.PersonPopeline;
import com.lavector.collector.crawler.project.gengmei.pipepine.QuestionPipeline;
import com.lavector.collector.crawler.project.gengmei.pipepine.UserDiaryPipeline;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GengmeiCrawler {


    public static void main(String[] args) {
        //https://www.soyoung.com/searchNew/product?keyword=%E8%89%BE%E8%8E%89%E8%96%87
//        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/gengmei/person.txt", ",");

        Spider spider = Spider.create(new GengmeiProcesser(new CrawlerInfo()))
                .addPipeline(new UserDiaryPipeline("G:/text/gengmei/data/diary"));
//        for (SkuData skuData : skuDatas) {
//            String url = skuData.getUrl();

            Request request = new Request("https://www.soyoung.com/searchNew/diary?keyword=%E8%89%BE%E8%8E%89%E8%96%87&cityId=&page_size=200&_json=1&page=1");
//            request.putExtra("skuData", skuData);
            spider.addRequest(request);
//        }
        spider.setDownloader(new SougouWeixinDownloader());
        spider.thread(10);
        spider.start();




    }

    public static String handlerUrl(String url) {
        String substring = "";
        try{
            substring = url.substring(url.indexOf(".com") + 6);
        }catch (Exception e){

        }

        return substring;
    }


    public static void process(String id,String url) {
        try {
            File file = getFile("G:/text/gengmei", "userID" + ".txt");
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
            FileInputStream fis = new FileInputStream(file);
            int available = fis.available();
            if (available == 0) {
                List<String> headWords = new ArrayList<>();
                headWords.add("用户ID");
                headWords.add("url");
                String title = "";
                for (String str : headWords) {
                    title += str + ",";
                }
                printWriter.println(title);
            }
            printWriter.print(id + ",");
            printWriter.println(url + ",");
            printWriter.close();
        } catch (IOException var10) {
        }
    }


    //检查是否存在
    private static File getFile(String path, String name) {
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
