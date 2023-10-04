package com.brookfieldpropertiesprogram.core.services.impl;


import com.brookfieldpropertiesprogram.core.services.PropertyListExternalService;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import com.google.gson.JsonArray;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(AemContextExtension.class)
public class PropertyListExternalTest {

    private final String UNIT_EXAMPLE_JSON = "{\"data\":{\"SearchAvailabilitiesByOneSiteId\":[{\n" +
            "        \"pk\":\"3031818:28:unitNumber:208\"\n" +
            "      },{" +
            "        \"pk\":\"3031818:28:unitNumber:209\"\n" +
            "      }]}}";

    EndPoint graphqlMock;

    PropertyListExternalService propertyListExternalServiceMock;

    @BeforeEach
    public void setup(AemContext context) throws Exception {
        graphqlMock = Mockito.mock(EndPoint.class);
        Map<String, Object> propsEndPointGraphql = new HashMap<>();
        propsEndPointGraphql.put("getEndPointName", "searchAvailabilityByOneSiteID");
        context.registerService(EndPoint.class, graphqlMock, propsEndPointGraphql);

        propertyListExternalServiceMock = new PropertyListExternalServiceImpl();
        context.registerInjectActivateService(propertyListExternalServiceMock);
    }

    @Test
    public void testSearchAvailabilityByProperty() throws IOException {
        Assertions.assertNotNull(graphqlMock);
        Assertions.assertNotNull(propertyListExternalServiceMock);

        Mockito.when(graphqlMock.callEndPoint(eq(HttpMethodType.POST), anyString())).thenReturn(UNIT_EXAMPLE_JSON);

        JsonArray result = propertyListExternalServiceMock.searchAvailabilityByOneSiteID("test-pk");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals("3031818:28:unitNumber:208", result.get(0).getAsJsonObject().get("pk").getAsString());
        Assertions.assertEquals("3031818:28:unitNumber:209", result.get(1).getAsJsonObject().get("pk").getAsString());
    }
}
