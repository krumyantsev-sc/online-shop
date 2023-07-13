package com.scand.bookshop.service.Metadataextractor;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface Extractor {
    Metadata extractMetaData(MultipartFile file) throws IOException;
    String getExtension();
}
