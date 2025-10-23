package com.sarahemmati.wallet.api.controller;


import com.sarahemmati.wallet.application.services.WalletService;
import jakarta.validation.constraints.Positive;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController @RequestMapping("/api/wallet")
public class WalletController {
    private final WalletService wallet;
    public WalletController(WalletService wallet){ this.wallet = wallet; }

    public record AmountReq(@Positive BigDecimal amount){}
    public record TransferReq(String toUsername, @Positive BigDecimal amount){}

    @GetMapping("/me")
    public WalletService.WalletView me(Authentication auth){
        return wallet.me(auth.getName());
    }

    @PostMapping("/deposit")
    public void deposit(Authentication auth, @RequestBody AmountReq req){
        wallet.deposit(auth.getName(), req.amount());
    }

    @PostMapping("/transfer")
    public void transfer(Authentication auth,
                         @RequestHeader(value="X-Idempotency-Key", required=false) String ref,
                         @RequestBody TransferReq req){
        if(ref == null || ref.isBlank()) ref = UUID.randomUUID().toString();
        wallet.transfer(auth.getName(), req.toUsername(), req.amount(), ref);
    }
}
