package com.logicea.cards.controllers;

import com.logicea.cards.component.JwtTokenProvider;
import com.logicea.cards.entity.User;
import com.logicea.cards.models.Response;
import com.logicea.cards.models.SignInRequest;
import com.logicea.cards.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cards/auth")
public class AuthController {
    public static Logger logger = LogManager.getLogger("com.logicea.cards");

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        try {
            Response response;
            String email = signInRequest.getEmail();
            String password = signInRequest.getPassword();
            User user = userService.authenticateUser(email, password);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
            String token = tokenProvider.generateToken(user);
            response = new Response();
            response.setEmail(user.getEmail());
            response.setRole(String.valueOf(user.getRole()));
            response.setToken(token);
            return ResponseEntity.ok(response);
        } catch (Exception e)
        {
            logger.error("Error logging in user "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Couldn't process request, please check server logs");
        }

    }
}
