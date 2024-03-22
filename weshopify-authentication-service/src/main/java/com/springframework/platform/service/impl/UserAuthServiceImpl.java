package com.springframework.platform.service.impl;

import com.springframework.platform.bean.UserAuthBean;
import com.springframework.platform.dto.WSO2User;
import com.springframework.platform.dto.WSO2UserAuthBean;
import com.springframework.platform.outbound.IamAuthCommunicator;
import com.springframework.platform.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private IamAuthCommunicator authCommunicator;

    private RedisTemplate<String,String> redisTemplate;

    HashOperations<String, String, String> hashOp = null;

    @Autowired
    public UserAuthServiceImpl(IamAuthCommunicator authCommunicator, RedisTemplate<String, String> redisTemplate) {
        this.authCommunicator = authCommunicator;
        this.redisTemplate = redisTemplate;
        this.hashOp = redisTemplate.opsForHash();
    }

    @Override
    public String authenticate(UserAuthBean userAuthBean) {

        WSO2UserAuthBean wso2UserAuthBean = WSO2UserAuthBean.builder()
                .username(userAuthBean.getUserName())
                .password(userAuthBean.getPassword())
                .build();
        String authResponse = authCommunicator.authenticate(wso2UserAuthBean);
        log.info("authentication response is {}", authResponse);
        JSONObject jsonObject = new JSONObject(authResponse);
        String access_token = jsonObject.getString("access_token");
        log.info("access token is:\t" + access_token);

        int expiry = jsonObject.getInt("expires_in");
        if (StringUtils.isNotEmpty(authResponse)) {
            String randomHash = userAuthBean.getUserName() + "_" + RandomStringUtils.random(512);
            log.info("token hash is {}", randomHash);
            hashOp.put("tokenExpiry", access_token, String.valueOf(expiry));

            String wso2UserData = authCommunicator.getUserProfile(access_token);
            hashOp.put(access_token,randomHash,wso2UserData);
        }
        return authResponse;
    }

    @Override
    public String logout(String tokenType, String token) {
        String logoutResp = authCommunicator.logout(tokenType, token);
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotEmpty(logoutResp)) {
            try {
                // Retrieve keys for the user session
                Set<String> hkset = hashOp.keys("*" + token + "*");
                // Log the keys to ensure they are correct
                log.info("Keys found in Redis: {}", hkset);

                hkset.stream().forEach(randomHash -> {
                    String wso2User = hashOp.get(token,randomHash);

                    hashOp.delete(token, randomHash);
                });

                // Delete other related keys
                hashOp.delete("tokenExpiry", token);

                String logoutMessage = "User logged out successfully !!";
                jsonObject.put("message", logoutMessage);
            } catch (Exception e) {
                log.error("Error occurred during logout: {}", e.getMessage());
                // Handle any exceptions
                jsonObject.put("error", "Error occurred during logout");
            }
        } else {
            // Handle empty response from authCommunicator.logout
            jsonObject.put("error", "Empty response from logout endpoint");
        }
        return jsonObject.toString();
    }

}
