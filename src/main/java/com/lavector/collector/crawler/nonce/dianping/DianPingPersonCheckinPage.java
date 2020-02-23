package com.lavector.collector.crawler.nonce.dianping;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.dianping.entity.DianPingMessage;
import com.lavector.collector.crawler.nonce.dianping.entity.MessageType;
import com.lavector.collector.crawler.nonce.dianping.entity.Person;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.project.food.NewDianpingDownloader;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.TimeUtils;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on 11/05/2018.
 *
 * @author zeng.zhao
 */
public class DianPingPersonCheckinPage implements PageParse {

    private JsonMapper jsonMapper = JsonMapper.buildNormalBinder();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("/checkin");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        String userId = page.getRequest().getExtra("userId").toString();
        String referer = page.getRequest().getExtra("referer").toString();
        List<DianPingMessage> messages = new ArrayList<>();
        JSONArray checkins = JsonPath.read(json, "$.msg.checkinList");
        checkins.forEach(checkin -> {
            String shopName = JsonPath.read(checkin, "$.shopName");
            String shopId = JsonPath.read(checkin, "$.shopId");
            if (shopId == null) {
                return;
            }
            String addTime = JsonPath.read(checkin, "$.addTime").toString();
            DianPingMessage message = new DianPingMessage();
            message.setType(MessageType.SIGN_IN);
            message.setShopId(shopId);
            Date startTime = new Date(Long.parseLong(addTime));
            if (!(startTime.after(DianPingCommentListPage.startTime) && startTime.before(DianPingCommentListPage.endTime))) {
                return;
            }
            message.setTime(format.format(new Date(Long.parseLong(addTime))));
            message.setPerson(new Person(userId, null));
            message.setShopName(shopName);
            messages.add(message);
        });

        if (messages.size() > 0) {
//            DianPingPageProcessor.write(jsonMapper.toJson(messages));

            WriteFile.write(jsonMapper.toJson(messages), "/Users/zeng.zhao/Desktop/dianping_sign.json");

//            if (TimeUtils.fromStringToDate(messages.get(messages.size() - 1).getTime().trim()).after(DateUtils.addYears(new Date(), -1))) {
                String currentPage = page.getUrl().regex("&page=(\\d+)").get();
                Integer nextPage = Integer.parseInt(currentPage) + 1;
                String nextUrl = page.getUrl().get().replace("&page=" + currentPage, "&page=" + nextPage);
                Request request = new Request(nextUrl);
                request.setMethod(HttpConstant.Method.POST);
                request.putExtra("referer", referer);
                request.putExtra("userId", userId);
                HttpRequestBody requestBody = new HttpRequestBody();
                requestBody.setEncoding("utf-8");
                requestBody.setContentType("application/x-www-form-urlencoded");
                request.setRequestBody(requestBody);
                result.addRequest(request);
//            }
        }
        return result;
    }

    private Result parseHtml(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String userId = page.getUrl().regex("(\\d+)/checkin").get();
        String userName = html.xpath("//h2[@class='name']/text()").get();
        List<Selectable> nodes = html.xpath("//div[@class='sign-list']/ul/li").nodes();
        List<DianPingMessage> messages = new ArrayList<>();
        nodes.forEach(node -> {
            String time = node.xpath("//span[@class='time']/text()").regex("(.*)在").get();
            String shopId = node.xpath("h6/a/@href").regex("\\d+").get();
            String shopName = node.xpath("h6/a/text()").get();
            DianPingMessage message = new DianPingMessage();
            message.setType(MessageType.SIGN_IN);
            message.setShopId(shopId);
            message.setTime("2018-" + time);
            message.setPerson(new Person(userId, userName));
            message.setShopName(shopName);
            messages.add(message);

//            result.addRequest(new Request("http://www.dianping.com/shop/" + shopId).putExtra("referer", "http://www.dianping.com"));
        });

        DianPingPageProcessor.write(jsonMapper.toJson(messages));

        return result;
    }

    public static void main (String[] args) throws IOException {
        String url = "http://www.dianping.com/ajax/member/checkin/checkinList?memberId=155047116&page=100";
//        HttpRequestBase httpGet = new HttpPost(url);
//        httpGet.addHeader("content-type", "application/x-www-form-urlencoded;charset=UTF-8;");
//        RequestConfig config = RequestConfig.custom().setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123)).build();
//        httpGet.setConfig(config);
//        httpGet.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
//        httpGet.addHeader("referer", "http://www.dianping.com/member/155047116/checkin");
//        httpGet.addHeader("cookie", "_hc.v=167d0c4a-9c97-40e6-ddfe-ee76b4910fb4.1528078742; _lxsdk_cuid=163c897342c28-03e18d81657652-33697706-13c680-163c897342dc8; _lxsdk=163c897342c28-03e18d81657652-33697706-13c680-163c897342dc8; s_ViewType=10; ua=%E5%A4%AA%E7%A9%BA%E7%9A%84%E6%B0%A7%E6%B0%94; ctu=d1123a22f332317e09ff2123f7139509c42f4bc0993642c42ba7a097bf17f2b1; cityid=1; switchcityflashtoast=1; _tr.u=7OzjpC5JH9tOtIQk; m_flash2=1; default_ab=shop%3AA%3A1%7Cindex%3AA%3A1%7CshopList%3AA%3A1%7Cshopreviewlist%3AA%3A1; dper=dea716dc6cdd7e8396ae7318b6899d24d9485d6e629b0c0a0b3f85dd5bd3dcc7a3bb32dd268c777524aec21f7aeabc3c5086a02ae4e0908b72b1a84d6bfdeac6676b0213790885f0f278f885cfac52c7c14047555a693cca8ad7eec576737246; cy=5; cye=nanjing; aburl=1; Hm_lvt_dbeeb675516927da776beeb1d9802bd4=1532077135; __mta=40663837.1532076998643.1532077339876.1532077388035.8; ll=7fd06e815b796be3df069dec7836c3df; _lxsdk_s=164c4dabc76-146-26b-dc9%7C%7C23");
//        httpGet.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());

        Request request = new Request(url);
        request.putExtra("referer", "http://www.dianping.com/member/155047116/checkin");
        HttpRequestBody requestBody = new HttpRequestBody();
        requestBody.setContentType("application/x-www-form-urlencoded;charset=UTF-8;");
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(requestBody);
        HttpClient client = new DynamicProxyDownloader().ignoreValidating().build();
    }
}
