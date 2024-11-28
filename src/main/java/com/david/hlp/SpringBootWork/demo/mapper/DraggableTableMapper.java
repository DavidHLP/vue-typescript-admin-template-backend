package com.david.hlp.SpringBootWork.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.SpringBootWork.demo.entity.DraggableTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface DraggableTableMapper extends BaseMapper<DraggableTable> {

    List<DraggableTable> getFilteredArticles(
            @Param("importance") Integer importance,
            @Param("title") String title,
            @Param("type") String type,
            @Param("sortField") String sortField,
            @Param("sortOrder") String sortOrder,
            @Param("offset") int offset,
            @Param("limit") int limit);

    int countFilteredArticles(
            @Param("importance") Integer importance,
            @Param("title") String title,
            @Param("type") String type);
}
