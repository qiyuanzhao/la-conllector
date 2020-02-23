package com.lavector.collector.crawler.project.weibo.weiboPerson;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.PersonDetilPage;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.PersonPage;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SameFollowPage;
import us.codecraft.webmagic.Site;

import java.util.Random;

public class WeiboPersonProcesser extends BaseProcessor {


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            //SINAGLOBAL=9262762579760.895.1542259856140; UOR=,,login.sina.com.cn; _s_tentry=-; Apache=1576104332918.8884.1565580626049; ULV=1565580626062:5:1:1:1576104332918.8884.1565580626049:1564453376032; login_sid_t=dc9b27d1e87a25b4b3853a931131f555; cross_origin_proto=SSL; webim_unReadCount=%7B%22time%22%3A1565922256845%2C%22dm_pub_total%22%3A1%2C%22chat_group_client%22%3A0%2C%22allcountNum%22%3A14%2C%22msgbox%22%3A0%7D; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhCYUFE6FDNcQ_.N.4UgvQu5JpX5K2hUgL.Fo-Neo5XSh-7SK.2dJLoIEXLxK-LBo5L12qLxK.LBKzL1KqLxKML1-2L1hBLxK-L1KeLBK-LxK-L1KeLBK-t; ALF=1597458276; SSOLoginState=1565922277; SCF=AtgnXFBDvRA-ACAFTJbJ_U2hd-M9-tcEg6lbqA47ypoCWMabGRGdX5e1o5hCgxVhRkPhOGv7CtdHatYVjZkXjoU.; SUB=_2A25wUmO1DeRhGeNJ6VIV9CvMzjWIHXVTJtJ9rDV8PUNbmtAKLVXCkW9NS_6kL5aOCXG5UHHE_nMuqsnMthYVxHrW; SUHB=0gHiK0cNWKaU84; un=13295431080; wvr=6; WBStorage=4b0a0a9ea4ac3871|undefined
//            .addHeader("Cookie", "SINAGLOBAL=4055574598565.138.1552739633784; ULV=1568630019422:24:4:1:9165706386253.932.1568630019417:1568095257220; SCF=At11QL9NiB6T5mv9M1TSBoZ2V0Ox3PCarw74beZqbVjuNGCr0Vhecja3_PGKPb4Kesc6ItzklZuxZ9B571A_m7U.; SUHB=0auQNnCU0pCVBp; UOR=,,login.sina.com.cn; Ugrow-G0=9ec894e3c5cc0435786b4ee8ec8a55cc; TC-V5-G0=42b289d444da48cb9b2b9033b1f878d9; login_sid_t=63e270d6c86094293606d8ccf1c7bf2e; cross_origin_proto=SSL; _s_tentry=login.sina.com.cn; Apache=9165706386253.932.1568630019417; un=86oz68nu@duoduo.cafe; TC-Page-G0=ffc89a27ffa5c92ffdaf08972449df02|1568865979|1568865970; webim_unReadCount=%7B%22time%22%3A1568865961195%2C%22dm_pub_total%22%3A3%2C%22chat_group_client%22%3A0%2C%22allcountNum%22%3A13%2C%22msgbox%22%3A0%7D; WBtopGlobal_register_version=307744aa77dd5677; secsys_id=adf4d275857a5ee3397dd3d5816e36e5; SUB=_2AkMq3nGbdcPxrARTkfASxGvqZYhH-jyZCxhtAn7uJhMyAxh77k0vqSVutBF-XM94CxGouGrxN7nNK6h8QsPzXicr; SUBP=0033WrSXqPxfM72wWs9jqgMF55529P9D9WhHVwjyF4ogcX2yEVsw_9k_5JpV8hncdNQc1s4fBsxfdcxfdE4oqg9L; wb_view_log_7298780895=1920*10801; wb_timefeed_7298780895=1; WBStorage=1e0922ef6c40c94d|undefined; wb_view_log=1920*10801")
            .addCookie("Cookie","Ugrow-G0=d52660735d1ea4ed313e0beb68c05fc5; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WF9iDhqz1jik-vRlE.XOrYN; login_sid_t=62428204737f5748042bf4f7a781acbc; cross_origin_proto=SSL; TC-V5-G0=799b73639653e51a6d82fb007f218b2f; WBStorage=42212210b087ca50|undefined; _s_tentry=passport.weibo.com; wb_view_log=1366*7681; Apache=193202714760.84726.1574957086317; SINAGLOBAL=193202714760.84726.1574957086317; ULV=1574957086324:1:1:1:193202714760.84726.1574957086317:; SUB=_2AkMqg35Vf8NxqwJRmP4RzG7kZIl1yg7EieKc34-OJRMxHRl-yj9kqhQQtRB6AQNQuhSPEata63L0dbPl2trkcnUIkWXU; TC-Page-G0=b993e9b6e353749ed3459e1837a0ae89|1574957667|1574957410")
//            .addHeader("User-Agent", randomUserAgent())
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
//            .addHeader("Host","weibo.com")
//            .addHeader("Connection","keep-alive")
//            .addHeader("Cache-Control","max-age=0")
//            .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//            .addHeader("Upgrade-Insecure-Requests","1")
//            .addHeader("Accept-Encoding","gzip, deflate, br")
//            .addHeader("Accept-Language","zh-CN,zh;q=0.9")
//            .addHeader("Cache-Control","max-age=0")
            .setTimeOut(10 * 1000);

    public WeiboPersonProcesser(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo, new PersonPage(), new PersonDetilPage(),new SameFollowPage());
    }


    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(5000);
        return site.setSleepTime(3000);
    }

}
