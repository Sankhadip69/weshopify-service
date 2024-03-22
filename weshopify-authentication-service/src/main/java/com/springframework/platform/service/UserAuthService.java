package com.springframework.platform.service;

import com.springframework.platform.bean.UserAuthBean;

public interface UserAuthService {

    String authenticate(UserAuthBean userAuthBean);

    String logout(String tokenType, String token);
}
