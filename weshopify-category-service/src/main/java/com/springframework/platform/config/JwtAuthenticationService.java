package com.springframework.platform.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.platform.dto.RoleDto;
import com.springframework.platform.dto.WSO2User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

@Component
@Slf4j
public class JwtAuthenticationService {

    private RedisTemplate<String,String> redisTemplate;
    HashOperations<String,String,String> hashOps = null;

    @Autowired
    private ObjectMapper objectMapper;

    public JwtAuthenticationService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOps = redisTemplate.opsForHash();
    }

    private static final String JWT_TOKEN_HEADER_NAME = "Authorization";
    private static final String JWT_TOKEN_TYPE = "Bearer ";
    private static final String JWT_TOKEN_EXPIRY_KEY = "tokenExpiry";
    private static final String USERS_ROLES_KEY = "USER_ROLES";
    private static final String USER_SUBJECT_NAME = "SUBJECT";

    public Authentication authenticateUser(HttpServletRequest request) {

        Authentication authentication =null;
        String token = resolveToken(request);
        boolean isTokenValid = validateToken(token);
        if (isTokenValid) {

            Set<String> hkset = hashOps.keys(token);

            List<GrantedAuthority> roles  = new ArrayList<>();

            Iterator<String> it  = hkset.iterator();
            while (it.hasNext()) {
                String randomHash = it.next();
                String wso2UserData = hashOps.get(token, randomHash);

                try{
                    WSO2User wso2User = objectMapper.readValue(wso2UserData,WSO2User.class);
                    List<RoleDto> roleDtos = wso2User.getRoleBeanList();
                    if (roleDtos != null) {
                        for (RoleDto roleDto : roleDtos) {
                            if ("default".equals(roleDto.getType())) {
                                String[] userRolesList = roleDto.getValue().split("\\, ");
                                for (String userRole : userRolesList) {
                                    if (!userRole.equals("Internal/everyone")) {
                                        userRole = userRole.replace("Application/", "").trim();
                                        log.info("provisioned user role is :\t" + userRole);
                                        roles.add(new SimpleGrantedAuthority(userRole));
                                    } else {
                                        log.info("skipping the Internal/everyone role");
                                    }
                                }
                            } else {
                                log.info("skipping the non default roles");
                            }
                        }
                    }else {
                        log.warn("Role list is null for user: {}");
                    }

                    String userName = wso2User.getUserName();
                    authentication = new UsernamePasswordAuthenticationToken(userName,null,roles);
                }catch (JsonMappingException ex) {
                    ex.printStackTrace();
                }catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
            }

        }
        return authentication;
    }
    private boolean validateToken(String token) {
        if (hashOps.hasKey(JWT_TOKEN_EXPIRY_KEY, token)) {
            String expiryInSeconds = hashOps.get(JWT_TOKEN_EXPIRY_KEY, token);
            if (expiryInSeconds != null) {
                long tokenExpiryInSeconds = Long.parseLong(expiryInSeconds);
                if (expiryDate(tokenExpiryInSeconds).before(new Date())) {
                    log.error("Token has expired: {}", token);
                    return false;
                }
                return true;
            } else {
                log.error("Expiry information not found for token: {}", token);
                return false;
            }
        } else {
            log.error("Token not found in Redis: {}", token);
            return false;
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String headerValue = request.getHeader(JWT_TOKEN_HEADER_NAME);
        if (StringUtils.hasText(headerValue)) {
            headerValue = headerValue.replace(JWT_TOKEN_TYPE,"");
            log.info("jwt token is {}",headerValue);
            return headerValue;
        } else {
            throw HttpClientErrorException.Unauthorized.create("No Token Provided", HttpStatus.FORBIDDEN,"Forbidden",null,null,null);
        }

    }

    private Date expiryDate(long tokenExpiry) {
        Date date = new Date();
        long time = date.getTime()+tokenExpiry*1000;
        Date updatedDate = new Date(time);
        return updatedDate;
    }

}
