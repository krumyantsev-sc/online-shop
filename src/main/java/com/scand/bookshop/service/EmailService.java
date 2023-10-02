package com.scand.bookshop.service;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

  public void sendEmail(String to, String subject, String body) {
    Email email = EmailBuilder.startingBlank()
        .from("haunearli1988@mail.ru")
        .to(to)
        .withSubject(subject)
        .withPlainText(body)
        .buildEmail();

    Mailer mailer = MailerBuilder
        .withTransportStrategy(TransportStrategy.SMTPS)
        .withSMTPServer("smtp.mail.ru", 465, "haunearli1988@mail.ru", "qB8BPjUKaj5Rrw8S5TbS")
        .buildMailer();

    mailer.sendMail(email);
  }
}