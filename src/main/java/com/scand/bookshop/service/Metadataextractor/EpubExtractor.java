package com.scand.bookshop.service.Metadataextractor;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class EpubExtractor implements Extractor {

    @Override
    public Metadata extractMetaData(MultipartFile file) throws IOException {
        return new Metadata();
    }

    @Override
    public String getExtension() {
        return "epub";
    }
}