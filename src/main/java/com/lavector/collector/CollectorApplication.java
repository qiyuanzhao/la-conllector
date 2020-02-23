package com.lavector.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CollectorApplication {

//    @Autowired
//    private EmailHelper emailHelper;
//
//    @Override
//    public void run(String... strings) throws Exception {
//        emailHelper.emailTemplateTest();
//    }

    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }
}
