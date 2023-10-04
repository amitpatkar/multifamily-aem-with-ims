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
        name = "BrookfieldProperties ImsPrivateKeyConfig",
        description = "This configuration is used all Adobe IO Integration")
public @interface ImsPrivateKeyConfig {

    @AttributeDefinition(
            name = "password",
            description = "password, this is the keyStorePassword added to the user",
            type = AttributeType.STRING)
    public String password();
    
    
     @AttributeDefinition(
            name = "keyAlias",
            description = "keyAlias when the private key was imported in the user's keystore",
            type = AttributeType.STRING)
    public String keyAlias() default "my-key";
    
    @AttributeDefinition(
            name = "keyAsPlainText",
            description = "keyAsPlainText",
            type = AttributeType.STRING)
    public String keyAsPlainText();
    
    
}
