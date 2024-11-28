package com.david.hlp.SpringBootWork.demo.service;
import com.david.hlp.SpringBootWork.demo.Repository.DraggableTableRepository;
import com.david.hlp.SpringBootWork.demo.entity.DraggableTable;
import com.david.hlp.SpringBootWork.demo.mapper.DraggableTableMapper;
import com.david.hlp.SpringBootWork.demo.util.DraggableTableResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DraggableTableService {

    private final DraggableTableRepository draggableTableRepository;

    private final DraggableTableMapper draggableTableMapper;

    private final ObjectMapper objectMapper;

    public void loadDataFromJson(String filePath) {
        try {
            List<DraggableTable> draggableTables = objectMapper.readValue(
                    new File(filePath),
                    new TypeReference<List<DraggableTable>>() {}
            );

            // 检查并设置默认值
            draggableTables.forEach(table -> {
                if (table.getAbstractContent() == null) {
                    table.setAbstractContent("Default Abstract Content");
                }
            });

            draggableTables.forEach(table -> {
                table.setId(null);
            });

            draggableTableRepository.saveAll(draggableTables);
            System.out.println("Data loaded and saved to the database successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from JSON file: " + e.getMessage(), e);
        }
    }

    public DraggableTableResult getAllArticles() {
        List<DraggableTable> res = draggableTableRepository.findAll();
        return DraggableTableResult.builder().items(res).total(res.size()).build();
    }

    public Optional<DraggableTable> getArticleById(Long id) {
        return draggableTableRepository.findById(id);
    }

    public DraggableTable createArticle(DraggableTable article) {
        return draggableTableRepository.save(article);
    }

    public DraggableTable updateArticle(Long id, DraggableTable articleDetails) {
        return draggableTableRepository.findById(id)
                .map(existingArticle -> {
                    existingArticle.setAbstractContent(articleDetails.getAbstractContent());
                    existingArticle.setAuthor(articleDetails.getAuthor());
                    existingArticle.setDisableComment(articleDetails.getDisableComment());
                    existingArticle.setFullContent(articleDetails.getFullContent());
                    existingArticle.setImageURL(articleDetails.getImageURL());
                    existingArticle.setImportance(articleDetails.getImportance());
                    existingArticle.setPageviews(articleDetails.getPageviews());
                    existingArticle.setPlatforms(articleDetails.getPlatforms());
                    existingArticle.setReviewer(articleDetails.getReviewer());
                    existingArticle.setSourceURL(articleDetails.getSourceURL());
                    existingArticle.setStatus(articleDetails.getStatus());
                    existingArticle.setTimestamp(articleDetails.getTimestamp());
                    existingArticle.setTitle(articleDetails.getTitle());
                    existingArticle.setType(articleDetails.getType());
                    return draggableTableRepository.save(existingArticle);
                })
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id));
    }

    public void deleteArticle(Long id) {
        draggableTableRepository.deleteById(id);
    }

    public DraggableTableResult getPageviews(
            Integer page,
            Integer limit,
            Integer importance,
            String title,
            String type,
            String sort) {

        // 解析排序字段和顺序
        String sortField = sort.substring(1); // 去掉前缀 "+" 或 "-"
        String sortOrder = sort.startsWith("+") ? "ASC" : "DESC";

        // 计算分页偏移量
        int offset = (page - 1) * limit;

        // 查询文章列表
        List<DraggableTable> articles = draggableTableMapper.getFilteredArticles(
                importance, title, type, sortField, sortOrder, offset, limit);

        // 查询文章总数
        int total = draggableTableMapper.countFilteredArticles(importance, title, type);

        // 构建结果
        return DraggableTableResult.builder()
                .items(articles)
                .total(total)
                .build();
    }

}

