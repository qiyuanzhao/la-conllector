package com.lavector.collector.crawler.project.weibo.weiboPerson.page;


import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weibo.weiboStar.page.Person;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PersonPage implements PageParse {

    //https://weibo.com/p/1003061087770692/info?mod=pedit_more

    private Logger logger = LoggerFactory.getLogger(PersonPage.class);

//    private static String filePath = "G:/text/newWeibo/star/安佳用户信息.xlsx";
//
//    private static ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file(filePath));
//
//    static {
//        String[] headList = {"用户id", "url", "性别", "年龄", "城市", "简介", "标签"};
//
//        List<String> strings = CollUtil.newArrayList(headList);
//        writer.writeHeadRow(strings);
//        writer.flush();
//    }

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://weibo.com/u/.*", url) || RegexUtil.isMatch("https://weibo.com/.*\\?refer_flag=1028035010_", url);
    }

    @Override
    public Result parse(Page page) {
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");

        getUserInfoAll(page, skuData);

        return null;
    }

    private void getUserInfoAll(Page page, SkuData skuData) {

        Html html = page.getHtml();
        List<String> scripts = html.xpath("script/html()").all();

        Person person = new Person();

        Map<String, String> params = new HashMap<>();
        scripts.stream().filter(s -> s.contains("var $CONFIG = {}")).findFirst().ifPresent(config -> {
            String pageId = new Json(config).regex("\\$CONFIG\\['page_id'\\]='(\\d+)';").get();
            String script = new Json(config).regex("\\$CONFIG\\['watermark'\\]='(u/\\d+)';").get();
            String domain = new Json(config).regex("\\$CONFIG\\['domain'\\]='(\\d+)';").get();

//            String substring = pageId.substring(0, pageId.indexOf(";", 1) - 1);
            params.put("pageId", pageId);
            params.put("script", script);
            params.put("domain", domain);
        });

        Optional<String> optionalS = scripts.stream().filter(s -> s.contains("\"pl.content.homeFeed.index\",\"domid\":\"Pl_Core_UserInfo__6")).findFirst();
        Optional<String> optionas = scripts.stream().filter(s -> s.contains("\"pl.header.head.index\",\"domid\":\"Pl_Official_Headerv6__1")).findFirst();
        Optional<String> fans = scripts.stream().filter(s -> s.contains("\"ns\":\"\",\"domid\":\"Pl_Core_T8CustomTriColumn__3\"")).findFirst();
        if (optionalS == null) {
            Request request = new Request();
            request.setUrl(page.getUrl().get());
            request.putExtra("skuData", skuData);
            page.addTargetRequest(request);
            page.setSkip(true);
            return;
        }
        optionalS.ifPresent((String s) -> {
            String json = new Json(s).regex("FM.view\\((.*)\\)").get();
            String htmlStr = JsonPath.read(json, "$.html");
            Html newHtml = new Html(htmlStr);
            List<Selectable> nodes = newHtml.xpath("//div[@class='WB_cardwrap S_bg2']//ul[@class='ul_detail']/li").nodes();
            for (Selectable node : nodes) {
                if ("2".equals(node.xpath("//em[@class='W_ficon ficon_cd_place S_ficon']/text()").get())) {

                    String provinceAndCity = node.xpath("//span[@class='item_text W_fl']/text()").get().trim();
//                    String[] split = provinceAndCity.split(" ");
//                    String province = split[0];
//                    String city = split[0];

                    person.setCity(provinceAndCity);
                }
                String s1 = node.xpath("//span[@class='item_text W_fl']/text()").get().trim();

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

                String s2 = node.xpath("//span[@class='item_ico W_fl']/em/text()").get();
                if (!StringUtils.isEmpty(s2) && "T".equalsIgnoreCase(s2)) {
                    List<String> all = node.xpath("//span[@class='item_text W_fl']/a[@target='_blank']/text()").all();
                    String clearFix = "";
                    for (String str : all) {
                        clearFix += str + "，";
                    }
                    person.setClearfix(clearFix);
                }

                String s3 = node.xpath("//span[@class='item_ico W_fl']/em/text()").get();
                if (!StringUtils.isEmpty(s2) && "Ü".equalsIgnoreCase(s2)) {
                    String introduction = node.xpath("//span[@class='item_text W_fl']/text()").get();

                    person.introduction = introduction.replace(",", "，");
                }
            }
        });
        optionas.ifPresent(s -> {
            String json = new Json(s).regex("FM.view\\((.*)\\)").get();
            String htmlStr = JsonPath.read(json, "$.html");
            Html newHtml = new Html(htmlStr);
            String sexStr = newHtml.xpath("//div[@class='PCD_header']/div[@class='pf_wrap']//div[@class='pf_username']/span[@class='icon_bed']//i/@class").get();
            if ("W_icon icon_pf_male".equals(sexStr)) {
                person.setSex("男");
            } else if ("W_icon icon_pf_female".equals(sexStr)) {
                person.setSex("女");
            }
        });
        if (fans != null) {
            fans.ifPresent(s -> {
                String json = new Json(s).regex("FM.view\\((.*)\\)").get();
                String htmlStr = JsonPath.read(json, "$.html");
                Html newHtml = new Html(htmlStr);
                List<Selectable> nodes = newHtml.xpath("//div[@class='WB_cardwrap S_bg2']//table[@class='tb_counter']//td[@class='S_line1']").nodes();
                nodes.forEach(node -> {
                    String s1 = node.xpath("//a//span[@class='S_txt2']/text()").get();
                    if (!StringUtils.isEmpty(s1)) {
                        switch (s1) {
                            case "关注":
                                String friendsCount = node.xpath("//a//strong/text()").get();
                                logger.info("关注数：" + friendsCount);
                                person.friendsCount = friendsCount;
                                break;
                            case "粉丝":
                                String followersCount = node.xpath("//a//strong/text()").get();
                                logger.info("粉丝数：" + followersCount);
                                person.followersCount = followersCount;
                                break;
                            case "微博":
                                String messagesCount = node.xpath("//a//strong/text()").get();
                                logger.info("微博数：" + messagesCount);
                                person.messagesCount = messagesCount;
                                break;
                            default:
                        }
                    }
                });
            });


        String url = "https://weibo.com/p/" + params.get("pageId") + "/info?mod=pedit_more";
        logger.info("url:" + url);


//        String[] rowsList = {skuData.getBrand(), url, person.getSex(), person.getAge(), person.getCity(), person.introduction, person.getClearfix()};
//        writeExcel( rowsList);
        String path = "G:/text/newWeibo/star/" + "厉峰用户信息第二批" + ".csv";
        try {
            if (!new File(path).exists()) {
                boolean newFile = new File(path).createNewFile();
                if (newFile) {
                    String header = "用户id,url,性别,年龄,城市,粉丝数,简介,标签\n";
                    FileUtils.writeStringToFile(new File(path), header, Charset.forName("GBK"), true);
                }
            }
            String writeContent = skuData.getBrand() + "," + url + "," + person.getSex() + "," + person.getAge() + "," + person.getCity() + ","  + person.followersCount + "," + person.introduction + "," + person.getClearfix() + "\n";
            FileUtils.writeStringToFile(new File(path), writeContent, Charset.forName("GBK"), true);
            logger.info("写入一条数据。。。");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Request request = new Request(url).putExtra("skuData", skuData);
//        page.addTargetRequest(request);
        page.setSkip(true);

    }

//    public static Integer rowsNumer = 1;

//    public synchronized void writeExcel( String[] rowsList) {
//
//
//
//        List<String> row = CollUtil.newArrayList(rowsList);
//        for (int i = 0; i < row.size(); i++) {
////            writer.writeCellValue(i, rowsNumer, row.get(i));
////            writer.flush();
//        }
//        rowsNumer++;
//        System.out.println("rowsNumber:" + rowsNumer);
////        writer.close();
    }


    private void getUserInfoPart(Page page, SkuData skuData) {


        //        List<Request> requests = new ArrayList<>();
//        List<String> scripts = html.xpath("script/html()").all();
//        Map<String, String> params = new HashMap<>();
//        scripts.stream().filter(s -> s.contains("var $CONFIG = {}")).findFirst().ifPresent(config -> {
//            String pageId = new Json(config).regex("\\$CONFIG\\['page_id'\\]='(\\d+)';").get();
//            params.put("pageId", pageId);
//        });
//        person.setUrl("https://weibo.com/p/" + params.get("pageId") + "/info?mod=pedit_more");
//        if (person.getCity() == null) {
//            page.addTargetRequest(new Request().setUrl(page.getUrl().toString()).putExtra("skuData", skuData));
//            page.setSkip(true);
//            return null;
//        }

        Html html = page.getHtml();
        List<String> scripts = html.xpath("script/html()").all();


        Person person = new Person();

        Optional<String> optionalS = scripts.stream().filter(s -> s.contains("\"pl.content.homeFeed.index\",\"domid\":\"Pl_Core_UserInfo__6")).findFirst();
        Optional<String> optionas = scripts.stream().filter(s -> s.contains("\"pl.header.head.index\",\"domid\":\"Pl_Official_Headerv6__1")).findFirst();
        if (optionalS == null) {
            Request request = new Request();
            request.setUrl(page.getUrl().get());
            request.putExtra("skuData", skuData);
            page.addTargetRequest(request);
            page.setSkip(true);
            return;
        }
        optionalS.ifPresent(s -> {
            String json = new Json(s).regex("FM.view\\((.*)\\)").get();
            String htmlStr = JsonPath.read(json, "$.html");
            Html newHtml = new Html(htmlStr);
            List<Selectable> nodes = newHtml.xpath("//div[@class='WB_cardwrap S_bg2']//ul[@class='ul_detail']/li").nodes();
            for (Selectable node : nodes) {
                if ("2".equals(node.xpath("//em[@class='W_ficon ficon_cd_place S_ficon']/text()").get())) {

                    String provinceAndCity = node.xpath("//span[@class='item_text W_fl']/text()").get().trim();
                    String[] split = provinceAndCity.split(" ");
                    String province = split[0];
                    String city = split[0];

                    person.setCity(city);
                }
                String s1 = node.xpath("//span[@class='item_text W_fl']/text()").get().trim();

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

        });
        optionas.ifPresent(s -> {
            String json = new Json(s).regex("FM.view\\((.*)\\)").get();
            String htmlStr = JsonPath.read(json, "$.html");
            Html newHtml = new Html(htmlStr);
            String sexStr = newHtml.xpath("//div[@class='PCD_header']/div[@class='pf_wrap']//div[@class='pf_username']/span[@class='icon_bed']//i/@class").get();
            if ("W_icon icon_pf_male".equals(sexStr)) {
                person.setSex("男");
            } else if ("W_icon icon_pf_female".equals(sexStr)) {
                person.setSex("女");
            }
        });
        page.getRequest().putExtra("person", person);
        page.putField("skuData", skuData);
        page.putField("person", person);
    }


    @Override
    public String pageName() {

        return null;
    }

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("234");
        list.add("234");
        list.add("234");
        list.add("234");

        list.removeIf(str -> {
            if (str.equals("123")) {
                return true;
            }
            return false;
        });

        System.out.println(list);


    }


}
