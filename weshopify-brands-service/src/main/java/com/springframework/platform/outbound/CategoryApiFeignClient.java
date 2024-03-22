package com.springframework.platform.outbound;

import com.springframework.platform.bean.CategoryBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "WESHOPIFY-CATEGORY-SERVICE", value = "WESHOPIFY-CATEGORY-SERVICE")
public interface CategoryApiFeignClient {

    @GetMapping(value = "/categories/{catId}")
    public ResponseEntity<String> findCategoryById(
            @PathVariable("catId") int catId,
            @RequestHeader final Map<String,String> header);


}
