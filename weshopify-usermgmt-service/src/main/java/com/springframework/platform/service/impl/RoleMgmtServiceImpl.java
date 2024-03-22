package com.springframework.platform.service.impl;

import com.springframework.platform.bean.RoleBean;
import com.springframework.platform.outbound.WSO2ImRoleMgmtClient;
import com.springframework.platform.service.RoleMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMgmtServiceImpl implements RoleMgmtService {

    @Autowired
    private WSO2ImRoleMgmtClient wso2ImRoleMgmtClient;

    @Override
    public List<RoleBean> getAllRoles() {

        return wso2ImRoleMgmtClient.findAllRoles();
    }

    @Override
    public List<RoleBean> createRole(RoleBean roleBean) {
        return wso2ImRoleMgmtClient.createRole(roleBean);
    }
}
