package com.lavector.collector.crawler.project.weibo.weiboPepsiCola;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page.WeiboAjaxCommentPage;
import com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page.WeiboDetilPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Random;


public class WeibolPepsiColaPageProcessor implements PageProcessor{

    private Logger logger = LoggerFactory.getLogger(WeibolPepsiColaPageProcessor.class);


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("Cookie", "SINAGLOBAL=7581892984152.969.1550571663869; _ga=GA1.2.343077002.1553504824; OUTFOX_SEARCH_USER_ID_NCOO=1696060429.710752; UOR=www.kaixian.tv,widget.weibo.com,login.sina.com.cn; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWUS6L9sjBg2N9zKYL4fNGK5JpX5KMhUgL.Fo2c1hMNeo24S052dJLoIpBLxK.L1-zLB-2LxK.LBonLBKe7SBtt; wvr=6; ALF=1594376958; SSOLoginState=1562840962; SCF=AjbCiPoTfmlP4VCAnnNi1cRbiUHXWYYemKc8Nj87vmzJ3XqbTyl5vNwnRbOekytXZRu6MYPEoVhmc-1HhX3gWA8.; SUB=_2A25wI3_SDeRhGedI41UW8i_FzDyIHXVTWdYarDV8PUNbmtAKLXPdkW9NVoi5O5mjZt6Whepw3t_L0H-RFNDWi3EH; SUHB=0P19WqEOKSQ4EM; _s_tentry=login.sina.com.cn; Apache=9422547238587.045.1562840972451; ULV=1562840972700:66:4:3:9422547238587.045.1562840972451:1562748582487; webim_unReadCount=%7B%22time%22%3A1562904845794%2C%22dm_pub_total%22%3A10%2C%22chat_group_pc%22%3A0%2C%22allcountNum%22%3A46%2C%22msgbox%22%3A0%7D")
//            .addCookie("cookie","SINAGLOBAL=9262762579760.895.1542259856140; UOR=,,login.sina.com.cn; YF-V5-G0=46bd339a785d24c3e8d7cfb275d14258; Ugrow-G0=ea90f703b7694b74b62d38420b5273df; _s_tentry=-; Apache=3163223882061.6987.1545708585211; ULV=1545708585226:11:6:1:3163223882061.6987.1545708585211:1545397871556; YF-Page-G0=ab26db581320127b3a3450a0429cde69; wb_view_log_6690881782=1920*10801; TC-V5-G0=04dc502e635144031713f186989293c7; login_sid_t=a3c041a86d1c89974663863957c92cad; cross_origin_proto=SSL; WBStorage=bfb29263adc46711|undefined; wb_view_log=1920*10801; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFub6ZK67wp.UEalrslEV3s5JpX5K2hUgL.Foqc1h.41K2peKz2dJLoIp7LxKML1KBLBKnLxKqL1hnLBoMcSon41K.peK2E; ALF=1577267509; SSOLoginState=1545731509; SCF=AtgnXFBDvRA-ACAFTJbJ_U2hd-M9-tcEg6lbqA47ypoC-CGCvmKfByG2RWD5W2V9siUpsAVp0Cb6QvaKiBtK7kQ.; SUB=_2A25xJY3lDeRhGeBI41sY-S_Nyj6IHXVSUvgtrDV8PUNbmtAKLWbXkW9NRn4tdZf1fgFo07gRH8uALbn0_-e3po7l; SUHB=0IIFLvejYmVlfF; un=kntbjd236763@game.weibo.com; wvr=6; wb_view_log_6689991112=1920*10801")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36")
            .addHeader("Content-Type","application/json; charset=utf-8")
            .addHeader("Referer","https://weibo.com/3591355593/HxMKQC3o2?type=comment")
            .addHeader("Connection","keep-alive")
            .addHeader("Host","rm.api.weibo.com")
            .addHeader("Accept","*/*")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .setTimeOut(10 * 1000);


    @Override
    public void process(Page page) {

        WeiboAjaxCommentPage weiboAjaxCommentPage = new WeiboAjaxCommentPage();
        weiboAjaxCommentPage.parse(page);


//        String url = page.getUrl().get();
//
//        List<Request> targetRequests = page.getTargetRequests();
//
//       /* if(url.contains("s.weibo.com")){
//            WeiboSearchPage weiboSearchPage = new WeiboSearchPage();
//            PageParse.Result result = weiboSearchPage.parse(page);
//            checkResult(result,page);
//        }else */
//       if (url.matches("https://weibo.com/aj/v6/comment/big\\?id=[0-9]+&from=singleWeiBo")){
//            WeiboDetilPage weiboDetilPage = new WeiboDetilPage();
//            PageParse.Result result = weiboDetilPage.parse(page);
//            checkResult(result,page);
//        }else if(url.matches("https://weibo.com/aj/v6/comment/big\\?id=[0-9]+&page=[0-9]+")){
//
//            PageParse.Result result = weiboAjaxCommentPage.parse(page);
////            checkResult(result,page);
//        }
    }

    private void checkResult(PageParse.Result result,Page page) {
        if (result.getRequests()!=null){
            List<Request> requests = result.getRequests();
            for (Request request:requests){
                page.addTargetRequest(request);
            }
        }
    }

    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(10) + 4;
        return site.setSleepTime(time*1000);
    }


}
