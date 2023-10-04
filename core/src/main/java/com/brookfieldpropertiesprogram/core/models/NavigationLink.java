/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Adobe
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.brookfieldpropertiesprogram.core.models;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = {Resource.class})
public class NavigationLink {

    private final static Logger LOG = LoggerFactory.getLogger(NavigationLink.class);

    @Inject
    @org.apache.sling.models.annotations.Optional
    private String icon;
    @Inject
    @org.apache.sling.models.annotations.Optional
    private String label;
    @Inject
    @org.apache.sling.models.annotations.Optional
    private String id;
    @Inject
    @org.apache.sling.models.annotations.Optional
    private String cssClass;
    @Inject
    @org.apache.sling.models.annotations.Optional
    private String url;
    @Inject
    @org.apache.sling.models.annotations.Optional
    private String openInNewTab;
    
    @Inject
    @org.apache.sling.models.annotations.Optional
    private String btnType;

    @Inject
    @org.apache.sling.models.annotations.Optional
    private List<NavigationLink> subMenuLinks;
    
     @Inject
    @org.apache.sling.models.annotations.Optional
    private List<NavigationLink> attributes;

    //private String target;
    private String active;

    @PostConstruct
    protected void initModel() {
        //LOG.info("Card Component Model Post Construct called.");
    }

    public String getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public String getOpenInNewTab() {
        return openInNewTab;
    }

    public String getUrl() {
        return url;
    }

    public String getAbsUrl() {
        if (!StringUtils.isEmpty(url)) {
            if (url.startsWith("/content/") && !url.endsWith(".html")) {
                url += ".html";
            }
        }
        return url;
    }

    public String getTarget() {
        if (!StringUtils.isEmpty(openInNewTab) && Boolean.valueOf(openInNewTab)) {
            return "_new";
        } else {
            return "_self";
        }
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public List<NavigationLink> getSubMenuLinks() {
        return new ArrayList(subMenuLinks);
    }

    public String getId() {
        return id;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getBtnType() {
        return btnType;
    }
    
    
    public List<NavigationLink> getAttributes() {
        return new ArrayList(attributes);
    }
    
    

}
