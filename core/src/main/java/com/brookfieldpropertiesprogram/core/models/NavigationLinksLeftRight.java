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
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.via.ChildResource;

@Model(adaptables = {Resource.class})
public class NavigationLinksLeftRight {

    private final static Logger LOG = LoggerFactory.getLogger(NavigationLinksLeftRight.class);  
    
    Resource resource;

    @Inject
    @org.apache.sling.models.annotations.Optional
    //@Via(value = "menuLinksLeft", type = ChildResource.class)
    private List<NavigationLink> menuLinksLeft;
    
    @Inject
    @org.apache.sling.models.annotations.Optional
    //@Via(value = "menuLinksRight", type = ChildResource.class)
    private List<NavigationLink> menuLinksRight;
    
    public NavigationLinksLeftRight(Resource resource) {
        this.resource = resource;
    }
    
    @PostConstruct
    protected void initModel() {
       
    }

    public List<NavigationLink> getMenuLinksLeft() {
        return (menuLinksLeft != null ? new ArrayList<>(menuLinksLeft): null) ;
    }

    public List<NavigationLink> getMenuLinksRight() {
        return (menuLinksRight != null ? new ArrayList<>(menuLinksRight): null) ;
    }

        
}
