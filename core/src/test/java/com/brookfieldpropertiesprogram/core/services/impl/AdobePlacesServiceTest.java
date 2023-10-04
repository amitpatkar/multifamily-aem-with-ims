package com.brookfieldpropertiesprogram.core.services.impl;

import com.adobe.granite.keystore.KeyStoreService;
import com.adobe.granite.oauth.jwt.JwsBuilderFactory;
import com.brookfieldpropertiesprogram.core.models.PageInfoRequestModel;
import com.brookfieldpropertiesprogram.core.services.ImsClient;
import com.brookfieldpropertiesprogram.core.services.ImsPrivateKey;
import com.brookfieldpropertiesprogram.core.services.PlacesService;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.impl.EndPointImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@ExtendWith(AemContextExtension.class)
public class AdobePlacesServiceTest {

    EndPoint endPointMockPlaces;

    EndPoint endPointMock;
    ImsPrivateKey mockPrivateKey;
    ImsClient mockImsClient;

    PlacesService mock;

    PageInfoRequestModel pageInfoRequestModel;

    @BeforeEach
    public void setup(AemContext context) throws Exception {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(this.getClass().getClassLoader().getResourceAsStream("keystore.p12"), "my-password".toCharArray());
        KeyStoreService keyStoreServiceMock = Mockito.mock(KeyStoreService.class);
        //KeyStore keyStoreMock = Mockito.mock()
        Mockito.when(keyStoreServiceMock.getKeyStore(ArgumentMatchers.any(ResourceResolver.class))).thenReturn(keyStore);
        //Mockito.when(keyStore.getEntry(ArgumentMatchers.anyString(),ArgumentMatchers.any(KeyStore.ProtectionParameter.class))).thenReturn(new PrivateKeyEntryMockClass(mockPrivateKeyText));
        context.registerService(KeyStoreService.class, keyStoreServiceMock);

        mockPrivateKey = new AdobeImsPrivateKey();
        Map<String, Object> propsMockPrivateKey = new HashMap<>();
        propsMockPrivateKey.put("password", "my-password");
        propsMockPrivateKey.put("keyAlias", "my-key");
        context.registerInjectActivateService(mockPrivateKey, propsMockPrivateKey);

        JwsBuilderFactory jwsBuilderFactoryMock = Mockito.mock(JwsBuilderFactory.class);
        Mockito.when(jwsBuilderFactoryMock.getInstance("RS256", mockPrivateKey.getPrivateKey())).thenReturn(new JwsBuilderMockClass(null));
        context.registerService(JwsBuilderFactory.class, jwsBuilderFactoryMock);

        endPointMock = new EndPointImpl();
        Map<String, Object> propsEndPointMock = new HashMap<>();
        propsEndPointMock.put("getEndPointName", "adobeImsJwtExchange");
        propsEndPointMock.put("getEndPointURL", "https://ims-na1.adobelogin.com/ims/exchange/jwt");
        context.registerInjectActivateService(endPointMock, propsEndPointMock);

        mockImsClient = new AdobeImsClient();
        Map<String, Object> propsMockImsClient = new HashMap<>();
        propsMockImsClient.put("imsHost", "https://ims-na1.adobelogin.com");
        propsMockImsClient.put("apiKey", "542e941a7ad14f888e71c9de96f54f44");
        propsMockImsClient.put("iss", "9BFD16EE60BDE2EE0A495C4E@AdobeOrg");
        propsMockImsClient.put("sub", "0BAE4F81612BFD980A495CE5@techacct.adobe.com");
        propsMockImsClient.put("metascopes", "[https://ims-na1.adobelogin.com/s/ent_places_sdk,https://ims-na1.adobelogin.com/s/ent_marketing_sdk]");
        propsMockImsClient.put("clientSecret", "p8e-YDe2hs3e8rDdfEv2xNNanBXxDU1P9Bm2");
        context.registerInjectActivateService(mockImsClient, propsMockImsClient);

        endPointMockPlaces = new EndPointImpl();
        Map<String, Object> propsEndPointMockPlaces = new HashMap<>();
        propsEndPointMockPlaces.put("getEndPointName", "adobePlacesService");
        propsEndPointMockPlaces.put("getEndPointURL", "https://query.places.adobe.com/placesedgequery");
        context.registerInjectActivateService(endPointMockPlaces, propsEndPointMockPlaces);

        mock = new AdobePlacesService();
        Map<String, Object> propsMock = new HashMap<>();
        propsMock.put("apiKey", "542e941a7ad14f888e71c9de96f54f44");
        propsMock.put("iss", "9BFD16EE60BDE2EE0A495C4E@AdobeOrg");
        context.registerInjectActivateService(mock, propsMock);

    }

    @Test
    public void getAccessToken() {
        Assertions.assertNotNull(mockImsClient.getAccessToken());
        
        mock.getPlaces("40.75105850814752", "-73.9935292945602", 10);
    }

}
