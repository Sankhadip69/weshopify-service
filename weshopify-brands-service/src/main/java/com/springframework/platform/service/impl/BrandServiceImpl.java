package com.springframework.platform.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.platform.bean.BrandBean;
import com.springframework.platform.bean.CategoryBean;
import com.springframework.platform.entity.Brand;
import com.springframework.platform.exception.APIException;
import com.springframework.platform.outbound.CategoriesApiClient;
import com.springframework.platform.outbound.CategoryApiFeignClient;
import com.springframework.platform.repository.BrandRepository;
import com.springframework.platform.service.BrandService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
public class BrandServiceImpl implements BrandService {

    private BrandRepository brandRepository;

    //@Autowired
    //private CategoriesApiClient categoriesApiClient;
    @Autowired
    private CategoryApiFeignClient apiFeignClient;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ObjectMapper objectMapper;


    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public BrandBean createBrand(BrandBean brandBean) {

        try {
            return convertEntityToBean(brandRepository.save(convertBeanToEntity(brandBean)));
        }catch (Exception ex) {
            throw new APIException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @Override
    public BrandBean updateBrand(BrandBean brandBean) {
        return convertEntityToBean(brandRepository.save(convertBeanToEntity(brandBean)));
    }

    @Override
    public List<BrandBean> findAllBrands() {
        List<Brand> brandList = brandRepository.findAll();
        if (!CollectionUtils.isEmpty(brandList)) {
            List<BrandBean> brandBeanList = new ArrayList<>();
            brandList.stream().forEach(brand -> {
                brandBeanList.add(convertEntityToBean(brand));
            });
            return brandBeanList;
        } else {
            throw new RuntimeException("No Brands Available in DB");
        }
    }

    @Override
    public BrandBean findBrandById(int brandId) {
        return convertEntityToBean(brandRepository.findById(brandId).get());
    }

    @Override
    public List<BrandBean> deleteBrand(int brandId) {
        brandRepository.deleteById(brandId);
        return findAllBrands();
    }

    @Override
    public void cleanDB() {
        brandRepository.deleteAll();
    }

    /**
     * converting Bean to Entity
     * and making a rest template call to
     * category service
    private Brand convertBeanToEntity(BrandBean brandBean) {

        Brand brand = new Brand();
        brand.setId(brandBean.getId());
        brand.setName(brandBean.getName());
        brand.setLogoPath(brandBean.getLogoPath());


         * Invoke the categories service and fetch the categories
         * from categories service

        if (!CollectionUtils.isEmpty(brandBean.getCategories())) {
            List<CategoryBean> originalCats = new ArrayList<CategoryBean>();
            brandBean.getCategories().parallelStream().forEach(categoryBean -> {

                    String catResp = categoriesApiClient.findCategoryById(null, categoryBean.getId());
                    try {
                        objectMapper.readValue(catResp, CategoryBean.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            brand.setCategories(originalCats);
        }

        if (brandBean.getId() > 0) {
            brand.setId(brand.getId());
        }
        return brand;
    }
     **/

    /**
     * converting Bean to Entity
     * and making a feign client call to
     * category service
     * @param
     * @return
     */

    private Brand convertBeanToEntity(BrandBean brandBean) {

        Brand brand = new Brand();
        brand.setId(brandBean.getId());
        brand.setName(brandBean.getName());
        brand.setLogoPath(brandBean.getLogoPath());


         /** Invoke the categories service and fetch the categories
         * from categories service
          **/

        if (!CollectionUtils.isEmpty(brandBean.getCategories())) {
            Set<CategoryBean> originalCats = new HashSet<>( );
            String headerWithBearer = request.getHeader(HttpHeaders.AUTHORIZATION);
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put(HttpHeaders.AUTHORIZATION, headerWithBearer);

            brandBean.getCategories().parallelStream().forEach(categoryBean -> {
                long startTime  = System.currentTimeMillis();

                ResponseEntity<String> catRespEntity = apiFeignClient.findCategoryById( categoryBean.getId(),headerMap);

                long endTime  = System.currentTimeMillis();
                log.info("total time taken by category service feign client {}",(endTime - startTime));
                if (catRespEntity != null && HttpStatus.OK.value() == catRespEntity.getStatusCode().value()) {

                    try {
                        originalCats.add(objectMapper.readValue(catRespEntity.getBody(), CategoryBean.class));
                    } catch (JsonProcessingException e) {
                        throw new APIException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
                    }
                }
            });

            brand.setCategories(originalCats);
        }

        if (brandBean.getId() > 0) {
            brand.setId(brand.getId());
        }
        return brand;
    }
    private BrandBean convertEntityToBean(Brand brand) {

        BrandBean brandBean = new BrandBean();
        brandBean.setId(brand.getId());
        brandBean.setName(brand.getName());
        brandBean.setLogoPath(brand.getLogoPath());
        brandBean.setCategories(brand.getCategories());
        return brandBean;
    }
}
