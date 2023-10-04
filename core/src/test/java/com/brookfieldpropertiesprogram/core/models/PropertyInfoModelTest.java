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
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class PropertyInfoModelTest {
    private final AemContext context = BrookfieldTestContext.newAemContext();
    
    private PropertyConfigModel mock;
    
    public PropertyInfoModelTest() {
        super();
    }
                
    @BeforeEach
    public void setup() throws Exception {                
        context.currentPage(BrookfieldTestContext.CONFIG_P);     
        context.currentResource(BrookfieldTestContext.CONFIG_P);     
        mock =  context.currentPage().adaptTo(PropertyConfigModel.class);  
        
    }
    
     @Test
    void testAnyoneHomeEmailAddress() throws Exception {
        // some very basic junit tests        
        assertNotNull(mock);       
        assertNotNull(mock.getAddress());       
        //System.out.println(mock.getAddress());
        assertNotNull(mock.getAnyoneHomeEmailAddress());       
        assertNotNull(mock.getCity());       
        assertNotNull(mock.getCustServiceEmailAddress());       
        assertNotNull(mock.getData());       
        assertNotNull(mock.getDescription());       
             
        assertNotNull(mock.getFeaturedImage());       
        assertNotNull(mock.getGoogleAddress());       
        assertNotNull(mock.getHeadline());       
             
        assertNotNull(mock.getLogo());       
        assertNotNull(mock.getOnesiteID());       
        assertNotNull(mock.getPhoneNumber());               
        assertNotNull(mock.getTitle());   
        
        assertNotNull(mock.getResidentPortalURL());       
        assertNotNull(mock.getFacebookURL());  
        assertNotNull(mock.getInstagramURL());  
        assertNotNull(mock.getTwitterURL());       
        assertNotNull(mock.getWebsiteURL());       
        
 
        
        TestLoggerFactory.getInstance().getLogger(PropertyInfoModelTest.class).info(mock.getAnyoneHomeEmailAddress());
        //System.out.println(pageInfoModel.getData());
        //assertNotNull(pageInfoModel.getData());       
    }

}
