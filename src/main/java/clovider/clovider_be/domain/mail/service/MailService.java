package clovider.clovider_be.domain.mail.service;

import jakarta.mail.MessagingException;

public interface MailService {


    void sendEmailMessage(String email) throws MessagingException;
}
