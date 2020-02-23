package com.lavector.collector.crawler.project.weibo.weiboPerson.page;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weibo.weiboStar.page.Person;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PersonDetilPage implements PageParse {


    private static String filePath = "G:/text/newWeibo/star/安佳用户信息.xlsx";

    public static Integer rowsNumer = 3146;


    @Override
    public Result parse(Page page) {
        Person person = new Person();
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        Html html = page.getHtml();
        List<String> scripts = html.xpath("script/html()").all();

        Optional<String> stringOptional = scripts.stream().filter(s -> s.contains("\"domid\":\"Pl_Official_PersonalInfo")).findFirst();

        stringOptional.ifPresent(s -> {
            String json = new Json(s).regex("FM.view\\((.*)\\)").get();

            String selectable = JsonPath.read(json, "$.html");

            String all = selectable.replace("\\", "");
            Html newHtml = new Html(all);

            List<Selectable> nodes = newHtml.xpath("//div[@class='WB_cardwrap S_bg2']").nodes();
            for (Selectable selectable1 : nodes) {
                String text = selectable1.xpath("//div[@class='WB_cardtitle_b S_line2']/div/h2/text()").get();
                if (text.contains("基本信息")) {
                    List<Selectable> selectables = selectable1.xpath("//li[@class='li_1 clearfix']").nodes();
                    for (Selectable sel : selectables) {

                        String string = sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_title S_txt2']/text()").get();

                        if (string != null) {
                            if (string.contains("昵称")) {
                                person.setName(sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_detail']/text()").get());
                            } else if (string.contains("所在地")) {
                                person.setCity(sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_detail']/text()").get());
                            } else if (string.contains("性别")) {
                                person.setSex(sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_detail']/text()").get());
                            } else if (string.contains("简介")) {
                                person.introduction = sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_detail']/text()").get();
                            } else if (string.contains("生日")) {
                                String s1 = sel.xpath("//li[@class='li_1 clearfix']/span[@class='pt_detail']/text()").get();
                                if ((s1.contains("月") || s1.contains("日")) && !s1.contains("年")) {
                                    s1 = "2018年" + s1;
                                }

                                if (s1.contains("月") && s1.contains("日") && s1.contains("年") && s1.length() < 12) {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                                    try {
                                        Date parse = simpleDateFormat.parse(s1);
                                        LocalDate from = LocalDate.from(parse.toInstant().atZone(ZoneId.systemDefault()));
                                        LocalDate to = LocalDate.now();
                                        int i = to.compareTo(from);
                                        System.out.println(i);

                                        person.setAge(i + "");
                                        continue;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }

                } else if (text.contains("标签信息")) {
                    Selectable clearfix = selectable1.xpath("//li[@class='li_1 clearfix']");
                    List<Selectable> nodesTag = clearfix.xpath("//li[@class='li_1 clearfix']//span[@class='pt_detail']//a[@class='W_btn_b W_btn_tag']").nodes();

                    if (!CollectionUtils.isEmpty(nodesTag)) {

                        List<String> tag = new ArrayList<>();
                        for (Selectable selectable2 : nodesTag) {
                            String s1 = selectable2.xpath("//a[@class='W_btn_b W_btn_tag']/text()").get().trim();
                            tag.add(s1);
                        }

                        String tags = tag.toString();
                        String substring = tags.substring(1, tags.lastIndexOf("]"));
                        String replace = substring.replace(",", "，");
                        person.setClearfix(replace);
                    }
                }
            }
        });

        List<String> headWords = skuData.getHeadWords();
        String[] strings = headWords.toArray(new String[headWords.size()]);
        String url = "https://weibo.com/u/" + skuData.getBrand() + "?refer_flag=1028035010_,";
        String[] rowList = {skuData.getBrand(), url, person.getSex(), person.getAge(), person.getName(), person.getCity(), person.introduction, person.getClearfix()};

        writeExcel(strings, rowList);

//        if ("女".equals(person.getSex()) && person.getAge() != null && Integer.parseInt(person.getAge()) >= 20 && Integer.parseInt(person.getAge()) <= 60) {
//        page.getRequest().putExtra("person", person);
//        page.putField("person", person);
//        page.putField("skuData", skuData);
//        } else {
        page.setSkip(true);
//        }


        return null;
    }


    public synchronized void writeExcel(String[] headList, String[] rowsList) {
        ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file(filePath));

        List<String> strings = CollUtil.newArrayList(headList);
        writer.writeHeadRow(strings);
        List<String> row = CollUtil.newArrayList(rowsList);

        for (int i = 0; i < row.size(); i++) {
            writer.writeCellValue(i, rowsNumer, row.get(i));
            writer.flush();
        }
        rowsNumer++;
        System.out.println("rowsNumber:" + rowsNumer);
        writer.close();
    }


    @Override
    public String pageName() {
        return null;
    }


    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://weibo.com/p/[0-9]+/info\\?mod=pedit_more", url);
    }

}
