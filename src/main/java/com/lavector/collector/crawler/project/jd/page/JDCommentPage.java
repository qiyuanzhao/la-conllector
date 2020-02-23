package com.lavector.collector.crawler.project.jd.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.StringToDateConverter;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/1/17.
 *
 * @author zeng.zhao
 */
public class JDCommentPage implements PageParse {

    private JsonMapper mapper = JsonMapper.buildNormalBinder();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final String filePath = "/Users/zeng.zhao/Desktop/jd_comment(果醋).json";

    private Integer max_page;

    private boolean nonce = true;

    private StringToDateConverter converter = new  StringToDateConverter();

    private Date startDate = converter.convert("2018-03-20 00:00:00");

    private Date endDate = converter.convert("2018-06-22 23:59:59");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("sclub.jd.com/comment/skuProductPageComments.action");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String itemId = page.getRequest().getExtra("itemId").toString();
//        String shopId = page.getRequest().getExtra("shopId").toString();
//        String shopName = page.getRequest().getExtra("shopName").toString();

        @SuppressWarnings("unchecked")
        Map<String, String> parameter = (Map<String, String>) page.getRequest().getExtra("parameter");
        String json = page.getJson().get();
        if (StringUtils.isBlank(json)) {
            result.addRequest(page.getRequest());
            System.out.println("json is null" + page.getUrl());
            return result;
        }

        if (max_page == null) {
            max_page = JsonPath.read(json, "$.maxPage");
        }

        List<String> contents = JsonPath.read(json, "$.comments[*].content");
        List<String> dates = JsonPath.read(json, "$.comments[*].creationTime");
        List<Integer> scores = JsonPath.read(json, "$.comments[*].score");
//        List<String> itemNames = JsonPath.read(json, "$.comments[*].referenceName");
//        List<String> infos = JsonPath.read(json, "$.comments[*].productColor");
        List<Object> mids = JsonPath.read(json, "$.comments[*].id");

//        if (nonce) {
//            synchronized (this) {
//                if (nonce) {
//                    String head = "店铺ID,店铺名,商品ID,商品名,内容,评分,商品信息,评论时间,链接";
//                    WriteFile.write(head, filePath);
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    nonce = false;
//                }
//            }
//        }

        String itemUrl = "https://item.jd.com/" + itemId + ".html";
        boolean isWrite = false;
        for (int i = 0; i < contents.size(); i++) {
            Date publishTime = converter.convert(dates.get(i));
            NonceMessage message = new NonceMessage();
            message.setContent(contents.get(i));
            message.setMid(mids.get(i).toString());
            message.setSite("jd");
            message.setTime(format.format(publishTime));
            message.setType("Post");
            message.setParameter(parameter);
            message.setUrl(itemUrl);

            NonceMessage.SocialCount socialCount = new NonceMessage.SocialCount();
            socialCount.setLikes(scores.get(i));
            message.setSocialCount(socialCount);

            if (publishTime != null && publishTime.after(startDate)) {
                isWrite = true;
                String s = mapper.toJson(message);
                FileUtils.write(s, filePath);
//                String content = shopId + "," + shopName + "," + itemId + "," + itemNames.get(i) + "," +
//                        contents.get(i).replaceAll(",", "，") + "," +
//                        scores.get(i) + "," + infos.get(i) + "," + dates.get(i) + "," +itemUrl;
//                WriteFile.write(content, filePath);
            }
        }

        Integer currentPage = Integer.parseInt(page.getUrl().regex("page=(\\d+)").get());
        if (contents.size() > 0 && isWrite) {
            Integer nextPage = currentPage + 1;
            String nextUrl = page.getUrl().get().replace("page=" + currentPage, "page=" + nextPage);
            Request request = new Request(nextUrl);
            request.putExtra("parameter", parameter).putExtra("itemId", itemId);
            result.addRequest(request);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://sclub.jd.com/comment/productPageComments.action?callback=&productId=1979646912&score=0&sortType=6&page=0&pageSize=10&isShadowSku=0&fold=1";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        System.out.println(content);
    }
}
