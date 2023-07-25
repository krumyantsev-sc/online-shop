package com.scand.bookshop.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class BookCoverService {
    public byte[] generateCover(byte[] data) {
        PDDocument document = getDocumentFromBytes(data);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage image = getCoverBufferedImage(pdfRenderer);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeImageToStream(image, baos);
        return baos.toByteArray();
    }

    private PDDocument getDocumentFromBytes(byte[] data) {
        try {
            return PDDocument.load(data);
        } catch (IOException e) {
            throw new RuntimeException("File error");
        }
    }

    private BufferedImage getCoverBufferedImage(PDFRenderer pdfRenderer) {
        try {
            return pdfRenderer.renderImage(0);
        } catch (IOException e) {
            throw new RuntimeException("Error while creating cover image");
        }
    }

    private void writeImageToStream(BufferedImage image, ByteArrayOutputStream baos) {
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing image to stream");
        }
    }

}
