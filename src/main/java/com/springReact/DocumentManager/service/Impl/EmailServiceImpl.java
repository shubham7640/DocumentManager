package com.springReact.DocumentManager.service.Impl;

import com.springReact.DocumentManager.exception.ApiException;
import com.springReact.DocumentManager.service.EmailService;
import com.springReact.DocumentManager.util.EmailUtil;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String NEW_USER_ACCOUNT_VERIFICATION="NEW USER ACCOUNT VERIFICATION";
    private static final String PASSWORD_RESET_MAIL="PASSWORD RESET MAIL";
    private final JavaMailSender sender;

    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendNewAccountEmail(String name, String toEmail, String token) {
        try {
            // We can create MimeMessage object instead of simple mail message
            // to add html content/attachment in the mail body
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setText(EmailUtil.getEmailMessage(name,host,token));
            sender.send(simpleMailMessage);
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ApiException("unable to send email");
        }
    }

    @Async
    @Override
    public void sendPasswordResetEmail(String name, String toEmail, String token) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(PASSWORD_RESET_MAIL);
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setText(EmailUtil.getResetPasswordEmailMessage(name,host,token));
            sender.send(simpleMailMessage);
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ApiException("unable to send email");
        }
    }


}
