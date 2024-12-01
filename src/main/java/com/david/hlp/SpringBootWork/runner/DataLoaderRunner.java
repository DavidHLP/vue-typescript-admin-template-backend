package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.demo.service.DraggableTableService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 数据加载运行器。
 *
 * 该类在应用启动时自动运行，用于从 JSON 文件加载数据并初始化服务所需的资源。
 */
@Component // 将该类标记为 Spring 组件，使其在容器启动时被扫描和管理
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
public class DataLoaderRunner {

    // 注入 DraggableTableService，用于执行数据加载逻辑
    private final DraggableTableService dataLoaderService;

    /**
     * 初始化方法。
     *
     * 使用 @PostConstruct 注解，确保该方法在依赖注入完成后立即运行。
     * 该方法从指定的 JSON 文件加载数据。
     */
    @PostConstruct
    public void init() {
        // 定义 JSON 文件路径
        String filePath = "src/main/resources/draggable_table_data_detailed.html.json";

        // 调用服务层的方法加载数据
        dataLoaderService.loadDataFromJson(filePath);
    }
}