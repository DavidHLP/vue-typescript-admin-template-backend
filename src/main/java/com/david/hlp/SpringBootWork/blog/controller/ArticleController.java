package com.david.hlp.SpringBootWork.blog.controller;

import com.david.hlp.SpringBootWork.blog.entity.ArticleTable;
import com.david.hlp.SpringBootWork.blog.entity.ArticleWrapper;
import com.david.hlp.SpringBootWork.blog.service.ArticleService;
import com.david.hlp.SpringBootWork.system.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/getArticle")
    public Result<ArticleWrapper<ArticleTable>> getWrapperArticle(
            @RequestParam("id") Long id
    ){
        return Result.ok(
                ArticleWrapper.<ArticleTable>builder()
                        .article(articleService.getArticle(id))
                        .build()
        );
    }
    @PutMapping("/updateArticle")
    public Result<ArticleTable> updateArticle(
            @RequestBody ArticleWrapper<ArticleTable> article,
            @RequestParam("id") Long id
    ){
        ArticleTable articleTable = articleService.updateArticle(id, article.getArticle());
        return Result.ok(articleTable);
    }

    @GetMapping("/getArticleByStatusAndKeyWord")
    public Result<ArticleWrapper<List<ArticleTable>>> getWrapperArticleByStatusAndKeyWord(
            @RequestParam(value = "status" , required = false) Boolean status,
            @RequestParam(value = "keyWord" , required = false) String keyWord
    ){
        return Result.ok(
                ArticleWrapper.<List<ArticleTable>>builder()
                        .article(articleService.getWrapperArticleByStatusAndKeyWord(status,keyWord))
                        .build()
        );
    }

    @DeleteMapping("/deleteArticleById")
    public Result<Void> deleteArticleById(
            @RequestParam("id") Long id
    ){
        return Result.ok(articleService.deleteArticleById(id));
    }
}
