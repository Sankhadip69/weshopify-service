package com.springframework.platform.service;

import com.springframework.platform.bean.BrandBean;

import java.util.List;

public interface BrandService {

    BrandBean createBrand(BrandBean brandBean);

    BrandBean updateBrand(BrandBean brandBean);

    List<BrandBean> findAllBrands();

    BrandBean findBrandById(int brandId);

    List<BrandBean> deleteBrand(int brandId);

    void cleanDB();
}
