package com.sarahemmati.wallet.api.controller;


import com.sarahemmati.wallet.api.dto.AmountReq;
import com.sarahemmati.wallet.api.dto.TransferReq;
import com.sarahemmati.wallet.api.dto.WalletResponse;
import com.sarahemmati.wallet.application.services.WalletService;
import com.sarahemmati.wallet.domain.Wallet;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final WalletService wallet;
    public WalletController(WalletService wallet){ this.wallet = wallet; }

    @Operation(summary = "My wallet", description = "Balance and last 20 ledger items")
    @GetMapping("/myWallet")
    public WalletResponse myWallet(Authentication auth){
        return wallet.myWallet(auth.getName());
    }

    @Operation(summary = "Deposit", description = "Credit to your own wallet")
    @PostMapping("/deposit")
    public void deposit(Authentication auth,
                        @RequestHeader(value="X-Idempotency-Key", required=false) String ref,
                        @RequestHeader(value="X-Request-Id", required=false) String reqId,
                        @RequestBody @Valid AmountReq req) {

        wallet.deposit(auth.getName(), req.amount(), ref,reqId);
    }


    @PostMapping("/transfer")
    @Operation(summary = "Transfer", description = "transfer to other user's wallet")

    public void transfer(Authentication auth,
                         @RequestHeader(value="X-Idempotency-Key", required=false) String ref,
                         @RequestHeader(value="X-Request-Id", required=false) String reqId,
                         @RequestBody @Valid TransferReq req) {
        if(ref == null || ref.isBlank()) ref = UUID.randomUUID().toString();
        wallet.transfer(auth.getName(), req.toUsername(), req.amount(), ref,reqId);
    }
}
