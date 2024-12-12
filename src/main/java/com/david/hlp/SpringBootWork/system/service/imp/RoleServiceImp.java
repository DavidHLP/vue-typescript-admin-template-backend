package com.david.hlp.SpringBootWork.system.service.imp;

import com.david.hlp.SpringBootWork.blog.util.DraggableTableResult;
import com.david.hlp.SpringBootWork.system.entity.Permission;
import com.david.hlp.SpringBootWork.system.entity.Role;
import com.david.hlp.SpringBootWork.system.entity.RolePermissionInfo;
import com.david.hlp.SpringBootWork.system.mapper.PermissionMapper;
import com.david.hlp.SpringBootWork.system.mapper.RoleMapper;
import com.david.hlp.SpringBootWork.system.requestentity.RoleRequest;
import com.david.hlp.SpringBootWork.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public List<Role> getAllRoles() {
        return roleMapper.selectAllRole();
    }

    public void disableUser(Long id) {
        roleMapper.updateStatus(id,false);
    }

    public void enableUser(Long id) {
        roleMapper.updateStatus(id,true);
    }

    public DraggableTableResult<Role> getRoleAndPermission(
            int page,int limit,String sort,String roleName, Boolean status , String permission
    ) {
        int offset = (page - 1) * limit;
        sort = sort.equals("+id") ? "ASC" : "DESC";
        List<Role> roles = roleMapper.selectByRoleAndPermissino(offset,page,limit,sort,roleName,status,permission);
        int total = roleMapper.countFilteredRoles(roleName,status ,permission);
        return com.david.hlp.SpringBootWork.blog.util.DraggableTableResult.<Role>builder().items(roles).total(total).build();
    }

    @Override
    public List<Permission> getPermissionsInfo() {
        return permissionMapper.selectPermissions();
    }

    public Role updateRole(Long roleId, RoleRequest roleRequest) {
        roleMapper.updataRole(roleRequest.getRoleName(),roleRequest.getStatus(),roleRequest.getDescription(),roleId);
        List<RolePermissionInfo> rolePermissionInfos = permissionMapper.selectRolePermissionInfoByRoleId(roleId);
        HashSet<Long> permissionsSet = new HashSet<>(roleRequest.getPermissionsArrId());
        HashSet<Long> noCreatePermissionsSet = new HashSet<>();
        for (RolePermissionInfo rolePermissionInfo : rolePermissionInfos) {
                rolePermissionInfo.setAction(
                        ! permissionsSet.contains(rolePermissionInfo.getPermissionId())
                        ? RolePermissionInfo.Action.DELETE
                        : RolePermissionInfo.Action.UPDATE
                );
            noCreatePermissionsSet.add(rolePermissionInfo.getPermissionId());
        }
        permissionsSet.removeAll(noCreatePermissionsSet);
        System.out.println(Arrays.toString(permissionsSet.toArray()));
        for (Long permissionId : permissionsSet) {
            rolePermissionInfos.add(RolePermissionInfo.builder()
                    .permissionId(permissionId)
                    .roleId(roleId)
                    .action(RolePermissionInfo.Action.CREATE)
                    .build()
            );
        }
        for (RolePermissionInfo rolePermissionInfo : rolePermissionInfos){
            if (rolePermissionInfo.getAction().equals(RolePermissionInfo.Action.DELETE)){
                roleMapper.deleteRolePermissions(rolePermissionInfo);
            } else if (rolePermissionInfo.getAction().equals(RolePermissionInfo.Action.CREATE)) {
                roleMapper.insertRolePermissions(rolePermissionInfo);
            }else {
                roleMapper.updateRolePermissions(rolePermissionInfo);
            }
        }

        return roleMapper.selectByRoleId(roleId);
    }
}
