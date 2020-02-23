package com.lavector.collector.crawler.project.instagram;

import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created on 2018/3/8.
 *
 * @author zeng.zhao
 */
public class InstagramTest {

    private static void test() throws IOException {
        String url = "https://www.instagram.com/explore/tags/%E5%B7%A7%E5%85%8B%E5%8A%9B/?__a=1";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString(Charset.forName("utf-8"));
        System.out.println(content);
    }

    public static void main(String[] args) throws IOException {
//        new Thread(InstagramTest::lockTest).start();
//        new Thread(InstagramTest::lockTest).start();
        test();
    }


    static ReentrantLock lock = new ReentrantLock();

    private static void lockTest() {
        lock.lock();
        System.out.println("sss");
        lock.unlock();
    }


}
