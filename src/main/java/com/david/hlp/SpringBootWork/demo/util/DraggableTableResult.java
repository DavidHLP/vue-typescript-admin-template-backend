package com.david.hlp.SpringBootWork.demo.util;

import com.david.hlp.SpringBootWork.demo.entity.DraggableTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DraggableTableResult {
    private List<DraggableTable> items;
    private int total;
}
