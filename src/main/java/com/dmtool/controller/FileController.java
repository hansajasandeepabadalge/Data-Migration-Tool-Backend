
package com.dmtool.controller;

import com.dmtool.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:4200")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/uploadFiles")
    public ResponseEntity<Map<String, String>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        Map<String, String> response = new HashMap<>();

        if (files == null || files.length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "No files uploaded."));
        }

        try {
            int successCount = 0;
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    fileService.uploadToInputFile(file);
                    successCount++;
                }
            }

            response.put("message", String.format("Successfully uploaded %d files", successCount));
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "File upload or conversion failed: " + e.getMessage()));
        }
    }
}