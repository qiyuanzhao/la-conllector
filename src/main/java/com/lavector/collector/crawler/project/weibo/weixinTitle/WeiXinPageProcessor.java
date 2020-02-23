package com.lavector.collector.crawler.project.weibo.weixinTitle;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weibo.weixinTitle.page.WeiXinDeteilePage;
import com.lavector.collector.crawler.project.weibo.weixinTitle.page.WeiXinSearchPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;


public class WeiXinPageProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(WeiXinPageProcessor.class);


    private static Site site = Site.me()
            .setSleepTime(10000)
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
                                //ABTEST=0|1552375984|v1; IPLOC=CN1100; SUID=525BFE2B232C940A000000005C8760B0; SUV=006FB4192BFE5B525C8760B17DD66472; SUID=525BFE2B2213940A000000005C8760B1; SNUID=BA7D2B176C69E827771908DD6CCCCF41; weixinIndexVisited=1; JSESSIONID=aaacXVNktPgKkdBevG-Lw; ppinf=5|1552814868|1554024468|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo5OiVFMyU4MCU4MnxjcnQ6MTA6MTU1MjgxNDg2OHxyZWZuaWNrOjk6JUUzJTgwJTgyfHVzZXJpZDo0NDpvOXQybHVDdFdILU1zOGdlQ04ySllzZzNVWEdRQHdlaXhpbi5zb2h1LmNvbXw; pprdig=V6v9QhT2y1iaNcaPE-AFxHNlMndZewbWjQYnp0nxeuNZhlWgBu6eFQbZAGWKW3Jr5vvEzGr8flIGVUuvox6nEPQpwJpo0Tp4qXDIOW85b_VEbZ6Kx0VcEY401kLz3LUncjRnfM_L0RpAHwq-QeHFpBpSUUY1j3rmmf7lM-Ld3Dg; sgid=21-39684667-AVyOExQRHT0FXLDqcsk0CZ8; ppmdig=155283068300000003fba975916f65159b76299fa293f794; sct=6; PHPSESSID=hbf5c1226meg1qhvqk3f4fst97; successCount=1|Sun, 17 Mar 2019 14:35:01 GMT
//            .addHeader("cookie", "ABTEST=0|1552375984|v1; IPLOC=CN1100; SUID=525BFE2B232C940A000000005C8760B0; SUV=006FB4192BFE5B525C8760B17DD66472; SUID=525BFE2B2213940A000000005C8760B1; SNUID=BA7D2B176C69E827771908DD6CCCCF41; weixinIndexVisited=1; JSESSIONID=aaacXVNktPgKkdBevG-Lw; ppinf=5|1552814868|1554024468|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo5OiVFMyU4MCU4MnxjcnQ6MTA6MTU1MjgxNDg2OHxyZWZuaWNrOjk6JUUzJTgwJTgyfHVzZXJpZDo0NDpvOXQybHVDdFdILU1zOGdlQ04ySllzZzNVWEdRQHdlaXhpbi5zb2h1LmNvbXw; pprdig=V6v9QhT2y1iaNcaPE-AFxHNlMndZewbWjQYnp0nxeuNZhlWgBu6eFQbZAGWKW3Jr5vvEzGr8flIGVUuvox6nEPQpwJpo0Tp4qXDIOW85b_VEbZ6Kx0VcEY401kLz3LUncjRnfM_L0RpAHwq-QeHFpBpSUUY1j3rmmf7lM-Ld3Dg; sgid=21-39684667-AVyOExQRHT0FXLDqcsk0CZ8; ppmdig=1552874376000000d6e89286095972223b69e349bf02d68e; sct=6; PHPSESSID=hbf5c1226meg1qhvqk3f4fst97")
//            .addHeader("cookie", "ABTEST=0|1552375984|v1; IPLOC=CN1100; SUID=525BFE2B232C940A000000005C8760B0; SUV=006FB4192BFE5B525C8760B17DD66472; SUID=525BFE2B2213940A000000005C8760B1; SNUID=4F88D8E5999D1CD20AF6508F999FD890; weixinIndexVisited=1; JSESSIONID=aaacXVNktPgKkdBevG-Lw; ppinf=5|1552814868|1554024468|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo5OiVFMyU4MCU4MnxjcnQ6MTA6MTU1MjgxNDg2OHxyZWZuaWNrOjk6JUUzJTgwJTgyfHVzZXJpZDo0NDpvOXQybHVDdFdILU1zOGdlQ04ySllzZzNVWEdRQHdlaXhpbi5zb2h1LmNvbXw; pprdig=V6v9QhT2y1iaNcaPE-AFxHNlMndZewbWjQYnp0nxeuNZhlWgBu6eFQbZAGWKW3Jr5vvEzGr8flIGVUuvox6nEPQpwJpo0Tp4qXDIOW85b_VEbZ6Kx0VcEY401kLz3LUncjRnfM_L0RpAHwq-QeHFpBpSUUY1j3rmmf7lM-Ld3Dg; sgid=21-39684667-AVyOExQRHT0FXLDqcsk0CZ8; ppmdig=155283068300000003fba975916f65159b76299fa293f794; sct=5; PHPSESSID=hbf5c1226meg1qhvqk3f4fst97; seccodeErrorCount=1|Sun, 17 Mar 2019 13:56:43 GMT; seccodeRight=success; successCount=1|Sun, 17 Mar 2019 13:56:59 GMT")
//            .addHeader("cookie", "ABTEST=0|1552375984|v1; IPLOC=CN1100; SUID=525BFE2B232C940A000000005C8760B0; SUV=006FB4192BFE5B525C8760B17DD66472; SUID=525BFE2B2213940A000000005C8760B1; SNUID=BA7D2B176C69E827771908DD6CCCCF41; weixinIndexVisited=1; JSESSIONID=aaacXVNktPgKkdBevG-Lw; sct=8; PHPSESSID=hbf5c1226meg1qhvqk3f4fst97; ppinf=5|1552891966|1554101566|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo5OiVFMyU4MCU4MnxjcnQ6MTA6MTU1Mjg5MTk2NnxyZWZuaWNrOjk6JUUzJTgwJTgyfHVzZXJpZDo0NDpvOXQybHVDdFdILU1zOGdlQ04ySllzZzNVWEdRQHdlaXhpbi5zb2h1LmNvbXw; pprdig=KDWWHqsjziKfqkhyUz0lqmJVispt28i4QI1q_7zfLhP4i08aULQHe4SoZtj8wWPKU-9DhfUzeG9F5vyYiV0e3t3L7Evz2DEGO5bMd-Wh7Uw9hGlUmIW8XAAVpGXDp-q1IRj3WWAOC29pSDazHX83_y6k7j33-6U2n2IoJSrgdCM; sgid=21-39684667-AVyPQD77WK3frV9w7p1lPks; ppmdig=155289196600000044c6beea4ae49b1c7a232048cd43e21b")
//            .addHeader("cookie", "ABTEST=0|1552375984|v1; IPLOC=CN1100; SUID=525BFE2B232C940A000000005C8760B0; SUV=006FB4192BFE5B525C8760B17DD66472; SUID=525BFE2B2213940A000000005C8760B1; SNUID=5B9CCEF18D880AD7DD1956C88E4F230D; weixinIndexVisited=1; JSESSIONID=aaacXVNktPgKkdBevG-Lw; sct=10; PHPSESSID=hbf5c1226meg1qhvqk3f4fst97; ppinf=5|1552893943|1554103543|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZToyMTolRTUlODglOUQlRTUlQkYlODNmbHl8Y3J0OjEwOjE1NTI4OTM5NDN8cmVmbmljazoyMTolRTUlODglOUQlRTUlQkYlODNmbHl8dXNlcmlkOjQ0Om85dDJsdU9CdTZWczlyeUtQSngyMVlaSGJlNGNAd2VpeGluLnNvaHUuY29tfA; pprdig=SaC9FSSzd3QuHLEZofktY9i4GMRoE6vs9AKoqVQRwx_DsoOz8oLgsx9yXFcaDVjPeQDLLe21teowvQ86OmLnAHesFDiYjjoPEz3EtQvUbZXomplx0FmXx5Dr0pU86j158-Bxay3AcvTYPEoKJtbyolJwrEWlRBh6KDJcszc5tys; sgid=24-39473265-AVyPRiccY2khyFDe1celTF00; ppmdig=15528939430000000725d0cf35efe6ea61fb787ae60694e1; seccodeErrorCount=2|Mon, 18 Mar 2019 07:31:03 GMT; seccodeRight=success; successCount=1|Mon, 18 Mar 2019 07:32:02 GMT")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36")
            .setTimeOut(15 * 1000);


    @Override
    public void process(Page page) {

        String url = page.getUrl().get();

        if (url.contains("https://weixin.sogou.com/weixin")) {
            WeiXinSearchPage weiXinSearchPage = new WeiXinSearchPage();
            weiXinSearchPage.parse(page);
        }else if(url.contains("http://mp.weixin.qq.com/")){
            WeiXinDeteilePage weiXinDeteilePage = new WeiXinDeteilePage();
            weiXinDeteilePage.parse(page);
        }
    }

    private void checkResult(PageParse.Result result, Page page) {
        if (result.getRequests() != null) {
            List<Request> requests = result.getRequests();
            for (Request request : requests) {
                page.addTargetRequest(request);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


}
