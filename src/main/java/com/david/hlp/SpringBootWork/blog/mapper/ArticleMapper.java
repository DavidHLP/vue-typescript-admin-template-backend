package com.david.hlp.SpringBootWork.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.SpringBootWork.blog.entity.ArticleTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 数据访问层接口：DraggableTableMapper。
 *
 * 继承 MyBatis Plus 的 BaseMapper 接口，提供基础的 CRUD 操作。
 * 定义自定义 SQL 查询方法，用于复杂的分页和条件查询。
 */
@Mapper // 标记为 MyBatis 的 Mapper 接口
public interface ArticleMapper extends BaseMapper<ArticleTable> {

    void insertArticle(@Param("article") ArticleTable draggableTable);

    void updateArticle(@Param("id")Long id, @Param("version")Long version,@Param("article")ArticleTable article);

    @Select("SELECT * FROM article WHERE unique_identifier = #{uniqueIdentifier}")
    ArticleTable selectByUniqueIdentifier(@Param("uniqueIdentifier") String uniqueIdentifier);

    @Select("SELECT * FROM article WHERE id = #{id}")
    ArticleTable selectById(@Param("id") Long id);

    List<ArticleTable> selectWrapperArticleByStatusAndKeyWord(@Param("status")Boolean status , @Param("keyWord")String keyWord);
}