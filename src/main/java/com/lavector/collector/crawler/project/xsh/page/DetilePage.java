package com.lavector.collector.crawler.project.xsh.page;

import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


public class DetilePage implements PageParse {


    private Logger logger = LoggerFactory.getLogger(DetilePage.class);

    @Override
    public boolean handleUrl(String url) {
        return url.contains("https://www.xiaohongshu.com/user/profile/");
    }

    @Override
    public Result parse(Page page) {

        String name = page.getRequest().getExtra("name").toString();

//        Selectable userItem = page.getHtml().xpath("//div[@id='app']//div[@class='user-item']");
//
//        Selectable authorItem = userItem.xpath("//div[@class='author-item']");

        Selectable right = page.getHtml().xpath("//div[@id='app']//div[@class='right']");

        String userName = right.xpath("//div[@class='user-name']//span[@class='name-detail']/text()").get();
        //简介
        String brief = right.xpath("//div[@class='user-brief']/text()").get();
        String local = right.xpath("//div[@class='location']/span[@class='location-text']/text()").get();

        List<Selectable> nodes = right.xpath("//div[@class='card-info']/div[@class='info']").nodes();

        //关注
        String note = nodes.get(0).xpath("//span[@class='info-number']/text()").get();
        //粉丝
        String fans = nodes.get(1).xpath("//span[@class='info-number']/text()").get();
        //获赞与收藏
        String collect = nodes.get(2).xpath("//span[@class='info-number']/text()").get();

        XhsPerson xhsPerson = new XhsPerson();
        xhsPerson.setUserName(userName);
        xhsPerson.setBrief(brief);
        xhsPerson.setCollect(collect);
        xhsPerson.setFans(fans);
        xhsPerson.setLocal(local);
        xhsPerson.setNote(note);


        String path = "G:/text/xhsSearch/" + "厉峰用户信息补充" + ".csv";
        try {
            if (!new File(path).exists()) {
                boolean newFile = new File(path).createNewFile();
                if (newFile) {
                    String header = "用户id,用户名称,简介,省份,关注,粉丝,收藏,链接\n";
                    FileUtils.writeStringToFile(new File(path), header, Charset.forName("GBK"), true);
                }
            }
            String writeContent = name + "," + userName + "," + brief + "," + local + "," + note + "," + fans + "," + collect + "," + page.getUrl() + "\n";

            FileUtils.writeStringToFile(new File(path), writeContent, Charset.forName("GBK"), true);
            logger.info("写入一条数据。。。");
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
