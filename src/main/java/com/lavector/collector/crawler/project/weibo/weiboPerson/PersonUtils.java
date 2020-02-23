package com.lavector.collector.crawler.project.weibo.weiboPerson;


import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

public class PersonUtils {




    public static PhantomJSDriver getPhantomJSDriver(){


        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
//        dcaps.setCapability("takesScreenshot", false);
        //css搜索支持
//        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        dcaps.setCapability("phantomjs.page.settings.loadImages", false);
        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "phantomjs.exe");
        return new PhantomJSDriver(dcaps);
    }


}
