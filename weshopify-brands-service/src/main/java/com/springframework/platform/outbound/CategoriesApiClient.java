package com.springframework.platform.outbound;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@Slf4j
public class CategoriesApiClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.categories.base-uri}")
    private String categoriesUri;

    @Autowired
    private HttpServletRequest request;

    public String findCategoryById(String access_token, int catId) {
        /**
         * step - 1: Prepare the Authorization header
         */
        String headerWithBearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, headerWithBearer);

        /**
         * step -2: Prepare Request Body
         */
        HttpEntity<String> requestBody = new HttpEntity<>(headers);

        /**
         * step -3: Invoke the categories api
         */
        String findCatByIdUri = categoriesUri+"/"+catId;
        log.info("get categories uri is {}",findCatByIdUri);
        ResponseEntity<String> catResp = restTemplate.exchange(findCatByIdUri, HttpMethod.GET, requestBody, String.class);
        log.info("response code is {}", catResp.getStatusCode().value());
        if (HttpStatus.OK.value() == catResp.getStatusCode().value()) {
            String responseBody = catResp.getBody();
            log.info("Category service api response is {}",responseBody);
            return responseBody;
        } else {
            log.error("No Category Found with the category Id "+catId);
          throw new RuntimeException("No Category Found with the category Id "+catId);
        }
    }
}
