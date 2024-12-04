package com.david.hlp.SpringBootWork.demo.controller;

import com.david.hlp.SpringBootWork.demo.service.DraggableTableService;
import com.david.hlp.SpringBootWork.demo.util.DraggableTableResult;
import com.david.hlp.SpringBootWork.system.auth.BaseController;
import com.david.hlp.SpringBootWork.system.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 页面浏览量控制器。
 *
 * 提供 RESTful API，用于分页查询页面浏览量数据，支持多种筛选和排序条件。
 */
@RestController
@RequestMapping("/api/v1/pageviews") // 设置请求路径前缀
@RequiredArgsConstructor // 自动生成构造函数并注入所需依赖
public class PageviewsController extends BaseController {

    private final DraggableTableService draggableTableService; // 注入页面浏览量服务层

    /**
     * 获取分页的页面浏览量数据。
     *
     * 支持按重要性、标题和类型筛选，支持指定排序规则。
     *
     * @param page       当前页码，默认为 1。
     * @param limit      每页记录数，默认为 20。
     * @param importance 筛选条件：记录的重要性（可选）。
     * @param title      筛选条件：记录的标题（可选）。
     * @param type       筛选条件：记录的类型（可选）。
     * @param sort       排序规则，默认为按 ID 升序（+id 表示升序，-id 表示降序）。
     * @return 包含分页结果的通用响应对象。
     */
    @GetMapping
    public Result<DraggableTableResult> getPageviews(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "importance", required = false) Integer importance,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sort", defaultValue = "+id") String sort) {

        // 调用服务层方法获取分页结果，并返回响应对象
        return Result.ok(draggableTableService.getPageviews(page, limit, importance, title, type, sort));
    }
}