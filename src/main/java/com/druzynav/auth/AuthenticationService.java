package com.druzynav.auth;

import com.druzynav.auth.dto.AuthenticationRequest;
import com.druzynav.auth.dto.AuthenticationResponse;
import com.druzynav.auth.dto.RegisterRequest;
import com.druzynav.auth.email.EmailSender;
import com.druzynav.auth.token.ConfirmationToken;
import com.druzynav.auth.token.ConfirmationTokenService;
import com.druzynav.exceptions.EmailIsAlreadyInUse.EmailIsAlreadyInUseException;
import com.druzynav.models.photo.Photo;
import com.druzynav.models.activity.Activity;
import com.druzynav.models.user.Role;
import com.druzynav.models.user.Status;
import com.druzynav.models.user.User;
import com.druzynav.repositories.PhotoRepository;
import com.druzynav.repositories.ActivityRepository;
import com.druzynav.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    private final PhotoRepository photoRepository;

    private final ActivityRepository activityRepository;


    @Value("${app.domain}")
    private String domain;

    @Value("${frontend.domain}")
    private String frontend;

    public ResponseEntity register(RegisterRequest request) {

        if(EmailIsTaken(request.getEmail())){
            throw new EmailIsAlreadyInUseException(request.getEmail());
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .phonenumber(request.getPhonenumber())
                .gender(request.getGender())
                .enabled(false)
                .role(Role.USER)
                .status(Status.INACTIVE)
                .description("")
                .still_looking(true)
                .build();

        //Moze tutaj tez warto by bylo dodac opis, zeby o sobie napisac pare slow

        userRepository.save(user);


        activityRepository.save(new Activity(request.getEmail(), LocalDateTime.now()));


        try {
            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user
            );

            confirmationTokenService.saveConfirmationToken(confirmationToken);

            //URI link = URI.create(domain + "/api/v1/auth/confirm?token=" + token);
            URI link = URI.create(frontend + "confirmEmail?token=" + token);

            emailSender.sendConfirmationMail(request.getEmail(), link.toString());

        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity("Zarejestrowanie powiodło się", HttpStatus.CREATED);
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null)
            throw new IllegalStateException("email is already confirmed");

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())){
            throw  new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userRepository.enableUser(confirmationToken.getUser().getEmail());

        return "confirmed";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();


        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public boolean EmailIsTaken(String email){
        Optional<User> user = userRepository.findByEmail(email);

        return user.isPresent();
    }
}



