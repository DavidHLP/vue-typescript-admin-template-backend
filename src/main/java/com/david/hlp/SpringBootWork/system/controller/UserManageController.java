package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.demo.entity.ArticleWrapper;
import com.david.hlp.SpringBootWork.demo.util.DraggableTableResult;
import com.david.hlp.SpringBootWork.system.auth.BaseController;
import com.david.hlp.SpringBootWork.system.entity.Result;
import com.david.hlp.SpringBootWork.system.entity.UserInfo;
import com.david.hlp.SpringBootWork.system.responsentity.UserInfoRequest;
import com.david.hlp.SpringBootWork.system.service.imp.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/manage")
public class UserManageController extends BaseController {

    private final UserServiceImp userServiceImp;

    /**
     * 获取用户信息（支持分页、排序和筛选）。
     *
     * @param page  当前页码，默认为1。
     * @param limit 每页记录数，默认为20。
     * @param sort  排序字段，默认为id。
     * @param name  可选筛选条件 - 用户名。
     * @param email 可选筛选条件 - 邮箱。
     * @return 包含分页结果的统一返回对象。
     */
    @GetMapping("/getUserInfo")
    public Result<DraggableTableResult<UserInfo>> getUserPageviews(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "sort", defaultValue = "+id") String sort,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "role",required = false) String role,
            @RequestParam(value = "status", required = false) Boolean status
    ) {
        return Result.ok(
                userServiceImp.getFilteredUsers(
                        page, limit, sort, name, email,role,status
                )
        );
    }

    @DeleteMapping("/disable")
    public Result<Void> disableUser( @RequestParam("id") Long id ) {
        userServiceImp.disableUser(id);
        return Result.ok(null);
    }

    @PutMapping("/enable")
    public Result<Void> enableUser( @RequestParam("id") Long id ) {
        userServiceImp.enableUser(id);
        return Result.ok(null);
    }

    @PutMapping("updateUser")
    public Result<ArticleWrapper<UserInfo>> updateUser(
            @RequestParam("id") Long id,
            @RequestBody ArticleWrapper<UserInfoRequest> articleWrapper
    ) {
        return Result.ok(userServiceImp.updateUserInfo(id,articleWrapper.getArticle()));
    }
}