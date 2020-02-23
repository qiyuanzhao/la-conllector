package com.lavector.collector.crawler.project.gengmei.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.gengmei.entity.ArrProduct;
import com.lavector.collector.crawler.util.RegexUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrProductPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(ArrProductPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://www.soyoung.com/searchNew/product.*", url);
    }

    @Override
    public Result parse(Page page) {

        logger.info("******* URL {}" ,page.getUrl().get());

        String text = page.getRawText();
        System.out.println(text);

        List<ArrProduct> arrProducts = new ArrayList<>();
        JSONObject jsonObject = JSONObject.fromObject(text);
        JSONArray jsonArray = jsonObject.getJSONObject("responseData").getJSONArray("arr_product");
        for (Object o : jsonArray){
            JSONObject jsonObject1 = JSONObject.fromObject(o);
            jsonObject1.remove("wei_kuan");
            jsonObject1.remove("wei_kuan_list");
            jsonObject1.remove("tuan");
            jsonObject1.remove("pin_tuan");
            jsonObject1.remove("doctor");
            ArrProduct arrProduct = (ArrProduct) JSONObject.toBean(jsonObject1, ArrProduct.class);
            arrProduct.setUrl("https://y.soyoung.com/cp" + arrProduct.getPid());
            arrProducts.add(arrProduct);
        }
        if (jsonArray.size() > 0) {
            String url = page.getUrl().get();
            String number = url.substring(url.indexOf("page=") + 5);
            int anInt = Integer.parseInt(number) + 1;
            Request request = new Request("https://www.soyoung.com/searchNew/product?keyword=%E6%B6%A6%E8%87%B4&cityId=&page_size=100&_json=1&sort=0&service=&coupon=&group=&maxprice=&minprice=&page=" + anInt);
            page.addTargetRequest(request);
        }
        page.getRequest().putExtra("arrProducts",arrProducts);
        page.putField("arrProducts",arrProducts);
        return null;
    }

    @Override
    public String pageName() {
        return null;
    }

}
