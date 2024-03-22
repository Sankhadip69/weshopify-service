package com.springframework.platform.service.impl;

import com.springframework.platform.bean.UserBean;
import com.springframework.platform.dto.WSO2PhoneNumbers;
import com.springframework.platform.dto.WSO2User;
import com.springframework.platform.dto.WSO2UserPersonals;
import com.springframework.platform.exception.APIException;
import com.springframework.platform.outbound.UserMgmtClient;
import com.springframework.platform.service.UserMgmtService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserMgmtServiceImpl implements UserMgmtService {

    @Autowired
    private UserMgmtClient userMgmtClient;

    @Override
    public List<UserBean> getAllUsers() {

        List<WSO2User> wso2UsersList = userMgmtClient.findAllUsers();

        if (!CollectionUtils.isEmpty(wso2UsersList)) {

            log.info("wso2 Users size",wso2UsersList.size());

            List<UserBean> usersList = new ArrayList<>();

            wso2UsersList.stream().forEach(wso2User -> {
                /**
                 * Filter the users list not to display
                 * the wso2 admin users details in the response
                 */

                Arrays.asList(wso2User.getEmails()).forEach(email->{
                    if (!email.contentEquals("admin@wso2.com")) {
                        usersList.add(mapWSO2UserToUserBean(wso2User));
                    }
                });

            });
            return usersList;

        } else {
            throw new RuntimeException("No Users Found");
        }

    }

    @Override
    public List<UserBean> createUser(UserBean userBean) {
        List<WSO2User> userList = userMgmtClient.createUser(mapUserBeanToWSO2User(userBean));
        if (!CollectionUtils.isEmpty(userList)) {
            List<UserBean> usersBeanList = new ArrayList<>();

            userList.parallelStream().forEach(wso2User -> {
                usersBeanList.add(mapWSO2UserToUserBean(wso2User));
            });

            return usersBeanList;
        } else {
            throw new APIException("No users found", HttpStatus.NOT_FOUND.value());
        }

    }

    /**
     * To Convert WSO2User to UserBean
     * @param wso2User
     * @return
     */
    private UserBean mapWSO2UserToUserBean(WSO2User wso2User) {
        UserBean userBean = UserBean
                            .builder()
                            .id(wso2User.getId())
                            .firstName(wso2User.getName().getGivenName())
                            .lastName(wso2User.getName().getFamilyName())
                            .emails(wso2User.getEmails())
                            .userId(wso2User.getUserName())
                            .build();

        return userBean;
    }

    /**
     * To Convert the UserBean to WSO2User
     * @param userBean
     * @return
     */
    private WSO2User mapUserBeanToWSO2User(UserBean userBean) {
        WSO2UserPersonals personals = WSO2UserPersonals.builder().familyName(userBean.getLastName())
                .givenName(userBean.getFirstName()).build();

        WSO2PhoneNumbers userContactNum = WSO2PhoneNumbers.builder().type("work").value(userBean.getMobile()).build();

        WSO2User wso2UserDto = WSO2User.builder()
                .emails(userBean.getEmails())
                .name(personals)
                .password(userBean.getPassword())
                .phoneNumbers(Arrays.asList(userContactNum))
                .schemas(new String[] {})
                .userName(userBean.getUserId())
                .build();

        return wso2UserDto;
    }
}
