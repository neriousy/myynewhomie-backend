package com.druzynav.controllers;

import com.druzynav.auth.AuthenticationService;
import com.druzynav.auth.dto.AuthenticationRequest;
import com.druzynav.auth.dto.AuthenticationResponse;
import com.druzynav.auth.dto.RegisterRequest;
import com.druzynav.models.user.dto.UserDTO;
import com.druzynav.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/api/v1/auth/register")
    public ResponseEntity register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        return authenticationService.register(request);
    }

    @GetMapping("/api/v1/auth/confirm")
    public ResponseEntity<String> confirm(
            @RequestParam("token") String token
    ) {

        return ResponseEntity.ok(authenticationService.confirmToken(token));
    }



    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        return ResponseEntity.ok(authenticationResponse);
    }

    @GetMapping("/api/auth/test")
    public ResponseEntity<String> authenticate(
    ) {
        return ResponseEntity.ok("xd-test");
    }

}

