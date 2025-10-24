package com.sarahemmati.wallet.application.services;


import com.sarahemmati.wallet.application.JwtUtil;
import com.sarahemmati.wallet.domain.User;
import com.sarahemmati.wallet.domain.Wallet;
import com.sarahemmati.wallet.infra.repository.UserRepo;
import com.sarahemmati.wallet.infra.repository.WalletRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepo users;
    private final WalletRepo wallets;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthService(UserRepo users, WalletRepo wallets, PasswordEncoder encoder, JwtUtil jwt){
        this.users = users; this.wallets = wallets; this.encoder = encoder; this.jwt = jwt;
    }

    @Transactional
    public void signup(String username, String rawPassword){
        users.findByUsername(username).ifPresent(u -> { throw new IllegalArgumentException("USERNAME_TAKEN"); });
        User u = new User(username, encoder.encode(rawPassword), "ROLE_USER");
        users.save(u);
        wallets.save(new Wallet(u));
    }

    public String login(String username, String rawPassword){
        User u = users.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("INVALID_CREDENTIALS"));
        if(!encoder.matches(rawPassword, u.getPasswordHash())) throw new IllegalArgumentException("INVALID_CREDENTIALS");
        return jwt.generate(u.getUsername());
    }
}
