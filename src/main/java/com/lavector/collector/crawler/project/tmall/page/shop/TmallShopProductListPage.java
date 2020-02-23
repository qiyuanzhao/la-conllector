package com.lavector.collector.crawler.project.tmall.page.shop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.base.RequestExtraKey;
import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.project.tmall.TmallConfig;
import com.lavector.collector.crawler.project.tmall.page.TmallPageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.crawler.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/11/23.
 *
 * @author zeng.zhao
 */
public class TmallShopProductListPage implements PageParse {

    private  static Logger logger = LoggerFactory.getLogger(TmallShopProductListPage.class);

    private String path = "G:\\text\\tmall\\data\\";

    @Override
    public boolean handleUrl(String url) {
//        return request.getExtra(RequestExtraKey.KEY_PAGE_NAME).toString().equals("list");
        return url.contains("asynSearch.htm");
    }


    @Override
    public Result parse(Page page) {
//        TmallConfig.TmallShopConfig tmallShopConfig = (TmallConfig.TmallShopConfig) page.getRequest().getExtra("config");
        String brand = page.getRequest().getExtra("brand").toString();
        String keyword = page.getRequest().getExtra("url").toString();
        String clearContent = page.getRawText().replace("\\\"", "\"");
        Html html = new Html(clearContent);
//        List<Selectable> selectables = html.xpath("//div[@class='item4line1']").nodes();
//        if (CollectionUtils.isEmpty(selectables)){
//            logger.info("获取的集合为空");
//            return null;
//        }
//        Selectable selectable = selectables.get(0);
        List<Selectable> nodes = html.xpath("//dl[@class='item ']").nodes();


        File file = new File(path + "汤臣倍健.csv");
//        ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file(path + "汤臣倍健.xlsx"));
        if (!file.exists() && nodes.size() > 0) {
            try {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    String head = "关键词,分类,商品,交易数,评论数,价格,链接\n";
                    org.apache.commons.io.FileUtils.writeStringToFile(file, head, Charset.forName("GBK"), true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }





//        ArrayList<List<String>> list = CollUtil.newArrayList();
//        ArrayList<String> strings = CollUtil.newArrayList("关键词","分类","商品","总销量","评价","价格","链接");
//        list.add(strings);
        for (int i = 0; i < nodes.size(); i++) {
            Selectable node = nodes.get(i).xpath("//dd[@class='detail']");
            Selectable rate = nodes.get(i).xpath("//dd[@class='rates']");


            String product = node.xpath("//a[@class='J_TGoldData']/allText()").get();
            String soldCount = node.xpath("//span[@class='sale-num']/allText()").get();
            if (soldCount == null) {
                soldCount = "0";
            }

            String price = node.xpath("//span[@class='c-price']/text()").get();
            if (price == null) {
                price = "0";
            }

            String comment = rate.xpath("//div[@class='title']//span/text()").get();
            if (StringUtils.isEmpty(comment)) {
                comment = "0";
                continue;
            } else {
                comment = comment.substring(4);
            }

            String url = "https:" + node.xpath("//a[@class='J_TGoldData']/@href").get();
//            if (tmallShopConfig.getKeywords().size() > 0) {
//                boolean flag = false;
//                for (String keyword : tmallShopConfig.getKeywords()) {
//                    if (product.contains(keyword)) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (flag) {
//                    String writeContent = product + "," + soldCount + "," + url;
//                    FileUtils.write(writeContent, path + tmallShopConfig.getBrand() + ".csv");
//                    return;
//                }
//            }
//            ArrayList<String> newArrayList = CollUtil.newArrayList(brand, keyword, product, soldCount, comment, price, url);

            String writeContent = brand + "," + keyword + "," + product + "," + soldCount + "," + comment + "," + price + "," + url + "\n";
            try {
                org.apache.commons.io.FileUtils.writeStringToFile(file, writeContent, Charset.forName("GBK"), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            list.add(newArrayList);
            System.out.println("成功写一条数据");
        }

//        ArrayList<List<String>> arrayList = CollUtil.newArrayList(list);
//        writer.write(arrayList);

//        writer.close();
        if (nodes.size() == 0) {
            System.out.println(brand + " 没有数据。");
            return null;
        }

        String isNext = html.xpath("//a[@class='J_SearchAsync next']").get();
        if (isNext == null) {
            System.out.println("没有后续页面， brand : " + brand + ", url : " + page.getUrl().get());
            return null;
        }


        String currentPage = page.getUrl().regex("pageNo=(\\d+)").get();
        Integer nextPage = Integer.parseInt(currentPage) + 1;
        String nextUrl = page.getUrl().get().replace("pageNo=" + currentPage, "pageNo=" + nextPage);


        page.addTargetRequest(new Request(nextUrl).putExtra("url", keyword).putExtra("brand", brand));
//        page.addTargetRequest(new Request(nextUrl).putExtra("config", tmallShopConfig).putExtra(RequestExtraKey.KEY_PAGE_NAME, "list"));

        return null;
    }

    @Override
    public String pageName() {
        return null;
    }


    public static void main(String[] args) throws IOException {
//        String url = "https://sisley.tmall.com/i/asynSearch.htm?mid=w-14574995857-0&wid=14574995857&pageNo=1";
        String url = "https://sisley.tmall.com/i/asynSearch.htm?mid=w-14574995862-0&wid=14574995862&pageNo=1";
        Map<String, String> header = new HashMap<>();
        header.put("cookie", "cna=6KZmFH+sOWkCAXjAWe1mcgAH; otherx=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; x=__ll%3D-1%26_ato%3D0; hng=CN%7Czh-CN%7CCNY%7C156; lid=question0001; enc=JZ9CJvydKPduBpqtdalkAhQUD43N9p0De5DwnHNowwZjDXzdTLWnXLufHF2dGw9trxZ%2FI0xYRdSWeQWuMszTXw%3D%3D; sm4=110100; t=3d2ddf1a611d4b6ad4b94275966f138e; _tb_token_=5164b4e74e873; cookie2=1c17a804698b284ee2f1dde2494e35e6; pnm_cku822=; cq=ccp%3D1; _m_h5_tk=30c05833461936cf169ee6184cf1d43f_1568177851402; _m_h5_tk_enc=c248a0de10c9a73ccb11f424f5c78352; l=cBQi_XIuvqhLKRuUBOCwourza77OSIRAguPzaNbMi_5QW6L_9UQOkPH1uFp6VjWdtN8B4JuaUM29-etkiPxH88K-ih1V.; isg=BK2teaaHjHS60Wnd9UEoBiZ6vEknCuHcSFoAPO-y6cSzZs0Yt1rxrPswUHolZvmU");
        String content = RequestUtil.getContent(url, header);
        System.out.println(content);
    }
}
