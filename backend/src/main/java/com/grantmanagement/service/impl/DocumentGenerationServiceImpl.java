package com.grantmanagement.service.impl;

import com.grantmanagement.model.Agreement;
import com.grantmanagement.service.DocumentGenerationService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class DocumentGenerationServiceImpl implements DocumentGenerationService {

    private static final String PDF_STORAGE_PATH = "/tmp/grant_agreements/";

    @Override
    public byte[] generateAgreementPDF(Agreement agreement) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Grant Agreement");
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.showText("Grant ID: " + agreement.getGrant().getId());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Participant: " + agreement.getParticipant().getName());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Status: " + agreement.getStatus());
                contentStream.newLineAtOffset(0, -30);
                contentStream.showText("Agreement Details:");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(agreement.getGrant().getDescription());
                contentStream.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    @Override
    public String saveGeneratedPDF(byte[] pdfContent, String fileName) throws IOException {
        File directory = new File(PDF_STORAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = PDF_STORAGE_PATH + fileName;
        Files.write(Paths.get(filePath), pdfContent);
        return filePath;
    }

    @Override
    public byte[] getGeneratedPDF(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    @Override
    public void deleteGeneratedPDF(String filePath) throws IOException {
        Files.delete(Paths.get(filePath));
    }
}
