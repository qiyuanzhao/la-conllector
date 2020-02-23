package com.lavector.collector.crawler.project.tmall.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2018/1/15.
 *
 * @author zeng.zhao
 */
public class TmallSearchListPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(TmallSearchListPage.class);

    @Override
    public boolean handleUrl(String url) {
        return url.contains("search_product.htm");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        return parsePageByKeyword(page);
    }

    @SuppressWarnings("unchecked")
    private Result parsePageByKeyword(Page page) {
        Result result = Result.get();


        getSearchList(page);
//

//        getProductBrand(page);


        page.setSkip(true);
        return result;
    }

    /**
     * 获取品牌信息
     *
     * @param page
     */
    private void getProductBrand(Page page) {

        String brand = page.getRequest().getExtra("brand").toString();

        Html html = page.getHtml();


        Selectable options = html.xpath("//div[@class='page']//div[@id='content']//div[@class='attrValues']//div[@class='av-options']");

        String url = options.xpath("//a[@class='j_More avo-more ui-more-drop-l']/@data-url").get();
        if (StringUtils.isEmpty(url)) {
            return;
        }

        url = "http:" + url.replace("amp;", "");
        logger.info("获取的品牌链接:{}", url);
        page.addTargetRequest(new Request(url).putExtra("brand", brand));

    }


    /**
     * 获取商品列表
     *
     * @param page
     */
    private void getSearchList(Page page) {
        Html html = page.getHtml();
        String brand = page.getRequest().getExtra("brand").toString();
//        String gongxiao = page.getRequest().getExtra("gongxiao").toString();
        Integer number = (Integer) page.getRequest().getExtra("number");
//        List<String> excludeKeywords = (List<String>) page.getRequest().getExtra("excludeKeywords");
        List<Selectable> nodes = html.xpath("//div[@class='product']").nodes();
        if (nodes.size() == 0) {
            System.out.println(nodes);
        }
        nodes.stream().filter(node -> {
//            String goodsName = node.xpath("//p[@class='productTitle']/a/allText()").get();
//            if (goodsName == null) {
//                goodsName = node.xpath("//div[@class='productTitle']/allText()").get();
//            }
//            for (String excludeKeyword : excludeKeywords) {
//                if (goodsName.contains(excludeKeyword)) {
//                    logger.info("该商品包含排除词, {}, {}", goodsName, excludeKeyword);
//                    return false;
//                }
//            }

//            return goodsName.contains(keyword);
            return true;
        }).forEach(node -> {
            String goodsName = node.xpath("//p[@class='productTitle']/a/allText()").get();
            if (goodsName == null) {
                goodsName = node.xpath("//div[@class='productTitle']/allText()").get();
            }

            String price = node.xpath("//p[@class='productPrice']/em/text()").get();
            String comment = node.xpath("//p[@class='productStatus']/span[2]/a/text()").get();


            String goodsId = node.xpath("//p[@class='productTitle']/a/@href").regex("htm\\?id=(\\d+)").get();
            String skuId = node.xpath("//p[@class='productTitle']/a/@href").regex("skuId=(\\d+)").get();

            if (goodsId == null) {
                goodsId = node.xpath("//div[@class='productTitle']/a[1]/@href").regex("htm\\?id=(\\d+)").get();
            }
            if (skuId == null) {
                skuId = node.xpath("//div[@class='productTitle']/a[1]/@href").regex("skuId=(\\d+)").get();
            }
            logger.info("skuId : " + skuId);
            String sellCount = node.xpath("//p[@class='productStatus']/span[1]/em/text()").get();
            if (sellCount == null) {
                return;
            }
            sellCount = sellCount.replace("笔", "");
//            String path = "/Users/zeng.zhao/Desktop/tmall/" + brand + ".csv";
            String path = "G:/text/tmall/data/" + "商品列表按新品" + ".csv";
            try {
                if (!new File(path).exists()) {
                    boolean newFile = new File(path).createNewFile();
                    if (newFile) {
                        String header = "类别,商品,月销量,价格,评论量,链接\n";
                        FileUtils.writeStringToFile(new File(path), header, Charset.forName("GBK"), true);
                    }
                }
                String writeContent = brand + "," + goodsName.replace(",", "，") + "," + sellCount + "," + price + "," + comment + "," + "https://detail.tmall.com/item.htm?id=" + goodsId;
                if (!StringUtils.isEmpty(skuId)) {
                    writeContent = writeContent + "&skuId=" + skuId + "\n";
                } else {
                    writeContent = writeContent + "\n";
                }

                FileUtils.writeStringToFile(new File(path), writeContent, Charset.forName("GBK"), true);
                logger.info("写入一条数据。。。");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        if (number<2){
            String nextUrl = html.xpath("//a[@class='ui-page-next']/@href").toString();
            if (StringUtils.isNotBlank(nextUrl)) {
                if (!nextUrl.contains("https://list.tmall.com/search_product.htm")) {
                    nextUrl = "https://list.tmall.com/search_product.htm" + nextUrl;
                }
                page.addTargetRequest(new Request(nextUrl).putExtra("brand", brand)./*putExtra("gongxiao", gongxiao).*/putExtra("number",2));
//            result.addRequest(new Request(nextUrl).putExtra("brand", brand).putExtra("excludeKeywords", excludeKeywords));
//                .putExtra("keyword", keyword));
            }
        }
        page.setSkip(true);
    }

    private String getGoodsInfo(String goodsId) {
        String url = "https://mdskip.taobao.com/core/initItemDetail.htm?isUseInventoryCenter=false&cartEnable=true&service3C=false&isApparel=false&isSecKill=false&tmallBuySupport=true&isAreaSell=false&tryBeforeBuy=false&offlineShop=false&itemId=" + goodsId + "&showShopProm=false&isPurchaseMallPage=false&itemGmtModified=1542181419000&isRegionLevel=false&household=false&sellerPreview=false&queryMemberRight=true&addressLevel=2&isForbidBuyItem=false&callback=setMdskip&timestamp=1542186009636&isg=null&isg2=BJaWNN590Yq48uUu0yTv5Ec050pYn9uRVOflpgD_63kUwzZdaMfbgf_yXx-K69KJ&areaId=110100&cat_id=2";
        try {
            return org.apache.http.client.fluent.Request.Get(url)
                    .addHeader("referer", "https://detail.tmall.com/item.htm")
                    .execute()
                    .returnContent()
                    .asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Result parseZuanShi(Page page) {
        Result result = Result.get();
        String brand = page.getRequest().getExtra("brand").toString();
        Html html = page.getHtml();
        List<String> urls = html.xpath("//p[@class='productTitle']/a/@href").all();
        List<Request> requests = urls.stream().map(url -> {
            if (!url.contains("http:") || !url.contains("https:")) {
                url = "http:" + url;
            }
            Request request = new Request(url);
            request.putExtra("brand", brand);
            return request;
        }).collect(Collectors.toList());

        for (int i = 0; i < requests.size(); i++) {
            if (i >= 10) {
                break;
            }
            result.addRequest(requests.get(i));
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://list.tmall.com/search_product.htm?q=%D5%EB%D6%AF%C9%C0";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(url));
        page.setRequest(new Request(url));
        new TmallSearchListPage().parse(page);
    }
}
