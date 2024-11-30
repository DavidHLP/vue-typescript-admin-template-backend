package com.david.hlp.SpringBootWork.system.responsentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePage<T>{
    private List<T> items;
    private int total;
    private int page;
    private int size;
}
