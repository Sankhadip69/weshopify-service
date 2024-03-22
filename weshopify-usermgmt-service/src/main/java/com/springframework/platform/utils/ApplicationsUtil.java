package com.springframework.platform.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.springframework.platform.bean.RoleBean;
import com.springframework.platform.dto.WSO2User;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Component
@Data
@Slf4j
public class ApplicationsUtil {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${iamserver.base-url}")
    private String iam_server_api_base_url;

    @Value("${iamserver.role-api}")
    private String role_api_context;

    @Value("${iamserver.user-api}")
    private String user_api_context;

    @Value("${iamserver.user-name}")
    private String iam_user_name;

    @Value("${iamserver.password}")
    private String iam_password;

    @Value("${iamserver.role-api-schema}")
    private String roleApiSchema;

    public HttpEntity<String> prepareRequestBody(String rolePayload) {

        HttpEntity<String> requestBody = null;
        String adminCreds = iam_user_name+":"+iam_password;
        log.info("admin creds are:\t"+adminCreds);

        String encodedAdminCreds = Base64.getEncoder().encodeToString(adminCreds.getBytes());
        log.info("admin creds encoded are:\t"+encodedAdminCreds);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Basic "+encodedAdminCreds);
        if (StringUtils.isEmpty(rolePayload) || StringUtils.isBlank(rolePayload)) {
            requestBody = new HttpEntity<>(headers);
        } else {
            headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            requestBody = new HttpEntity<String>(rolePayload,headers);
        }

        return requestBody;
    }

    public List<RoleBean> parseRoleResponse(Object responseBody) {

        List<RoleBean> resourcesList = null;
        try {
            String response = objectMapper.writeValueAsString(responseBody);
            log.info("the response body is:\t"+response);

            JSONObject jsonResponseObject = new JSONObject(response);
            JSONArray jsonArray = (JSONArray) Optional.ofNullable(jsonResponseObject)
                    .filter(condition -> jsonResponseObject.has("Resources"))
                    .get().get("Resources");
            log.info("Resources are:\t" + jsonArray.toString());

            Gson gson = new Gson();
            Type type = new TypeToken<List<RoleBean>>() {
            }.getType();
            resourcesList = gson.fromJson(jsonArray.toString(), type);
            log.info("Resources list are:\t" + resourcesList.size());

        }catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
        }
        return Optional.ofNullable(resourcesList).get();
    }

    public List<WSO2User> parseUserResponse(Object responseBody) {

        List<WSO2User> resourcesList = null;
        try {
            String response = objectMapper.writeValueAsString(responseBody);
            log.info("the response body is:\t"+response);

            JSONObject jsonResponseObject = new JSONObject(response);
            JSONArray jsonArray = (JSONArray) Optional.ofNullable(jsonResponseObject)
                    .filter(condition -> jsonResponseObject.has("Resources"))
                    .get().get("Resources");
            log.info("Resources are:\t" + jsonArray.toString());

            Gson gson = new Gson();
            Type type = new TypeToken<List<WSO2User>>() {
            }.getType();
            resourcesList = gson.fromJson(jsonArray.toString(), type);
            log.info("Resources list are:\t" + resourcesList.size());

        }catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
        }
        return Optional.ofNullable(resourcesList).get();
    }

}
