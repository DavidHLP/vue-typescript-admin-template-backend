package com.david.hlp.SpringBootWork.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体类 DraggableTable。
 *
 * 表示可拖动表格的文章实体，包含文章的各种属性和默认值设置。
 */
@Data // 自动生成 getter、setter、equals、hashCode 和 toString 方法
@Entity // 标记为 JPA 实体类
@Builder // 提供 Builder 模式，便于对象构建
@NoArgsConstructor // 生成无参构造函数
@AllArgsConstructor // 生成全参构造函数
@Table(name = "article") // 定义 JPA 表名
@TableName("article") // 定义 MyBatis Plus 表名
public class ArticleTable {

    /**
     * 主键 ID，自增策略。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章摘要内容。
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String abstractContent ;

    /**
     * 文章作者。
     */
    @Column(length = 255, nullable = false)
    private String author;

    /**
     * 是否禁用评论。
     */
    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    private Boolean disableComment;

    @Column(columnDefinition = "TEXT")
    private String MarkdownContent;

    @Column(nullable = false)
    private Long version = 0L;

    /**
     * 文章封面图片 URL。
     */
    @Column(length = 2083)
    private String imageURL;

    /**
     * 页面浏览量。
     */
    @Column(columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    @Builder.Default
    private Integer pageviews = 0;

    /**
     * 文章标题。
     */
    @Column(length = 255, columnDefinition = "VARCHAR(255)", nullable = false)
    private String title;

    /**
     * 时间戳，用于记录文章创建或更新的时间。
     */
    @Column(columnDefinition = "BIGINT DEFAULT 0", nullable = false)
    @Builder.Default
    private Long timestamp = System.currentTimeMillis();

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    private Boolean status ;

    @Column(length = 255, columnDefinition = "VARCHAR(255) ", nullable = false ,unique = true)
    private String uniqueIdentifier;
}