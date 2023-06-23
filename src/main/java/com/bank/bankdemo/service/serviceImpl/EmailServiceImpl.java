package com.bank.bankdemo.service.serviceImpl;

import com.bank.bankdemo.dto.EmailDetails;
import com.bank.bankdemo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

//    @Value("${spring.mail.username}")
    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {

        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            System.out.println("mail sent successfully");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
