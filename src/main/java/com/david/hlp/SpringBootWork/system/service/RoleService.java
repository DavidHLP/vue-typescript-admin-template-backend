package com.david.hlp.SpringBootWork.system.service;

import com.david.hlp.SpringBootWork.demo.util.DraggableTableResult;
import com.david.hlp.SpringBootWork.system.entity.Permission;
import com.david.hlp.SpringBootWork.system.entity.Role;
import com.david.hlp.SpringBootWork.system.requestentity.RoleRequest;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles() ;

    void disableUser(Long id);
    void enableUser(Long id) ;

    DraggableTableResult<Role> getRoleAndPermission(int page, int limit, String sort, String roleName, Boolean status , String permission) ;

    List<Permission> getPermissionsInfo();
    Role updateRole(Long roleId, RoleRequest roleRequest);
}
