package com.lavector.collector.crawler.project.meituan.com.pipeline;


import com.lavector.collector.crawler.project.babyTreeUserInfo.page.MyFilePipeline;
import com.lavector.collector.crawler.project.meituan.MeituanCrawler;
import com.lavector.collector.crawler.project.meituan.com.entity.MeituanProduct;
import com.lavector.collector.crawler.project.meituan.com.entity.MeituanShopEntity;
import com.lavector.collector.crawler.project.meituan.com.entity.ShopEntity;
import com.lavector.collector.crawler.project.weibo.weiboStar.page.Person;
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
import java.util.List;

public class MeituanSearchPipeline extends FilePipeline {

    private Logger logger = LoggerFactory.getLogger(MyFilePipeline.class);

    public MeituanSearchPipeline(String path) {
        super(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path;
        try {
            //文件名字
            SkuData skuData = (SkuData) resultItems.getRequest().getExtra("skuData");
            List<MeituanShopEntity> shopEntities = (List<MeituanShopEntity>) resultItems.getRequest().getExtra("shopEntities");
            File file = getFile(path, "shopEntities.txt");
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

            for (MeituanShopEntity shopEntity : shopEntities) {
                logger.info("开始写入文件");

                printWriter.print(skuData.getBrand() + ",");

                printWriter.print(shopEntity.id + ",");

                printWriter.print(shopEntity.name + ",");

                printWriter.print(shopEntity.monthSalesTip + ",");

                printWriter.print(shopEntity.wmPoiScore + ",");

                if (shopEntity.averagePriceTip != null) {
                    printWriter.print(shopEntity.averagePriceTip + ",");
                } else {
                    printWriter.print(" " + ",");
                }

                printWriter.println("https://waimai.meituan.com/comment/" + shopEntity.id);

                logger.info("完成一条数据的写入");

                List<MeituanProduct> productList = shopEntity.productList;

                printWriterProduct(path, productList, shopEntity.id);
            }
            printWriter.close();
        } catch (IOException var10) {
            this.logger.warn("write file error", var10);
        }
    }


    private void printWriterProduct(String path, List<MeituanProduct> productList, String id) throws IOException {

        File file = getFile(path, "shopProduct.txt");
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));

        FileInputStream fis = new FileInputStream(file);
        int available = fis.available();
        if (available == 0) {
            String[] headWords = {"店铺id", "产品id", "产品名称", "点赞数", "价格"};
            String title = "";
            for (String str : headWords) {
                title += str + ",";
            }
            printWriter.println(title);
        }

        for (MeituanProduct meituanProduct : productList) {
            logger.info("产品开始写入文件");
            printWriter.print(id + ",");

            printWriter.print(meituanProduct.productSpuId + ",");

            printWriter.print(meituanProduct.productName + ",");

            printWriter.print(meituanProduct.praiseContent + ",");

            printWriter.println(meituanProduct.price + ",");

            logger.info("完成一条产品数据的写入");
        }

        printWriter.close();
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
