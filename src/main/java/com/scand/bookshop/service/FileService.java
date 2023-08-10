package com.scand.bookshop.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileService {
    private final MessageSource messageSource;
    private final HttpServletRequest request;
    private static final org.apache.logging.log4j.Logger logger =
            org.apache.logging.log4j.LogManager.getLogger(FileService.class);

    public void writeFile(Path path, byte[] content) {
        logger.info("Writing file to path: " + path.toString());
        try {
            Files.write(path, content);
            logger.info("File written successfully to path: " + path.toString());
        } catch (IOException e) {
            logger.error("Error writing file to path: " + path.toString(), e);
            throw new RuntimeException(messageSource.getMessage(
                    "file_write_error", null, request.getLocale()));
        }
    }

    public byte[] readFile(String path) {
        logger.info("Reading file from path: " + path);
        File file = new File(path);
        try {
            byte[] content = Files.readAllBytes(file.toPath());
            logger.info("File read successfully from path: " + path);
            return content;
        } catch (IOException e) {
            logger.error("Error reading file from path: " + path, e);
            throw new RuntimeException(messageSource.getMessage(
                    "file_read_error", null, request.getLocale()));
        }
    }

    public Resource getImageResource(Path path) {
        logger.info("Fetching image resource from path: " + path.toString());
        try {
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(path));
            logger.info("Image resource fetched successfully from path: " + path.toString());
            return resource;
        } catch (IOException e) {
            logger.error("Error fetching image resource from path: " + path.toString(), e);
            throw new RuntimeException(messageSource.getMessage(
                    "file_read_error", null, request.getLocale()));
        }
    }

    public String getExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            logger.warn("Failed to get extension. File has no original filename.");
            throw new IllegalArgumentException(messageSource.getMessage(
                    "file_no_name_error", null, request.getLocale()));
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    public boolean deleteIfExists(Path path) {
        logger.info("Attempting to delete file if exists at path: " + path.toString());
        try {
            boolean deleted = Files.deleteIfExists(path);
            if(deleted) {
                logger.info("File deleted successfully from path: " + path.toString());
            } else {
                logger.info("File does not exist at path: " + path.toString());
            }
            return deleted;
        } catch (IOException e) {
            logger.error("Error removing file at path: " + path.toString(), e);
            throw new RuntimeException(messageSource.getMessage(
                    "error_removing_file", null, request.getLocale()));
        }
    }
}
