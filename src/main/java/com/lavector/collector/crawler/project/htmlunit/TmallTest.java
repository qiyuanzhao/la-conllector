package com.lavector.collector.crawler.project.htmlunit;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.ProcessManager;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.selector.Html;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/3/29.
 *
 * @author zeng.zhao
 */
public class TmallTest {

    private static String url = "https://detail.tmall.com/item.htm?id=535735385204&user_id=793475151&cat_id=56012004&is_b=1";

    private static void test() throws Exception {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setUseInsecureSSL(false);
        HtmlPage page = client.getPage(url);
//        client.waitForBackgroundJavaScript(5 * 1000);
        String textContent = page.getBody().getTextContent();
        System.out.println(textContent);
        client.close();
    }

    private static void headlessTest() throws Exception {
        Launcher launcher = new Launcher();
        SessionFactory sessionFactory = launcher.launch(Arrays.asList("--headless", "--disable-gpu"));
//        String browserContext = sessionFactory.createBrowserContext();
//        System.out.println(browserContext);
        Session session = sessionFactory.create();
        session.navigate(url);
        session.waitDocumentReady();
        session.getOptions("div").forEach(System.out::println);
        String content = session.getContent();
        Html html = new Html(content);
        html.$("#J_AttrUL > li", "text").all().forEach(System.out::println);
        String s = html.$("#J_PromoPrice > dd > div > span", "text").get();
        System.out.println(s);
        launcher.getProcessManager().kill();
    }

    private static void httpClientTest() throws Exception {
        String content = Request.Get(url)
                .version(HttpVersion.HTTP_1_1)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        String price = new Html(content).$("#J_PromoPrice > dd > div > span", "text").get();
        System.out.println(price);
    }

    public static void main(String[] args) throws Exception {
        douyinTest();
    }

    public static void douyinTest() throws Exception {
        String url = "https://aweme.snssdk.com/aweme/v1/user/?iid=29768428396&ac=WIFI&os_api=18&app_name=aweme&channel=App%20Store&idfa=A4FF2021-5954-4586-B50A-628B97F2C29D&device_platform=iphone&build_number=17805&vid=7716D2B4-345A-4881-89D6-47807F28E3CA&openudid=e5a0140d02e0307ac283a75813a86401730dd6f0&device_type=iPhone6,2&app_version=1.7.8&device_id=50529548072&version_code=1.7.8&os_version=10.0.1&screen_width=640&aid=1128&user_id=60679291746&mas=00432deda4b02b22a2d601200b536f488dc852624ccbcb3647028e&as=a1952dbce9fe4aaf219933&ts=1522655209";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        System.out.println(content);
    }
}
