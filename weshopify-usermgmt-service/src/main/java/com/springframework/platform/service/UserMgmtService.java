package com.springframework.platform.service;

import com.springframework.platform.bean.UserBean;

import java.util.List;

public interface UserMgmtService {

    List<UserBean> getAllUsers();

    List<UserBean> createUser(UserBean userBean);
}
