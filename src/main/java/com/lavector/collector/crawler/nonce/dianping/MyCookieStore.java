package com.lavector.collector.crawler.nonce.dianping;


import org.apache.http.client.CookieStore;
import org.apache.http.client.fluent.Request;
import org.apache.http.cookie.Cookie;

import java.util.Date;
import java.util.List;

/**
 * Created on 15/05/2018.
 *
 * @author zeng.zhao
 */
public class MyCookieStore implements CookieStore {
    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public List<Cookie> getCookies() {
        return null;
    }

    @Override
    public boolean clearExpired(Date date) {
        return false;
    }

    @Override
    public void clear() {

    }

    public static void main(String[] args) throws Exception {
        String url  = "http://www.dianping.com/member/29277761/reviews";
        String content = Request.Get(url)
                .addHeader("cookie", "_hc.v=d509c15f-757f-92b5-0b59-5544c71f68b2.1526362946; _lxsdk_cuid=16362523c0dc8-043e0365bed7fc-33627f06-13c680-16362523c0ec8; _lxsdk=16362523c0dc8-043e0365bed7fc-33627f06-13c680-16362523c0ec8; _lxsdk_s=16362523c0f-18f-b8e-056%7C%7C30")
                .addHeader("host", "www.dianping.com")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                .execute()
                .returnContent()
                .asString();
        System.out.println(content);
    }
}
