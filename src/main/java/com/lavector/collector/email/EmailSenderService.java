package com.lavector.collector.email;

import java.util.Collection;

/**
 * Created by zeng.zhao on 2017/10/11
 */
public interface EmailSenderService {

    void sendEmailToUser(Collection<String> toAddress, String subject, String emailBody);

    void sendEmailToUser(String toAddress, String subject, String emailBody);
}
