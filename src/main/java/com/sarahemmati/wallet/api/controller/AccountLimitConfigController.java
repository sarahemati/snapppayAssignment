package com.sarahemmati.wallet.api.controller;

import com.sarahemmati.wallet.api.dto.CreateConfigDto;
import com.sarahemmati.wallet.application.services.AccountLimitConfigService;
import com.sarahemmati.wallet.infra.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account-limits")
@RequiredArgsConstructor
public class AccountLimitConfigController {
    private final AccountLimitConfigService service;

    @PostMapping
    @Transactional
    public ResponseEntity<CreateConfigDto> create(@RequestBody CreateConfigDto req) {
        return ResponseEntity.ok(service.createLimitConfig(req));
    }

    @GetMapping("/{username}")
    public ResponseEntity<CreateConfigDto> getForUser(@PathVariable String username) {
        return ResponseEntity.ok(service.getByUsername(username));
    }


}

