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

import com.brookfieldpropertiesprogram.core.dto.ConfigPath;
import static com.brookfieldpropertiesprogram.core.models.PageInfoModel.LOG;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.JsonArray;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import javax.annotation.PostConstruct;

import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

@Model(adaptables = {SlingHttpServletRequest.class})
public class PageInfoRequestModel {

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    private final SlingHttpServletRequest request;

    Page siteConfig = null;
    Page propertyConfig = null;
    Page cityConfig = null;
    Page neighborhoodConfig = null;
    Page metroConfig = null;

    SiteConfigModel siteConfigModel = null;
    PageInfoModel pageInfoModel;
    PropertyConfigModel propertyConfigModel = null;
    CityConfigModel cityConfigModel = null;
    NeighborhoodConfigModel neighborhoodConfigModel = null;
    MetroAreaConfigModel metroAreaConfigModel = null;

    String contentFragmentPath;

    Map<String, Object> valueMapFinal;

    public PageInfoRequestModel(SlingHttpServletRequest request) {
        this.request = request;
    }

    //@Inject
    //@Source("script-bindings")
    //Resource resource;

    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Page currentPage;

    @PostConstruct
    protected void init() {
        //this.currentPage = resource.adaptTo(Page.class);
        if (currentPage == null) {
            //lets try to find the page here
            currentPage = request.getResource().adaptTo(Page.class);
            if (currentPage == null) {
                LOG.warn("Unable to find the current page from resource path:{}",request.getResource().getPath());
                return;
            }            
        }
        if (request.getAttribute("pageInfoModel") != null) {
            pageInfoModel = (PageInfoModel) request.getAttribute("pageInfoModel");
            siteConfigModel = (SiteConfigModel) request.getAttribute("siteConfigModel");
            propertyConfigModel = (PropertyConfigModel) request.getAttribute("propertyConfigModel");

            cityConfigModel = (CityConfigModel) request.getAttribute("cityConfigModel");
            neighborhoodConfigModel = (NeighborhoodConfigModel) request.getAttribute("neighborhoodConfigModel");
            metroAreaConfigModel = (MetroAreaConfigModel) request.getAttribute("metroAreaConfigModel");

            valueMapFinal = (Map<String, Object>) request.getAttribute("valueMapFinal");

            contentFragmentPath = (String) request.getAttribute("contentFragmentPath");
            return;
        }
        pageInfoModel = currentPage.adaptTo(PageInfoModel.class);
        resolveConfigurations(); //rest of the configurations
        valueMapFinal = new HashMap<>();
        //populate with page. prefix
        populateValueMap(currentPage.getName(), currentPage.getContentResource().adaptTo(ValueMap.class), "page.", null);
        //populate with site. prefix
        if (siteConfig != null) {
            populateValueMap(siteConfig.getName(), siteConfig.getContentResource().adaptTo(ValueMap.class), "site.", null);
        }

        if (propertyConfig != null) {
            populateValueMap(propertyConfig.getName(), propertyConfig.getContentResource().adaptTo(ValueMap.class), null, propertyConfigModel.getAdditionalMap());
        } else if (cityConfig != null) {
            populateValueMap(cityConfig.getName(), cityConfig.getContentResource().adaptTo(ValueMap.class), null, null);
        } else if (neighborhoodConfig != null) {
            populateValueMap(neighborhoodConfig.getName(), neighborhoodConfig.getContentResource().adaptTo(ValueMap.class), null, null);
        } else if (metroConfig != null) {
            populateValueMap(metroConfig.getName(), metroConfig.getContentResource().adaptTo(ValueMap.class), null, null);
        }
        request.setAttribute("pageInfoModel", pageInfoModel);
        request.setAttribute("siteConfigModel", siteConfigModel);
        request.setAttribute("propertyConfigModel", propertyConfigModel);
        request.setAttribute("cityConfigModel", cityConfigModel);
        request.setAttribute("neighborhoodConfigModel", neighborhoodConfigModel);
        request.setAttribute("metroAreaConfigModel", metroAreaConfigModel);
        request.setAttribute("valueMapFinal", valueMapFinal);
        request.setAttribute("contentFragmentPath", contentFragmentPath);
    }

    public PageInfoModel getPageInfoModel() {
        return pageInfoModel;
    }

    public Map<String, Object> getValueMapFinal() {
        return valueMapFinal;
    }

    public ValueMap getCombinedProps() {
        return new ValueMapDecorator(valueMapFinal);
    }

    public PropertyConfigModel getPropertyConfigModel() {
        return propertyConfigModel;
    }

    public CityConfigModel getCityConfigModel() {
        return cityConfigModel;
    }

    public NeighborhoodConfigModel getNeighborhoodConfigModel() {
        return neighborhoodConfigModel;
    }

    public MetroAreaConfigModel getMetroAreaConfigModel() {
        return metroAreaConfigModel;
    }

    public SiteConfigModel getSiteConfigModel() {
        return siteConfigModel;
    }

    public String getData() {
        JsonObject jsonObj = new JsonObject();
        //currentPage
        String currentPagePath = currentPage.getPath();
        /*
        if (currentPagePath.startsWith("/content/portfolio")) {
            siteName = "portfolio";
        } else if (currentPagePath.startsWith("/content/standalone")) {
            String[] parts = currentPagePath.trim().split("/", 0);
            siteName = parts[3];
            siteType = "standalone";
        }
         */
        //pageName
        String pageName = currentPagePath.replaceAll("/content/", "");
        pageName = pageName.replaceAll("standalone/", "");
        pageName = pageName.replaceAll("/", "-");

        if (pageInfoModel == null) {
            return "{}";
        }
        //Analytics        
        JsonObject jsonObjPageInfo = new JsonObject();
        jsonObjPageInfo.addProperty("currentPagePath", currentPagePath);
        jsonObjPageInfo.addProperty("pageName", pageName);
        //jsonObjPageInfo.addProperty("siteName", pageInfoModel.getSiteName());
        //jsonObjPageInfo.addProperty("siteType", pageInfoModel.getSiteType());

        jsonObjPageInfo.addProperty("serverName", request.getServerName());
        jsonObj.add("pageInfo", jsonObjPageInfo);
        if (getSiteConfigModel() != null && getSiteConfigModel().getData() != null) {
            jsonObj.add("siteInfo", new JsonParser().parse(getSiteConfigModel().getData()).getAsJsonObject());
        }
        if (getPropertyConfigModel() != null && getPropertyConfigModel().getData() != null) {
            jsonObj.add("propertyInfo", new JsonParser().parse(getPropertyConfigModel().getData()).getAsJsonObject());
        }
        return jsonObj.toString();
    }

    protected void resolveConfigurations() {
        //Step 1. StandAlone versus Portfolio
        ConfigPath cp = null;
        if (currentPage.getPath().startsWith("/content/portfolio")) {
            cp = resolvePortfolioConfigurations(request.getRequestPathInfo().getSuffix());
        } else if (currentPage.getPath().startsWith("/content/standalone")) {
            String[] parts = currentPage.getPath().trim().split("/", 0);
            LOG.info("Standalone SiteName:{}", parts[3]);
            cp = resolveStandaloneConfigurations(parts[3]);
        }
        if (cp == null) {
            return; //this is an ERROR
        } else {
            LOG.info("Configuration Paths:{}", cp.toString());
        }
        //now which of the paths are really valid ??
        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);

        //Validate Paths
        siteConfig = (cp.getSiteConfigPathSecondary() != null ? pageManager.getPage(cp.getSiteConfigPathSecondary()) : null);
        if (siteConfig == null) {
            siteConfig = (cp.getSiteConfigPath() != null ? pageManager.getPage(cp.getSiteConfigPath()) : null);
        }
        propertyConfig = (cp.getPropertyConfigPath() != null ? pageManager.getPage(cp.getPropertyConfigPath()) : null);
        cityConfig = (cp.getCityConfigPath() != null ? pageManager.getPage(cp.getCityConfigPath()) : null);
        neighborhoodConfig = (cp.getNeighborhoodConfigPath() != null ? pageManager.getPage(cp.getNeighborhoodConfigPath()) : null);
        metroConfig = (cp.getMetroConfigPath() != null ? pageManager.getPage(cp.getMetroConfigPath()) : null);

        //Now Adapt To
        siteConfigModel = (siteConfig != null ? siteConfig.adaptTo(SiteConfigModel.class) : null);
        propertyConfigModel = (propertyConfig != null ? propertyConfig.adaptTo(PropertyConfigModel.class) : null);
        cityConfigModel = (cityConfig != null ? cityConfig.adaptTo(CityConfigModel.class) : null);
        neighborhoodConfigModel = (neighborhoodConfig != null ? neighborhoodConfig.adaptTo(NeighborhoodConfigModel.class) : null);
        //resolve the content fragment Path

        String firstChoice = "/master";
        Page firstChoicePage = null;
        if (request.getRequestPathInfo().getSuffix() != null) {
            firstChoicePage = pageManager.getPage(cp.getContentFragmentPath() + request.getRequestPathInfo().getSuffix());
        }
        if (firstChoicePage == null) { //suffix is null then check if activeVariation is there
            if (siteConfigModel != null && StringUtils.isNotEmpty(siteConfigModel.getActiveVariation())) {
                firstChoicePage = pageManager.getPage(cp.getContentFragmentPath() + "/" + siteConfigModel.getActiveVariation());
                if (firstChoicePage != null) {
                    firstChoice = "/" + siteConfigModel.getActiveVariation();
                }
            }
        } else {
            firstChoice = request.getRequestPathInfo().getSuffix();
        }
        //it is important to ensure if this fragment is not available we don't send it to the component        
        contentFragmentPath = cp.getContentFragmentPath() + firstChoice;
        if (pageManager.getPage(contentFragmentPath) == null) contentFragmentPath = null; //no need to send it 

    }

    protected ConfigPath resolvePortfolioConfigurations(String suffix) {
        LOG.info("resolvePortfolioConfigurations suffix:{}", suffix);
        //city
        ConfigPath cp = new ConfigPath();
        cp.setSiteConfigPath("/content/config/sites/portfolio/master");
        //if Property or Not
        if (currentPage.getParent().getName().equalsIgnoreCase("properties")) { //property/the-eugene.html
            cp.setPropertyConfigPath("/content/config/properties/" + currentPage.getName());
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/properties/" + currentPage.getName());
            cp.setSiteConfigPathSecondary("/content/config/sites/portfolio/properties/" + currentPage.getName());
        } else if (currentPage.getParent().getName().equalsIgnoreCase("cities")) { //city/oakland.html
            cp.setCityConfigPath("/content/config/cities/" + currentPage.getName());
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/cities/" + currentPage.getName());
            cp.setSiteConfigPathSecondary("/content/config/sites/portfolio/cities/" + currentPage.getName());
        } else if (currentPage.getParent().getName().equalsIgnoreCase("neighborhoods")) { // neighborhood/oakland.html
            cp.setNeighborhoodConfigPath("/content/config/neighborhoods/" + currentPage.getName());
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/neighborhoods/" + currentPage.getName());
            cp.setSiteConfigPathSecondary("/content/config/sites/portfolio/neighborhoods/" + currentPage.getName());
        } else if (currentPage.getParent().getName().equalsIgnoreCase("collections")) { //collection/the-collection.html -- SUFFIX
            cp.setCollectionConfigPath("/content/config/collections/" + currentPage.getName());
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/collections/" + currentPage.getName());
            cp.setSiteConfigPathSecondary("/content/config/sites/portfolio/collections/" + currentPage.getName());
        } else if (currentPage.getParent().getName().equalsIgnoreCase("metroareas")) { //metro/oakland.html -- SUFFIX
            cp.setMetroConfigPath("/content/config/metroareas/" + currentPage.getName());
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/metroareas/" + currentPage.getName());
            cp.setSiteConfigPathSecondary("/content/config/sites/portfolio/metroareas/" + currentPage.getName());
        } else if (currentPage.getParent().getName().equalsIgnoreCase("states")) {
            cp.setStateConfigPath("/content/config/states" + currentPage.getName());
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/states/" + currentPage.getName());
            cp.setSiteConfigPathSecondary("/content/config/sites/portfolio/states/" + currentPage.getName());
        } else {
            cp.setContentFragmentPath("/content/experience-fragments/portfolio/en/content/" + currentPage.getName());
            cp.setSiteConfigPathSecondary("/content/config/sites/portfolio/" + currentPage.getName());
        }
        return cp;
    }

    /**
     *
     * @param siteName
     * @return
     */
    protected ConfigPath resolveStandaloneConfigurations(String siteName) {
        LOG.info("resolveStandaloneConfigurations siteName:{}", siteName);
        ConfigPath cp = new ConfigPath();
        cp.setSiteConfigPath("/content/config/sites/standalone/" + siteName);
        cp.setSiteConfigPathSecondary("/content/config/sites/standalone/master"); //pick it from master if the <<the-site-name>> is not available
        cp.setPropertyConfigPath("/content/config/properties/" + siteName);
        return cp;
    }

    /**
     *
     * @param nodeName
     * @param valueMap
     * @param prefix
     * @param additionalMap
     */
    protected void populateValueMap(String nodeName, ValueMap valueMap, String prefix, Map<String, Object> additionalMap) {
        if (valueMapFinal == null) {
            valueMapFinal = new HashMap<>();
        }
        if (valueMap == null || valueMap.isEmpty()) {
            return;
        }
        if (additionalMap != null && !additionalMap.isEmpty()) {
            valueMapFinal.putAll(additionalMap);
        }
        valueMapFinal.put(prefix != null ? prefix + "nodeName" : "nodeName", nodeName);
        valueMapFinal
                .put(prefix != null ? prefix + "name" : "name", valueMap.get(JcrConstants.JCR_TITLE, String.class
                ));
        for (String aKey
                : valueMap.keySet()) {
            if (aKey.startsWith("nt:") || aKey.startsWith("cq:") || aKey.startsWith("sling:") || aKey.startsWith("jcr:")) {
                continue;
            }
            String value = valueMap.get(aKey, String.class); //ignore any other type
            if (value == null) {
                continue;
            }
            if (prefix == null) {
                valueMapFinal.put(aKey, value);
            }
            if (prefix != null) {
                valueMapFinal.put(prefix + aKey, value);
            }
        }

    }

    /**
     *
     * @return
     */
    public String getJsonLdString() {
        JsonArray arr = null;
        String baseURIString = "";
        String selfAbsolutePath = "";
        try {
            URI baseURI = new URI(request.getScheme(), null, request.getServerName(), request.getServerPort(), null, null, null);
            baseURIString = baseURI.toString();
            
            URI currentAbsolutePathURI = new URI(request.getScheme(), null, request.getServerName(), request.getServerPort(), request.getRequestURI(), null, null);
            selfAbsolutePath = currentAbsolutePathURI.toString();
        } catch (URISyntaxException e) {
            LOG.error(null, e);
        }

        if (this.propertyConfigModel != null) {
            JsonObject p = propertyConfigModel.getJsonLdObject();
            if (p != null) {
                if (arr == null) {
                    arr = new JsonArray();
                }
                arr.add(p);
            }
        }

        if (this.siteConfigModel != null) {
            JsonObject p = siteConfigModel.getJsonLdObject();
            if (p != null) {
                if (arr == null) {
                    arr = new JsonArray();
                }
                arr.add(p);
            }
        }
        if (this.neighborhoodConfigModel != null) {
            JsonObject p = neighborhoodConfigModel.getJsonLdObject();
            if (p != null) {
                if (arr == null) {
                    arr = new JsonArray();
                }
                arr.add(p);
            }
        }
        if (this.metroAreaConfigModel != null) {
            JsonObject p = metroAreaConfigModel.getJsonLdObject();
            if (p != null) {
                if (arr == null) {
                    arr = new JsonArray();
                }
                arr.add(p);
            }
        }
        if (this.cityConfigModel != null) {
            JsonObject p = cityConfigModel.getJsonLdObject();
            if (p != null) {
                if (arr == null) {
                    arr = new JsonArray();
                }
                arr.add(p);
            }
        }
        return arr == null ? null : arr.toString().replaceAll("BASE_SERVER_URL",baseURIString).replaceAll("SELF", selfAbsolutePath);
    }

    public String getContentFragmentPath() {
        return contentFragmentPath;
    }

}
