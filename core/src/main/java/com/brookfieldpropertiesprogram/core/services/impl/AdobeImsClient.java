/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.adobe.granite.crypto.CryptoException;
import com.adobe.granite.oauth.jwt.JwsBuilder;
import com.brookfieldpropertiesprogram.core.services.ImsPrivateKey;
import com.brookfieldpropertiesprogram.core.services.ImsClient;
import com.brookfieldpropertiesprogram.core.services.ImsClientConfig;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amitpatkar
 */
@Component(service = ImsClient.class)
@Designate(ocd = ImsClientConfig.class)
public class AdobeImsClient implements ImsClient {

    static final Logger LOG = LoggerFactory.getLogger(AdobeImsClient.class);

    @Reference
    com.adobe.granite.oauth.jwt.JwsBuilderFactory jwsBuilderFactory;

    @Reference(target = "(getEndPointName=adobeImsJwtExchange)")
    EndPoint adobeImsJwtExchange;

    PrivateKey privateKey;

    ImsClientConfig config;
    
   

    @Activate
    @Modified
    protected void activate(ImsClientConfig config) {
        this.config = config;
    }

    @Reference(
            name = "imsPrivateKey",
            service = ImsPrivateKey.class,
            updated = "bindImsPrivateKey",
            bind = "bindImsPrivateKey",
            unbind = "unbindImsPrivateKey",
            policy = ReferencePolicy.DYNAMIC,
            cardinality = ReferenceCardinality.MULTIPLE
    )
    /**
     *
     */
    protected final void bindImsPrivateKey(final ImsPrivateKey imsPrivateKey) {
        this.privateKey = imsPrivateKey.getPrivateKey();
    }

    /**
     *
     * @param imsPrivateKey
     */
    protected final void unbindImsPrivateKey(final ImsPrivateKey imsPrivateKey) {
        this.privateKey = null;
    }

    @Override
    public String getAccessToken() {
        try {
            String jwtToken = getJWTToken();

            // Load relevant properties from prop file
            String accessToken = "";
            String apiKey = config.apiKey();
            String secret = config.clientSecret();

            List<NameValuePair> form = new ArrayList<>();
            form.add(new BasicNameValuePair("x-api-key", apiKey));
            form.add(new BasicNameValuePair("client_id", apiKey));
            form.add(new BasicNameValuePair("client_secret", secret));
            form.add(new BasicNameValuePair("jwt_token", jwtToken));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
            String response = adobeImsJwtExchange.callEndPoint(HttpMethodType.POST, null, entity, null); //no query String params and no headers

            JsonObject jObject = new Gson().fromJson(response, JsonObject.class);
            //JSONObject jObject = new JSONObject(response.toString());
            accessToken = jObject.get("access_token").getAsString();

            return accessToken;
        } catch (InvalidKeySpecException | IOException | CryptoException e) {
            LOG.error(null, e);
        }
        return "{error:true}";
    }

    public String getJWTToken() throws InvalidKeySpecException, IOException, CryptoException {

        LOG.info("Issuer:{}", config.iss());
        LOG.info("IMS HOST:{}", config.imsHost());
        LOG.info("Subject:{}", config.sub());
        LOG.info("apiKey:{}", config.apiKey());

        for (String metaScope : config.metascopes()) {
            LOG.info("MetaScope: {}", metaScope);
        }
        String aud = config.imsHost() + "/c/" + config.apiKey();
        LOG.info("Audience:{}", aud);
        JwsBuilder builder = jwsBuilderFactory.getInstance("RS256", privateKey);
        builder.setSubject(config.sub());
        builder.setIssuer(config.iss());
        builder.setAudience(aud);

        builder.setExpiresIn(config.exp());

        for (String metaScope : config.metascopes()) {
            builder.setCustomClaimsSetField(metaScope, true);
        }
        String jwtToken = builder.build();

        return jwtToken;
    }
}
