//package com.lavector.collector.crawler.project.music;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created on 02/05/2018.
// *
// * @author zeng.zhao
// */
//public class DriverUtils {
//
//    private DriverUtils() {}
//
//    private static class DriverIns {
//        private static final WebDriver chromeDriver = init();
//
//        private static WebDriver init() {
//            System.setProperty("webdriver.chrome.driver", "/Users/zeng.zhao/Develop/chromedriver");
//            Map<String, Object> contentSettings = new HashMap<>();
//            contentSettings.put("images", 2);
//
//            Map<String, Object> preferences = new HashMap<>();
//            preferences.put("profile.default_content_settings", contentSettings);
//
////            DesiredCapabilities caps = DesiredCapabilities.chrome();
////            caps.setCapability("chrome.prefs", preferences);
//            ChromeOptions chromeOptions = new ChromeOptions();
//            return new ChromeDriver(chromeOptions);
//        }
//    }
//
//    public static WebDriver getDriver() {
//        return DriverIns.chromeDriver;
//    }
//
//    public static void close() {
//        DriverIns.chromeDriver.quit();
//    }
//}
