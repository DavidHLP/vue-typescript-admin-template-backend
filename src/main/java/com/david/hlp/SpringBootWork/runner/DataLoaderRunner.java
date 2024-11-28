package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.demo.service.DraggableTableService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoaderRunner {

    private final DraggableTableService dataLoaderService;

    @PostConstruct
    public void init() {
        String filePath = "src/main/resources/draggable_table_data_detailed.html.json"; // JSON 文件路径
        dataLoaderService.loadDataFromJson(filePath);
    }
}


