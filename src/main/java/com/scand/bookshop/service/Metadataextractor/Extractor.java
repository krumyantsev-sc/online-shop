package com.scand.bookshop.service.Metadataextractor;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface Extractor {
    Map<String, String> extractMetaData(MultipartFile file) throws IOException;
    String getExtension();
}
