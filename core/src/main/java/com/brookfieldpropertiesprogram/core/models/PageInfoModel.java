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

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {Resource.class})
public class PageInfoModel {

    final static Logger LOG = LoggerFactory.getLogger(PageInfoModel.class);

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    private final Resource currentResource;

    private String siteName = "portfolio";
    private String siteType = "portfolio";

    private String currentPagePath;

    //private String siteConfigPath = null;
    private String pageName = "";

    public PageInfoModel(Resource resource) {
        this.currentResource = resource;
    }

    @PostConstruct
    protected void init() {
        currentPagePath = currentResource.getChild("jcr:content").getPath();
        if (currentPagePath.startsWith("/content/portfolio")) {
            siteName = "portfolio";
        } else if (currentPagePath.startsWith("/content/standalone")) {
            String[] parts = currentPagePath.trim().split("/", 0);
            siteName = parts[3];
            siteType = "standalone";
        }
        //pageName
        pageName = currentPagePath.replaceAll("/content/", "");
        pageName = pageName.replaceAll("standalone/", "");
        pageName = pageName.replaceAll("/", "-");
        //how to find property for this page ??
    }

    public String getName() {
        return currentResource.getName();
    }
}
