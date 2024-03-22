package com.springframework.platform.service.impl;

import com.springframework.platform.bean.CategoryBean;
import com.springframework.platform.cqrs.commands.CategoryCommand;
import com.springframework.platform.entity.Category;
import com.springframework.platform.repository.CategoryRepository;
import com.springframework.platform.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    private CommandGateway commandBus;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryBean createCategory(CategoryBean categoryBean) {
        return convertEntityToBean(categoryRepository.save(convertBeanToEntity(categoryBean)));
    }

    @Override
    public CategoryBean updateCategory(CategoryBean categoryBean) {
        CategoryBean catBean = convertEntityToBean(categoryRepository.save(convertBeanToEntity(categoryBean)));
        CategoryCommand command = createCommand(catBean);
        log.info("step-1: Command sending to Command Handler");
        CompletableFuture<CategoryCommand> future = commandBus.send(command);
        if (future.isDone()) {
            log.info("category updates were delivered to consumer services");
        }else {
            log.error("category updates were not delivered to the consumers. they may retry the event store ");
        }
        return catBean;
    }

    @Override
    public List<CategoryBean> findAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        if (!CollectionUtils.isEmpty(categoryList)) {
            List<CategoryBean> categoryBeanList = new ArrayList<>();
            categoryList.stream().forEach(category -> {
                categoryBeanList.add(convertEntityToBean(category));
            });

            return categoryBeanList;
        } else {
            throw new RuntimeException("No categories found in Database");
        }

    }

    @Override
    public CategoryBean findCategoryById(int catId) {
        return convertEntityToBean(categoryRepository.findById(catId).get());
    }

    @Override
    public List<CategoryBean> deleteCategory(int catId) {
        categoryRepository.deleteById(catId);
        return findAllCategories();
    }

    @Override
    public List<CategoryBean> findAllChilds(int parentId) {
        List<Category> catEntityList = categoryRepository.findChildsOfAParent(parentId);
        if (!CollectionUtils.isEmpty(catEntityList)) {
            List<CategoryBean> categoryBeanList = new ArrayList<>();
            catEntityList.stream().forEach(category -> {
                categoryBeanList.add(convertEntityToBean(category));
            });

            return categoryBeanList;
        } else {
            throw new RuntimeException("No Child categories found For the Parent:\t"+parentId);
        }

    }


    /**
     * converting bean to entity
     * @param categoryBean
     * @return
     */
    private Category convertBeanToEntity(CategoryBean categoryBean) {
        Category catEntity = new Category();
        catEntity.setName(categoryBean.getName());
        catEntity.setAlias(categoryBean.getAlias());
        catEntity.setEnabled(true);
        //catEntity.setImagePath();
        if (categoryBean.getPcategory() > 0) {
            catEntity.setParent(categoryRepository.findById(categoryBean.getPcategory()).get());
        }

        if(categoryBean.getId() > 0) {
            log.info("updating the entity");
            catEntity.setId(categoryBean.getId());
        }

        return catEntity;

    }

    /**
     * convert Entity to Bean
     * @param catEntity
     * @return
     */
    private CategoryBean convertEntityToBean(Category catEntity) {
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setAlias(catEntity.getAlias());
        categoryBean.setName(catEntity.getName());
        categoryBean.setEnabled(catEntity.isEnabled());
        categoryBean.setId(catEntity.getId());
        if (catEntity.getParent() != null) {
            categoryBean.setPcategory(catEntity.getParent().getId());
        }

        return categoryBean;
    }

    /**
     * Converting the updated bean to command
     * @param categoryBean
     * @return
     */
    private CategoryCommand createCommand(CategoryBean categoryBean) {

        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setAlias(categoryBean.getAlias());
        categoryCommand.setName(categoryBean.getName());
        categoryCommand.setEnabled(categoryBean.isEnabled());
        String randomId = RandomStringUtils.randomAlphanumeric(17).toUpperCase();
        categoryCommand.setEventId(randomId);
        categoryCommand.setId(categoryBean.getId());
        categoryCommand.setPcategory(categoryBean.getPcategory());

        return categoryCommand;
    }
}
