package com.david.hlp.SpringBootWork.blog.controller;

import com.david.hlp.SpringBootWork.blog.entity.ArticleWrapper;
import com.david.hlp.SpringBootWork.blog.entity.ArticleTable;
import com.david.hlp.SpringBootWork.blog.service.ArticleService;
import com.david.hlp.SpringBootWork.system.auth.BaseController;
import com.david.hlp.SpringBootWork.system.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeArticleController extends BaseController {

    private final ArticleService articleService;

    @GetMapping("/getArticle")
    public Result<ArticleWrapper<ArticleTable>> getWrapperArticle(){
        return null;
    }
}
