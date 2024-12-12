package com.david.hlp.SpringBootWork.blog.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章包装类。
 *
 * 用于封装文章实体，提供更灵活的数据传输结构。
 * 适配 JSON 数据的序列化和反序列化。
 */
@Data // 自动生成 getter、setter、equals、hashCode 和 toString 方法
@Builder // 提供 Builder 模式，便于对象构造
@NoArgsConstructor // 生成无参构造函数
@AllArgsConstructor // 生成全参构造函数
public class ArticleWrapper <T> {

    /**
     * 包装的文章对象。
     *
     * 在 JSON 序列化和反序列化时，映射到 "article" 字段。
     */
    @JsonProperty("article")
    private T article;
}