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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = {SlingHttpServletRequest.class})
public class UpdatedPropRequestModel {

    private final static Logger LOG = LoggerFactory.getLogger(UpdatedPropRequestModel.class);

    SlingHttpServletRequest request;

    Map<String, Object> finalMap = new HashMap<>();

    final String pattern = "(\\$\\{)(.*?)(\\})";

    public UpdatedPropRequestModel(SlingHttpServletRequest request) {
        this.request = request;
    }

    @PostConstruct
    public void init() {
        substituteString();
    }

    /**
     * We take the currentResource Properties and then add the SiteConfig
     * Properties too SiteConfiguration can also use substitutions
     */
    protected void substituteString() {
        PageInfoRequestModel pageInfoRequestModel = request.adaptTo(PageInfoRequestModel.class);
        if (pageInfoRequestModel == null || pageInfoRequestModel.getPageInfoModel() == null || pageInfoRequestModel.getCombinedProps() == null || pageInfoRequestModel.getSiteConfigModel() == null) {
            finalMap.putAll(request.getResource().getValueMap());
            return;
        }
        HashMap<String,Object> combinedHashMapWithResource = new HashMap(request.getResource().getValueMap());
        combinedHashMapWithResource.putAll(pageInfoRequestModel.getSiteConfigModel().getCurrentResource().getChild(JcrConstants.JCR_CONTENT).getValueMap());
            //pageInfoRequestModel.getSiteConfigModel().getCurrentResource().getChild(JcrConstants.JCR_CONTENT).getValueMap()
        
        ValueMap combinedValueMapWithResource = new ValueMapDecorator(combinedHashMapWithResource);
        for (String aKey : combinedValueMapWithResource.keySet()) {
            LOG.info("Key:{}",aKey);
            if (aKey.startsWith("jcr:") || aKey.startsWith("cq:") || aKey.startsWith("sling:") || aKey.startsWith("nt:")) {
                continue;
            }
            String valueToParse = combinedValueMapWithResource.get(aKey, String.class);
            if (valueToParse != null) {
                String finalString = substitute(valueToParse, pageInfoRequestModel.getCombinedProps());
                LOG.info("Substitution aKey:{} valueToParse:{} finalString:{} ", aKey, valueToParse, finalString);
                finalMap.put(aKey, finalString);
            }
        }

    }

    protected String substitute(String inStr, Map<String, Object> valueMap) {
        Pattern p = Pattern.compile(this.pattern, Pattern.MULTILINE);
        Matcher matcher = p.matcher(inStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            //String group2= matcher.group(1);
            String group3 = matcher.group(2);
            //String group4= matcher.group(3);
            if (valueMap.containsKey(group3)) {
                matcher.appendReplacement(sb, (String) valueMap.get(group3));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public ValueMap getProperties() {
        return new ValueMapDecorator(finalMap);
    }
}
