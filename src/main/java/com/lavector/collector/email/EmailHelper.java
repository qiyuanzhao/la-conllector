package com.lavector.collector.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zeng.zhao on 2017/10/11
 */
@Component
public class EmailHelper {

    @Value("${email.welcome.template:/templates/welcome.vm}")
    private String welcomeEmailTemplate;

    @Value("${email.news.template:/templates/news.vm}")
    private String newsEmailTemplate;

    private final EmailSenderService emailSenderService;

    private final EmailTemplateService emailTemplateService;

    @Autowired
    public EmailHelper(EmailSenderService emailSenderService, EmailTemplateService emailTemplateService) {
        this.emailSenderService = emailSenderService;
        this.emailTemplateService = emailTemplateService;
    }

    public void emailTemplateTest () {
        Message message = new Message();
        message.setTitle("点击访问百度首页");
        message.setUrl("http://www.baidu.com");
        Map<String, Object> config = new HashMap<>();
        config.put("message", message);
        String emailBody = emailTemplateService.mergeIntoTemplate(newsEmailTemplate, config);
        String toAddress = "1425947180@qq.com";
        String subject = "测试";
        emailSenderService.sendEmailToUser(toAddress, subject, emailBody);
        System.out.println("邮件发送成功！");
    }

}
