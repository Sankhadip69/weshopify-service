package com.springframework.platform.resource;

import com.springframework.platform.bean.RoleBean;
import com.springframework.platform.bean.UserBean;
import com.springframework.platform.bean.WeshopifyPlatformUserBean;
import com.springframework.platform.service.RoleMgmtService;
import com.springframework.platform.service.UserMgmtService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author : Sankhadip
 * @since : 26/12/2023
 */

@RestController
@Slf4j
public class UserResource {

    @Autowired
    private RoleMgmtService roleMgmtService;

    @Autowired
    private UserMgmtService userMgmtService;

    @PostMapping(value = "/users")
    public ResponseEntity<List<UserBean>> createUser(@Valid @RequestBody UserBean userBean) {
        log.info("Weshopify User's data is " +userBean.toString());
        List<UserBean> usersList = userMgmtService.createUser(userBean);
        return ResponseEntity.ok(usersList);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserBean>> findAllUsers() {
        List<UserBean> usersList = userMgmtService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }

    @PutMapping(value = "/users")
    public ResponseEntity<List<UserBean>> updateUser(@RequestBody UserBean userBean) {
        log.info("Weshopify User's data is " +userBean.toString());
        return null;
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<UserBean> findUserById(@PathVariable("userId") String userId) {
        return null;
    }

    @DeleteMapping(value = "/users/{userId}")
    public ResponseEntity<List<UserBean>> deleteUserById(@PathVariable("userId") String userId) {
        return null;
    }

    @GetMapping(value = "/users/roles")
    public ResponseEntity<List<RoleBean>> findAllRoles( ) {
        List<RoleBean> rolesList = roleMgmtService.getAllRoles();
        ResponseEntity<List<RoleBean>> rolesResponse = null;

        if (null != rolesList && rolesList.size() > 0) {
            rolesResponse = ResponseEntity.ok().body(rolesList);
        } else {
            rolesResponse = ResponseEntity.noContent().build();
        }
        return rolesResponse;
    }

    @PostMapping(value = "/users/roles")
    public ResponseEntity<List<RoleBean>> createRole(@RequestBody RoleBean roleBean) {
        List<RoleBean> rolesList = roleMgmtService.createRole(roleBean);
        ResponseEntity<List<RoleBean>> rolesResponse = null;

        if (null != rolesList && rolesList.size() > 0) {
            rolesResponse = ResponseEntity.ok().body(rolesList);
        } else {
            rolesResponse = ResponseEntity.noContent().build();
        }
        return rolesResponse;
    }
}
