package com.david.hlp.SpringBootWork.download.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {
    // 文件上传
    String uploadFile(MultipartFile file);

    // 文件下载
    InputStream downloadFile(String fileName);
}
