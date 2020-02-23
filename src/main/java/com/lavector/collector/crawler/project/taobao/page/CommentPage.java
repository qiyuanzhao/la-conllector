package com.lavector.collector.crawler.project.taobao.page;


import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.babyTreeUserInfo.page.TaoBaoPo;
import com.lavector.collector.entity.readData.SkuData;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(CommentPage.class);


    @Override
    public boolean handleUrl(String url) {
        Pattern pattern = Pattern.compile("(https://rate.taobao.com/feedRateList.htm\\?auctionNumId=[0-9]+&currentPageNum=[0-9]+&pageSize=20&orderType=feedbackdate)");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        Integer currentPageNum = (Integer) page.getRequest().getExtra("currentPageNum");
        String codeStr = (String) page.getRequest().getExtra("codeStr");
        String html = page.getRawText();
        String newHtml = html.substring(html.indexOf("(", 1) + 1, html.lastIndexOf(")"));

        JSONArray jsonArray = JsonPath.read(newHtml, "$.comments");
        List<TaoBaoPo> taoBaoPoList = new ArrayList<>();
        String url = page.getUrl().get();
        String code = url.substring(url.lastIndexOf("=") + 1);
        for (Object object : jsonArray) {
            TaoBaoPo taoBaoPo = new TaoBaoPo();
            String date = JsonPath.read(object, "$.date");
            String content = JsonPath.read(object, "$.content");
            String nick = JsonPath.read(object, "$.user.nick");
            taoBaoPo.setComment(content.replace(",", "，"));
            taoBaoPo.setCommentTime(date);
            taoBaoPo.setUserName(nick);
            taoBaoPo.setDetailUrl("//item.taobao.com/item.htm?id=" + code + "&ns=1&abbucket=15#detail");
            taoBaoPoList.add(taoBaoPo);
        }

        page.getRequest().putExtra("taoBaoPoList", taoBaoPoList);
        page.putField("taoBaoPoList", taoBaoPoList);
        String commentTime = taoBaoPoList.get(taoBaoPoList.size() - 1).getCommentTime();
        Boolean flag = handleDate(commentTime);
        page.putField("skuData", skuData);
        if (taoBaoPoList.size() > 0 && flag) {
            currentPageNum += 1;
            String newUrl = "https://rate.taobao.com/feedRateList.htm?auctionNumId=" + codeStr + "&currentPageNum=" + currentPageNum + "&pageSize=20&orderType=feedbackdate";
            Request request = new Request();
            request.setUrl(newUrl);
            request.putExtra("skuData", skuData).putExtra("currentPageNum", currentPageNum).putExtra("codeStr", codeStr);
            page.addTargetRequest(request);
        }
        logger.info("*****获取完一条：{} *****", page.getUrl().get());
        return result;
    }

    private boolean handleDate(String commentTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
        boolean flag = false;
        String year = commentTime.replace("年", "-");
        String month = year.replace("月", "-");
        String dateStr = month.replace("日", "");
        try {
            Date parse = simpleDateFormat.parse(dateStr);
            Date resource = simpleDateFormat.parse("2018-09-30 00:00");
            int i = resource.compareTo(parse);
            if (resource.compareTo(parse)<0){
                flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public String pageName() {
        return null;
    }


}
