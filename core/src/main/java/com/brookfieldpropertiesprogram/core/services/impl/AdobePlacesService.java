/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.services.AdobePlacesServiceConfig;
import com.brookfieldpropertiesprogram.core.services.ImsClient;
import com.brookfieldpropertiesprogram.core.services.PlacesService;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
@Component(service = PlacesService.class, name = "adobePlacesService")
@Designate(ocd = AdobePlacesServiceConfig.class)
public class AdobePlacesService implements PlacesService {

    static final Logger LOG = LoggerFactory.getLogger(AdobePlacesService.class);

    @Reference(target = "(getEndPointName=adobePlacesService)")
    EndPoint adobePlacesService;

    @Reference
    ImsClient imsClient;

    AdobePlacesServiceConfig config;

    @Activate
    @Modified
    protected void activate(AdobePlacesServiceConfig config) {
        this.config = config;
    }

    @Override
    public String getPlaces(String latitude, String longitude, int limit) {
        String accessToken = imsClient.getAccessToken();
        if (accessToken == null) {
            return null;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", accessToken);
        headers.put("x-api-key", config.apiKey());
        headers.put("x-gw-ims-org-id", config.iss());

        List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(new BasicNameValuePair("latitude", latitude));
        queryParams.add(new BasicNameValuePair("longitude", longitude));
        queryParams.add(new BasicNameValuePair("limit", String.valueOf(limit))); 
        //f8839c0e-8f62-4c8b-bda3-f82265327791 default
        //430c6a60-5dd7-4856-bdaa-9294b758e5b1 US-EAST
        queryParams.add(new BasicNameValuePair("library", "430c6a60-5dd7-4856-bdaa-9294b758e5b1"));
        
        
        try {
           String returnValue = adobePlacesService.callEndPoint(HttpMethodType.GET, queryParams,null, headers);
           LOG.info("Return Value:{}",returnValue);
           return returnValue;
        } catch (IOException e) {
            LOG.error(null,e);
        }
        return null;
    }

}
