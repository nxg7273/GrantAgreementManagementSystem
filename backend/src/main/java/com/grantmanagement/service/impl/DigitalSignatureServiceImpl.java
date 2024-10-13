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
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Calendar;

@Service
public class DigitalSignatureServiceImpl implements DigitalSignatureService {

    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String KEYSTORE_FILE = "/path/to/keystore.p12";
    private static final String KEYSTORE_PASSWORD = "keystorePassword";
    private static final String KEY_ALIAS = "myalias";
    private static final String KEY_PASSWORD = "keyPassword";

    @Override
    public byte[] signPDF(byte[] pdfContent, String reason, String location) throws Exception {
        try (PDDocument document = PDDocument.load(pdfContent)) {
            PDSignature signature = new PDSignature();
            signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
            signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
            signature.setName("Signer Name");
            signature.setLocation(location);
            signature.setReason(reason);
            signature.setSignDate(Calendar.getInstance());

            KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
            try (InputStream is = getClass().getResourceAsStream(KEYSTORE_FILE)) {
                keystore.load(is, KEYSTORE_PASSWORD.toCharArray());
            }

            PrivateKey privateKey = (PrivateKey) keystore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
            Certificate[] certificateChain = keystore.getCertificateChain(KEY_ALIAS);

            SignatureOptions signatureOptions = new SignatureOptions();
            signatureOptions.setPreferredSignatureSize(SignatureOptions.DEFAULT_SIGNATURE_SIZE * 2);

            document.addSignature(signature, signatureOptions);

            ExternalSigningSupport externalSigning = document.saveIncrementalForExternalSigning(new ByteArrayOutputStream());
            byte[] cmsSignature = sign(externalSigning.getContent(), privateKey, certificateChain);

            externalSigning.setSignature(cmsSignature);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private byte[] sign(byte[] content, PrivateKey privateKey, Certificate[] certificateChain) throws Exception {
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        X509Certificate cert = (X509Certificate) certificateChain[0];
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);
        gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().build()).build(signer, cert));
        gen.addCertificates(new JcaCertStore(Arrays.asList(certificateChain)));
        return gen.generate(new org.bouncycastle.asn1.cms.CMSProcessableByteArray(content), false).getEncoded();
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
}
