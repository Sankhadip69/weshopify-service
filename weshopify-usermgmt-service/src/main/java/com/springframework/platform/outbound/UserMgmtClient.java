package com.springframework.platform.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.platform.dto.WSO2User;
import com.springframework.platform.exception.APIException;
import com.springframework.platform.utils.ApplicationsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserMgmtClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationsUtil propertiesUtil;

    @Autowired
    private ObjectMapper objectMapper;

    public List<WSO2User> findAllUsers() {

        try{

            String user_api_uri = propertiesUtil.getIam_server_api_base_url() + propertiesUtil.getUser_api_context();
            log.info("user api is ", user_api_uri);
            List<WSO2User> wso2UsersList = null;
            HttpEntity<String> requestBody = propertiesUtil.prepareRequestBody(null);

            ResponseEntity<Object> apiResponse = restTemplate.exchange(user_api_uri, HttpMethod.GET, requestBody, Object.class);
            log.info("response code of the role api is:\t"+apiResponse.getStatusCode().value());

            if (HttpStatus.OK.value() == apiResponse.getStatusCode().value()) {
                Object responseBody = apiResponse.getBody();
                wso2UsersList = propertiesUtil.parseUserResponse(responseBody);
            } else {
                throw new APIException(apiResponse.getBody().toString(),apiResponse.getStatusCode().value());
            }

            return Optional.ofNullable(wso2UsersList).get();

        }catch (Exception ex) {
            throw new APIException(ex.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    public List<WSO2User> createUser(WSO2User wso2User) {

        try {

            String user_api_uri = propertiesUtil.getIam_server_api_base_url() + propertiesUtil.getUser_api_context();
            log.info("user api is ", user_api_uri);
            List<WSO2User> wso2UsersList = null;

            String payload = null;
            payload = objectMapper.writeValueAsString(wso2User);

            HttpEntity<String> requestBody = propertiesUtil.prepareRequestBody(payload);

            ResponseEntity<Object> apiResponse = restTemplate.exchange(user_api_uri,HttpMethod.POST,requestBody, Object.class);

            log.info("response code of the role api is:\t"+apiResponse.getStatusCode().value());

            if (HttpStatus.CREATED.value() == apiResponse.getStatusCode().value()) {
                Object responseBody = apiResponse.getBody().toString();
                wso2UsersList = findAllUsers();
            } else {
                throw new APIException(apiResponse.getBody().toString(),apiResponse.getStatusCode().value());
            }

            return Optional.ofNullable(wso2UsersList).get();

        }catch (Exception ex) {

            throw new APIException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        }


    }
}
