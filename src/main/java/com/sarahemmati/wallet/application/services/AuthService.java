package com.sarahemmati.wallet.application.services;

import com.sarahemmati.wallet.api.dto.LoginReq;
import com.sarahemmati.wallet.api.dto.SignupReq;

public interface AuthService {
     void signup(SignupReq req);
     String login(LoginReq req);
}
