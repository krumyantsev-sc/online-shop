package com.scand.bookshop.service.Metadataextractor;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EpubExtractor implements Extractor {
    @Getter
    public final String extension = "epub";

    @Override
    public Map<String, String> extractMetaData(MultipartFile file) throws IOException {
        Map<String, String> metadata = new HashMap<>();
        return metadata;
    }
}