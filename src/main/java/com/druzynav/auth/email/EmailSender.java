package com.druzynav.auth.email;

import jakarta.mail.MessagingException;

public interface EmailSender {
    void sendResetPasswordEmail(String recipientEmail, String resetPasswordUrlWithToken) throws MessagingException;
    void sendConfirmationMail(String recipientEmail, String confirmAccountUrlWithToken) throws MessagingException;
    void sendRequestToConfirmHousing(String recipientEmail, String confirmHousingUrlWithToken, String senderName, String senderSurname, String receiverName) throws MessagingException;
}
