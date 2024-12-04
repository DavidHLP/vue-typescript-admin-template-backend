package com.david.hlp.SpringBootWork.download.service.imp;

import com.david.hlp.SpringBootWork.download.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileServiceImpl implements FileService {

    // 文件存储路径（根据需要修改为实际路径）
    @Value("${file.upload-dir}")
    private String uploadPath;

    @Value("${file.http}")
    private String http;

    @Value("${file.url-address}")
    private String urlAddress;

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        String fileName = file.getOriginalFilename(); // 获取原始文件名
        File destFile = new File(uploadPath + fileName);

        // 创建目录（如果不存在）
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        try {
            // 保存文件
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }

        return "http://"+http+urlAddress+fileName; // 返回文件名
    }

    @Override
    public InputStream downloadFile(String fileName) {
        File file = new File(uploadPath + fileName);

        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在");
        }

        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件读取失败", e);
        }
    }
}

