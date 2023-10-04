/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.brookfieldpropertiesprogram.core.models;

import com.brookfieldpropertiesprogram.core.utils.JsonLdUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.google.gson.JsonObject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.via.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class PropertyConfigModel {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyConfigModel.class);
    Resource resource;

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @Inject
    @Named(JcrConstants.JCR_TITLE)
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String title;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String description;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String headline;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String featuredImage;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String logo;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String legalText;
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String theme;

    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    @Named("activeVariation")
    private String activeVariation;

    //Details
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String googleAddress;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String address;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String city;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String zipCode;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String latitude;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String longitude;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String phoneNumber;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String websiteURL;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String residentPortalURL;

    //social
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String facebookURL;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String instagramURL;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String twitterURL;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String custServiceEmailAddress;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String anyoneHomeEmailAddress;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String onesiteID;

    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String embedAnyoneHome;
    @Inject
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String embedAnyoneHomeHyLy;

    @Inject
    @Named(value = "cq:tags")
    @org.apache.sling.models.annotations.Optional
    @Via(value = JcrConstants.JCR_CONTENT, type = ChildResource.class)
    private String[] cqTags;

    @Inject
    ResourceResolver resourceResolver;

    String stateCode;
    String stateName;
    List<String> officeHours;

    private final ValueMap valueMap;

    public PropertyConfigModel(Resource resource) {
        this.resource = resource;
        valueMap = resource.getChild(JcrConstants.JCR_CONTENT).adaptTo(ValueMap.class);
    }

    @PostConstruct
    protected void init() {
        //PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        /*
        String currentPagePath = Optional.ofNullable(pageManager)
                .map(pm -> pm.getContainingPage(currentResource))
                .map(Page::getPath).orElse("");       
         */
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        if (tagManager == null) {
            return;
        }
        if (cqTags != null) {
            for (String aTag : cqTags) {
                Tag theTag = tagManager.resolve(aTag);
                if (theTag != null && theTag.getNamespace().getName().equals("states")) {
                    stateCode = theTag.getName();
                    stateName = theTag.getTitle();
                } else if (theTag != null && theTag.getNamespace().getName().equals("office-hours")) {
                    if (officeHours == null) {
                        officeHours = new ArrayList<>();
                    }
                    officeHours.add(theTag.getTitle());
                }
            }
        }
        activeVariation = valueMap.get("activeVariation", String.class);
        theme = valueMap.get("theme", String.class);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getHeadline() {
        return headline;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public String getLogo() {
        return logo;
    }

    public String getTheme() {
        return theme;
    }

    public String getActiveVariation() {
        return activeVariation;
    }

    public String getGoogleAddress() {
        return googleAddress;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public List<String> getOfficeHours() {
        return officeHours;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneNumberFormatted() {
        if (StringUtils.isNotEmpty(phoneNumber) && phoneNumber.length() == 10) {
            String number = phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
            return number;
        }
        return phoneNumber;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public String getResidentPortalURL() {
        return residentPortalURL;
    }

    public String getFacebookURL() {
        return facebookURL;
    }

    public String getInstagramURL() {
        return instagramURL;
    }

    public String getTwitterURL() {
        return twitterURL;
    }

    public String getCustServiceEmailAddress() {
        return custServiceEmailAddress;
    }

    public String getAnyoneHomeEmailAddress() {
        return anyoneHomeEmailAddress;
    }

    public String getOnesiteID() {
        return onesiteID;
    }

    public String getEmbedAnyoneHome() {
        return embedAnyoneHome;
    }

    public String getEmbedAnyoneHomeHyLy() {
        return embedAnyoneHomeHyLy;
    }

    public String getLegalText() {
        return legalText;
    }

    public ValueMap getValueMap() {
        return valueMap;
    }

    public String getData() {
        JsonObject jsonObj = new JsonObject();
        //Analytics        
        jsonObj.addProperty("onesiteID", StringUtils.isEmpty(onesiteID) ? "" : onesiteID);
        return jsonObj.toString();
    }

    /**
     *
     * @param scheme
     * @param host
     * @param port
     * @return
     * @throws URISyntaxException
     * @throws java.net.MalformedURLException
     */
    public JsonObject getJsonLdObject() {
        Map<String, Object> ld = new HashMap<>();
        ld.put("@context", "https://schema.org/");
        ld.put("@type", "Place");
        if (!StringUtils.isEmpty(this.logo)) {
            ld.put("logo", "BASE_SERVER_URL" + this.logo);
        }
        if (StringUtils.isNotEmpty(this.getName())) {
            ld.put("name", this.getTitle());
        }
        if (StringUtils.isNotEmpty(this.getDescription())) {
            ld.put("description", this.getDescription());
        }
        if (StringUtils.isNotEmpty(this.getHeadline())) {
            ld.put("slogan", this.getHeadline());
        }
        if (StringUtils.isNotEmpty(this.getFeaturedImage())) {
            ld.put("image", "BASE_SERVER_URL" + this.getFeaturedImage());
        }
        if (StringUtils.isNotEmpty(this.getEmbedAnyoneHome())) {
            ld.put("tourBookingPage", "SELF#schedule-tour-popup-button");
        }

        Map<String, Object> postalAddress = new HashMap<>();
        postalAddress.put("addressCountry", "US");
        postalAddress.put("@type", "PostalAddress");
        if (StringUtils.isNotEmpty(this.address)) {
            postalAddress.put("streetAddress", this.address);
        }
        if (StringUtils.isNotEmpty(this.city)) {
            postalAddress.put("addressRegion", this.city);
        }
        if (StringUtils.isNotEmpty(this.zipCode)) {
            postalAddress.put("postalCode", this.zipCode);
        }
        if (StringUtils.isNotEmpty(this.phoneNumber)) {
            postalAddress.put("telephone", this.phoneNumber);
        }
        ld.put("address", postalAddress);

        if (StringUtils.isNotEmpty(this.phoneNumber)) {
            ld.put("telephone", this.phoneNumber);
        }

        JsonObject objToReturn = JsonLdUtils.getJsonLd(ld);

        return objToReturn;
    }

    public String getName() {
        return resource.getName();
    }

    public Map<String, Object> getAdditionalMap() {
        Map<String, Object> additionalMap = new HashMap<>();
        if (getPhoneNumberFormatted() != null) {
            additionalMap.put("phoneNumberFormatted", getPhoneNumberFormatted());
        }
        if (!StringUtils.isEmpty(stateCode)) {
            additionalMap.put("stateCode", getStateCode());
        }
        if (!StringUtils.isEmpty(stateName)) {
            additionalMap.put("stateName", getStateName());
        }
        try {
            additionalMap.put("mapLink", getMapLink());

        } catch (URISyntaxException e) {
            LOG.warn(null, e);
        }
        return additionalMap;
    }

    public String getMapLink() throws URISyntaxException {
        String query = address + "," + city + "," + stateCode;
        return "https://www.google.com/maps/search/?api=1&query=" + query.replaceAll(" ", "+").replaceAll(",", "%2C");
    }

}
