package com.sarahemmati.wallet.api.controller;

import com.sarahemmati.wallet.application.services.AuthService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth){ this.auth = auth; }

    public record SignupReq(@NotBlank String username, @Size(min=6) String password){}
    public record LoginReq(@NotBlank String username, @NotBlank String password){}
    public record TokenRes(String accessToken){}

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupReq req){
        auth.signup(req.username(), req.password());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public TokenRes login(@RequestBody LoginReq req){
        return new TokenRes(auth.login(req.username(), req.password()));
    }
}
