package com.lavector.collector.crawler.project.food.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.food.RecommendConfig;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.crawler.util.UrlUtils;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.util.List;

/**
 * Created on 2017/11/6.
 *
 * @author zeng.zhao
 */
public class ShopDetailPage implements PageParse {

    private static final String token = "eJx1jF1vgjAUhv9Lrxsp2tJCsgtFcTh1UZksLFzwoYB8SC3iZNl/X8242cWSkzznPOfN+wUudgwMFSGEVQgaIXeCdJ2qeISJTiCI/jqNahCEl/0UGB/y1uCI6f7DbKWQBiOoI+TD3yej1IdDLOeRsWUEpE1TG4pyu90GcRZUdVYlg+hcKiI91wqlhOlDwgD8NwdkU+nIJsm8Z9Cz6SmypAIGOCw+nZ3Agh+3K+Hsd3c0WnXP69flrFh3d2aaJAn45D0vtMgdepxb7jG3utNUtWd4YwfkGpRW7c3riXdqC5KFQXtlYcQ7LRYHZy9VWuXza1fjhLsmz8Vu+aaurbE5flm2i/j8BL5/AMXpaUw=";

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch(".*www.dianping.com/shop/\\d+", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String category = page.getRequest().getExtra("category").toString();
        String shopId = page.getUrl().regex("http://www.dianping.com/shop/(\\d+)").get();
        String dishPageUrl = "http://www.dianping.com/shop/" + shopId + "/dishlist";
        us.codecraft.webmagic.Request request = new us.codecraft.webmagic.Request(dishPageUrl);
        request.putExtra("category", category);
        result.addRequest(request);
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private void getRecommendDish(Html html) {
        List<String> scripts = html.xpath("//script").all();
        String shop_config = "";
        for (String script : scripts) {
            if (script.contains("window.shop_config")) {
                shop_config = script;
                break;
            }
        }
        RecommendConfig config = new RecommendConfig();
        String shopId = new Json(shop_config).regex("shopId: (\\d+),\n").get();
        config.setShopId(shopId);
        String cityId = new Json(shop_config).regex("cityId: \"(\\d+)\",\n").get();
        config.setCityId(cityId);
//        String shopName = new Json(shop_config).regex("shopName: \"(.*)\",\n").get();
        String shopName = html.xpath("//meta[@itemprop='name']/@content").get();
        config.setShopName(shopName);
        String power = new Json(shop_config).regex("power:(\\d+),\n").get();
        config.setPower(power);
        String shopType = new Json(shop_config).regex("shopType:(\\d+),\n").get();
        config.setShopType(shopType);
        String mainCategoryId = new Json(shop_config).regex("mainCategoryId:(\\d+),\n").get();
        config.setCategoryId(mainCategoryId);
        String url = "http://www.dianping.com/ajax/json/shopDynamic/shopTabs?" +
                "shopId=" + config.getShopId()
                + "&cityId=" + config.getCityId()
                + "&shopName=" + UrlUtils.encodeStr(config.getShopName())
                + "&power=" + config.getPower()
                + "&mainCategoryId=" + config.getCategoryId()
                + "&shopType=" + config.getShopType()
                + "&shopCityId=" + config.getCityId();
//                + "&_token=" + token;
        try {
            String content = Request.Get(url)
                    .addHeader("host", "www.dianping.com")
                    .addHeader("referer", "http://www.dianping.com/shop/77589258")
                    .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                    .execute()
                    .returnContent().asString();
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.dianping.com/shop/24780098";
        String content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        Html html = new Html(content);
        ShopDetailPage detailPage = new ShopDetailPage();
        detailPage.getRecommendDish(html);
    }
}
