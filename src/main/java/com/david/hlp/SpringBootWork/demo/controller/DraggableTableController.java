package com.david.hlp.SpringBootWork.demo.controller;

import com.david.hlp.SpringBootWork.demo.entity.ArticleWrapper;
import com.david.hlp.SpringBootWork.demo.entity.DraggableTable;
import com.david.hlp.SpringBootWork.demo.service.DraggableTableService;
import com.david.hlp.SpringBootWork.demo.util.DraggableTableResult;
import com.david.hlp.SpringBootWork.system.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 文章管理控制器。
 *
 * 提供 RESTful API，用于对文章进行分页查询、查看详情、创建、更新和删除操作。
 */
@RestController
@RequestMapping("/api/v1/articles") // 设置请求路径前缀
@RequiredArgsConstructor // 自动生成构造函数并注入所需依赖
public class DraggableTableController {

    private final DraggableTableService draggableTableService; // 注入文章服务层

    /**
     * 获取分页文章列表。
     *
     * 支持按重要性、标题和类型筛选，支持指定排序规则。
     *
     * @param page       当前页码，默认为 1。
     * @param limit      每页记录数，默认为 20。
     * @param importance 筛选条件：文章重要性（可选）。
     * @param title      筛选条件：文章标题（可选）。
     * @param type       筛选条件：文章类型（可选）。
     * @param sort       排序规则，默认为按 ID 升序（+id 表示升序，-id 表示降序）。
     * @return 包含分页结果的通用响应对象。
     */
    @GetMapping
    public Result<DraggableTableResult<DraggableTable>> getAllArticles(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "importance", required = false) Integer importance,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sort", defaultValue = "id") String sort
    ) {
        // 调用服务层方法获取分页结果，并返回响应对象
        return Result.ok(draggableTableService.getPageviews(page, limit, importance, title, type, sort));
    }

    /**
     * 根据 ID 获取文章详情。
     *
     * @param id 文章的唯一标识。
     * @return 包含文章详情的通用响应对象，如果未找到则返回 404。
     */
    @GetMapping("/{id}")
    public Result<ArticleWrapper<DraggableTable>> getArticleById(@PathVariable Long id) {
        // 调用服务层方法获取文章详情
        return draggableTableService.getArticleById(id)
                .map(article -> ArticleWrapper.<DraggableTable>builder().article(article).build()) // 封装为 ArticleWrapper
                .map(Result::ok) // 包装为通用响应对象
                .orElseGet(() -> Result.notFound("Article with ID " + id + " not found")); // 未找到返回 404 响应
    }

    /**
     * 创建文章。
     *
     * @param articleWrapper 包含文章实体的包装对象。
     * @return 包含创建成功的文章详情的通用响应对象。
     */
    @PostMapping
    public Result<ArticleWrapper<DraggableTable>> createArticle(@RequestBody ArticleWrapper<DraggableTable> articleWrapper) {
        // 确保新创建的文章没有 ID（由数据库自动生成）
        articleWrapper.getArticle().setId(null);
        // 调用服务层方法创建文章，并返回响应对象
        return Result.ok(ArticleWrapper.<DraggableTable>builder()
                .article(draggableTableService.createArticle(articleWrapper.getArticle()))
                .build());
    }

    /**
     * 更新文章。
     *
     * 根据 ID 更新文章的内容，如果文章不存在则返回 404。
     *
     * @param id             需要更新的文章 ID。
     * @param articleWrapper 包含更新内容的文章包装对象。
     * @return 包含更新后的文章详情的通用响应对象。
     */
    @PutMapping("/{id}")
    public Result<ArticleWrapper<DraggableTable>> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleWrapper<DraggableTable> articleWrapper) {
        try {
            // 调用服务层方法更新文章，并返回响应对象
            return Result.ok(ArticleWrapper.<DraggableTable>builder()
                    .article(draggableTableService.updateArticle(id, articleWrapper.getArticle()))
                    .build());
        } catch (RuntimeException e) {
            // 捕获运行时异常，返回 404 响应
            return Result.notFound();
        }
    }

    /**
     * 删除文章。
     *
     * 根据 ID 删除指定文章。
     *
     * @param id 需要删除的文章 ID。
     * @return 通用响应对象，表示操作成功。
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteArticle(@PathVariable Long id) {
        // 调用服务层方法删除文章
        draggableTableService.deleteArticle(id);
        // 返回操作成功的响应
        return Result.ok("");
    }
}