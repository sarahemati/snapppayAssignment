package com.sarahemmati.wallet.api.controller;

import com.sarahemmati.wallet.api.dto.LoginReq;
import com.sarahemmati.wallet.api.dto.SignupReq;
import com.sarahemmati.wallet.application.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth){ this.auth = auth; }

    public record TokenRes(String accessToken){}

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupReq req){
        auth.signup(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public TokenRes login(@RequestBody LoginReq req){
        return new TokenRes(auth.login(req));
    }
}
