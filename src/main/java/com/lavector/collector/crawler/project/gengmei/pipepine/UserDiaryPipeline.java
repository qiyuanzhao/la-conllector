package com.lavector.collector.crawler.project.gengmei.pipepine;


import com.lavector.collector.crawler.project.gengmei.entity.ArrProduct;
import com.lavector.collector.crawler.project.gengmei.entity.UserDiary;
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

public class UserDiaryPipeline extends FilePipeline {


    private Logger logger = LoggerFactory.getLogger(UserDiaryPipeline.class);

    public UserDiaryPipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            List<UserDiary> arrProducts = (List<UserDiary>) resultItems.getRequest().getExtra("userDiaries");
//            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            if (arrProducts != null && arrProducts.size() > 0) {
                File file = getFile(path, /*skuData.getBrand()*/"艾莉薇" + ".txt");
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
                FileInputStream fis = new FileInputStream(file);
                int available = fis.available();
                if (available == 0) {
                    List<String> headWords = new ArrayList<>();
//                    headWords.add("用户id");
//                    headWords.add("用户名");
//                    headWords.add("项目名");
//                    headWords.add("医院");
//                    headWords.add("医生");
//                    headWords.add("产品");
//                    headWords.add("价格");
//                    headWords.add("时间");
                    headWords.add("浏览量");
                    headWords.add("点赞数");
                    headWords.add("评论数");
                    headWords.add("日记ID");
                    headWords.add("链接");

                    String title = "";
                    for (String str : headWords) {
                        title += str + ",";
                    }
                    printWriter.println(title);
                }
                for (UserDiary arrProduct : arrProducts) {
                    logger.info("开始写入文件");
//                    printWriter.print(arrProduct.getUid() + ",");
//                    printWriter.print(arrProduct.getUser_name() + ",");
//                    printWriter.print(arrProduct.getHot_product().getItem_name() + ",");
//                    printWriter.print(arrProduct.getHot_product().getHospital_name() + ",");
//                    printWriter.print(arrProduct.getHot_product().getDoctor_name() + ",");
//                    printWriter.print(arrProduct.getHot_product().getPid() + ",");
//                    printWriter.print(arrProduct.getHot_product().getPrice() + ",");
//                    printWriter.print(arrProduct.getCreate_date() + ",");
                    printWriter.print(arrProduct.getHot_product().getView_cnt() + ",");
                    printWriter.print(arrProduct.getHot_product().getFavor_cnt() + ",");
                    printWriter.print(arrProduct.getHot_product().getComment_cnt() + ",");
                    printWriter.print(arrProduct.getGroup_id() + ",");
                    printWriter.println(arrProduct.getUrl() + ",");
                    logger.info("完成一条数据的写入");
                }
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
