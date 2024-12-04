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

/**
 * 服务层：DraggableTableService。
 *
 * 提供文章表的业务逻辑处理，包括增删改查和复杂查询。
 */
@Service // 标记为 Spring 服务组件
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
public class DraggableTableService {

    private final DraggableTableRepository draggableTableRepository; // JPA 仓库
    private final DraggableTableMapper draggableTableMapper; // MyBatis Mapper
    private final ObjectMapper objectMapper; // 用于 JSON 解析的工具

    /**
     * 从 JSON 文件加载数据到数据库。
     *
     * @param filePath JSON 文件路径。
     */
    public void loadDataFromJson(String filePath) {
        try {
            // 从 JSON 文件读取数据
            List<DraggableTable> draggableTables = objectMapper.readValue(
                    new File(filePath),
                    new TypeReference<List<DraggableTable>>() {}
            );

            // 设置默认值并调整数据
            draggableTables.forEach(table -> {
                if (table.getAbstractContent() == null) {
                    table.setAbstractContent("Default Abstract Content"); // 设置默认摘要
                }
                table.setId(null); // 确保新数据没有主键 ID
                table.setIscreate(false); // 标记数据为非创建状态
            });

            // 批量保存到数据库
            draggableTableRepository.saveAll(draggableTables);
        } catch (IOException e) {
            // 捕获 IO 异常并抛出运行时异常
            throw new RuntimeException("Failed to load data from JSON file: " + e.getMessage(), e);
        }
    }

    /**
     * 获取所有文章。
     *
     * @return 包含文章列表和总数的结果对象。
     */
    public DraggableTableResult<DraggableTable> getAllArticles() {
        List<DraggableTable> res = draggableTableRepository.findAll(); // 查询所有文章
        return DraggableTableResult.<DraggableTable>builder().items(res).total(res.size()).build(); // 构建结果对象
    }

    /**
     * 根据 ID 获取文章。
     *
     * @param id 文章的唯一标识。
     * @return 包装为 Optional 的文章对象。
     */
    public Optional<DraggableTable> getArticleById(Long id) {
        return draggableTableRepository.findById(id); // 根据 ID 查询文章
    }

    /**
     * 创建新文章。
     *
     * @param article 文章对象。
     * @return 保存后的文章对象。
     */
    public DraggableTable createArticle(DraggableTable article) {
        return draggableTableRepository.save(article); // 保存新文章
    }

    /**
     * 更新文章。
     *
     * @param id             文章的唯一标识。
     * @param articleDetails 更新的文章内容。
     * @return 更新后的文章对象。
     */
    public DraggableTable updateArticle(Long id, DraggableTable articleDetails) {
        return draggableTableRepository.findById(id)
                .map(existingArticle -> {
                    // 更新文章字段
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
                    return draggableTableRepository.save(existingArticle); // 保存更新后的文章
                })
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id)); // 如果文章不存在抛出异常
    }

    /**
     * 删除文章。
     *
     * @param id 文章的唯一标识。
     */
    public void deleteArticle(Long id) {
        draggableTableRepository.deleteById(id); // 根据 ID 删除文章
    }

    /**
     * 分页获取文章数据。
     *
     * @param page       当前页码。
     * @param limit      每页记录数。
     * @param importance 筛选条件：重要性。
     * @param title      筛选条件：标题。
     * @param type       筛选条件：类型。
     * @param sort       排序规则（如 "+id" 或 "-id"）。
     * @return 包含分页结果的结果对象。
     */
    public DraggableTableResult<DraggableTable> getPageviews(
            Integer page,
            Integer limit,
            Integer importance,
            String title,
            String type,
            String sort) {

        // 计算分页偏移量
        int offset = (page - 1) * limit;

        List<DraggableTable> articles = draggableTableMapper.getFilteredArticlesDefault(
                importance, title, type, sort, offset, limit);

        // 查询文章总数
        int total = draggableTableMapper.countFilteredArticles(importance, title, type);

        // 构建结果对象
        return DraggableTableResult.<DraggableTable>builder()
                .items(articles)
                .total(total)
                .build();
    }
}