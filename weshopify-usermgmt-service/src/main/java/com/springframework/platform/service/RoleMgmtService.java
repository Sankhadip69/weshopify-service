package com.springframework.platform.service;

import com.springframework.platform.bean.RoleBean;

import java.util.List;

public interface RoleMgmtService {

    List<RoleBean> getAllRoles();

    List<RoleBean> createRole(RoleBean roleBean);
}
