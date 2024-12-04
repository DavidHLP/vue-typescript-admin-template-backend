package com.david.hlp.SpringBootWork.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.SpringBootWork.demo.entity.DraggableTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 数据访问层接口：DraggableTableMapper。
 *
 * 继承 MyBatis Plus 的 BaseMapper 接口，提供基础的 CRUD 操作。
 * 定义自定义 SQL 查询方法，用于复杂的分页和条件查询。
 */
@Mapper // 标记为 MyBatis 的 Mapper 接口
public interface DraggableTableMapper extends BaseMapper<DraggableTable> {

    /**
     * 获取满足筛选条件的文章列表。
     *
     * @param importance 筛选条件：文章重要性（可选）。
     * @param title      筛选条件：文章标题（可选）。
     * @param type       筛选条件：文章类型（可选）。
     * @param sortField  排序字段（如 "id", "timestamp"）。
     * @param sortOrder  排序方式（"asc" 表示升序，"desc" 表示降序）。
     * @param offset     分页偏移量（从第几条记录开始）。
     * @param limit      分页限制（每页显示的记录数）。
     * @return 符合条件的文章列表。
     */
    List<DraggableTable> getFilteredArticles(
            @Param("importance") Integer importance,
            @Param("title") String title,
            @Param("type") String type,
            @Param("sortField") String sortField,
            @Param("sortOrder") String sortOrder,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * 获取满足筛选条件的文章总数。
     *
     * @param importance 筛选条件：文章重要性（可选）。
     * @param title      筛选条件：文章标题（可选）。
     * @param type       筛选条件：文章类型（可选）。
     * @return 符合条件的文章总数。
     */
    int countFilteredArticles(
            @Param("importance") Integer importance,
            @Param("title") String title,
            @Param("type") String type);


    /**
     * 获取满足筛选条件的文章列表。
     *
     * @param importance 筛选条件：文章重要性（可选）。
     * @param title      筛选条件：文章标题（可选）。
     * @param type       筛选条件：文章类型（可选）。
     * @param sortField  排序字段（如 "id", "timestamp"）。
     * @param offset     分页偏移量（从第几条记录开始）。
     * @param limit      分页限制（每页显示的记录数）。
     * @return 符合条件的文章列表。
     */
    List<DraggableTable> getFilteredArticlesDefault(
            @Param("importance") Integer importance,
            @Param("title") String title,
            @Param("type") String type,
            @Param("sortField") String sortField,
            @Param("offset") int offset,
            @Param("limit") int limit);
}