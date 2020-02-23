package com.lavector.collector.crawler.project.gengmei.pipepine;


import com.lavector.collector.crawler.project.babyTreeUserInfo.page.TaoBaoPo;
import com.lavector.collector.crawler.project.gengmei.entity.ArrProduct;
import com.lavector.collector.crawler.project.taobao.page.MyFilePipeline;
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

public class ArrProductPipeline extends FilePipeline {
    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public ArrProductPipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            List<ArrProduct> arrProducts = (List<ArrProduct>) resultItems.getRequest().getExtra("arrProducts");
            if (arrProducts != null && arrProducts.size() > 0) {
                File file = getFile(path, "润制" + ".txt");
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
                FileInputStream fis = new FileInputStream(file);
                int available = fis.available();
                if (available == 0) {
                    List<String> headWords = new ArrayList<>();
                    headWords.add("产品名称");
                    headWords.add("产品价格");
                    headWords.add("医院");
                    headWords.add("品牌");
                    headWords.add("医生");
                    headWords.add("预约数");
                    headWords.add("日记数");
                    headWords.add("链接");

                    String title = "";
                    for (String str : headWords) {
                        title += str + ",";
                    }
                    printWriter.println(title);
                }
                for (ArrProduct arrProduct : arrProducts) {
                    logger.info("开始写入文件");
                    printWriter.print(arrProduct.getTitle() + ",");
                    printWriter.print(arrProduct.getPrice_online() + ",");
                    printWriter.print(arrProduct.getHospital_name() + ",");
                    printWriter.print(arrProduct.getDoctor_name() + ",");
                    printWriter.print(arrProduct.getOrder_auto_cnt() + ",");
                    printWriter.print(arrProduct.getCalendar_cnt() + ",");
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
