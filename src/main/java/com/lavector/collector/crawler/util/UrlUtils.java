package com.lavector.collector.crawler.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UrlUtils {
    public static final String URL_UIDS_LOCAL = "http://localhost:8080/internal/uids";

    private static Logger log = LoggerFactory.getLogger(UrlUtils.class);

    public static String encode(String s) {
        try {
            s = URLEncoder.encode(s, "utf-8");
        } catch (Exception e) {
            //
        }
        return s;
    }

    public static String encodeStr(String keyword) {
        try {
            return URLEncoder.encode(keyword, "UTF-8")
//                    .replaceAll("\\+", "%20")
                    .replaceAll(" ", "+")
                    .replaceAll("%21", "!")
                    .replaceAll("%27", "'")
                    .replaceAll("%28", "(")
                    .replaceAll("%29", ")")
                    .replaceAll("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encode string : " + keyword + " error", e);
        }
    }

    public static List<String> getLinesFromResource(String resourcePath, boolean removeEmptyAndComment) {
        List<String> results = new LinkedList<>();
        InputStream is = UrlUtils.class.getResourceAsStream(resourcePath);
        try {
            List<String> lines = IOUtils.readLines(is);
            if (removeEmptyAndComment) {
                for (String line : lines) {
                    line = StringUtils.trim(line);
                    if (!StringUtils.isEmpty(line) && !line.startsWith("#")) {
                        results.add(line);
                    }
                }
            } else {
                results = lines;
            }
        } catch (Exception ignored) {
        } finally {
            IOUtils.closeQuietly(is);
        }
        return results;
    }

    public static String[] staticSearchKeywords = new String[0];

    public static String[] getStaticSearchKeywords() {
        if (staticSearchKeywords == null || staticSearchKeywords.length == 0) {
            List<String> lines = getLinesFromResource("/SearchKeywords.txt", true);
            staticSearchKeywords = lines.toArray(new String[0]);
        }
        return staticSearchKeywords;
    }

    public static String[] proxies = new String[0];

    public static String[] getProxies() {
        if (proxies == null || proxies.length == 0) {
            List<String> lines = getLinesFromResource("/Proxies.txt", true);
            List<String> validProxies = new ArrayList<>(lines.size());
            for (String proxy : lines) {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                proxy = StringUtils.trim(proxy);
                RequestConfig config = RequestConfig.custom().setProxy(HttpHost.create(proxy)).build();
                //HttpGet request = new HttpGet("http://www.qs5.org/tools/getPosition/getPosition.php");
                HttpGet request = new HttpGet("http://weixin.sogou.com");
                request.setConfig(config);
                CloseableHttpResponse response = null;
                try {
                    response = httpClient.execute(request);
                    //System.out.println(response.getStatusLine());
                    //System.out.println(EntityUtils.toString(response.getEntity()));
                    if (response.getStatusLine().getStatusCode() == 200) {
                        log.info("Proxy valid: " + proxy + " - " + response.getStatusLine());
                        validProxies.add(proxy);
                    } else {
                        log.info("Proxy error: " + proxy + " - " + response.getStatusLine());
                    }
                } catch (Exception e) {
                    log.info("Proxy error: " + proxy + " - " + e.getMessage());
                } finally {
                    if (response != null) {
                        try {
                            response.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
            proxies = validProxies.toArray(new String[0]);
        }
        return proxies;
    }

    public static String getRandomProxy() {
        getProxies();
        String proxy = null;
        if (proxies.length > 0) {
            int index = RandomUtils.nextInt(0, proxies.length);
            proxy = proxies[index];
            log.info("Selected proxy: " + proxy);
        }
        return proxy;
    }

    public static String[] weiboCookies = new String[0];

    public static String[] getWeiboCookies() {
        if (weiboCookies == null || weiboCookies.length == 0) {
            InputStream is = UrlUtils.class.getResourceAsStream("/WeiboCookies.txt");
            try {
                List<String> cookies = IOUtils.readLines(is);
                weiboCookies = cookies.toArray(new String[0]);
            } catch (Exception e) {
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
        return weiboCookies;
    }

    public static String getWeiboCookie(String name) {
        String[] weiboCookies = getWeiboCookies();
        int i = RandomUtils.nextInt(0, weiboCookies.length);
        String cookie = weiboCookies[i];
        cookie = StringUtils.substringAfter(cookie, name + "=");
        cookie = StringUtils.substringBefore(cookie, ";");
        return cookie;
    }


    public static String[] weiboTopicUrls =
            {
                    "https://api.weibo.com/2/search/topics.json?source=5786724301&q=" + encode("奥迪") + "&count=50&page=1",
                    "https://api.weibo.com/2/search/topics.json?source=5786724301&q=" + encode("宝马") + "&count=50&page=1",
                    "https://api.weibo.com/2/search/topics.json?source=5786724301&q=" + encode("奔驰") + "&count=50&page=1",
            };

    static {
        String[] keywords = getStaticSearchKeywords();
        if (keywords != null && keywords.length != 0) {
            weiboTopicUrls = new String[keywords.length];
            for (int i = 0; i < keywords.length; i++) {
                String keyword = keywords[i];
                weiboTopicUrls[i] = "https://api.weibo.com/2/search/topics.json?source=5786724301&q=" + encode(keyword) + "&count=50&page=1";
            }
        }
    }

    public static String[] weiboSearchUrls =
            {
                    "https://c.api.weibo.com/2/search/statuses/limited.json?source=674587526&access_token=2.008U7DHG0yBVejb67fb55139AycL9B&q=奥迪",
                    "https://c.api.weibo.com/2/search/statuses/limited.json?source=674587526&access_token=2.008U7DHG0yBVejb67fb55139AycL9B&q=宝马",
                    "https://c.api.weibo.com/2/search/statuses/limited.json?source=674587526&access_token=2.008U7DHG0yBVejb67fb55139AycL9B&q=宝马",
            };

    static {
        String[] keywords = getStaticSearchKeywords();
        if (keywords != null && keywords.length != 0) {
            weiboSearchUrls = new String[keywords.length];
            for (int i = 0; i < keywords.length; i++) {
                String keyword = keywords[i];
                weiboSearchUrls[i] = "https://c.api.weibo.com/2/search/statuses/limited.json?source=674587526&access_token=2.008U7DHG0yBVejb67fb55139AycL9B&q=" + keyword;
            }
        }
    }

    public static String[] getWeiboHomeUrls() {
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            int x = i - 1;
            list.add("http://weibo.com/aj/mblog/fsearch?pre_page=" + x + "&page=" + i + "&pagebar=0");
            list.add("http://weibo.com/aj/mblog/fsearch?pre_page=" + i + "&page=" + i + "&pagebar=0");
            list.add("http://weibo.com/aj/mblog/fsearch?pre_page=" + i + "&page=" + i + "&pagebar=1");
//			list.add("http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100505&profile_ftype=1&is_all=1&pl_name=Pl_Official_MyProfileFeed__25&id=1005051895964183&script_uri=/u/1895964183&feed_type=0&domain_op=100505&pre_page=" + x + "&page=" + i + "&pagebar=0");
//			list.add("http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100505&profile_ftype=1&is_all=1&pl_name=Pl_Official_MyProfileFeed__25&id=1005051895964183&script_uri=/u/1895964183&feed_type=0&domain_op=100505&pre_page=" + i + "&page=" + i + "&pagebar=0");
//			list.add("http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100505&profile_ftype=1&is_all=1&pl_name=Pl_Official_MyProfileFeed__25&id=1005051895964183&script_uri=/u/1895964183&feed_type=0&domain_op=100505&pre_page=" + i + "&page=" + i + "&pagebar=1");
        }
        String[] arr = (String[]) list.toArray(new String[list.size()]);
        return arr;
    }

    public static List<String> weiboStartRequests(List<String> ouids) {
        List<String> list = new ArrayList<String>();
        ouids.forEach(ouid -> {
            list.add("http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100606&is_all=1&id=100606" + ouid + "&feed_type=0&pagebar=0&pre_page=0&page=1");
            list.add("http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100606&is_all=1&id=100606" + ouid + "&feed_type=0&pagebar=0&pre_page=1&page=1");
            list.add("http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100606&is_all=1&id=100606" + ouid + "&feed_type=0&pagebar=1&pre_page=1&page=1");
        });
        return list;
    }

}
