package edu.phystech.terekhov_na.stickers.service;

import edu.phystech.terekhov_na.stickers.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestoredDataSender {
    private final JavaMailSender emailSender;

    public void sendRestoredData(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("noreply@stickerstodo.mooo.com");
        message.setFrom("6kola6@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("your restored data");
        message.setText(String.format("Login: %s\nEmail: %s\nPassword: %s\n",
                user.getLogin(), user.getEmail(), user.getPassword()));
        emailSender.send(message);
    }
}
