package com.lavector.collector.crawler.project.iqiyi;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.iqiyi.page.IQiYiSearchPage;
import com.lavector.collector.crawler.project.weixinnew.page.SearchPage;
import us.codecraft.webmagic.Site;

import java.util.Random;

public class IQiYiProcesser extends BaseProcessor {


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("Cookie", "__uuid=990b7361-d5ee-3766-d762-8d04959ee390; QC005=b2941925a50ff12c5d6cbc93326eb594; QC006=ge7yhx9otrhjt881k15gyto0; T00404=c17e31195029c453c9884240d39454ae; QC173=0; QC124=1%7C0; QP001=1; QC118=%7B%22color%22%3A%22FFFFFF%22%2C%22channelConfig%22%3A0%7D; QP0017=100; QP0018=100; QC175=%7B%22upd%22%3Atrue%2C%22ct%22%3A%22%22%7D; QP0013=; unrepeatCookie=600000001%2C; QC021=%5B%7B%22key%22%3A%22%E8%B5%84%E7%94%9F%E5%A0%82%22%7D%2C%7B%22key%22%3A%22%E7%99%BE%E4%BA%8B%E5%8F%AF%E4%B9%90%22%7D%2C%7B%22key%22%3A%22%E7%99%BE%E4%BA%8B%E5%8F%AF%E4%B9%90%E6%B1%AA%E6%B1%AA%E9%98%9F%E7%AB%8B%E5%A4%A7%E5%8A%9F%22%7D%5D; QC159=%7B%22color%22%3A%22FFFFFF%22%2C%22channelConfig%22%3A1%2C%22hadTip%22%3A1%2C%22isOpen%22%3A1%2C%22speed%22%3A13%2C%22density%22%3A30%2C%22opacity%22%3A86%2C%22isFilterColorFont%22%3A1%2C%22proofShield%22%3A0%2C%22forcedFontSize%22%3A24%2C%22isFilterImage%22%3A1%7D; QP007=1140; T00700=EgcIysDtIRABEgcI9L-tIRABEgcI0b-tIRABEgcIz7-tIRABEgcImMDtIRABEgcI58DtIRABEgcI77-tIRAHEgcIhcDtIRABEgcI5MDtIRABEgcIg8DtIRABEgcI67-tIRABEgcI5cDtIRAB; QYABEX={\"pcw_home_movie\":{\"value\":\"old\",\"abtest\":\"146_A\"},\"BI-Rec-pcw_listbox-beta-1572940760487\":{\"value\":\"pcw_clip\",\"abtest\":\"254_A\"},\"BI-Rec-pcw_listbox-prod-1572940936761\":{\"value\":\"pcw_clip\",\"abtest\":\"255_A\"},\"BI-Rec-pcw_theme-beta-1573121102824\":{\"value\":\"rm_fs_sf\",\"abtest\":\"294_A\"},\"BI-Rec-pcw_theme-prod-1573124684364\":{\"value\":\"rm_fs_sf\",\"abtest\":\"296_A\"}}; Hm_lvt_53b7374a63c37483e5dd97d78d9bb36e=1572431690,1574148993,1574149277,1574323677; websocket=true; QC007=https%253A%252F%252Fwww.baidu.com%252Flink%253Furl%253DeOIkEkXJqXIKr7RL4DGowCIEA_qls5KBwWNh8j1fcPO%2526wd%253D%2526eqid%253D8dcced860001b4af000000025dd645d9; QC008=1544429527.1572431690.1574323679.5; nu=0; QC178=true; JSESSIONID=aaa08dEKJkvFI9M34yF5w; QC176=%7B%22state%22%3A0%2C%22ct%22%3A1574323689044%7D; PCAU=0; Hm_lpvt_53b7374a63c37483e5dd97d78d9bb36e=1574323841; IMS=IggQABj_6NruBSokCiA2NzEwYzRiN2MxMTcyNjczYTQxM2U2YTdkNGZiNDQ2YhAA; QC010=93931289; __dfp=a16458b3e3e391493da2997d0cf6e859270a16a626ea37462e55153101f476031f@1575444996452@1574148997452")
//            .addHeader("Referer","https://news.sogou.com/news?ie=utf8&p=40230447&interV=kKIOkrELjboMmLkEkLYTkKIMkLELjbgQmLkElbcTkKIMkbELjb8TkKILmrELjbgRmUHpGz2IOzXejb0Ew+dByOsG0OV/zPsGwOVFmUHpGHIElKJLzO5Nj+lHzrGIOzzgjb0Ew+dBxfsGwOVF_506047069&query=%E8%B5%84%E7%94%9F%E5%A0%82&")
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36")
            .setTimeOut(10 * 1000);

    public IQiYiProcesser(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo, new IQiYiSearchPage());
    }


    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(10000);
        return site.setSleepTime(3000 + time);
    }

}
