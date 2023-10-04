/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import static com.brookfieldpropertiesprogram.core.services.impl.AdobeImsPrivateKey.LOG;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 *
 * @author patkara
 */
public class PrivateKeyEntryMockClass implements KeyStore.Entry{
    
    final String plainKeyText;
    
    public PrivateKeyEntryMockClass(String plainKeyText) {
        this.plainKeyText = plainKeyText;
    }
    
    public PrivateKey getPrivateKey() {
        try {
            String privatekeyString = this.plainKeyText;
            
            privatekeyString = privatekeyString.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
            LOG.debug("The sanitized private key string is " + privatekeyString);
            // Create the private key
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            LOG.debug("The key factory algorithm is " + keyFactory.getAlgorithm());
            byte[] byteArray = privatekeyString.getBytes();
            LOG.debug("The array length is " + byteArray.length);
            //KeySpec ks = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyString));
            byte[] encodedBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(privatekeyString);
            //KeySpec ks = new PKCS8EncodedKeySpec(byteArray);
            KeySpec ks = new PKCS8EncodedKeySpec(encodedBytes);  
            return  (RSAPrivateKey) keyFactory.generatePrivate(ks);             
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            LOG.error(null,e);          
        }
        return null;
    }
}
