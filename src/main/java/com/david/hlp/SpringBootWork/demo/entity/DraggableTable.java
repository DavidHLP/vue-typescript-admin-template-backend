package com.david.hlp.SpringBootWork.demo.entity;

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
@Table(name = "draggable_table") // 定义 JPA 表名
@TableName("draggable_table") // 定义 MyBatis Plus 表名
public class DraggableTable {

    /**
     * 主键 ID，自增策略。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 文章摘要内容。
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    @Builder.Default
    private String abstractContent = "Default abstract content";

    /**
     * 文章作者。
     */
    @Column(length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'Default Author'", nullable = false)
    @Builder.Default
    private String author = "Default Author";

    /**
     * 是否禁用评论。
     */
    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    @Builder.Default
    private Boolean disableComment = false;

    /**
     * 文章完整内容（HTML 格式）。
     */
    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String fullContent = "<!DOCTYPE html>\n<html>\n<head>\n<title>Sample Full Content</title>\n</head>\n<body>\n<h1>Welcome to the Article</h1>\n<p>This is a test article for testing purposes.</p>\n</body>\n</html>";

    /**
     * 文章封面图片 URL。
     */
    @Column(length = 2083)
    private String imageURL;

    /**
     * 文章的重要性等级。
     */
    @Column(columnDefinition = "INTEGER DEFAULT 1", nullable = false)
    @Builder.Default
    private Integer importance = 1;

    /**
     * 页面浏览量。
     */
    @Column(columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    @Builder.Default
    private Integer pageviews = 0;

    /**
     * 平台信息（JSON 格式存储）。
     */
    @Column(columnDefinition = "JSON", nullable = false)
    @Builder.Default
    private String platforms = "[]";

    /**
     * 审核人。
     */
    @Column(length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'Default Reviewer'")
    @Builder.Default
    private String reviewer = "Default Reviewer";

    /**
     * 文章来源 URL。
     */
    @Column(length = 2083)
    private String sourceURL;

    /**
     * 文章状态（枚举值）。
     */
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'draft'", nullable = false)
    @Builder.Default
    private Status status = Status.draft;

    /**
     * 时间戳，用于记录文章创建或更新的时间。
     */
    @Column(columnDefinition = "BIGINT DEFAULT 0", nullable = false)
    @Builder.Default
    private Long timestamp = System.currentTimeMillis();

    /**
     * 文章标题。
     */
    @Column(length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'Default Title'", nullable = false)
    @Builder.Default
    private String title = "Default Title";

    /**
     * 文章类型（枚举值）。
     */
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'CN'", nullable = false)
    @Builder.Default
    private Type type = Type.CN;

    /**
     * 是否为创建状态。
     */
    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    @Builder.Default
    private Boolean iscreate = true;

    // ---------------- Setter 方法 ----------------

    /**
     * 设置文章类型。
     *
     * @param type 类型的字符串表示。
     */
    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            this.type = Type.CN;
        } else {
            this.type = Type.valueOf(type);
        }
    }

    /**
     * 设置文章类型（枚举值）。
     *
     * @param type 类型的枚举值。
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * 设置文章状态。
     *
     * @param status 状态的字符串表示。
     */
    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            this.status = Status.draft;
        } else {
            this.status = Status.valueOf(status);
        }
    }

    /**
     * 设置文章状态（枚举值）。
     *
     * @param status 状态的枚举值。
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    // ---------------- 枚举定义 ----------------

    /**
     * 枚举类：文章状态。
     */
    public enum Status {
        draft("draft"), // 草稿
        published("published"), // 已发布
        deleted("deleted"); // 已删除

        private final String value;

        Status(String value) {
            this.value = value;
        }
    }

    /**
     * 枚举类：文章类型。
     */
    public enum Type {
        EU,
        CN,
        US,
        JP;
    }
}