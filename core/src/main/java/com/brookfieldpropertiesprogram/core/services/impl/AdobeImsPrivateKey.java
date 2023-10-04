/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.services.ImsPrivateKey;
import com.brookfieldpropertiesprogram.core.services.ImsPrivateKeyConfig;
import com.day.cq.wcm.api.PageManager;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amitpatkar
 */
@Component
@Designate(ocd = ImsPrivateKeyConfig.class)
public class AdobeImsPrivateKey implements ImsPrivateKey {

    static final Logger LOG = LoggerFactory.getLogger(AdobeImsPrivateKey.class);
    
    @Reference
    com.adobe.granite.keystore.KeyStoreService keyStoreService;

    @Reference 
    ResourceResolverFactory resourceResolverFactory;
    
    ImsPrivateKeyConfig config;
    
    PrivateKey privateKey;
    
    @Activate
    @Modified
    protected void activate(ImsPrivateKeyConfig config) {
        this.config = config;
        if (StringUtils.isNotEmpty(config.keyAsPlainText())) {
            setPrivateKeyInternalFromPlainText();
        }
        else {
            setPrivateKeyInternal();
        }
        
    }

    @Override
    public PrivateKey getPrivateKey()  {
        return this.privateKey;
    }
    
    /**
     * 
     */
    protected void setPrivateKeyInternal()  {                        
        final Map<String, Object> param = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, (Object) "keyStoreReader");
        ResourceResolver resourceResolver;
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);            
        } catch (LoginException e) {
           LOG.error(null,e);
           return;
        }
        //PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        //LOG.info("User Id:{}", resourceResolver.getUserID());

        char[] keyPassword = config.password().toCharArray();
        KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(keyPassword);
        KeyStore keyStore = keyStoreService.getKeyStore(resourceResolver);

        KeyStore.PrivateKeyEntry privateKeyEntry;
        try {
            privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(config.keyAlias(), entryPassword);
            if (privateKeyEntry != null) {
               this.privateKey = privateKeyEntry.getPrivateKey();
            }
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException e) {
            LOG.error(null,e);
        }
    }
    /**
     * 
     */
    protected void setPrivateKeyInternalFromPlainText()  {
        try {
            String privatekeyString = config.keyAsPlainText();
            
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
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(ks);             
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            LOG.error(null,e);          
        }
    }
}
