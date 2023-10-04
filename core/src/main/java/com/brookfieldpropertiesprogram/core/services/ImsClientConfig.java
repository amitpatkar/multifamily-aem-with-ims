/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 *
 * @author patkara
 */
@ObjectClassDefinition(
        name = "BrookfieldProperties Program",
        description = "This configuration is used all Adobe IO Integration")
public @interface ImsClientConfig {

    @AttributeDefinition(
            name = "imsHost",
            description = "imsHost",
            type = AttributeType.STRING)
    public String imsHost() default "https://ims-na1.adobelogin.com";
    
    @AttributeDefinition(
            name = "imsExchangeHost",
            description = "imsExchangeHost",
            type = AttributeType.STRING)
    public String imsExchangeHost() default "https://ims-na1.adobelogin.com/ims/exchange/jwt";

    @AttributeDefinition(
            name = "API key/CLIENT ID",
            description = "API key also known as Client ID",
            type = AttributeType.STRING)
    public String apiKey();
    
    @AttributeDefinition(
            name = "Client Secret",
            description = "Client Secret",
            type = AttributeType.STRING)
    public String clientSecret();
    

    @AttributeDefinition(
            name = "Issuer/ORGANIZATION ID",
            description = "Issuer",
            type = AttributeType.STRING)
    public String iss();

    @AttributeDefinition(
            name = "Subject/TECHNICAL ACCOUNT ID",
            description = "Subject also known as Technical Account ID",
            type = AttributeType.STRING)
    public String sub();

    @AttributeDefinition(
            name = "Metascopes",
            description = "Example https://ims-na1.adobelogin.com/s/ent_campaign_sdk",
            type = AttributeType.STRING)
    public String[] metascopes();

    @AttributeDefinition(
            name = "Expiry (Seconds)",
            description = "JWT Token will expire in SystemTime + <<expirySeconds>>",
            type = AttributeType.INTEGER)
    public int exp() default 20;
}
