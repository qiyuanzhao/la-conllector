package com.lavector.collector.crawler.nonce.dianping;

import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.dianping.entity.DianPingMessage;
import com.lavector.collector.crawler.nonce.dianping.entity.Shop;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 14/05/2018.
 *
 * @author zeng.zhao
 */
public class DianPingShopPage implements PageParse {


    private Map<String, String> rankMap = new HashMap<>();

    {
        rankMap.put("mid-rank-stars mid-str50", "5.0");
        rankMap.put("mid-rank-stars mid-str40", "4.0");
        rankMap.put("mid-rank-stars mid-str30", "3.0");
        rankMap.put("mid-rank-stars mid-str20", "2.0");
        rankMap.put("mid-rank-stars mid-str10", "1.0");

        rankMap.put("mid-rank-stars mid-str15", "1.5");
        rankMap.put("mid-rank-stars mid-str25", "2.5");
        rankMap.put("mid-rank-stars mid-str35", "3.5");
        rankMap.put("mid-rank-stars mid-str45", "4.5");
        rankMap.put("mid-rank-stars mid-str5", "0.5");
        rankMap.put("mid-rank-stars mid-str05", "0.5");
    }

    private JsonMapper mapper = JsonMapper.buildNormalBinder();

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.dianping.com/shop/\\d+", url);
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String shopName = html.xpath("//h1[@class='shop-name']/text()").get();
        if (shopName == null) {
            shopName = html.xpath("//h1[@class='shop-title']/text()").get();
        }
        if (shopName == null) {
            shopName = html.xpath("//div[@class='shop-name']/h1/text()").get();
        }
        if (shopName == null) {
            shopName = html.xpath("//h1[@itemprop='name']/text()").get();
        }
        String province = html.xpath("//meta[@name='location']/@content").regex("province=(.*);city").get();
        String city = html.xpath("//meta[@name='location']/@content").regex("city=(.*);").get();
        if (city == null) {
            city = html.xpath("//meta[@name='location']/@content").regex("city=(.*)").get();
        }
        if (StringUtils.isBlank(city)) {
            city = "南京";
        }
        if (StringUtils.isBlank(province)) {
            province = "江苏";
        }
        String address = getAddress(html);
        if (address == null) {
            address = html.xpath("//span[@itemprop='street-address']/text()").get();
        }
        if (address == null) {
            address = html.xpath("//div[@class='address']/text()").get();
        }
        if (address == null) {
            address = html.xpath("//div[@class='shop-addr']/span/text()").get();
        }
        if (address == null) {
            address = html.xpath("//div[@itemprop='address']/span[1]/text()").regex("地址：(.*)").get();
        }
        if (address == null) {
            address = html.xpath("//span[@class='fl road-addr']/text()").get();
        }
        if (address == null) {
            address = html.xpath("//p[@class='shop-contact address']/text()").get();
        }
        if (address == null) {
            System.out.println();
        }
        String shopId = page.getUrl().regex("shop/(\\d+)").get();
        String rank = html.xpath("//div[@class='brief-info']/span[1]/@class").get();
        List<Selectable> nodes = html.xpath("//div[@class='brief-info']/span[@id='comment_score']/span").nodes();
        List<Map<String, String>> reviewRank = nodes.stream()
                .map(node -> {
                    String[] scoreKV = node.xpath("span/text()").get().trim().split(":");
                    Map<String, String> map = new HashMap<>();
                    map.put(scoreKV[0], scoreKV[1]);
                    return map;
                })
                .collect(Collectors.toList());
        String price = html.xpath("//div[@class='brief-info']/span[@id='avgPriceTitle']/text()").get();
        if (price != null) {
            String[] priceKV = price.trim().split(":");
            Map<String, String> map = new HashMap<>();
            map.put(priceKV[0], priceKV[1]);
            reviewRank.add(map);
        }

        String category = html.xpath("//span[@class='cate-item all-cate J-all-cate']/text()").regex("全部(.*)分类").get();

        Shop shop = new Shop();
        shop.setName(shopName);
        shop.setCategory(category);
        shop.setAddress(address);
        shop.setCity(city);
        shop.setProvince(province);
        shop.setRank(rankMap.get(rank) == null ? "0" : rankMap.get(rank));
        shop.setShopId(shopId);
        shop.setUrl(page.getUrl().get());
        shop.setReviewRank(reviewRank);

        WriteFile.write(mapper.toJson(shop), "/Users/zeng.zhao/Desktop/dianping_shop_1.json");

//        result.addRequest(new Request(page.getUrl().get() + "/review_all/p1?queryType=sortType&queryVal=latest")
//                .putExtra("referer", page.getUrl().get()));
        return result;
    }

    private String getAddress(Html html) {
        String[] address = new String[1];
        List<String> scripts = html.xpath("//script").all();
        scripts.stream()
                .filter(s -> s.contains("window.shop_config="))
                .findAny()
                .ifPresent(s ->
                        address[0] = new PlainText(s).regex("address: \"(.*)\", publicTransit").get()
                );

        return address[0];
    }

    public static void main(String[] args) throws IOException {
        String url = "http://www.dianping.com/shop/23061804";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url));
        page.setUrl(new PlainText(url));
        new DianPingShopPage().parse(page);
    }
}
