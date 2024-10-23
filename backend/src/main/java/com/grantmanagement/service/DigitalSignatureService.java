package com.grantmanagement.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public interface DigitalSignatureService {
    byte[] signDocument(byte[] document, String signerName) throws IOException, GeneralSecurityException;
    boolean verifySignature(byte[] signedDocument) throws IOException, GeneralSecurityException;
    String extractSignerName(byte[] signedDocument) throws IOException, GeneralSecurityException;
}
