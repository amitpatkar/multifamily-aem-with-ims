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
import org.junit.jupiter.api.BeforeEach;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class PageInfoModelTest  {
    private final AemContext context = BrookfieldTestContext.newAemContext();
    
    private PageInfoModel pageInfoModel;

   
            
    @BeforeEach
    public void setup() throws Exception {
         context.currentPage(BrookfieldTestContext.HOME_S);     
       context.currentResource(BrookfieldTestContext.HOME_S);    
        //context.currentPage("/content/experience-fragments/config/sites/standalone/baseline");
        //System.out.println(context.currentPage().getName());
        pageInfoModel =  context.currentPage().adaptTo(PageInfoModel.class);       
    }

    @Test
    void testSiteConfigModel() throws Exception {
        // some very basic junit tests        
        assertNotNull(pageInfoModel);       
    }
    
     @Test
    void testSiteConfigModelData() throws Exception {
        // some very basic junit tests        
        assertNotNull(pageInfoModel);       
        //System.out.println(pageInfoModel.getData());
        //assertNotNull(pageInfoModel.getData());       
    }

}
