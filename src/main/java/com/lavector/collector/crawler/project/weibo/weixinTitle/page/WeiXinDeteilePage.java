package com.lavector.collector.crawler.project.weibo.weixinTitle.page;


import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiXinDeteilePage implements PageParse {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Result parse(Page page) {
        String url = page.getUrl().get();

        Project project = (Project) page.getRequest().getExtra("project");

        List<String> list = page.getHtml().xpath("//script/html()").all();

        String time = "";
        for (String str : list) {
            if (str.contains("var publish_time")) {
                Pattern pattern = Pattern.compile("([0-9]{4}-[0-9]{2}-[0-9]{2})");
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()) {
                    time = matcher.group();
                }
            }
        }
        String title = "";
        if (!"".equals(time)/* && checkTime(time)*/) {
            String stringList = page.getHtml().xpath("//div[@class='rich_media']//div[@id='js_content']/allText()").get();
            Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5，]+");
            Matcher matcher = pattern.matcher(stringList);
            while(matcher.find()){
                String group = matcher.group();
                if (!group.equals("宋体")&&!group.equals("黑体")&&!group.equals("微软雅黑")){
                    title += group+"，";
                }
            }
            project.setUrl(url);
            project.setTitle(title);
            project.setDate(time);
            page.putField("project",project);
        }else{
            page.setSkip(true);
        }



        return null;
    }


    private boolean checkTime(String time) {
        String start = "2018-07-31";
        String end = "2018-09-01";
        boolean flag = false;
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date startTime = simpleDateFormat.parse(start);
            Date endTime = simpleDateFormat.parse(end);
            if (date != null && startTime.before(date) && endTime.after(date)) {
                flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }


    @Override
    public boolean handleUrl(String url) {
        return false;
    }


    @Override
    public String pageName() {
        return null;
    }
}
