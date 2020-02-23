package com.lavector.collector.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * Actual logic when sending emails. For example, which email template, send to which user etc...
 *
 * @author tao
 */
@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${email.from.address:service@lavector.com}")
    private String emailFromAddress;
    @Value("${email.from.header:菱歌品牌}")
    private String emailFromHeader;

    @Override
    public void sendEmailToUser(Collection<String> toAddress, String subject, String emailBody) {
        for (String address : toAddress) {
            sendEmailToUser(address, subject, emailBody);
        }
    }

    @Override
    public void sendEmailToUser(String toAddress, String subject, String emailBody) {
        logger.info("Sending email {} to address {} ", subject, toAddress);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setFrom(new InternetAddress(emailFromAddress, emailFromHeader, "UTF-8"));
            mimeMessage.setSubject(subject, "UTF-8");
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            mimeMessage.setText(emailBody, "UTF-8", "html");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            logger.error("Error when sending email to: " + toAddress, ex);
        }
    }

}
