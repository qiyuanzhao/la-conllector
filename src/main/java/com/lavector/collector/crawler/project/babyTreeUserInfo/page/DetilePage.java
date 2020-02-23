package com.lavector.collector.crawler.project.babyTreeUserInfo.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.babyTree.page.BabyPrams;
import com.lavector.collector.crawler.project.babyTree.page.ConmentUser;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetilePage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        Pattern pattern = Pattern.compile("http://www.babytree.com/community/[a-z 0-9]+/topic_[0-9]+.html");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");

        List<Selectable> nodes = page.getHtml().xpath("//div[@class='clubTopicSinglePost']").nodes();
        if (nodes.isEmpty()){
            return result;
        }

        //收藏
        String collection = page.getHtml().xpath("//table[@id='DivHbbs']//script[@type='text/javascript']/html()").regex("[0-9]+").get();

//        String babyPramsNew = "";
        int i = 0;
        for(Selectable selectable : nodes){
            BabyPrams babyPrams = new BabyPrams();
            ConmentUser conmentUser = new ConmentUser();
            List<Selectable> selectables = selectable.xpath("//div[@class='postContent']").nodes();
            String newUrl = selectable.xpath("//div[@class='clubTopicSinglePost']//p[@class='userAvatar']/a/@href").get();
            Selectable xpath = selectable.xpath("//div[@class='clubTopicSinglePost']/div[@class='postBody']//p[@class='postTime']");
            String time = selectable.xpath("//div[@class='clubTopicSinglePost']/div[@class='postBody']//p[@class='postTime']/script/html()").regex("([0-9 : -]+)").all().get(0);


            //评论和内容
            String finalContent = "";
            for (Selectable node : selectables) {
                List<Selectable> list = node.xpath("//p").nodes();
                if (list.size()>0){
                    for (Selectable selectable1 : list){
                        String text = selectable1.xpath("//p/allText()").get();
                        finalContent += text;
                    }
//                    babyPramsNew = finalContent;
                }else {
                    finalContent = node.xpath("//div/text()").get();
                }
            }
            String replace = finalContent.replace(",", "，");
            if (i==0){
                babyPrams.setContent(replace);
                babyPrams.setUserFalg(true);
            }else {
//                babyPrams.setContent(babyPramsNew.replace(",", "，"));
                //评论内容
                conmentUser.setContent(replace);
                conmentUser.setDate(time);
            }
            babyPrams.setCollectionNumber(collection);
            babyPrams.setConmentUser(conmentUser);
            Request request = new Request(newUrl).putExtra("skuData",skuData).putExtra("babyPrams",babyPrams);
            result.addRequest(request);
            i++;
        }
        page.setSkip(true);
        return result;
    }


    @Override
    public String pageName() {
        return null;
    }
}
