package com.david.hlp.SpringBootWork.blog.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用结果类：DraggableTableResult。
 *
 * 用于封装分页查询的结果，包括文章列表和总记录数。
 */
@Data // 自动生成 getter、setter、equals、hashCode 和 toString 方法
@Builder // 提供 Builder 模式，便于构建对象
@AllArgsConstructor // 生成包含所有字段的全参构造函数
@NoArgsConstructor // 生成无参构造函数
public class DraggableTableResult<T>{

    /**
     * 当前页的文章列表。
     */
    private List<T> items;

    /**
     * 满足条件的总记录数。
     */
    private int total;
}