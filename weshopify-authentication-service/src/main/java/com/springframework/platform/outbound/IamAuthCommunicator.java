package com.springframework.platform.outbound;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.platform.dto.WSO2User;
import com.springframework.platform.dto.WSO2UserAuthBean;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Optional;

@Component
@Slf4j
public class IamAuthCommunicator {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${weshopify-platform.oauth2.grant_type}")
    private String grant_type;

    @JsonIgnore
    @Value("${weshopify-platform.oauth2.clientId}")
    private String clientId;

    @JsonIgnore
    @Value("${weshopify-platform.oauth2.clientSecret}")
    private String clientSecret;


    @Value("${weshopify-platform.oauth2.uri}")
    private String tokenUrl;

    @Value("${weshopify-platform.oauth2.scope}")
    private String scope;

    @Value("${weshopify-platform.oauth2.logoutUri}")
    private String logoutUri;

    @Value("${weshopify-platform.oauth2.userInfoUri}")
    private String userInfoUri;

    @Value("${weshopify-platform.scim2.usersUri}")
    private String usersUrl;

    public String authenticate(WSO2UserAuthBean authBean) {
        String responseData = null;
        try {
            authBean.setGrant_type(grant_type);
            authBean.setScope(scope);
            String payload = objectMapper.writeValueAsString(authBean);
            HttpHeaders headers = basicAuthHeader(clientId,clientSecret);
            HttpEntity<String> request = prepareJsonRequestBody(headers,payload);
            ResponseEntity<String> response = restTemplate
                    .exchange(tokenUrl, HttpMethod.POST, request, String.class);
            log.info("status code is:\t"+response.getStatusCode().value());
            if (HttpStatus.OK.value() == response.getStatusCode().value()) {
                responseData = response.getBody();
                log.info("response body is:\t"+responseData);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(responseData).get();
    }

    public String getUserProfile(String accessToken) {

        userInfoUri = userInfoUri+scope;
        log.info("uri is:\t" + userInfoUri);
        String responseData = null;
        WSO2User wso2User = null;
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer " + accessToken);
            HttpEntity<String> requestBody = new HttpEntity<String>(headers);
            ResponseEntity<String> response = restTemplate
                    .exchange(userInfoUri, HttpMethod.GET, requestBody, String.class);
            log.info("status code is:\t"+response.getStatusCode().value());
            if (HttpStatus.OK.value() == response.getStatusCode().value()) {
                responseData = response.getBody();
                log.info("response body is:\t"+responseData);
                JSONObject jsonObject = new JSONObject(responseData);
                String userId = jsonObject.getString("WeshopifyUserId");
                log.info("user id is:\t"+userId);
                String updatedUsersUrl = usersUrl;
                String updateUrl = updatedUsersUrl+userId;

                HttpHeaders adminHeaders = basicAuthHeader("admin","admin");
                HttpEntity<String> usersRequestBody = new HttpEntity<String>(adminHeaders);
                ResponseEntity<String> usersResponse = restTemplate
                        .exchange(updateUrl, HttpMethod.GET, usersRequestBody, String.class);
                if (HttpStatus.OK.value() == response.getStatusCode().value()) {
                     //wso2User = objectMapper.readValue(usersResponse.getBody(), WSO2User.class);
                    responseData = usersResponse.getBody();
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(responseData).get();
    }

    public String logout(String tokenType,String token) {

        log.info("uri is:\t" + logoutUri);
        String responseData = null;
        try {

            String payload = "token_type_hint="+tokenType+"&token="+token;
            HttpHeaders headers = basicAuthHeader(clientId,clientSecret);

            HttpEntity<String> request = prepareFormRequestBody(headers,payload);

            ResponseEntity<String> response = restTemplate
                    .exchange(logoutUri, HttpMethod.POST, request, String.class);
            log.info("status code is:\t"+response.getStatusCode().value());
            if (HttpStatus.OK.value() == response.getStatusCode().value()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message","User has been logged out successfully");
                responseData = jsonObject.toString();
                log.info("response body is:\t"+responseData);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(responseData).get();

    }

    private HttpHeaders basicAuthHeader(String username, String password) {

        String adminCreds = username + ":" + password;
        log.info("admin creds are:\t"+adminCreds);

        String encodedAdminCreds = Base64.getEncoder().encodeToString(adminCreds.getBytes());
        log.info("admin creds encoded are:\t"+encodedAdminCreds);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Basic "+encodedAdminCreds);

        return headers;
    }

    private HttpEntity<String> prepareJsonRequestBody( HttpHeaders headers, String payload) {

        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> requestBody = new HttpEntity<String>(payload,headers);
        return requestBody;
    }

    private HttpEntity<String> prepareFormRequestBody( HttpHeaders headers, String payload) {

        headers.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        HttpEntity<String> requestBody = new HttpEntity<String>(payload,headers);
        return requestBody;
    }
}
