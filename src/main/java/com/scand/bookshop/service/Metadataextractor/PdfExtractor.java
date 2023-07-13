package com.scand.bookshop.service.Metadataextractor;

import lombok.Getter;
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
    public Metadata extractMetaData(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDDocumentInformation info = document.getDocumentInformation();
        Metadata metadata = new Metadata(
                info.getTitle(),
                info.getAuthor(),
                info.getSubject(),
                info.getProducer(),
                info.getCreator()
        );
        document.close();
        return metadata;
    }


}
