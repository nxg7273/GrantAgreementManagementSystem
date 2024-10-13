package com.grantmanagement.service;

import com.grantmanagement.model.Agreement;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface DocumentGenerationService {
    byte[] generateAgreementPDF(Agreement agreement) throws IOException;
    String saveGeneratedPDF(byte[] pdfContent, String fileName) throws IOException;
    byte[] getGeneratedPDF(String filePath) throws IOException;
    void deleteGeneratedPDF(String filePath) throws IOException;
}
