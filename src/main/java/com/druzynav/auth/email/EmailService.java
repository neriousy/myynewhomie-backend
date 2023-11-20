package com.druzynav.auth.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService implements EmailSender {
    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    @Override
    public void sendConfirmationMail(String recipientEmail, String confirmAccountUrlWithToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setTo(recipientEmail);
        helper.setSubject("Aktywacja konta");

        Context context = new Context();

        context.setVariable("confirmAccountUrlWithToken", confirmAccountUrlWithToken);

        String content = templateEngine.process("confirm-mail", context);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @Async
    @Override
    public void sendResetPasswordEmail(String recipientEmail, String resetPasswordUrlWithToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setTo(recipientEmail);
        helper.setSubject("Resetowanie hasła");

        Context context = new Context();

        context.setVariable("resetPasswordUrlWithToken", resetPasswordUrlWithToken);

        String content = templateEngine.process("reset-password-email", context);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @Async
    @Override
    public void sendRequestToConfirmHousing(String recipientEmail, String confirmHousingUrlWithToken, String senderName, String senderSurname, String receiverName) throws MessagingException {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setTo(recipientEmail);
            helper.setSubject("Potwierdzenie zamieszkania");

            Context context = new Context();

            context.setVariable("confirmHousingUrlWithToken", confirmHousingUrlWithToken);
            context.setVariable("senderName", senderName);
            context.setVariable("senderSurname", senderSurname);
            context.setVariable("receiverName", receiverName);

            String content = templateEngine.process("confirm-housing", context);
            helper.setText(content, true);

            mailSender.send(message);
        }
    }
