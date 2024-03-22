package com.springframework.platform.resources;

import com.springframework.platform.bean.BrandBean;
import com.springframework.platform.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class BrandResource {

    private BrandService brandService;

    public BrandResource(BrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "createBrand", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/brands")
    public ResponseEntity<BrandBean> createBrand(@RequestBody BrandBean brandBean) {
        log.info("brands bean data is:\t"+brandBean.toString());
        return ResponseEntity.ok(brandService.createBrand(brandBean));
    }

    @Operation(summary = "updateBrandBean", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/brands")
    public ResponseEntity<BrandBean> updateBrandBean(@RequestBody BrandBean brandBean) {
        return ResponseEntity.ok(brandService.updateBrand(brandBean));
    }

    @Operation(summary = "deleteBrand", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/brands/{brandId}")
    public ResponseEntity<List<BrandBean>> deleteBrand(@PathVariable("brandId") int brandId) {
        return ResponseEntity.ok(brandService.deleteBrand(brandId));
    }

    @Operation(summary = "cleanDB", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/brands/cleandb")
    public ResponseEntity<Map<String,String>> cleanDB() {
        brandService.cleanDB();
        Map<String,String> messageMap = new HashMap<>();
        messageMap.put("message","All the records deleted from database");
        return ResponseEntity.ok(messageMap);
    }

    @Operation(summary = "findAllBrands", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/brands")
    public ResponseEntity<List<BrandBean>> findAllBrands() {
        return ResponseEntity.ok(brandService.findAllBrands());
    }

    @Operation(summary = "getBrandById", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/brands/{brandId}")
    public ResponseEntity<BrandBean> getBrandById(@PathVariable("brandId") int brandId) {
        return ResponseEntity.ok(brandService.findBrandById(brandId));
    }
}
