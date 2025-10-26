package com.sarahemmati.wallet.application.services.impl;


import com.sarahemmati.wallet.api.dto.LoginReq;
import com.sarahemmati.wallet.api.dto.SignupReq;
import com.sarahemmati.wallet.application.JwtUtil;
import com.sarahemmati.wallet.application.services.AuthService;
import com.sarahemmati.wallet.domain.User;
import com.sarahemmati.wallet.domain.Wallet;
import com.sarahemmati.wallet.infra.repository.UserRepo;
import com.sarahemmati.wallet.infra.repository.WalletRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final WalletRepo wallets;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthServiceImpl(UserRepo users, WalletRepo wallets, PasswordEncoder encoder, JwtUtil jwt){
        this.userRepo = users; this.wallets = wallets; this.encoder = encoder; this.jwt = jwt;
    }

    @Transactional
    public void signup(SignupReq req){
        log.info("Signup request received {}",req);
        userRepo.findByUsername(req.username()).ifPresent(u -> { throw new IllegalArgumentException("USERNAME_TAKEN"); });
        User u = new User(req.username(), encoder.encode(req.password()), "ROLE_USER");
        userRepo.save(u);
        log.info("Signup successful");
        wallets.save(new Wallet(u));
    }



    public String login(LoginReq req){
        User u = userRepo.findByUsername(req.username()).orElseThrow(() -> new IllegalArgumentException("INVALID_CREDENTIALS"));
        if(!encoder.matches(req.username(), u.getPasswordHash())) throw new IllegalArgumentException("INVALID_CREDENTIALS");
        return jwt.generate(u.getUsername());
    }
}
