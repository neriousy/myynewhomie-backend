package com.druzynav.controllers;

import com.druzynav.auth.email.EmailService;
import com.druzynav.exceptions.UserDoesNotExist.UserDoesNotExistException;
import com.druzynav.models.passwordResetToken.PasswordResetToken;
import com.druzynav.models.passwordResetToken.dto.PasswordResetTokenDTO;
import com.druzynav.models.user.User;
import com.druzynav.repositories.PasswordResetTokenRepository;
import com.druzynav.repositories.UserRepository;
import com.druzynav.services.PasswordResetTokenService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@CrossOrigin
public class PasswordResetTokenController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${reset.app.domain}")
    private String resetPasswordUrl;

    // Kontroler
    @GetMapping("/api/v1/reset-password")
    public String showResetPasswordPage() {
        return "reset-password";
    }

    @PostMapping("/api/v1/reset-password")
    public String resetPassword(Model model, @RequestParam("email") String email) throws MessagingException {
        // Sprawdź, czy użytkownik o podanym adresie e-mail istnieje
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserDoesNotExistException());

        // Utwórz token resetowania hasła dla użytkownika
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForUser(user);

        //TODO: Zmienic na front

        // Wyślij e-mail z linkiem resetowania hasła
        String resetPasswordUrlWithToken = resetPasswordUrl + "?token=" + passwordResetToken.getToken();
        emailService.sendResetPasswordEmail(user.getEmail(), resetPasswordUrlWithToken);

        model.addAttribute("message", "Instrukcje resetowania hasła zostały wysłane na podany adres e-mail.");
        return "reset-password";
    }
    @GetMapping("/api/v1/reset-password-form")
    public String showResetPasswordForm(Model model, @RequestParam("token") String token) {
        // Sprawdź, czy token resetowania hasła jest ważny
        PasswordResetToken passwordResetToken = passwordResetTokenService.getPasswordResetToken(token);
        if (passwordResetToken == null || passwordResetToken.getExpiryDate().before(new Date())) {
            model.addAttribute("error", "Nieprawidłowy lub wygasły token resetowania hasła.");
            return "reset-password-error";
        }

        model.addAttribute("token", token);
        return "reset-password-form";
    }

    @PostMapping("/api/v1/reset-password-form")
    public ResponseEntity resetPasswordForm(Model model, @RequestParam("password") String password, @RequestParam("token") String token) {
        // Sprawdź, czy token resetowania hasła jest ważny
        PasswordResetTokenDTO passwordResetTokenDTO = new PasswordResetTokenDTO(token, password);
        PasswordResetToken passwordResetToken = passwordResetTokenService.getPasswordResetToken(passwordResetTokenDTO.getToken());
        if (passwordResetToken == null || passwordResetToken.getExpiryDate().before(new Date())) {
            model.addAttribute("error", "Nieprawidłowy lub wygasły token resetowania hasła.");
            ResponseEntity.badRequest();
        }

        // Zaktualizuj hasło użytkownika i usuń token resetowania hasła
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(passwordResetTokenDTO.getPassword()));
        userRepository.save(user);
        passwordResetTokenService.deletePasswordResetToken(passwordResetToken);

        model.addAttribute("message", "Hasło zostało zaktualizowane.");
        return ResponseEntity.ok("Password has been updated");
    }


}
