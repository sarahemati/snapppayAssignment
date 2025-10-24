package com.sarahemmati.wallet.api.controller;


import com.sarahemmati.wallet.api.dto.AmountReq;
import com.sarahemmati.wallet.api.dto.TransferReq;
import com.sarahemmati.wallet.application.services.WalletService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final WalletService wallet;
    public WalletController(WalletService wallet){ this.wallet = wallet; }


    @GetMapping("/me")
    public WalletService.WalletView me(Authentication auth){
        return wallet.me(auth.getName());
    }

    @PostMapping("/deposit")
    public void deposit(Authentication auth,
                        @RequestHeader(value="X-Idempotency-Key", required=false) String ref,
                        @RequestBody AmountReq req){
        wallet.deposit(auth.getName(), req.amount(), ref);
    }


    @PostMapping("/transfer")
    public void transfer(Authentication auth,
                         @RequestHeader(value="X-Idempotency-Key", required=false) String ref,
                         @RequestBody TransferReq req){
        if(ref == null || ref.isBlank()) ref = UUID.randomUUID().toString();
        wallet.transfer(auth.getName(), req.toUsername(), req.amount(), ref);
    }
}
