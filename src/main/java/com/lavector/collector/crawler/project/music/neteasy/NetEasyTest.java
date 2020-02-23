package com.lavector.collector.crawler.project.music.neteasy;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import org.apache.http.client.fluent.Request;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created on 02/05/2018.
 *
 * @author zeng.zhao
 */
public class NetEasyTest {

    public static void main(String[] args) throws Exception {
        phantom();
    }

//    private static void chrome() {
//        System.setProperty("webdriver.chrome.driver", "/Users/zeng.zhao/Develop/chromedriver");
//        ChromeOptions chromeOptions = new ChromeOptions();
//        Proxy proxy = new Proxy();
//        proxy.setHttpProxy("s2.proxy.mayidaili.com:8123").setSslProxy("s2.proxy.mayidaili.com:8123");
//        chromeOptions.setCapability(CapabilityType.PROXY, proxy);
//        chromeOptions.addArguments("Proxy-Authorization:" + DynamicProxyDownloader.getAuthHeader());
////        ChromeDriver driver = new ChromeDriver(chromeOptions);
//
//        ChromeDriver driver = new ChromeDriver(chromeOptions);
//        driver.get("http://www.ipip.net/");
//        System.out.println();
////        String content = driver.switchTo().frame(driver.findElement(By.id("g_iframe"))).getPageSource();
//////        System.out.println(content);
//    }

    private static void phantom() {
        System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/Users/zeng.zhao/Develop/phantomjs-2.1.1-macosx/bin/phantomjs");
        DesiredCapabilities desc = new DesiredCapabilities();
//        Proxy proxy = new Proxy();
//        proxy.setHttpProxy("s2.proxy.mayidaili.com:8123").setSslProxy("s2.proxy.mayidaili.com:8123");
//        desc.setCapability("acceptSslCerts", true);
//        //截屏支持
//        desc.setCapability("takesScreenshot", true);
//        //css搜索支持
//        desc.setCapability("cssSelectorsEnabled", true);
//        //js支持
//        desc.setJavascriptEnabled(true);
//
        desc.setPlatform(Platform.ANY);
//        desc.setCapability(CapabilityType.PROXY, proxy);
        desc.setCapability("phantomjs.page.customHeaders.Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
//        desc.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        PhantomJSDriver driver = new PhantomJSDriver();
        driver.get("http://www.ipip.net/");
        System.out.println();
    }

    private static void zhilian() throws Exception {
        String url = "https://i.zhaopin.com/Home/ResumePreview?resumeNumber=JM761465277R90250002000&resumeId=248032952&version=1&language=1&fromtype=popdiv";
        String content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                .addHeader("cookie", "at=738b7b4452da4c0fab8e0680494938fc; rt=da92d6f2baf145648d975423aaa7f5d7;")
                .execute()
                .returnContent()
                .asString();
        System.out.println(content);
    }
}
