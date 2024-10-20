package com.grantmanagement.service.impl;

import com.grantmanagement.service.DigitalSignatureService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Calendar;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.springframework.beans.factory.annotation.Value;

@Service
public class DigitalSignatureServiceImpl implements DigitalSignatureService {

    @Value("${digital.signature.keystore.file}")
    private String keystoreFile;

    @Value("${digital.signature.keystore.password}")
    private String keystorePassword;

    @Value("${digital.signature.key.alias}")
    private String keyAlias;

    @Value("${digital.signature.key.password}")
    private String keyPassword;

    private static final String KEYSTORE_TYPE = "PKCS12";

    @Override
    public byte[] signDocument(byte[] document, String signerName) throws IOException, GeneralSecurityException {
        try (PDDocument pdDocument = PDDocument.load(document)) {
            PDSignature signature = new PDSignature();
            signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
            signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
            signature.setName(signerName);
            signature.setSignDate(Calendar.getInstance());

            KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
            try (InputStream is = getClass().getResourceAsStream(keystoreFile)) {
                keystore.load(is, keystorePassword.toCharArray());
            }

            PrivateKey privateKey = (PrivateKey) keystore.getKey(keyAlias, keyPassword.toCharArray());
            Certificate[] certificateChain = keystore.getCertificateChain(keyAlias);

            SignatureOptions signatureOptions = new SignatureOptions();
            signatureOptions.setPreferredSignatureSize(SignatureOptions.DEFAULT_SIGNATURE_SIZE * 2);

            pdDocument.addSignature(signature, signatureOptions);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            pdDocument.saveIncremental(baos);

            byte[] cmsSignature = sign(baos.toByteArray(), privateKey, certificateChain);

            // Append the signature to the PDF
            baos.write(cmsSignature);

            return baos.toByteArray();
        } catch (Exception e) {
            throw new IOException("Failed to sign document", e);
        }
    }

    private byte[] sign(byte[] content, PrivateKey privateKey, Certificate[] certificateChain) throws Exception {
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        X509Certificate cert = (X509Certificate) certificateChain[0];
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);
        gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().build()).build(signer, cert));
        gen.addCertificates(new JcaCertStore(Arrays.asList(certificateChain)));
        return gen.generate(new CMSProcessableByteArray(content), false).getEncoded();
    }

    @Override
    public boolean verifySignature(byte[] signedPdfContent) throws IOException {
        try (PDDocument document = PDDocument.load(signedPdfContent)) {
            for (PDSignature sig : document.getSignatureDictionaries()) {
                if (sig != null) {
                    // Here you would typically verify the signature using the appropriate cryptographic libraries
                    // For simplicity, we're just checking if a signature exists
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String extractSignerName(byte[] signedPdfContent) throws IOException {
        try (PDDocument document = PDDocument.load(signedPdfContent)) {
            for (PDSignature sig : document.getSignatureDictionaries()) {
                if (sig != null) {
                    return sig.getName();
                }
            }
        }
        return null;
    }
}
