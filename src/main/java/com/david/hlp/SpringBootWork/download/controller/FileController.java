package com.david.hlp.SpringBootWork.download.controller;

import com.david.hlp.SpringBootWork.download.entity.FileResult;
import com.david.hlp.SpringBootWork.download.service.FileService;
import com.david.hlp.SpringBootWork.system.auth.BaseController;
import com.david.hlp.SpringBootWork.system.entity.Result;
import com.david.hlp.SpringBootWork.system.service.imp.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController extends BaseController {

    private final FileService fileService;

    private final UserServiceImp userServiceImp;

    // 文件上传接口
    @PostMapping("/upload")
    public Result<FileResult> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileService.uploadFile(file);
        userServiceImp.updateAvatarByUsername(getCurrentUsername(), fileUrl);
        return Result.ok(FileResult.builder().filePath(fileUrl).build());
    }

    // 文件下载接口
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName) {
        try (InputStream inputStream = fileService.downloadFile(fileName)) {
            byte[] content = inputStream.readAllBytes();

            // 设置下载响应头
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .body(content);
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }
}

