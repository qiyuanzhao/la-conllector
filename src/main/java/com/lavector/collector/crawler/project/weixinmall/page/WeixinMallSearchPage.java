package com.lavector.collector.crawler.project.weixinmall.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;


public class WeixinMallSearchPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("http://open.koldata.net/wechat/simple/search");
    }

    @Override
    public Result parse(Page page) {
        String brand = page.getRequest().getExtra("brand").toString();

        String rawText = page.getRawText();
        JSONObject jsonObject = JSONObject.parseObject(rawText);
        String err = jsonObject.get("err").toString();

        if (!StringUtils.isEmpty(err) && err.contains("not found")) {

            String path = "G:\\text\\newWeixin\\weixinmall\\data/data.csv";
            try {
                if (!new File(path).exists()) {
                    boolean newFile = new File(path).createNewFile();
                    if (newFile) {
                        FileUtils.writeStringToFile(new File(path), "关键词" + "\n", Charset.forName("UTF-8"), true);
                        System.out.println("写入一条");
                    }
                }

                FileUtils.writeStringToFile(new File(path), brand +
                        "\n", Charset.forName("UTF-8"), true);
                System.out.println("写入一条");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");

        String path = "G:\\text\\newWeixin\\weixinmall\\data/weixin.csv";

        try {
            if (!new File(path).exists()) {
                boolean newFile = new File(path).createNewFile();
                if (newFile) {
                    FileUtils.writeStringToFile(new File(path), "关键词" + ","
                            + "标题" + "," + "digest" + ","

                            + "是否多图文" + "," + "文章位置" + ","
                            + "发布时间" + "," + "作者" + ","
                            + "是否含视频" + ","

                            + "浏览量" + "," + "点赞数" + ","
                            + "链接" + "\n", Charset.forName("UTF-8"), true);
                }
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.get("title").toString();
                String digest = jsonObject1.get("digest").toString();
                String isMulti = jsonObject1.get("is_multi").toString();
                if ("1".equalsIgnoreCase(isMulti)) {
                    isMulti = "是";
                } else if ("0".equalsIgnoreCase(isMulti)) {
                    isMulti = "否";
                }
                String idx = jsonObject1.get("idx").toString();
                idx = "第" + idx + "条";
                String pubTime = jsonObject1.get("pub_time").toString();
                String author = jsonObject1.get("author").toString();
                String isVideo = (String) jsonObject1.get("is_video");
                if ("1".equalsIgnoreCase(isVideo)) {
                    isVideo = "含视频";
                } else if ("0".equalsIgnoreCase(isVideo)) {
                    isVideo = "不含视频";
                }


                String contentUrl = jsonObject1.get("content_url").toString();
                String readNum = jsonObject1.get("read_num").toString();
                String likeNum = (String) jsonObject1.get("like_num");
                FileUtils.writeStringToFile(new File(path), brand + ","
                        + handleContent(title) + "," + handleContent(digest) + ","

                        + isMulti + "," + idx + ","
                        + pubTime + "," + author + ","
                        + isVideo + ","


                        + readNum + "," + likeNum + ","
                        + contentUrl + "\n", Charset.forName("UTF-8"), true);
                System.out.println("写入一条");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = page.getRequest().getUrl();
        String pageNumber = url.substring(url.lastIndexOf("=") + 1);
        int newPageNumber = Integer.parseInt(pageNumber) + 1;

        if (newPageNumber < 10 && jsonArray.size() > 0) {
            String newUrl = url.substring(0, url.lastIndexOf("=") + 1) + newPageNumber;
            page.addTargetRequest(new Request(newUrl).putExtra("brand", brand));
            page.setSkip(true);
        }


        return null;
    }

    @Override
    public String pageName() {
        return null;
    }

    public String handleContent(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        String replace = content.replace("\n", "").replace(",", "，").replace("\t", "");
        if (replace.length() > 30000) {
            replace = replace.substring(0, 30000);
        }
        return replace;
    }
}
