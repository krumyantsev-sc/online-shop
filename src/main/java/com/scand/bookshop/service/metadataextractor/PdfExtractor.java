package com.scand.bookshop.service.metadataextractor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class PdfExtractor implements Extractor {
    @Override
    public String getExtension() {
        return "pdf";
    }

    @Override
    public Metadata extractMetaData(MultipartFile file) {
        PDDocument document = loadDocument(file);
        PDDocumentInformation info = document.getDocumentInformation();
        Metadata metadata = new Metadata(
                info.getTitle(),
                info.getAuthor(),
                info.getSubject(),
                info.getProducer(),
                info.getCreator(),
                extractContent(file)
        );
        closeDocument(document);
        return metadata;
    }

    private byte[] extractContent(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Could not extract content");
        }
    }

    private PDDocument loadDocument(MultipartFile file) {
        try {
            return PDDocument.load(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error loading document");
        }
    }

    private void closeDocument(PDDocument document) {
        try {
            document.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing document");
        }
    }

}
