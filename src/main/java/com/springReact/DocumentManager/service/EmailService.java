package com.springReact.DocumentManager.service;

public interface EmailService {

    void sendNewAccountEmail(String name, String toEmail, String token);
    void sendPasswordResetEmail(String name, String toEmail, String token);

}
