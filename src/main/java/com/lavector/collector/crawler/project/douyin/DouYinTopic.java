package com.lavector.collector.crawler.project.douyin;

import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/11/22.
 *
 * @author zeng.zhao
 */
public class DouYinTopic {

    //话题 汽水
    // 第一页 	https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=0&hot_search=0&is_pull_refresh=1&keyword=%E6%B1%BD%E6%B0%B4&search_source=challenge&mas=01af205cd94e6f11da4a20c46ca0866a29b0c66d95ad646914acf1&as=a105f64f16d4bb6dc68946&ts=1542876486
    // 第二页    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=20&hot_search=0&keyword=%E6%B1%BD%E6%B0%B4&search_source=challenge&mas=01f42a2f162f7f3fa74f48d62b863d26873d8cbbee4cf18c672cf8&as=a1a5561f9538ebed267847&ts=1542876549
    // 第三页    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=40&hot_search=0&keyword=%E6%B1%BD%E6%B0%B4&search_source=challenge&mas=0143bf2b1f085be10767ba72c6c4b3fd4aa56b78a8d332a5066ebe&as=a115061f46e81b6d368805&ts=1542876550
    // 第四页    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=60&hot_search=0&keyword=%E6%B1%BD%E6%B0%B4&search_source=challenge&mas=01594d318dfe5b577865ca37bf25a977f0606a20ab64e74af66cfe&as=a175e63ff758ab2d165945&ts=1542876551
    // 第五页    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=80&hot_search=0&keyword=%E6%B1%BD%E6%B0%B4&search_source=challenge&mas=01bd36fd4cc60cf6132544929d64046cb7d36582aa13182167c6a1&as=a1a5a6ffb7183b9d968210&ts=1542876551
    // 第六页    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=100&hot_search=0&keyword=%E6%B1%BD%E6%B0%B4&search_source=challenge&mas=01e693086637eeba9e06239e31574332ff7a4d628da4bebd28e8d7&as=a1e5b6bf14304b6e662564&ts=1542876676
    // 第七页    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=120&hot_search=0&keyword=%E6%B1%BD%E6%B0%B4&search_source=challenge&mas=014220eae906ecb6dbe5f31e2c9f745e4d589dc3bfb7b9aecbd6a8&as=a175f65fb420ab7e966518&ts=1542876676
    // 第八页    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=140&hot_search=0&keyword=%E6%B1%BD%E6%B0%B4&search_source=challenge&mas=01d5ec30088c9a146ec47a4edf0a24a22fe9eaf93ddcd17e55e6a7&as=a105166fd5c0fb0ed61814&ts=1542876677

    // 话题 碳酸饮料
    // 1    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=0&hot_search=0&is_pull_refresh=1&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=01ef70143ae65b1a9a0a18efa6281669c53e6224d98428ccaeef37&as=a185261f8c294bfe569884&ts=1542876828
    // 2    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=20&hot_search=0&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=01308accc6f291281fa2877276719a5252ecc523fed8e5b5135cf1&as=a1c5d68fa8aa7bde467649&ts=1542876840
    // 3    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=40&hot_search=0&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=01c4c240a1a94130a2b8bf5048e7f13b3adcb0ba9062a42620e8d7&as=a155665fbadadb1e361764&ts=1542876842
    // 4    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=60&hot_search=0&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=011f93faa48e9562e626fde2500d071f096ee36d9ae6b0e749c4e1&as=a1e5861fdb9a4bbe168150&ts=1542876843
    // 5    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=80&hot_search=0&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=0100d59820d14a1b1eb586f19db8d447199cd2fed02e89c3966ebe&as=a105867ffbda2bce963505&ts=1542876843
    // 6    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=100&hot_search=0&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=017c8fb82bab3072d09a1cac95a9546d324d611461d0d521e20cfe&as=a165b62fd7bffb6e468443&ts=1542876919
    // 7    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=120&hot_search=0&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=0151be07447c373f811fc28c3456db99e750ebd96e4fa13a9e6a9e&as=a1d5b63fa7fffbae265425&ts=1542876919
    // 8    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=140&hot_search=0&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=0184a72debdebd577787545871e00bfaf310d00ca1b497e56becf7&as=a17596aff8bf2b6ed62344&ts=1542876920
    // 9    https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=160&hot_search=0&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=01ecebab8c5147608839d84a813c90f8af9310ada3b7ce3075e8d7&as=a1c5066f692f4bae968864&ts=1542876921
    // 10   https://aweme.snssdk.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=180&hot_search=0&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=010d3e1073aeef32067bf2c822cf973243506b7016bc5e427fe8d7&as=a125169f3b2feb4e367064&ts=1542876923
    // 11

    private static String[] qishuiUrls = new String[]{
            "https://api.amemv.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=180&hot_search=0&keyword=%E6%B1%BD%E6%B0%B4&search_source=challenge&mas=01108510682c09f881e04a4afd7d714057af3ebc900d11fabd8727&as=a115e61f79aa8b72376592&ts=1542939305",
    };

    private static String[] yinliaoUrls = new String[]{
            "https://api.amemv.com/aweme/v1/challenge/search/?version_code=3.4.0&pass-region=1&pass-route=1&js_sdk_version=1.3.0.1&app_name=aweme&vid=89F551DC-E706-4EF3-BE49-70444ECDD695&app_version=3.4.0&device_id=53051607236&channel=App%20Store&aid=1128&screen_width=1242&openudid=a6b0753b210eb4339fc92835a629fe1bc7f8c09c&os_api=18&ac=WIFI&os_version=11.4&device_platform=iphone&build_number=34008&iid=51964447448&device_type=iPhone10,2&idfa=378781D7-E302-46E7-9C46-AFED3942A64F&count=20&cursor=180&hot_search=0&keyword=%E7%A2%B3%E9%85%B8%E9%A5%AE%E6%96%99&search_source=challenge&mas=01e85f57e369440a136ddece11eea34452c558e2843235e9ce8cf7&as=a1d5c64f098dab53172042&ts=1542939609"
    };


    private static void start() {
        for (String url : yinliaoUrls) {
            String content = getContent(url);
            if (content == null) {
                System.out.println("content is null..." + url);
                continue;
            }
            parse(content, "碳酸饮料");
        }

    }

    private static String getContent(String url) {
        try (CloseableHttpClient httpClient = new DynamicProxyDownloader().ignoreValidating().build();) {

            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("user-agent", "Aweme/3.1.0 (iPhone; iOS 11.4; Scale/3.00)");
            httpGet.addHeader("cookie", "install_id=45961249921; odin_tt=938cb8423dd7a86ae1e794d8e5e6fd2f1992ed557f51ba4841c08660b072fdb6bc1706f6e48bcd90a5ba39ab81e21097; sessionid=93b556bb1bc291f93c2319b7bc2308a7; sid_guard=93b556bb1bc291f93c2319b7bc2308a7%7C1537977463%7C5184000%7CSun%2C+25-Nov-2018+15%3A57%3A43+GMT; sid_tt=93b556bb1bc291f93c2319b7bc2308a7; ttreq=1$b082073d1d6a13719f3302c3b8e108c2ccaab35e; uid_tt=63d788331a96b867cc9328d9eed30930");

            httpGet.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
            try (CloseableHttpResponse response = httpClient.execute(httpGet);) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void parse(String content, String keyword) {
        JSONArray topicList = null;
        try {
            topicList = JsonPath.read(content, "$.challenge_list");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String path = "/Users/zeng.zhao/Desktop/" + keyword + ".csv";
        File file = new File(path);
        topicList.forEach(topic -> {
            Integer count;
            try {
                count = JsonPath.read(topic, "$.challenge_info.view_count");
            } catch (Exception e) {
                count = JsonPath.read(topic, "$.challenge_info.user_count");
            }
            if (count == null) {
                System.out.println(topic);
            }
            String name = JsonPath.read(topic, "$.challenge_info.cha_name");
            String writeContent = name + "," + count + "\n";
            try {
                FileUtils.writeStringToFile(file, writeContent, Charset.forName("utf-8"), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        start();
    }
}
