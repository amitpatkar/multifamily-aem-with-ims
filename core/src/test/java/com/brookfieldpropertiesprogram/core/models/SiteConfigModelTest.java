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

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import com.day.cq.wcm.api.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class SiteConfigModelTest {
    AemContext context = BrookfieldTestContext.newAemContext();
    SiteConfigModel siteConfigModel;

    @BeforeEach
    public void setup() throws Exception {                
        Page currentPage = context.currentPage("/content/config/sites/standalone/the-eugene");        
        Resource currentResource = context.currentResource("/content/config/sites/standalone/the-eugene");
        
        siteConfigModel =  context.currentPage().adaptTo(SiteConfigModel.class);       
    }

    @Test
    void testSiteConfigModel() throws Exception {
        // some very basic junit tests        
        assertNotNull(siteConfigModel);       
    }
    
     @Test
    void testSiteConfigModelData() throws Exception {
        // some very basic junit tests        
        assertNotNull(siteConfigModel);       
        //System.out.println(siteConfigModel.getData());
        assertNotNull(siteConfigModel.getData());       
    }

}
