package com.lavector.collector.crawler.project.tmall.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.project.tmall.TmallConfig;
import com.lavector.collector.crawler.project.tmall.entity.Attribute;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created on 2018/1/15.
 *
 * @author zeng.zhao
 */
public class TmallProductPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(TmallProductPage.class);

    private static final String filePath = "G:/text/tmall/data/product/商品详情按销量.csv";

    private boolean nonce = true;

    @Override
    public boolean handleUrl(String url) {
        return url.contains("detail.tmall");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        return getShopComment(page);
    }


    //获取商品评论
    private Result getShopComment(Page page) {

        Result result = Result.get();
        Html html = page.getHtml();

        getAttribute(page, html);

//        result = getComment(page, html, result);

        page.setSkip(true);
        return result;
    }

    private Result getComment(Page page, Html html, Result result) {
        String brand = page.getRequest().getExtra("brand").toString();
        String title = page.getRequest().getExtra("title").toString();
        String xiaoliang = page.getRequest().getExtra("xiaoliang").toString();
        String commentNumber = page.getRequest().getExtra("commentNumber").toString();
        String price = page.getRequest().getExtra("price").toString();

        List<String> scripts = html.xpath("script").all();
        String shopApiJson = null;
            for (String script : scripts) {
                if (script.contains("TShop.Setup(")) {
                    shopApiJson = script.substring(script.indexOf("TShop.Setup(") + 13, script.lastIndexOf("})();") - 4);
                    break;
                }
        }
        if (shopApiJson == null) {
            return result;
        }

        Object read = JsonPath.read(shopApiJson, "$.valItemInfo.skuList");
        String skuListStr = JSON.toJSONString(read);
        JSONArray jsonArray = JSON.parseArray(skuListStr);

        TmallConfig tmallConfig = (TmallConfig) page.getRequest().getExtra("config");

        Object sellerId = JsonPath.read(shopApiJson, "$.rateConfig.sellerId");
        Object spuId = JsonPath.read(shopApiJson, "$.rateConfig.spuId");
        Object shopId = JsonPath.read(shopApiJson, "$.rstShopId");
        Object itemId = JsonPath.read(shopApiJson, "$.rateConfig.itemId");

        if (Objects.nonNull(sellerId) && Objects.nonNull(spuId)) {
            String shopName = html.xpath("//a[@class='slogo-shopname']/allText()").get();
            if (shopName == null) {
                if (page.getRawText().contains("天猫超市")) {
                    shopName = "天猫超市";
                } else {
                    shopName = "无";
                }
            }
            String itemName = html.xpath("//meta[@name='keywords']/@content").get();
            if (StringUtils.isBlank(itemName)) {
                itemName = html.xpath("//div[@class='tb-detail-hd']/h1/text()").get();
            }

            for (int i = 1; i <= 11; i++) {
                String commentUrl = "https://rate.tmall.com/list_detail_rate.htm?itemId=" + itemId +
                        "&spuId=" + spuId +
                        "&sellerId=" + sellerId +
                        "&order=1&currentPage=" + i +
                        "&append=0&content=1";
                us.codecraft.webmagic.Request request = new us.codecraft.webmagic.Request(commentUrl);
                request.putExtra("itemId", itemId);
                request.putExtra("shopId", shopId);
                request.putExtra("shopName", shopName);
                request.putExtra("itemName", itemName);
                request.putExtra("config", tmallConfig);
                request.putExtra("title", title);
                request.putExtra("xiaoliang", xiaoliang);
                request.putExtra("brand", brand);
                request.putExtra("price", price);
                request.putExtra("commentNumber", commentNumber);
                request.putExtra("url", page.getUrl().get());
                request.addHeader("Referer", page.getUrl().get());
                request.addHeader("Cookie", "t=015a21521c1a24a7565a0455c9ff7db1; _tb_token_=7e9fbde8eef33; cookie2=1f6d6758c8dbed052b5c2c27dc9088a2; cna=SwgrFgTxFBECAXLyIl+hWlbx; isg=BN7eYwUcD61481tJopymINlcLHTgN5Cfiss3_ohnKyEQq32F8ixqKXgBo3cCiJox; l=cBQ7FfKnq-xRGzOCBOCwZuIJHr7OLIO0YuPRwpHDi_5wL1LQFEbOkgQOAep6csWhTQYH4IqguYpTqFWaJsWNgPFtOLeuB; dnk=question0001; lid=question0001; uc4=id4=0%40U2xqITEc2o4r8f9cMWc4rBx%2BGsvZ&nk4=0%40EJ8nxZHL9p6QYwqM3TlbZkBym7AU%2FZs%3D; csg=3df67a05; enc=3HxD%2BYlThyMLRFwuOqtzss7EF2MZEEy%2FZ%2FxS7LSBCCRhwDVqk2dZm4UbI2VFkiCCCMswz3BHZRIA%2Bb5ZRKzHoA%3D%3D; x5sec=7b22726174656d616e616765723b32223a223932393535376163363334656164346164313038393438653539313765323662434e36356b4f3046454e7974374f7252325975317251453d227d");
                result.addRequest(request);
            }
        } else {
            System.out.println("该商品没有评论！" + page.getUrl().get());
        }

        return result;
    }

    private void getAttribute(Page page, Html html) {

        String brand = page.getRequest().getExtra("brand").toString();

        Selectable attributesList = html.xpath("//div[@id='page']//div[@id='bd']//div[@id='J_AttrList']");

        List<Selectable> nodes = attributesList.xpath("//ul[@id='J_AttrUL']/li").nodes();


        Attribute attribute = new Attribute();
        for (Selectable node : nodes) {
            String title = node.xpath("//li/allText()").get();
            String newTitle = "";
            if (!StringUtils.isEmpty(title)) {
                newTitle = title/*.replace("&nbsp;", "")*/.replace(",", "，");
            }
            if (newTitle.contains("厂名")) {
                attribute.setChangming(newTitle);
            }
            if (newTitle.contains("厂址")) {
                attribute.setChangzhi(newTitle);
            }
            if (newTitle.contains("厂家联系方式")) {
                attribute.setLianxifangshi(newTitle);
            }
            if (newTitle.contains("配料") || newTitle.contains("原料")) {
                attribute.setPeiliao(newTitle);
            }
            if (newTitle.contains("储藏") || newTitle.contains("贮藏")) {
                attribute.setChucun(newTitle);
            }
            if (newTitle.contains("保质期")) {
                attribute.setBaozhiqi(newTitle);
            }
            if (newTitle.contains("添加剂")) {
                attribute.setTianjiaji(newTitle);
            }
            if (newTitle.contains("品牌")) {
                attribute.setPinpai(newTitle);
            }
            if (newTitle.contains("系列")) {
                attribute.setXilie(newTitle);
            }
            if (newTitle.contains("产地")) {
                attribute.setChandi(newTitle);
            }
            if (newTitle.contains("性别")) {
                attribute.setShiyongxingbie(newTitle);
            }
            if (newTitle.contains("生产企业")) {
                attribute.setShengchanqiye(newTitle);
            }
            if (newTitle.contains("剂型")) {
                attribute.setChanpinjixing(newTitle);
            }
            if (newTitle.contains("规格")) {
                attribute.setGuige(newTitle);
            }
            if (newTitle.contains("单位")) {
                attribute.setJijiadanwei(newTitle);
            }
            if (newTitle.contains("用法") || newTitle.contains("方法")) {
                attribute.setYongfa(newTitle);
            }
            if (newTitle.contains("人群")) {
                attribute.setShiyongrenqun(newTitle);
            }
            if (newTitle.contains("提示") || newTitle.contains("注意事项")) {
                attribute.setShiyongtishi(newTitle);
            }
            if (newTitle.contains("省份")) {
                attribute.setShengfeng(newTitle);
            }
            if (newTitle.contains("城市")) {
                attribute.setChengshi(newTitle);
            }
            if (newTitle.contains("产品名称")) {
                newTitle = node.xpath("//li/@title").get();
                attribute.setChanpinmingcheng(newTitle);
            }
        }

        List<Selectable> selectableList = attributesList.xpath("//div[@class='tb-validity']").nodes();
        Selectable dateSele;
        String date = "";
        if (selectableList.size() > 1) {
            dateSele = selectableList.get(1);
            date = dateSele.xpath("//div[@class='tb-validity']/text()").get();
        } else if (CollectionUtils.isNotEmpty(selectableList)) {
            dateSele = selectableList.get(0);
            date = dateSele.xpath("//div[@class='tb-validity']/text()").get();
        }


        attribute.setShengchanriqi(date);

        String pingfen = html.xpath("//div[@id='J_SeaHeader']//div[@id='J_HdShopInfo']//div[@class='hd-shop-desc']/label//a[1]/span/text()").get();

        if (StringUtils.isEmpty(pingfen)) {
            List<Selectable> selectables = html.xpath("//div[@id='header']//div[@id='shopExtra']//div[@class='main-info']//div[@class='shopdsr-item']").nodes();
            if (CollectionUtils.isNotEmpty(selectables)) {
                pingfen = selectables.get(0).xpath("//span[@class='shopdsr-score-con']/text()").get();
            }

        }
        attribute.setPingfen(pingfen);


        List<Selectable> skuList = html.xpath("//div[@id='page']//div[@id='detail']//div[@class='tb-key']//div[@class='tb-sku']/dl").nodes();

        for (Selectable selectable : skuList) {

            String name = selectable.xpath("//dt/text()").get();
            if (!StringUtils.isEmpty(name) && name.contains("颜色分类")) {
                List<Selectable> names = selectable.xpath("//dd/ul/li").nodes();

                String all = "";
                for (Selectable node : names) {
                    String str = node.xpath("//li//span/text()").get();
                    all = all + str + "，";
                }
                attribute.setYansefenlei(all);
            }

            if (!StringUtils.isEmpty(name) && name.contains("口味")) {
                List<Selectable> names = selectable.xpath("//dd/ul/li").nodes();

                String all = "";
                for (Selectable node : names) {
                    String str = node.xpath("//li//span/text()").get();
                    all = all + str + "，";
                }
                attribute.setKouwei(all);
            }

        }


        String content = html.xpath("//meta[@name='keywords']/@content").get();


        File file = new File(filePath);
        if (!file.exists()) {
            try {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    String header = "链接,商品名,厂名,厂址,厂家联系方式,原料,储藏,保质期,食品添加剂,品牌,系列,产地,省份,城市,适用性别,生产企业,产品剂型,产品名称,规格,计价单位,用法,适用人群,食用提示,生产日期,评分,颜色分类,口味\n";
                    FileUtils.writeStringToFile(file, header, Charset.forName("GBK"), true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            String writeContent =
                    brand + "," +
                            content + "," +
                            attribute.getChangming() + "," +
                            attribute.getChangzhi() + "," +
                            attribute.getLianxifangshi() + "," +
                            attribute.getPeiliao() + "," +
                            attribute.getChucun() + "," +
                            attribute.getBaozhiqi() + "," +
                            attribute.getTianjiaji() + "," +
                            attribute.getPinpai() + "," +
                            attribute.getXilie() + "," +
                            attribute.getChandi() + "," +
                            attribute.getShengfeng() + "," +
                            attribute.getChengshi() + "," +
                            attribute.getShiyongxingbie() + "," +
                            attribute.getShengchanqiye() + "," +
                            attribute.getChanpinjixing() + "," +
                            attribute.getChanpinmingcheng() + "," +
                            attribute.getGuige() + "," +
                            attribute.getJijiadanwei() + "," +
                            attribute.getYongfa() + "," +
                            attribute.getShiyongrenqun() + "," +
                            attribute.getShiyongtishi() + "," +
                            attribute.getShengchanriqi() + "," +
                            attribute.getPingfen() + "," +
                            attribute.getYansefenlei() + "," +
                            attribute.getKouwei() + "," +
                            "\n";

            FileUtils.writeStringToFile(file, writeContent, Charset.forName("GBK"), true);
            logger.info("成功写入一条");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


/*    //获取各大钻石品牌 的价格信息
    private Result getShopPrice(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        Map<String, List<PropertyInfo>> map = new LinkedHashMap<>();
        String brand = page.getRequest().getExtra("brand").toString();
        String shopName = html.xpath("//meta[@name='keywords']/@content").get().replace("/", " ");
        List<Selectable> nodes = html.xpath("//dl[@class='tb-prop tm-sale-prop tm-clear ']").nodes();
        for (Selectable node : nodes) {
            String property = node.xpath("//dt[@class='tb-metatit']/text()").get();
            List<String> keys = node.xpath("//ul/li/a/span/text()").all();
            List<String> values = node.xpath("//ul/li/@data-value").all();
            List<PropertyInfo> propertyInfos = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                propertyInfos.add(new PropertyInfo(keys.get(i), values.get(i)));
            }
            map.put(property, propertyInfos);
        }


        List<String> scripts = html.xpath("script").all();
        String shopApiJson = null;
        for (String script : scripts) {
            if (script.contains("TShop.Setup(")) {
                shopApiJson = script.substring(script.indexOf("TShop.Setup(") + 13, script.lastIndexOf("})();") - 4);
                break;
            }
        }


        Map<String, String> match = new HashMap<>();
        List<String> priceKeys = new ArrayList<>();
        List<String> matchKeys = new ArrayList<>();
        map.forEach((key, value) -> {
            if (CollectionUtils.isEmpty(priceKeys)) {
                value.forEach(propertyInfo -> priceKeys.add(";" + propertyInfo.value + ";"));
                value.forEach(propertyInfo -> matchKeys.add(propertyInfo.key + ";"));
            } else {
                List<String> localPriceKeys = new ArrayList<>(priceKeys);
                priceKeys.clear();
                localPriceKeys.forEach(localPriceKey -> value.forEach(val -> {
                    String priceKey = localPriceKey + val.value + ";";
                    priceKeys.add(priceKey);
                }));

                List<String> localMatchKeys = new ArrayList<>(matchKeys);
                matchKeys.clear();
                localMatchKeys.forEach(localMatchKey -> value.forEach(val -> {
                    String matchKey = localMatchKey + val.key + ";";
                    matchKeys.add(matchKey);
                }));
            }
        });

        for (int i = 0; i < matchKeys.size(); i++) {
            match.put(matchKeys.get(i), priceKeys.get(i));
        }

        if (nonce) {
            String head = String.join(",", map.keySet());
            head = "品牌," + "商品," + head.trim() + ",价格" + ",链接";
            WriteFile.write(head, file);
            nonce = false;
        }
        if (Optional.ofNullable(shopApiJson).isPresent()) {
            final String json = shopApiJson;
            match.forEach((matchKey, priceKey) -> {
                if (!json.contains(priceKey)) {
                    return;
                }
                String price = JsonPath.read(json, "$.valItemInfo.skuMap." + priceKey + ".price");
                String content = String.join(",", matchKey.split(";"));
                content += "," + price + "," + page.getUrl().get();
                content = brand + "," + shopName + "," + content;
                WriteFile.write(content, file);
            });
        }
        return result;
    }

    // 钻石净度 data-value  --- https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.1.2ae71361D37VOA&id=38952752663&skuId=3258776561011&areaId=110100&user_id=2087981999&cat_id=2&is_b=1&rn=6855d421060b48e22b2848378e63be87
    // SI/小瑕 : 21735:43431
    private class PropertyInfo {
        String key;
        String value;

        PropertyInfo(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }*/


    public static void main(String[] args) throws Exception {
        String url = "https://detail.tmall.com/item.htm?id=3873545362&skuId=3713223134050&user_id=92449256&cat_id=50026473&is_b=1&rn=c6537c76b07707b6f8f52a72bfe9d564";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url).putExtra("brand", "珂兰"));
        TmallProductPage productPage = new TmallProductPage();
        productPage.parse(page);
    }
}
