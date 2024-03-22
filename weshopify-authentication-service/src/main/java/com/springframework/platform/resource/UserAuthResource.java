package com.springframework.platform.resource;

import com.springframework.platform.bean.UserAuthBean;
import com.springframework.platform.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserAuthResource {

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping(value = {"/users/token"})
    public ResponseEntity<String> authenticate(UserAuthBean userAuthBean) {
        String authResponse = userAuthService.authenticate(userAuthBean);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(value = {"/users/logout"})
    public ResponseEntity<String> logout(@RequestParam("token_type_hint") String tokenType,
                                         @RequestParam("token") String token) {

        String logoutResponse = userAuthService.logout(tokenType, token);
        return ResponseEntity.ok(logoutResponse);
    }


}
