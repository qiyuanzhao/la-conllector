package com.lavector.collector.crawler.kafka;

/**
 * Created on 2018/11/19.
 *
 * @author zeng.zhao
 */
public class KafkaStart {
    public static void main(String[] args) {
//        Producer producer = new Producer("topic1", false);
//        new Thread(producer).start();1:x

        Consumer consumer = new Consumer("kaTest");
        new Thread(consumer).start();
    }
}
