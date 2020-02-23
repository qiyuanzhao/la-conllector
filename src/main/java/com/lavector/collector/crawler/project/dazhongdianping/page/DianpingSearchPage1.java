package com.lavector.collector.crawler.project.dazhongdianping.page;

import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by qyz on 2019/8/7.
 */
public class DianpingSearchPage1 implements PageParse {

    private Logger logger = LoggerFactory.getLogger(DianpingSearchPage1.class);

    private static Map<String, String> rankMap = new HashMap<>();

    private static Map<String, String> numMap = new HashMap<>();

    static {
        rankMap.put("sml-rank-stars sml-str50", "5.0");
        rankMap.put("sml-rank-stars sml-str40", "4.0");
        rankMap.put("sml-rank-stars sml-str30", "3.0");
        rankMap.put("sml-rank-stars sml-str20", "2.0");
        rankMap.put("sml-rank-stars sml-str10", "1.0");

        rankMap.put("mid-rank-stars mid-str15", "1.5");
        rankMap.put("mid-rank-stars mid-str25", "2.5");
        rankMap.put("mid-rank-stars mid-str35", "3.5");
        rankMap.put("mid-rank-stars mid-str45", "4.5");
        rankMap.put("mid-rank-stars mid-str5", "0.5");
        rankMap.put("mid-rank-stars mid-str05", "0.5");

        numMap.put("<svgmtsi class=\"shopNum\">&#xf55e;</svgmtsi>", "2");
        numMap.put("<svgmtsi class=\"shopNum\">&#xf3ae;</svgmtsi>", "3");
        numMap.put("<svgmtsi class=\"shopNum\">&#xf7d5;</svgmtsi>", "9");
        numMap.put("<svgmtsi class=\"shopNum\">&#xeb9a;</svgmtsi>", "8");
        numMap.put("<svgmtsi class=\"shopNum\">&#xe92d;</svgmtsi>", "6");
        numMap.put("<svgmtsi class=\"shopNum\">&#xe6d2;</svgmtsi>", "5");
        numMap.put("<svgmtsi class=\"shopNum\">&#xf2eb;</svgmtsi>", "4");
        numMap.put("<svgmtsi class=\"shopNum\">&#xf307;</svgmtsi>", "7");
        numMap.put("<svgmtsi class=\"shopNum\">&#xe85e;</svgmtsi>", "0");
    }


    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.dianping.com");
    }

    @Override
    public Result parse(Page page) {

        String keyword = page.getRequest().getExtra("keyword").toString();


        Selectable htmlStr = page.getHtml().regex("<body>(.*)<body>");

        Html html = page.getHtml();

        List<Selectable> allShopList = html.xpath("//div[@class='section Fix J-shop-search']//div[@class='content-wrap']//div[@id='shop-all-list']//div[@class='txt']").nodes();

        List<DianpingShop> shopList = new ArrayList<>();
        for (Selectable node : allShopList) {
            String shopUrl = node.xpath("//div[@class='tit']/a[@data-click-name='shop_title_click']/@href").get();
            String shopId = node.xpath("//div[@class='tit']/a[@data-click-name='shop_title_click']/@data-shopid").get();
            String shopName = node.xpath("//div[@class='tit']/a[@data-click-name='shop_title_click']/@title").get();

            String shopPower = node.xpath("//div[@class='txt']/div[@class='comment']/span/@title").get();
//            String power = rankMap.get(shopPower);

//            String shopNum = node.xpath("//div[@class='txt']/div[@class='comment']/a/text()").get();
//            Set<String> keySet = numMap.keySet();
//            for (String key : keySet) {
//                if (shopNum.contains(key)) {
//                    shopNum = shopNum.replace(key, numMap.get(key));
//                }
//            }

//            List<Selectable> nodes = node.xpath("//div[@class='txt']/span[@class='comment-list']/span").nodes();
//
//            String kouweiStr = nodes.get(0).get();
//            String huanjingStr = nodes.get(1).get();
//            String fuwuStr = nodes.get(2).get();
//            for (String key : keySet) {
//                if (kouweiStr.contains(key)) {
//                    kouweiStr = kouweiStr.replace(key, numMap.get(key));
//                }
//                if (huanjingStr.contains(key)) {
//                    huanjingStr = huanjingStr.replace(key, numMap.get(key));
//                }
//                if (fuwuStr.contains(key)) {
//                    fuwuStr = fuwuStr.replace(key, numMap.get(key));
//                }
//            }

            DianpingShop dianpingShop = new DianpingShop();
            dianpingShop.url = shopUrl;
            dianpingShop.shopId = shopId;
            dianpingShop.shopName = shopName;
            dianpingShop.power = shopPower;
//            dianpingShop.comment = shopNum;
//            dianpingShop.kouwei = kouweiStr;
//            dianpingShop.fuwu = fuwuStr;
//            dianpingShop.huanjing = huanjingStr;
            shopList.add(dianpingShop);
        }

        String path = "G:/text/dianping/data/" + "search" + ".csv";
        try {
            if (!new File(path).exists()) {
                boolean newFile = new File(path).createNewFile();
                if (newFile) {
                    //评论量,口味,服务,环境,
                    String header = "关键词,店铺id,店铺名称,星级,链接\n";
                    FileUtils.writeStringToFile(new File(path), header, Charset.forName("GBK"), true);
                }
            }
            for (DianpingShop dianpingShop : shopList) {
                String writeContent = keyword + "," + dianpingShop.shopId + "," + dianpingShop.shopName + "," +
                        dianpingShop.power + "," + /*dianpingShop.comment + "," + dianpingShop.kouwei + "," + dianpingShop.fuwu + "," +
                        dianpingShop.huanjing + "," +*/ dianpingShop.url + "\n";
                FileUtils.writeStringToFile(new File(path), writeContent, Charset.forName("GBK"), true);
                logger.info("写入一条数据。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        page.setSkip(true);
        return null;
    }

    @Override
    public String pageName() {

        return null;
    }
}
