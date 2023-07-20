package com.scand.bookshop.service;

import org.springframework.stereotype.Service;

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
}
