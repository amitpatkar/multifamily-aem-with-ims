/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services;

import com.brookfieldpropertiesprogram.core.models.PageInfoModel;
import com.brookfieldpropertiesprogram.core.models.PropertyConfigModel;
import com.brookfieldpropertiesprogram.core.models.SiteConfigModel;
import com.day.cq.wcm.api.Page;
import java.io.IOException;
import org.apache.sling.api.resource.LoginException;

/**
 *
 * @author amitpatkar
 */
public interface ContactUsService {
    boolean parseJSONAndSendEmail(PropertyConfigModel propertyConfigModel, String jsonData) throws IOException,LoginException;
    boolean parseJSONAndSendEmail(SiteConfigModel siteConfigModel, String jsonData) throws IOException,LoginException;
}