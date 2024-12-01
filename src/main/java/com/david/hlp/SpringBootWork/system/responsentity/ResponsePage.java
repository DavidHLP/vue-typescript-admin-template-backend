package com.david.hlp.SpringBootWork.system.responsentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用分页响应类。
 * <p>
 * 用于封装分页查询的响应数据，适用于返回包含列表数据和分页信息的场景。
 * <p>
 * 支持泛型，以适配不同类型的分页内容。
 * <p>
 * 使用了 Lombok 注解简化代码：
 * <p>
 * - @Data 自动生成 Getter、Setter、toString、equals 和 hashCode 方法。
 * <p>
 * - @Builder 提供构建器模式，用于更灵活地创建对象实例。
 * <p>
 * - @AllArgsConstructor 自动生成包含所有字段的构造函数。
 * <p>
 * - @NoArgsConstructor 自动生成无参构造函数。
 * <p>
 *
 * @param <T> 表示分页中每一项数据的类型。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePage<T> {

    /**
     * 当前页的数据列表。
     * <p>
     * 描述：
     * <p>
     * - 包含当前页的数据项，类型由泛型 T 指定。
     */
    private List<T> items;

    /**
     * 数据总数。
     * <p>
     * 描述：
     * <p>
     * - 表示满足查询条件的所有数据的总条数。
     * <p>
     * - 用于前端分页组件计算总页数。
     */
    private int total;

    /**
     * 当前页码。
     * <p>
     * 描述：
     * <p>
     * - 表示当前返回数据的页码，通常从 1 开始。
     */
    private int page;

    /**
     * 每页大小。
     * <p>
     * 描述：
     * <p>
     * - 表示每页包含的数据条数。
     * <p>
     * - 用于与 `total` 和 `page` 一起计算分页信息。
     */
    private int size;
}