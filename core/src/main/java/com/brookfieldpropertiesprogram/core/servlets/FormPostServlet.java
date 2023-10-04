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
package com.brookfieldpropertiesprogram.core.servlets;

import com.brookfieldpropertiesprogram.core.models.PageInfoModel;
import com.brookfieldpropertiesprogram.core.models.PageInfoRequestModel;
import com.brookfieldpropertiesprogram.core.services.ContactUsService;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = {Servlet.class})
@SlingServletResourceTypes(
        resourceTypes = {"brookfieldpropertiesprogram/components/page", "sling/servlet/default"},
        methods = HttpConstants.METHOD_POST,
        selectors = {"contactus"},
        extensions = "html")
@ServiceDescription("Brookfield Properties - Form Post Servlet")
public class FormPostServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    transient Logger LOG = LoggerFactory.getLogger(FormPostServlet.class);
    @Reference
    transient ContactUsService contactUsService;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        String postRequestBody = req.getReader().lines().collect(Collectors.joining());
        final Resource resource = req.getResource();
        Page thePage = resource.adaptTo(Page.class);
        PageInfoRequestModel pageInfoRequestModel = null;
        pageInfoRequestModel = req.adaptTo(PageInfoRequestModel.class);

        LOG.info("Acessing the servlet Size of request parameters : {} List Size:{}", req.getRequestParameterMap().size(), req.getRequestParameterList().size());
        /*
        String postRequestBody = null;
        for (Map.Entry<String, RequestParameter[]> param : req.getRequestParameterMap().entrySet()) {
            //fileAttachments.add(new Document(rpm.get()));
            LOG.info("Param: {}",param.getKey());
            postRequestBody = param.getKey(); //the data is posted as string entity by the core form framework
            break;
            //}
        }
         */
        if (!StringUtils.isEmpty(postRequestBody)) {
            try {
                if (pageInfoRequestModel.getPropertyConfigModel() != null) {
                    contactUsService.parseJSONAndSendEmail(pageInfoRequestModel.getPropertyConfigModel(), postRequestBody);
                }
                else if (pageInfoRequestModel.getSiteConfigModel() != null) {
                    contactUsService.parseJSONAndSendEmail(pageInfoRequestModel.getSiteConfigModel(), postRequestBody);
                }
            } catch (LoginException ex) {
                LOG.error(null, ex);
            }
        } else {
            throw new IOException("Empty Post Data");
        }
        //String postRequestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        //get the email template configuration            

        //emailService.sendPlainText("{\"personalizations\": [{\"to\": [{\"email\": \"amitpatkar@gmail.com\"}]}],\"subject\": \"Hello, World!\",\"from\": {\"email\": \"amitpatkar@gmail.com\"},\"content\": [{\"type\": \"text/plain\", \"value\": \"Heya!\"}]}");
    }
}
