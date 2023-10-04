/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.adobe.granite.keystore.KeyStoreNotInitialisedException;
import com.adobe.granite.keystore.KeyStoreService;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import org.apache.sling.api.SlingIOException;
import org.apache.sling.api.resource.ResourceResolver;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 *
 * @author patkara
 */
public class KeyStoreServiceMockClass implements KeyStoreService{
    final String plainKeyText;
    
    public KeyStoreServiceMockClass(final String plainKeyText) {
        this.plainKeyText = plainKeyText;
    }

    @Override
    public KeyManager getKeyManager(ResourceResolver arg0) throws SlingIOException, SecurityException, IllegalArgumentException, KeyStoreNotInitialisedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore getKeyStore(ResourceResolver arg0) throws SlingIOException, SecurityException, IllegalArgumentException, KeyStoreNotInitialisedException {
        KeyStore keyStore = Mockito.mock(KeyStore.class);
        Entry entry = Mockito.mock(Entry.class);
        
        try {
            Mockito.when(keyStore.getEntry(ArgumentMatchers.anyString(), ArgumentMatchers.any(KeyStore.ProtectionParameter.class))).thenReturn(new PrivateKeyEntryMockClass(plainKeyText));
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException ex) {
            Logger.getLogger(KeyStoreServiceMockClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return keyStore;        
    }

    @Override
    public KeyStore getKeyStore(ResourceResolver arg0, String arg1) throws SlingIOException, SecurityException, IllegalArgumentException, KeyStoreNotInitialisedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TrustManager getTrustManager(ResourceResolver arg0) throws SlingIOException, SecurityException, KeyStoreNotInitialisedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore getTrustStore(ResourceResolver arg0) throws SlingIOException, SecurityException, IllegalArgumentException, KeyStoreNotInitialisedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changeKeyStorePassword(ResourceResolver arg0, String arg1, char[] arg2, char[] arg3) throws SlingIOException, SecurityException, KeyStoreNotInitialisedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changeTrustStorePassword(ResourceResolver arg0, char[] arg1, char[] arg2) throws SlingIOException, SecurityException, KeyStoreNotInitialisedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addKeyStoreKeyPair(ResourceResolver arg0, String arg1, KeyPair arg2, String arg3) throws SlingIOException, SecurityException, KeyStoreNotInitialisedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyPair getKeyStoreKeyPair(ResourceResolver arg0, String arg1, String arg2) throws SlingIOException, SecurityException, KeyStoreNotInitialisedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore createKeyStore(ResourceResolver arg0, String arg1, char[] arg2) throws SlingIOException, SecurityException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore createKeyStore(ResourceResolver arg0, char[] arg1) throws SlingIOException, SecurityException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore createTrustStore(ResourceResolver arg0, char[] arg1) throws SlingIOException, SecurityException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean keyStoreExists(ResourceResolver arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean trustStoreExists(ResourceResolver arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addKeyStoreKeyEntry(ResourceResolver arg0, String arg1, String arg2, Key arg3, Certificate[] arg4) throws SecurityException, KeyStoreNotInitialisedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore.Entry getKeyStoreEntry(ResourceResolver arg0, String arg1, String arg2) throws SecurityException, KeyStoreNotInitialisedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     

    

   
}
