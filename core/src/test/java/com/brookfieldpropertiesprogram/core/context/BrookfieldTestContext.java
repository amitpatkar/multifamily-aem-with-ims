/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.context;

/**
 *
 * @author patkara
 */
import org.apache.sling.testing.mock.sling.ResourceResolverType;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;

import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Provides a context for unit tests.
 */
public final class BrookfieldTestContext {

    public static final String TEST_CONTENT_JSON = "/test-content.json";
    public static final String TEST_TAGS_JSON = "/test-tags.json";
    public static final String TEST_CONTENT_DAM_JSON = "/test-content-dam.json";
    public static final String TEST_APPS_JSON = "/test-apps.json";
    public static final String TEST_CONF_JSON = "/test-conf.json";
    
    public static final String HOME_P="/content/portfolio/en/home";
    public static final String HOME_S="/content/standalone/the-eugene/home";
    
    public static final String HOME_P_N="/content/portfolio/en/neighborhoods/reston";
    public static final String HOME_P_C="/content/portfolio/en/cities/oakland";
    public static final String HOME_P_M="/content/portfolio/en/metroareas/chicago-metro-area";
    public static final String HOME_P_P="/content/portfolio/en/properties/the-eugene";
    
    
    public static final String CONFIG_S_S="/content/config/sites/standalone/the-eugene"; //standalone site config
    public static final String CONFIG_S_P="/content/config/sites/portfolio/master"; //portfolio site config
    
    public static final String CONFIG_C="/content/config/cities/oakland"; //city
    public static final String CONFIG_N="/content/config/neighborhoods/reston"; //city
    public static final String CONFIG_P="/content/config/properties/the-eugene"; //city
    public static final String CONFIG_M="/content/config/metroareas/chicago-metro-area"; //city

    private BrookfieldTestContext() {
        // only static methods
        TestLoggerFactory.getInstance().setPrintLevel(Level.TRACE);
    }

    public static AemContext newAemContext() {
        AemContext aemContext = new AemContextBuilder()
                .plugin(CACONFIG)
                .resourceResolverType(ResourceResolverType.JCR_MOCK)
            .<AemContext>afterSetUp(context -> {
                    context.addModelsForPackage("com.brookfieldpropertiesprogram.core.models");                    
                }
            )
            .build();
        
        
      //common configurations
        aemContext.load().json("/config-neighborhoods-reston.json", "/content/config/neighborhoods/reston");     
        aemContext.load().json("/config-metroareas-chicago-metro-area.json", "/content/config/metroareas/chicago-metro-area");            
        aemContext.load().json("/config-cities-oakland.json", "/content/config/cities/oakland");
        aemContext.load().json("/config-properties-the-eugene.json", "/content/config/properties/the-eugene");
        
        
        //standalone
        aemContext.load().json("/siteconfig-the-eugene.json", "/content/config/sites/standalone/the-eugene");        
        aemContext.load().json("/standalone-the-eugene-home.json", "/content/standalone/the-eugene/home");        
        
        
        //portfolio
        aemContext.load().json("/siteconfig-portfolio.json", "/content/config/sites/portfolio/master");
        aemContext.load().json("/portfolio-en.json", "/content/portfolio/en");
        /*
        aemContext.load().json("/portfolio-home.json", "/content/portfolio/en/home");
        aemContext.load().json("/portfolio-cities-oakland.json", "/content/portfolio/en/cities/oakland");
        aemContext.load().json("/portfolio-metroareas-chicago-metro-area.json", "/content/portfolio/en/metroareas/chicago-metro-area");
        aemContext.load().json("/portfolio-neighborhood-reston.json", "/content/portfolio/en/neighborhoods/reston");
        aemContext.load().json("/portfolio-properties-the-eugene.json", "/content/portfolio/en/properties/the-eugene");
        */
        
        ///content/portfolio/en/home
        
        
        //component models
        aemContext.load().json("/navigationcomponentmodel.json", "/content/navigationcomponentmodel");             
        aemContext.load().json("/menu_links_left_right.json", "/content/experience-fragments/standalone/en/site/header/master/jcr:content/root/navigation_standalon");     
        
        aemContext.load().json("/model-multi-line.json", "/content/multi_model");             
       
        
        
        
        
        return aemContext;
    }
}
