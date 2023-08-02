package com.scand.bookshop.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileService {
    public void writeFile(Path path, byte[] content) {
        try {
            Files.write(path, content);
        } catch (IOException e) {
            throw new RuntimeException("Error");
        }
    }

    public byte[] readFile(String path) {
        File file = new File(path);
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file");
        }
    }

    public Resource getImageResource(Path path) {
        try {
            return new InputStreamResource(Files.newInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file");
        }
    }

    public String getExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Files without name are not supported");
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    public boolean deleteIfExists(Path path) {
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Error while removing an old file");
        }
    }
}
