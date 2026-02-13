package com.workilnk.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {

        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());

            return "/uploads/profile-pics/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("File upload failed");
        }
    }
}
