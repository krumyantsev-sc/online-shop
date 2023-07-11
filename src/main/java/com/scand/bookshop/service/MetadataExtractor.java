package com.scand.bookshop.service;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class MetadataExtractor {

    public static Map<String, String> extractPdfMetadata(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDDocumentInformation info = document.getDocumentInformation();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Title", info.getTitle());
        metadata.put("Author", info.getAuthor());
        metadata.put("Subject", info.getSubject());
        metadata.put("Keywords", info.getKeywords());
        metadata.put("Creator", info.getCreator());
        metadata.put("Producer", info.getProducer());
        document.close();

        return metadata;
    }
}