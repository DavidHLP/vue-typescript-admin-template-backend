package com.david.hlp.SpringBootWork.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "draggable_table")
@TableName("draggable_table")
public class DraggableTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Builder.Default
    private String abstractContent = "Default abstract content";

    @Column(length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'Default Author'", nullable = false)
    @Builder.Default
    private String author = "Default Author";

    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    @Builder.Default
    private Boolean disableComment = false;

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String fullContent = "<!DOCTYPE html>\n<html>\n<head>\n<title>Sample Full Content</title>\n</head>\n<body>\n<h1>Welcome to the Article</h1>\n<p>This is a test article for testing purposes.</p>\n</body>\n</html>";

    @Column(length = 2083)
    private String imageURL;

    @Column(columnDefinition = "INTEGER DEFAULT 1", nullable = false)
    @Builder.Default
    private Integer importance = 1;

    @Column(columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    @Builder.Default
    private Integer pageviews = 0;

    @Column(columnDefinition = "JSON", nullable = false)
    @Builder.Default
    private String platforms = "[]";

    @Column(length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'Default Reviewer'")
    @Builder.Default
    private String reviewer = "Default Reviewer";

    @Column(length = 2083)
    private String sourceURL;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'draft'", nullable = false)
    @Builder.Default
    private Status status = Status.draft;

    @Column(columnDefinition = "BIGINT DEFAULT 0", nullable = false)
    @Builder.Default
    private Long timestamp = System.currentTimeMillis();

    @Column(length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'Default Title'", nullable = false)
    @Builder.Default
    private String title = "Default Title";

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'CN'", nullable = false)
    @Builder.Default
    private Type type = Type.CN;

    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    @Builder.Default
    private Boolean iscreate = true;

    // Setters with validation
    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            this.type = Type.CN;
        } else {
            this.type = Type.valueOf(type);
        }
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            this.status = Status.draft;
        } else {
            this.status = Status.valueOf(status);
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Enums for Status and Type
    public enum Status {
        draft("draft"),
        published("published"),
        deleted("deleted");

        private final String value;

        Status(String value) {
            this.value = value;
        }
    }

    public enum Type {
        EU,
        CN,
        US,
        JP;
    }
}
