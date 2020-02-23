package com.lavector.collector.thirdpartyapi.weixin;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.thirdpartyapi.weixin.entity.AccessTokenEntity;
import com.lavector.collector.thirdpartyapi.weixin.entity.AccountEntity;
import com.lavector.collector.thirdpartyapi.weixin.entity.ApiEntity;
import com.lavector.collector.thirdpartyapi.weixin.entity.ArticleEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2018/1/4.
 *
 * @author zeng.zhao
 */
public class WeiXinApiServiceImpl implements WeiXinApiService {

    private static final String APP_ID = "ubA2zKTYIH";

    private static final String APP_SECRET = "q4zqJnQpuXhl7Pu17mkz16nc2kX2sA8IPUsVInl7";

    private static final String USERNAME = "linggekeji";

    private static final String PASSWORD = "lavector100";

    private static final String DOMAIN = "http://open.koldata.net/";

    private static final String TOKEN_TYPE = "Bearer";

    private Logger logger = LoggerFactory.getLogger(WeiXinApiServiceImpl.class);

    private static HttpClient httpClient;

    private AccessTokenEntity accessTokenEntity = new AccessTokenEntity();

    private Limiter limiter = new Limiter();

    private AtomicInteger requestCount = new AtomicInteger(0);

    private Integer maxRequestCount = 300;

    public WeiXinApiServiceImpl() {
        this.init();
    }

    private void init() {
        httpClient = HttpClients.custom()
                .build();

        if (!debug) {
            getAccessToken();
        } else {
            accessTokenEntity.setExpires(7200L);
            accessTokenEntity.setAccess_token("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijg4ZGRiNzcxNWNiZGViM2E2YTgwYzVlZjRhMGIzZjA4ODIzZjUyOTc3YTBjNjg5NjZiMTIwMTdiZmE0N2Y3M2MyOTlkOTU0YjU5MmJhMWJkIn0.eyJhdWQiOiIyIiwianRpIjoiODhkZGI3NzE1Y2JkZWIzYTZhODBjNWVmNGEwYjNmMDg4MjNmNTI5NzdhMGM2ODk2NmIxMjAxN2JmYTQ3ZjczYzI5OWQ5NTRiNTkyYmExYmQiLCJpYXQiOjE1MTU1NjM4MzIsIm5iZiI6MTUxNTU2MzgzMiwiZXhwIjoxNTE1NTcxMDMyLCJzdWIiOiIyIiwic2NvcGVzIjpbImJhc2ljIl19.NtXufZbjpHMBibUjGPEFmvfxHXr2BsvFz4j5jXEg3vD3_XL2eYz0bOXIyiAjwfwnKoW9kysJwCUfKEVUa2TtvJ_7NU2TucNs9M1d_ds7eEwNgUcc1dnPwJsNAPZlO64b36HPWOXIcP-dn4i0MZo8ZK8rQWswBcWAnTo3HeN-XWo4ZIerge4DZTM8hkpC_r1BCtK5asjA_1PzWOvAY_KTwBXQP8iE36kUtNZAPcDMaGU13C46txno7C7BnidNVJ_tPeswTtSoNQWS_KTzB2nMEBk9xEAMSdXKqVnSpONegdZSZiTOnJiMEhq_VtkDHNhorpS3MeQpB0RDU48gGsU4AAwH5u6aW-q1eeDroJIbBzREUXlerF1L4cF-Mig8J9rTDtKcKmAor5Kt9HUQc-PVmsa4Xy18SgmbteQWvBuIH__IYOjFLB1fjpyMwv0g9UhWDJTDEQDIYZFR8sBe4bUuybY6K6cQzTRaDTY5uF3SBHCuJy5XOIWhfyKHIOEnp8zgIEEtdtXog8P9P9PEz5dpCc5yPzTNrz3cGVAM9ZiQWUnVaUd726vhFbYt610IJuYqydvs7kzbHgx3Xs54-uEQMXmE9oDnaX7FECSeDNB-KsjKukDNgJ527oCI1Mfv7sioHRvn8R53aBW7OjscCCv-fnJ42Uatd-KJkzQxN6xwOyQ");
            accessTokenEntity.setRefresh_token("def502009c8ebfb66f52a4ef7248a19ce5b9246658c2f6538927e88ed1babc5a9f7eddaa7a0084625b20976b0b114843bd12351ef346cfe32f020f2216f43d1614248323a730e135051536278aa8e77c2b6afb69a232ba90f2783acef4f5a099054035867b8afaa5c6e94a187edb420b5494e205da5dca422fbabce337facdcb8dbdb265608064694d921fbb5c22747d74a07fe89222f4a2d9d16d14f665d5a264b904d1e997d0b6a234d9a63f4d32c53985df2a18bba389cb0c4e71cdf001b060aa7bedc217b64f3be412d2e391f11624844e998e732664619c04d91f76c11384aeec37d092d5bfc632bc638d8fbcd9de096b93d0c808cf629624dfec1318b94806a21f2efcd660c054bd3f3d239595804b92fd9e5eea0b702f976e6ed84741b214f0e54d04a259e315e46b72510c11a525be0b71e11a618b19b61ededc9a199bd07009a31fa52c87bfcb4a3203a95cfc095e34363800cdd4200c3581dd40cc3877ca9c0005");
            accessTokenEntity.setStart(System.currentTimeMillis());
        }
    }


    private void getAccessToken() {
        String tokenJson = null;
        HttpPost httpPost;
        try {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("open.koldata.net")
                    .setPath("token")
                    .setParameter("username", USERNAME)
                    .setParameter("password", PASSWORD)
                    .setParameter("app_id", APP_ID)
                    .setParameter("app_secret", APP_SECRET)
                    .build();
            httpPost = new HttpPost(uri);
        } catch (URISyntaxException e) {
            logger.error("构建access_token请求出错！");
            return;
        }
        httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 429) {
                logger.error("获取token超频！");
                return;
            }
            tokenJson = resolveResponse(httpResponse);
        } catch (IOException e) {
            logger.error("获取access_token出错！", e);
        } finally {
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }
        if (tokenJson != null) {
            readAccessToken(tokenJson);
        }
    }

    private String resolveResponse(HttpResponse httpResponse) throws IOException {
        return IOUtils.toString(httpResponse.getEntity().getContent());
    }


    private void refreshAccessToken() {
        Long current = System.currentTimeMillis();
        if (current - accessTokenEntity.getStart() >= accessTokenEntity.getExpires() * 1000) {
            try {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost("open.koldata.net")
                        .setPath("token/refresh")
                        .setParameter("refresh_token", accessTokenEntity.getRefresh_token())
                        .setParameter("app_id", APP_ID)
                        .setParameter("app_secret", APP_SECRET)
                        .build();
                HttpPost httpPost = new HttpPost(uri);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == 429) {
                    logger.error("刷新token超频！");
                    return;
                }
                readAccessToken(resolveResponse(httpResponse));
            } catch (URISyntaxException | IOException e) {
                logger.error("refresh access_token error!", e);
            }
            logger.info("刷新access token 成功！ ");
        }
    }

    private void readAccessToken(String tokenJson) {
        String access_token = JsonPath.read(tokenJson, "$.access_token");
        String refresh_token = JsonPath.read(tokenJson, "$.refresh_token");
        Integer expires_in = JsonPath.read(tokenJson, "$.expires_in");
        accessTokenEntity.setAccess_token(access_token);
        accessTokenEntity.setRefresh_token(refresh_token);
        accessTokenEntity.setExpires(expires_in.longValue());
        accessTokenEntity.setStart(System.currentTimeMillis());
    }


    @Override
    public String getArticleJson(String wechatId, int pageNum, int pageSize) {
        if (this.accessTokenEntity.getAccess_token() == null) {
            throw new RuntimeException("获取 access_token 失败！");
        }
        pageSize = 20;
        refreshAccessToken();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(DOMAIN)
                    .append("wechat/articles?wechat_id=")
                    .append(URLEncoder.encode(wechatId, "utf-8"))
                    .append("&page=")
                    .append(pageNum)
                    .append("&page_size=")
                    .append(pageSize);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        String url = stringBuilder.toString();
        HttpResponse httpResponse = null;
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Authorization", TOKEN_TYPE + " " + accessTokenEntity.getAccess_token());
        try {
            boolean check = limiter.check();
            if (check) {
                logger.warn("请求速度过快！");
                TimeUnit.SECONDS.sleep(2);
            }
            httpResponse = httpClient.execute(httpGet);
            int currentCount = requestCount.getAndIncrement();
            if (currentCount > maxRequestCount) {
                logger.info("当天限制请求 {} 次！", maxRequestCount);
                throw new RuntimeException();
            }
            return resolveResponse(httpResponse);
        } catch (IOException | InterruptedException e) {
            logger.error("请求文章列表出错！", e);
        } finally {
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }
        return null;
    }

    @Override
    public ApiEntity getArticleEntityList(String wechatId, int pageNum, int pageSize) {
        ApiEntity apiEntity = new ApiEntity();
        List<ArticleEntity> articleEntities = new ArrayList<>();
        String articleJson = getArticleJson(wechatId, pageNum, pageSize);
        if (articleJson == null) {
            return apiEntity;
        }
        Integer status = JsonPath.read(articleJson, "$.status");
        if (status == 0) {
            String error = JsonPath.read(articleJson, "$.err");
            logger.warn("wechatId : {}, error : {}", wechatId, error);
            return apiEntity;
        }
        if (CollectionUtils.isEmpty(JsonPath.read(articleJson, "$.data.data"))) {
            logger.info("没有更多数据！ID:{}, pageNum:{}", wechatId, pageNum);
            return apiEntity;
        }
        List<?> mids = JsonPath.read(articleJson, "$.data.data[*].mid");
        List<String> titles = JsonPath.read(articleJson, "$.data.data[*].title");
        List<String> digests = JsonPath.read(articleJson, "$.data.data[*].digest");
        List<String> content_urls = JsonPath.read(articleJson, "$.data.data[*].content_url");
        List<String> source_urls = JsonPath.read(articleJson, "$.data.data[*].source_url");
        List<String> covers = JsonPath.read(articleJson, "$.data.data[*].cover");
        List<Integer> is_multis = JsonPath.read(articleJson, "$.data.data[*].is_multi");
        List<Integer> is_tops = JsonPath.read(articleJson, "$.data.data[*].is_top");
        List<Integer> idxs = JsonPath.read(articleJson, "$.data.data[*].idx");
        List<String> pub_times = JsonPath.read(articleJson, "$.data.data[*].pub_time");
        List<?> read_nums = JsonPath.read(articleJson, "$.data.data[*].read_num");
        List<Integer> like_nums = JsonPath.read(articleJson, "$.data.data[*].like_num");
        List<String> authors = JsonPath.read(articleJson, "$.data.data[*].author");
        List<Integer> copyrights = JsonPath.read(articleJson, "$.data.data[*].copyright");
        List<String> wehchat_ids = JsonPath.read(articleJson, "$.data.data[*].wechat_id");
        List<String> collect_times = JsonPath.read(articleJson, "$.data.data[*].collect_time");


//        if (JsonPath.read(articleJson, "$.data.data[*].account_info") != null) {
//            ori_weixin_ids = JsonPath.read(articleJson, "$.data.data[*].account_info.ori_weixin_id");
//            weixin_nicks = JsonPath.read(articleJson, "$.data.data[*].account_info.weixin_nick");
//            descriptions = JsonPath.read(articleJson, "$.data.data[*].account_info.description");
//            is_verifieds = JsonPath.read(articleJson, "$.data.data[*].account_info.is_verified");
//            verified_infos = JsonPath.read(articleJson, "$.data.data[*].account_info.verified_info");
//            tags = JsonPath.read(articleJson, "$.data.data[*].account_info.tags");
//            qr_urls = JsonPath.read(articleJson, "$.data.data[*].account_info.qr_url");
//            estimate_fans_nums = JsonPath.read(articleJson, "$.data.data[*].account_info.estimate_fans_num");
//        }
        try {
            for (int i = 0; i < mids.size(); i++) {
                ArticleEntity articleEntity = new ArticleEntity();
                articleEntity.setMid(mids.get(i).toString());
                articleEntity.setTitle(titles.get(i));
                articleEntity.setDigest(digests.get(i));
                articleEntity.setContent_url(content_urls.get(i));
                articleEntity.setSource_url(source_urls.get(i));
                articleEntity.setCover(covers.get(i));
                articleEntity.setIs_multi(is_multis.get(i));
                articleEntity.setIs_top(is_tops.get(i));
                articleEntity.setIdx(idxs.get(i));
                articleEntity.setPub_time(pub_times.get(i));
                if (read_nums.get(i).toString().contains("w+")) {
                    String readNum = read_nums.get(i).toString().replace("w+", "0001");
                    articleEntity.setRead_num(Integer.parseInt(readNum));
                }
                articleEntity.setLike_num(like_nums.get(i));
                articleEntity.setAuthor(authors.get(i));
                articleEntity.setCopyright(copyrights.get(i));
                articleEntity.setWechat_id(wehchat_ids.get(i));
                articleEntity.setCollect_time(collect_times.get(i));


                //账号信息
                Object accountInfo = JsonPath.read(articleJson, "$.data.data[" + i + "].account_info");
                if (Objects.nonNull(accountInfo)) {
                    String account_info = JsonMapper.buildNonNullBinder().toJson(accountInfo);
                    AccountEntity accountEntity = new AccountEntity();
                    accountEntity.setWeixin_id(wehchat_ids.get(i));
                    accountEntity.setOri_weixin_id(JsonPath.read(account_info, "$.ori_weixin_id"));
                    accountEntity.setWeixin_nick(JsonPath.read(account_info, "$.weixin_nick"));
                    accountEntity.setDescription(JsonPath.read(account_info, "$.description"));
                    accountEntity.setIs_verified(JsonPath.read(account_info, "$.is_verified"));
                    accountEntity.setVerified_info(JsonPath.read(account_info, "$.verified_info"));
                    accountEntity.setTags(JsonPath.read(account_info, "$.tags"));
                    accountEntity.setQr_url(JsonPath.read(account_info, "$.qr_url"));
                    Integer fans_num = 0;
                    try {
                        fans_num = JsonPath.read(account_info, "$.estimate_fans_num");
                    } catch (Exception e) {
//                        logger.info("没有粉丝信息 : {}", account_info);
                    }
                    accountEntity.setEstimate_fans_num(fans_num);
                    articleEntity.setAccount_info(accountEntity);
                }

                articleEntities.add(articleEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(articleJson);
        }

        Integer current_page = JsonPath.read(articleJson, "$.data.current_page");
        Integer from = JsonPath.read(articleJson, "$.data.from");
        Integer last_page = JsonPath.read(articleJson, "$.data.last_page");
        String path = JsonPath.read(articleJson, "$.data.path");
        Integer pre_page = JsonPath.read(articleJson, "$.data.per_page");
        String prev_page_url = JsonPath.read(articleJson, "$.data.prev_page_url");
        Integer to = JsonPath.read(articleJson, "$.data.to");
        Integer total = JsonPath.read(articleJson, "$.data.total");
        apiEntity.setCurrent_page(current_page);
        apiEntity.setFrom(from);
        apiEntity.setLast_page(last_page);
        apiEntity.setPath(path);
        apiEntity.setPre_page(pre_page);
        apiEntity.setPrev_page_url(prev_page_url);
        apiEntity.setTo(to);
        apiEntity.setTotal(total);
        apiEntity.setArticleEntities(articleEntities);
        return apiEntity;
    }


    private class Limiter {

        private int maxLongTimes = 300; //访问限制次数
        private int timeMillisecond = 60000; //限制时长：mm
        private LinkedList<Long> requestRecordList = new LinkedList<>();

        /**
         * 检测是否超频
         */
        private boolean check() {
            long currentTimeMillis = System.currentTimeMillis();
            requestRecordList.addLast(currentTimeMillis);
            if (requestRecordList.size() < maxLongTimes) {
                return false;
            }
            Long first = requestRecordList.getFirst();
            if (currentTimeMillis - first <= timeMillisecond) {
                requestRecordList.removeFirst();
                return true;
            } else {
                requestRecordList.removeFirst();
                return false;
            }
        }
    }

    private boolean debug = true;

    public static void main(String[] args) throws Exception {
        WeiXinApiServiceImpl weiXinApiService = new WeiXinApiServiceImpl();
        weiXinApiService.accessTokenEntity.setExpires(7200L);
        weiXinApiService.accessTokenEntity.setAccess_token("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjNlNTBkYWM3OTE3MWVhMWM4ZjQ3MjM0MjBjYjVlZjg3OGFhMWExZTEyMmVjY2Y2ZjY1ZWZjOTliNmMwYWQzYTRmYzkxODc3NDJkMzVlMDJlIn0.eyJhdWQiOiIyIiwianRpIjoiM2U1MGRhYzc5MTcxZWExYzhmNDcyMzQyMGNiNWVmODc4YWExYTFlMTIyZWNjZjZmNjVlZmM5OWI2YzBhZDNhNGZjOTE4Nzc0MmQzNWUwMmUiLCJpYXQiOjE1MTU1NTM4NzMsIm5iZiI6MTUxNTU1Mzg3MywiZXhwIjoxNTE1NTYxMDczLCJzdWIiOiIyIiwic2NvcGVzIjpbImJhc2ljIl19.ZElsizF_oxPogu2cwo4bPmAvMfo6haOAYngB_jbvfDXCwsY2rWcU4Z4BLUdmWeInODUl0WpsqHUmyyFKSa5QjU3G-WSF_dsnpMNRwEHF3WtaI6-n8LpW34-kw0ku4mv5UXEE1so6cOZwnPlWAqkbafnVTmIEHwrXqY7tqIVdVNyrOMUmxNTXoNuKUNiymoZYKDWjij9guFgwCOZySIVLhs8kk8qJt1EmPLN7Use1S8R84JBj4csh_13HHPd0yPtpvHK1nkuwxJGF43KWeVVhXtb8V0hBPW6ZrgNZyPQZkxXvdKE-5ELqNqPzY4J9nXmyXXgdQjqOVSv2kOerCWPZw4Hvw55QS1gKO_gx2TghKpQtCX5BjBsbVTxyIDjhVfltLnqHBHCJDU1EKOivUwSFluaPDcIrwe5WDxobcjo5v63Osd_MrgJZGr44M4Iql0FqigM2-8AMP9lYrrcrKnLgZtrrqsJ4KUyXLXjO21U07O1hHxp6vA4f1vQhCGCvSO5-ayhd8XzXzs-Tx8ebCe5-Gstff-loX4NfRXOyflQhZ-dlV1YtDCtSVyQFQyLeyGinlYBDqY29HKzJKYq-g9w2ZRBeKjEmj-oL2oHUKT0YheFOgbFONqkRuWWmP8nBKP8SYIm_ngBeq3rkudKDeNW_--Dod2x2i7W1tIXAd2P1Dgw");
        weiXinApiService.accessTokenEntity.setRefresh_token("def502001dac26f664bb23f77f27bffe90ee36a59ef3dfd640f07e438ba6d04651a04f79ca9999d60d3021d15ca35dac4056b213433f0d844777eab3e74b7a7e8d5e87b9903491182fcdcfd472ed65a6bd74a02cc352f8712fdc4c1f4cab9a4209b1342c3ffed1f4607d6448e37705ead7e9d0fec9ad3470e0b00028484297a38e34c733fb2c41edea7326225383285b1a7b53fd71f9cbe14449d212b7c4839626878538c7f9a18b1c63b8a448e6e6724c3545ac900c257486270f471b9c3218f128777b2568dee4486145fda31eb665c816cf74c79ee087962b93d107dcc3d45fc85e338f00970fcdb57d018fe22ce1bf0bf0eef472eb66b60ab8a0ea068d115f977eddf8f703ff8e66df17750be0e431b5bbd6f73cceb480679d60be471cb3a6b4b5cf4ae2fad52cc162a1658d2d503addf9ee6deec49c8d74d83ce72805e42a5d5cd66ebd47efb7604ae975b081263b5ff28bfdc5e5337bdd3dc2235ffcc57cb70eb64c87");
        weiXinApiService.accessTokenEntity.setStart(System.currentTimeMillis());
        ApiEntity nc_paul_frank = weiXinApiService.getArticleEntityList("lilycollection", 4, 10);
        System.out.println(nc_paul_frank);
    }
}
