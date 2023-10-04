/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.models.PropertyConfigModel;
import com.brookfieldpropertiesprogram.core.models.SiteConfigModel;
import com.brookfieldpropertiesprogram.core.services.ContactUsService;
import com.brookfieldpropertiesprogram.core.services.EmailService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author amitpatkar
 */
@Component(service = ContactUsService.class)
public class ContactUsServiceAnyoneHome implements ContactUsService {

    final static private Logger LOG = LoggerFactory.getLogger(ContactUsServiceAnyoneHome.class);
    @Reference
    EmailService emailService;

    String stringOrEmpty(String val) {
        return val != null ? val : "";
    }

    String intOrEmpty(int val) {
        return val > 0 ? "" + val : "";
    }

    String doubleOrEmpty(double val) {
        return val > 0 ? "" + val : "";
    }

    String dateOrEmpty(Date val) {
        return val != null ? new SimpleDateFormat("MM/dd/yy").format(val) : "";
    }

    String formatSubject(PropertyConfigModel propertyModel) {
        return "Inquiry for " + propertyModel.getTitle() + ", " + propertyModel.getAddress() + " - Webform AH";
    }

    String formatHtml(PropertyConfigModel propertyModel, ContactUsDTO jsonData) {
        return "<p> Account ID: 500i0000p6444</p>\n" +
                "<br />\n" +
                "<p> First Name: " + stringOrEmpty(jsonData.first_name) + "</p>\n" +
                "<br />\n" +
                "<p> Last Name: " + stringOrEmpty(jsonData.last_name) + "</p>\n" +
                "<br />\n" +
                "<p> Email: " + stringOrEmpty(jsonData.email) + "</p>\n" +
                "<br />\n" +
                "<p> Phone: " + stringOrEmpty(jsonData.phone_number) + "</p>\n" +
                "<br />\n" +
                "<p> Mobile: " + stringOrEmpty(jsonData.phone_number) + "</p>\n" +
                "<br />\n" +
                "<p> Desired Minimum Rent Range:" + doubleOrEmpty(jsonData.min_rent_range) + "</p>\n" +
                "<br />\n" +
                "<p> Max Rent Range: " + doubleOrEmpty(jsonData.max_rent_range) + "</p>\n" +
                "<br />\n" +
                "<p> Desired Move In: " + dateOrEmpty(jsonData.move_in_date) + "</p>\n" +
                "<br />\n" +
                "<p> Desired Lease Term: " + stringOrEmpty(jsonData.desired_lease_term) + "</p>\n" +
                "<br />\n" +
                "<p> Desired Bedrooms: " + stringOrEmpty(jsonData.bedrooms) + "</p>\n" +
                "<br />\n" +
                "<p> Desired Bathrooms: " + intOrEmpty(jsonData.bathrooms) + "</p>\n" +
                "<br />\n" +
                "<p> Pet Type: " + stringOrEmpty(jsonData.pets) + "</p>\n" +
                "<br />\n" +
                "<p> Source: " + stringOrEmpty(propertyModel.getWebsiteURL()) + "</p>\n" +
                "<br />\n" +
                "<p> Military Pay Grade: " + stringOrEmpty(jsonData.military_pay_grade) + "</p>\n" +
                "<br />\n" +
                "<p> Reason For Moving: " + stringOrEmpty(jsonData.reason_for_moving) + "</p>\n" +
                "<br />\n" +
                "<p></p>Comments: " + stringOrEmpty(jsonData.comments) + "</p>\n";
    }

    @Override
    public boolean parseJSONAndSendEmail(PropertyConfigModel propertyConfigModel, String jsonData) throws IOException {
        //where to send this email
        if (propertyConfigModel != null) {
            if (StringUtils.isNotEmpty(propertyConfigModel.getAnyoneHomeEmailAddress())) {
                LOG.info("infoModel.getSiteConfigModel().getPropertyConfigModel().getAnyoneHomeEmailAddress():{}", propertyConfigModel.getAnyoneHomeEmailAddress());
                Gson gson = new GsonBuilder().setDateFormat("d-M-yyyy").create();
                ContactUsDTO formPost = gson.fromJson(jsonData, ContactUsDTO.class);
                String subject = formatSubject(propertyConfigModel);
                String htmlData = formatHtml(propertyConfigModel, formPost);
                emailService.sendPlainText(propertyConfigModel.getAnyoneHomeEmailAddress(), null, subject, htmlData);
            } else {
                LOG.info("infoModel.getSiteConfigModel().getPropertyConfigModel().getAnyoneHomeEmailAddress():is null");
            }
        }
        return true;
    }
    
    @Override
    public boolean parseJSONAndSendEmail(SiteConfigModel siteConfigModel, String jsonData) throws IOException {
        //Need to Implement this
        //Portfolio Pages which does't have a property configuration for example neighbourhood, city, state and the homepage itself
        return true;
    }

    public static class ContactUsDTO {
        String first_name;
        String last_name;
        String email;
        String phone_number;
        double min_rent_range;
        double max_rent_range;
        Date move_in_date;
        String desired_lease_term;
        String bedrooms;
        int bathrooms;
        String pets;
        String military_pay_grade;
        String reason_for_moving;
        String comments;
    }

}
