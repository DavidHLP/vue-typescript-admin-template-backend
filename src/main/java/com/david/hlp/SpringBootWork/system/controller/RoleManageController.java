package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.demo.entity.ArticleWrapper;
import com.david.hlp.SpringBootWork.demo.util.DraggableTableResult;
import com.david.hlp.SpringBootWork.system.auth.BaseController;
import com.david.hlp.SpringBootWork.system.entity.Permission;
import com.david.hlp.SpringBootWork.system.entity.Result;
import com.david.hlp.SpringBootWork.system.entity.Role;
import com.david.hlp.SpringBootWork.system.entity.UserInfo;
import com.david.hlp.SpringBootWork.system.requestentity.RoleRequest;
import com.david.hlp.SpringBootWork.system.responsentity.UserInfoRequest;
import com.david.hlp.SpringBootWork.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role/manage")
public class RoleManageController extends BaseController {
    private final RoleService roleServiceImp;
    @GetMapping("/getRoleInfo")
    public Result<List<Role>> getAllRoles() {
        return Result.ok(roleServiceImp.getAllRoles());
    }

    @DeleteMapping("/disable")
    public Result<Void> disableUser( @RequestParam("id") Long id ) {
        roleServiceImp.disableUser(id);
        return Result.ok(null);
    }

    @PutMapping("/enable")
    public Result<Void> enableUser( @RequestParam("id") Long id ) {
        roleServiceImp.enableUser(id);
        return Result.ok(null);
    }

    @GetMapping("/getRolesAndPermissionsInfo")
    public Result<DraggableTableResult<Role>> getAllRolesAndPermissions(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "sort", defaultValue = "+id") String sort,
            @RequestParam(value = "roleName",required = false) String roleName,
            @RequestParam(value = "status", required = false) Boolean status,
            @RequestParam(value = "permission", required = false) String permission
    ) {
        return Result.ok(roleServiceImp.getRoleAndPermission(page, limit, sort, roleName, status , permission));
    }

    @GetMapping("/getPermissionsInfo")
    public Result<List<Permission>> getAllPermissions() {
        return Result.ok(roleServiceImp.getPermissionsInfo());
    }

    @PutMapping("/updateRole")
    public Result<ArticleWrapper<Role>> updateRole(
            @RequestParam("id") Long id,
            @RequestBody ArticleWrapper<RoleRequest> articleWrapper
    ) {
       return Result.ok(ArticleWrapper.<Role>builder()
                        .article(roleServiceImp.updateRole(id, articleWrapper.getArticle()))
                .build());
    }
}
