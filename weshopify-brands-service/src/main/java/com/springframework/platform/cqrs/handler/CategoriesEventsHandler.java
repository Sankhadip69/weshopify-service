package com.springframework.platform.cqrs.handler;

import com.springframework.platform.bean.CategoryBean;
import com.springframework.platform.cqrs.events.CategoryEvent;
import com.springframework.platform.repository.BrandRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class CategoriesEventsHandler {

    @Autowired
    private BrandRepository brandRepository;
    @EventHandler
    public CategoryEvent categoryEventHandler(CategoryEvent event) {
        log.info("event recived in category event  handler is "+event.toString());
        Set<CategoryBean> updatedCatSet = new HashSet<>();

        brandRepository.findAll().stream().forEach(brand -> {
            if (!CollectionUtils.isEmpty(brand.getCategories())) {
                brand.getCategories().forEach(cat -> {
                    if (event.getId() == cat.getId()) {
                        CategoryBean updatedCatBean = new CategoryBean();
                        updatedCatBean.setId(event.getId());
                        updatedCatBean.setEnabled(event.isEnabled());
                        updatedCatBean.setAlias(event.getAlias());
                        updatedCatBean.setName(event.getName());
                        updatedCatBean.setPcategory(event.getPcategory());
                        updatedCatSet.add(updatedCatBean);
                    } else {
                        updatedCatSet.add(cat);
                    }
                });
                brand.setCategories(updatedCatSet);
                brandRepository.save(brand);
            }
        });
        return null;
    }

    @QueryHandler
    public CategoryEvent queryCategoryEvents(CategoryEvent event) {
        log.info("event recived in category query  handler is "+event.toString());
        return null;
    }
}
