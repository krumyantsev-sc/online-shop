package com.scand.bookshop.service.Metadataextractor;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PdfExtractor implements Extractor {
    @Getter
    private final String extension = "pdf";

    @Override
    public Map<String, String> extractMetaData(MultipartFile file) throws IOException {
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
