package com.lavector.collector.crawler.project.tmall.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.project.tmall.TmallConfig;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.StringToDateConverter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/1/16.
 *
 * @author zeng.zhao
 */
public class TmallCommentPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(TmallCommentPage.class);

    private static final String filePath = "G:/text/tmall/data/comment/comment.csv";

    private boolean nonce = true;

    private Integer last_page = 6;

    private StringToDateConverter converter = new StringToDateConverter();

    private Date startDate = converter.convert("2017-06-01 00:00:00");

    private Date endDate = converter.convert("2019-01-01 00:00:00");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("rate.tmall.com/list_detail_rate.htm");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        try {
            return parseComment(page);
        } catch (Exception e) {
            System.out.println("错误:" + page.getUrl().get());
        }
        page.setSkip(true);
        return Result.get();
    }

    private Result parseComment(Page page) {
        Result result = Result.get();
        TmallConfig config = (TmallConfig) page.getRequest().getExtra("config");
        String shopId = page.getRequest().getExtra("shopId").toString();
        String shopName = page.getRequest().getExtra("shopName").toString();
        String itemId = page.getRequest().getExtra("itemId").toString();
        String itemName = page.getRequest().getExtra("itemName").toString();
        String brand = page.getRequest().getExtra("brand").toString();
        String url = page.getRequest().getExtra("url").toString();
        String title = page.getRequest().getExtra("title").toString();
        String xiaoliang = page.getRequest().getExtra("xiaoliang").toString();
        String commentNumber = page.getRequest().getExtra("commentNumber").toString();
        String price = page.getRequest().getExtra("price").toString();


        String json = page.getJson().regex("\\((.*)\\)").get();
//        if (last_page == null) {
//            synchronized (this) {
//                if (last_page == null) {
//                    last_page = JsonPath.read(json, "$.rateDetail.paginator.lastPage");
//                }
//            }
//        }

//        List<String> itemTypes = JsonPath.read(json, "$.rateDetail.rateList[*].auctionSku");//商品分类
        List<String> contents = JsonPath.read(json, "$.rateDetail.rateList[*].rateContent");//评论内容
        List<String> dates = JsonPath.read(json, "$.rateDetail.rateList[*].rateDate");//评论时间
        List<String> userNames = JsonPath.read(json, "$.rateDetail.rateList[*].displayUserNick");//用户
        List<String> userIds = JsonPath.read(json, "$.rateDetail.rateList[*].id");//用户id
//        if (nonce) {
//            synchronized (this) {
//                if (nonce) {
//                    nonce = false;
//                    String head = "店铺ID,店铺名,商品ID,商品名,内容,商品信息,评论时间,链接";
//                    WriteFile.write(head, filePath);
//                }
//            }
//        }
        String itemUrl = "https://detail.tmall.com/item.htm?id=" + itemId;
        for (int i = 0; i < contents.size(); i++) {
            if (StringUtils.isEmpty(dates.get(i)) && !dates.get(i).contains("2019")) {
                logger.info("排除一条");
                continue;
            }
//            Date publishDate = converter.convert(dates.get(i));
//            if (publishDate != null && (publishDate.after(startDate) && publishDate.before(endDate))) {
//                String content = shopId + "," + shopName + "," + itemId + "," +
//                        itemName + "," + contents.get(i).replaceAll(",", "，") + "," +
//                        itemTypes.get(i) + "," +
//                        dates.get(i) + "," + itemUrl;
//                WriteFile.write(content, filePath);
//            }
            File file = new File(filePath);
            if (!file.exists()) {
                try {
                    boolean newFile = file.createNewFile();
                    if (newFile) {
                        String header = "渠道,url,时间,关键词,content,title,用户昵称,用户id,转发数,评论数,点赞数,用户url,性别,城市,简介,标签,月销量,价格,分享数,评论时间,评论人,收藏数\n";
                        FileUtils.writeStringToFile(file, header, Charset.forName("GBK"), true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                String writeContent = "tmall," + url + "," + dates.get(i) + "," + brand + ","
                        + contents.get(i).replace(",", "，") + "," + title +
                        "," + "" + "," + "" + "," + "" + "," + commentNumber + "," + "" + "," + "" + "," + "" + "," + "" +
                        "," + "" + "," + "" + "," + xiaoliang + "," + price + "," + "" + "," + "" + "," + "" + "," + "" + "\n";

                FileUtils.writeStringToFile(file, writeContent, Charset.forName("GBK"), true);
                logger.info("成功写入一条");
//                Map<String, String> jsonMap = new HashMap<>();
//                jsonMap.put("category", config.getCategory());
//                jsonMap.put("brand", config.getBrand());
//                jsonMap.put("product", config.getProduct());
//                jsonMap.put("url", config.getUrl());
//                jsonMap.put("content", contents.get(i).replace(",", "，"));
//                jsonMap.put("time", dates.get(i));
//                FileUtils.writeStringToFile(Paths.get("/Users/zeng.zhao/Desktop/tmall1.json").toFile()
//                        , JsonMapper.buildNormalBinder().toJson(jsonMap) + "\n", Charset.defaultCharset(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

//        Integer currentPage = Integer.parseInt(page.getUrl().regex("currentPage=(\\d+)").get());
//        if (last_page > currentPage && !CollectionUtils.isEmpty(contents) && contents.size()>19) {
//            Integer nextPage = currentPage + 1;
//            String nextUrl = page.getUrl().get()
//                    .replace("currentPage=" + currentPage, "currentPage=" + nextPage);
//            Request request = new Request(nextUrl);
//            request.putExtra("itemId", itemId);
//            request.putExtra("shopId", shopId);
//            request.putExtra("shopName", shopName);
//            request.putExtra("itemName", itemName);
//            request.putExtra("config", config);
////            request.addHeader("Referer",page.getUrl().get());
//            page.addTargetRequest(request);
//
//            result.addRequest(request);
//        }
        page.setSkip(true);
        return result;
    }
}
