package com.david.hlp.SpringBootWork.demo.controller;

import com.david.hlp.SpringBootWork.demo.entity.ArticleWrapper;
import com.david.hlp.SpringBootWork.demo.service.DraggableTableService;
import com.david.hlp.SpringBootWork.demo.util.DraggableTableResult;
import com.david.hlp.SpringBootWork.system.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class DraggableTableController {

    private final DraggableTableService draggableTableService;

    @GetMapping
    public Result<DraggableTableResult> getAllArticles(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "importance", required = false) Integer importance,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sort", defaultValue = "+id") String sort
    ) {
        return  Result.ok(draggableTableService.getPageviews(page, limit, importance, title, type, sort));
    }

    @GetMapping("/{id}")
    public Result<ArticleWrapper> getArticleById(@PathVariable Long id) {
        return draggableTableService.getArticleById(id)
                .map(article -> ArticleWrapper.builder().article(article).build())
                .map(Result::ok)
                .orElseGet(() -> Result.notFound("Article with ID " + id + " not found"));
    }

    @PostMapping
    public Result<ArticleWrapper> createArticle(@RequestBody ArticleWrapper articleWrapper) {
        articleWrapper.getArticle().setId(null);
        return Result.ok(ArticleWrapper.builder().article(draggableTableService.createArticle(articleWrapper.getArticle())).build());
    }

    @PutMapping("/{id}")
    public Result<ArticleWrapper> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleWrapper articleWrapper) {
        try {
            return Result.ok(ArticleWrapper.builder().article(draggableTableService.updateArticle(id, articleWrapper.getArticle())).build());
        } catch (RuntimeException e) {
            return Result.notFound();
        }
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteArticle(@PathVariable Long id) {
        draggableTableService.deleteArticle(id);
        return Result.ok("");
    }
}
