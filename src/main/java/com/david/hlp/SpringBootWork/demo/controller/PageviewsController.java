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

@RestController
@RequestMapping("/api/v1/pageviews")
@RequiredArgsConstructor
public class PageviewsController extends BaseController {

    private final DraggableTableService draggableTableService;

    @GetMapping
    public Result<DraggableTableResult> getPageviews(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "importance", required = false) Integer importance,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sort", defaultValue = "+id") String sort) {

        System.out.println("page:" + page+"limit:" + limit+"importance:" + importance+"title:" + title+"type:" + type);

        return Result.ok(draggableTableService.getPageviews(page, limit, importance, title, type, sort));
    }

}
