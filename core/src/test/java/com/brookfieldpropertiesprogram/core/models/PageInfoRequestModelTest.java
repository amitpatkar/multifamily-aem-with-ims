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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class PageInfoRequestModelTest {

    private final AemContext context = BrookfieldTestContext.newAemContext();

    private PageInfoRequestModel pageInfoRequestModel;

    @BeforeEach
    public void setup() throws Exception {

    }
    
    @Test
    void testStandaloneHome() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getPropertyConfigModel());
    }

    @Test
    void testPortfolioHome() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P);
        context.currentResource(BrookfieldTestContext.HOME_P);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
    }
    
    @Test
    void testPortfolioProperty() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_P);
        context.currentResource(BrookfieldTestContext.HOME_P_P);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getPropertyConfigModel());
        assertNull(pageInfoRequestModel.getCityConfigModel());
        assertNull(pageInfoRequestModel.getNeighborhoodConfigModel());        
        assertNull(pageInfoRequestModel.getMetroAreaConfigModel());
    }

    @Test
    void testPortfolioNeighborhood() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_N);
        context.currentResource(BrookfieldTestContext.HOME_P_N);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getNeighborhoodConfigModel());

        assertNull(pageInfoRequestModel.getPropertyConfigModel());
        assertNull(pageInfoRequestModel.getCityConfigModel());
        assertNull(pageInfoRequestModel.getMetroAreaConfigModel());
    }

    @Test
    void testPortfolioCity() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_C);
        context.currentResource(BrookfieldTestContext.HOME_P_C);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getCityConfigModel());

        assertNull(pageInfoRequestModel.getNeighborhoodConfigModel());
        assertNull(pageInfoRequestModel.getPropertyConfigModel());
        assertNull(pageInfoRequestModel.getMetroAreaConfigModel());
    }

    
    @Test
    void testJSONLd() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_C);
        context.currentResource(BrookfieldTestContext.HOME_P_C);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getJsonLdString());        
    }
}
