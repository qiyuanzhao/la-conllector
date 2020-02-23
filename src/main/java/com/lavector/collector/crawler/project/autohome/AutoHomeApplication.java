package com.lavector.collector.crawler.project.autohome;


import com.lavector.collector.crawler.project.autohome.crawler.AutoHomeCrawler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoHomeApplication implements CommandLineRunner {

    private AutoHomeCrawler autoHomeCrawler;

    public static void main(String[] args) {

        SpringApplication.run(AutoHomeApplication.class);
    }


    @Override
    public void run(String... strings) throws Exception {
//        autoHomeCrawler.startCrawler();
    }
}
