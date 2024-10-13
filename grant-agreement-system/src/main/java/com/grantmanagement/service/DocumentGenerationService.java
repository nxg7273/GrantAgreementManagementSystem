package com.grantmanagement.service;

import com.grantmanagement.model.Agreement;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface DocumentGenerationService {
    String generateAgreementPDF(Agreement agreement) throws IOException;
    byte[] getAgreementPDFContent(String filePath) throws IOException;
    void deleteAgreementPDF(String filePath) throws IOException;
}
