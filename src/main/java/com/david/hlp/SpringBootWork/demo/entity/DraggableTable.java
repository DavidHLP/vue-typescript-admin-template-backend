package com.david.hlp.SpringBootWork.demo.entity;

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
public class DraggableTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String abstractContent;

    @Column(length = 255, nullable = false)
    private String author;

    @Column(nullable = false)
    private Boolean disableComment = false;

    /**
     * 完整的HTML代码
     */
    @Column(columnDefinition = "TEXT")
    private String fullContent;

    @Column(length = 2083)
    private String imageURL;

    @Column(nullable = false)
    private Integer importance;

    @Column(nullable = false)
    private Integer pageviews = 0;

    @Column(columnDefinition = "JSON", nullable = false)
    private String platforms; // Store JSON as String; conversion handled separately

    @Column(length = 255)
    private String reviewer;

    @Column(length = 2083)
    private String sourceURL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Long timestamp;

    @Column(length = 255, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    // Enums for status and type
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
        JP
    }
}