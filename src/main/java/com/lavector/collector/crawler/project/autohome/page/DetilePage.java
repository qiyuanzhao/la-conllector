package com.lavector.collector.crawler.project.autohome.page;


import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.readData.SkuData;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class DetilePage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(DetilePage.class);

//    @Autowired
//    private RestTemplate restTemplate;

    @Override
    public boolean handleUrl(String url) {
        return url.contains("http://club.autohome.com.cn/bbs/forum-c-") || url.contains("https://club.autohome.com.cn/bbs/forum-c-");
    }

    @Override
    public Result parse(Page page) {
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        String url = page.getUrl().get();
        List<Selectable> nodes;
        try {
            nodes = page.getHtml().xpath("//div[@class='carea']/div[@id='subcontent']/dl[@class='list_dl']").nodes();
        } catch (Exception e) {
            logger.info("下载页面失败....");
            return null;
        }
        nodes.remove(0);
        List<AutoHomeEntity> autoHomeEntityList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(nodes)) {
            nodes.forEach(node -> {
                AutoHomeEntity autoHomeEntity = new AutoHomeEntity();
                String title = node.xpath("//dl[@class='list_dl']/dt/a/text()").get();
                String titleUrl = "https://club.autohome.com.cn" + node.xpath("//dl[@class='list_dl']/dt/a/@href").get();
                String userName = node.xpath("//dl[@class='list_dl']/dd[1]/a/text()").get();
                String date = node.xpath("//dl[@class='list_dl']/dd[1]/span/text()").get();
                String code = node.xpath("//dl[@class='list_dl']/dd[@class='cli_dd']/@lang").get();

                autoHomeEntity.setTitle(title);
                autoHomeEntity.setUserName(userName);
                autoHomeEntity.setDate(date);
                autoHomeEntity.setUrl(url);
                autoHomeEntity.setCode(code);
//                autoHomeEntityList.add(autoHomeEntity);

                Request request = new Request();
                request.putExtra("skuData", skuData);
                request.putExtra("autoHomeEntity", autoHomeEntity);
                request.setUrl(titleUrl);
                page.addTargetRequest(request);
            });
        }
        page.setSkip(true);
//        getReplysAndViews(autoHomeEntityList);

//        page.getRequest().putExtra("autoHomeEntityList", autoHomeEntityList);

//        page.putField("skuData", skuData);
//        page.putField("autoHomeEntityList", autoHomeEntityList);

        return null;
    }

    private void getReplysAndViews(List<AutoHomeEntity> autoHomeEntityList) {

        String ids = "";
        if (CollectionUtils.isNotEmpty(autoHomeEntityList)) {
            for (AutoHomeEntity autoHomeEntity : autoHomeEntityList) {
                if (!StringUtils.isEmpty(autoHomeEntity.getCode())) {
                    ids += autoHomeEntity.getCode() + "%2C";
                }
            }
        }
        if (!StringUtils.isEmpty(ids)) {
            String url = "https://clubajax.autohome.com.cn/topic/rv?fun=jsonprv&callback=jsonprv&ids=" + ids;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("cookie", "sessionip=43.254.91.82; sessionid=4F9CAAA1-3084-496B-9255-EEA0DF96B2D7%7C%7C2018-11-30+15%3A16%3A45.562%7C%7C0; area=110105; __ah_uuid=32167255-89A5-4D39-84DD-1086C31A5E57; fvlid=1543562201097IL4wrxg3ty; sessionuid=4F9CAAA1-3084-496B-9255-EEA0DF96B2D7%7C%7C2018-11-30+15%3A16%3A45.562%7C%7C0; ahpau=1; historybbsName4=c-4080%7C%E8%8D%A3%E5%A8%81RX5%2FRX5%E6%96%B0%E8%83%BD%E6%BA%90%2Cc-4309%7C%E4%B8%AD%E5%8D%8EH230EV%2Cc-4845%7C%E7%BA%A2%E6%97%97H7%E6%96%B0%E8%83%BD%E6%BA%90; Hm_lvt_9924a05a5a75caf05dbbfb51af638b07=1548997477; ahsids=2357_4078_2664; Hm_lpvt_9924a05a5a75caf05dbbfb51af638b07=1548997526; sessionvid=75B6F8FE-41CD-485A-840C-C4B3D815E00D; ahpvno=39; pvidchain=100834,3311668,100834; ref=www.baidu.com%7C0%7C0%7Cwww.sogou.com%7C2019-02-01+14%3A20%3A48.133%7C2019-02-01+10%3A55%3A01.164; ahrlid=1549002044382hSLmNe02X8-1549002060647");
            httpHeaders.add("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Mobile Safari/537.36");
            HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);
//            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
//            String group = RegexUtil.findFirstGroup(responseEntity.getBody(), "\\[.*\\]");

        }


    }

    @Override
    public String pageName() {
        return null;
    }
}
