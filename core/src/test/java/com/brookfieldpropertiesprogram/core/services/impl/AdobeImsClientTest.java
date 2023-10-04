package com.brookfieldpropertiesprogram.core.services.impl;

import com.adobe.granite.keystore.KeyStoreService;
import com.adobe.granite.oauth.jwt.JwsBuilderFactory;
import com.brookfieldpropertiesprogram.core.models.PageInfoRequestModel;
import com.brookfieldpropertiesprogram.core.services.ImsClient;
import com.brookfieldpropertiesprogram.core.services.ImsPrivateKey;
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
public class AdobeImsClientTest {

    EndPoint endPointMock;
    ImsPrivateKey mockPrivateKey;
    ImsClient mock;

    PageInfoRequestModel pageInfoRequestModel;

    @BeforeEach
    public void setup(AemContext context) throws Exception {

        /*
        imsHost="https://ims-na1.adobelogin.com"
    apiKey="542e941a7ad14f888e71c9de96f54f44"
    iss="9BFD16EE60BDE2EE0A495C4E@AdobeOrg"
    sub="0BAE4F81612BFD980A495CE5@techacct.adobe.com"
    metascopes="[https://ims-na1.adobelogin.com/s/ent_places_sdk,https://ims-na1.adobelogin.com/s/ent_marketing_sdk]"
    clientSecret="p8e-YDe2hs3e8rDdfEv2xNNanBXxDU1P9Bm2"                        
         */    
        String mockPrivateKeyText = "-----BEGIN PRIVATE KEY-----\n"
                + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCJk/18qIpirpvijJ1Dk49T5Ck9\n"
                + "6fSGdlYi3mEiRpHm0WB9w83+xS653vjsyva75hBrvS4TqZqgoVsZKGQbsXn4GNCGajPeG6kgk0UA\n"
                + "1pk0kvHy/5mJW0QdPH2CmMGMye2mxMM83jNwtSOu2tY9pmHEXXTCzd58lm9iux5rYqXrUbc9noPQ\n"
                + "HX+d2uWSqvwqogOx3bYs9RtYl0j14ayPRPNwFw7uhgEUYZ1amG3LLrHO+D5+3e9fB5X/47AhGQYG\n"
                + "W6U0BC+PfR7f6Q7Sez2OrH7peXE0LplMrsJA0X0Z9k1WZ7v5jDzXb3R8vO1mgNNv1yJjpb4BFbU9\n"
                + "UwnfRSEkuJlvAgMBAAECggEAS9h8bNb1w8WDRvhCUtjssVEds3ZhSie/AOYcIqtJEDjpuyvRSt0m\n"
                + "D25vSQvzzk7dSqg22Lwt9NQ/xrTPy7yiAzaa7x9pjM8vBDhsWDP2rlc8b7ydxm28NWn9v5u7SISi\n"
                + "MchT4iCVt1MFYwZeCgrJ51mmXybju0esDIAgGw99bSb9Vj6dSS+rez5nRq/YUy2fVfuAfm+/PFSg\n"
                + "6VgIIXgPLBVSCJmNSsAOTBIy8BZAI62VcJn+joYLtfX+BaYS+oaPrBdWugtv9oA5oJibW8YDUMzy\n"
                + "4iT/+Eo5ZziqmkVvn88oLoH4y99CkJtTNPlgBs9DTNsX3N5U9Lc9t7QxnQKTQQKBgQD9QiJT2LX2\n"
                + "0yf8kUOoyxvb7SrHc7IW+ABOj6006hXnaCxAuBOYT+ZuSeKUf+HCt3YukIFkx4ewV2qfKZhBR/TU\n"
                + "zsEXjR/4Ww2KclKtmjGLX1jtGMl1qL89Ru9yLt6eNW1N/pD4f11EauJD/VADezh94DkoTrFPAcJP\n"
                + "X614fEnKnwKBgQCLEUQvzDuOjY02JRtRtTd8+07o7ZuhiJ+6HpsrLorbT1JWn68eaO05m44FYNUE\n"
                + "VbFM80hpB/TvI3ONybc1kcQdZXtN9nkjYzpvEhpHOfrEN3clWgSMbRegLJ1mOtxviNUTJXcZOP5Y\n"
                + "X4bFPZ/vQhHLuUy5H+7aC5St3YTNT1aPMQKBgQD0gmoz+1FK+PhtHy5sg5PlFCguImQ5Sry5HHwE\n"
                + "T1BQd0HqRg4dCl8V8d2As6qaXJgMQtW6H+2/8dXtzrHBrSASaX8BJu5L0FTVaqSqGOlmfuqTFlBC\n"
                + "eWYRWjAJVHj30+sw1urOD+FTEcJcE++xPMEAvn9QjBEhjzgze9M2NQDlTwKBgFL1Vt7Ij5KnYGC3\n"
                + "XAeu+c3lwJn0/E0tSlNl5Jyq5AyV0gF4uqeCYllmMR+GNyyhzeWgj0aQh42McWdYuKENNpdurVLB\n"
                + "wFva2sXdX62zqaMZj0rJ20LF5T0YwEB2xn8Gek00oUp7lEa1nZjV/S1mFWWZQWqB4SNTmzrolUtA\n"
                + "k9tRAoGAOT/7sv09lFVd6jW3MvOH3IV4Q/+QGm+mfLuaQROvbmJr0hj85/MRDQ5Ql4Ql6M4q3yVG\n"
                + "/00OVDwluykdnY9wsRiUv0dGpNWA2e5gehwtLZaAiSjcTWdc6bFXp1HDUSNgydUejPlO8R7VKDTw\n"
                + "k+lzrLI5P/zmyEzHIeOzfWitY0I=\n"
                + "-----END PRIVATE KEY-----";
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(this.getClass().getClassLoader().getResourceAsStream("keystore.p12"), "my-password".toCharArray());
        KeyStoreService keyStoreServiceMock = Mockito.mock(KeyStoreService.class);
        //KeyStore keyStoreMock = Mockito.mock()
        Mockito.when(keyStoreServiceMock.getKeyStore(ArgumentMatchers.any(ResourceResolver.class))).thenReturn(keyStore);
        //Mockito.when(keyStore.getEntry(ArgumentMatchers.anyString(),ArgumentMatchers.any(KeyStore.ProtectionParameter.class))).thenReturn(new PrivateKeyEntryMockClass(mockPrivateKeyText));
        context.registerService(KeyStoreService.class, keyStoreServiceMock);

        mockPrivateKey = new AdobeImsPrivateKey();
        Map<String, Object> propsMockPrivateKey = new HashMap<>();
        propsMockPrivateKey.put("password","my-password");
        propsMockPrivateKey.put("keyAlias","my-key");
        /*
        propsMockPrivateKey.put("keyAsPlainText", "-----BEGIN PRIVATE KEY-----\n"
                + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCJk/18qIpirpvijJ1Dk49T5Ck9\n"
                + "6fSGdlYi3mEiRpHm0WB9w83+xS653vjsyva75hBrvS4TqZqgoVsZKGQbsXn4GNCGajPeG6kgk0UA\n"
                + "1pk0kvHy/5mJW0QdPH2CmMGMye2mxMM83jNwtSOu2tY9pmHEXXTCzd58lm9iux5rYqXrUbc9noPQ\n"
                + "HX+d2uWSqvwqogOx3bYs9RtYl0j14ayPRPNwFw7uhgEUYZ1amG3LLrHO+D5+3e9fB5X/47AhGQYG\n"
                + "W6U0BC+PfR7f6Q7Sez2OrH7peXE0LplMrsJA0X0Z9k1WZ7v5jDzXb3R8vO1mgNNv1yJjpb4BFbU9\n"
                + "UwnfRSEkuJlvAgMBAAECggEAS9h8bNb1w8WDRvhCUtjssVEds3ZhSie/AOYcIqtJEDjpuyvRSt0m\n"
                + "D25vSQvzzk7dSqg22Lwt9NQ/xrTPy7yiAzaa7x9pjM8vBDhsWDP2rlc8b7ydxm28NWn9v5u7SISi\n"
                + "MchT4iCVt1MFYwZeCgrJ51mmXybju0esDIAgGw99bSb9Vj6dSS+rez5nRq/YUy2fVfuAfm+/PFSg\n"
                + "6VgIIXgPLBVSCJmNSsAOTBIy8BZAI62VcJn+joYLtfX+BaYS+oaPrBdWugtv9oA5oJibW8YDUMzy\n"
                + "4iT/+Eo5ZziqmkVvn88oLoH4y99CkJtTNPlgBs9DTNsX3N5U9Lc9t7QxnQKTQQKBgQD9QiJT2LX2\n"
                + "0yf8kUOoyxvb7SrHc7IW+ABOj6006hXnaCxAuBOYT+ZuSeKUf+HCt3YukIFkx4ewV2qfKZhBR/TU\n"
                + "zsEXjR/4Ww2KclKtmjGLX1jtGMl1qL89Ru9yLt6eNW1N/pD4f11EauJD/VADezh94DkoTrFPAcJP\n"
                + "X614fEnKnwKBgQCLEUQvzDuOjY02JRtRtTd8+07o7ZuhiJ+6HpsrLorbT1JWn68eaO05m44FYNUE\n"
                + "VbFM80hpB/TvI3ONybc1kcQdZXtN9nkjYzpvEhpHOfrEN3clWgSMbRegLJ1mOtxviNUTJXcZOP5Y\n"
                + "X4bFPZ/vQhHLuUy5H+7aC5St3YTNT1aPMQKBgQD0gmoz+1FK+PhtHy5sg5PlFCguImQ5Sry5HHwE\n"
                + "T1BQd0HqRg4dCl8V8d2As6qaXJgMQtW6H+2/8dXtzrHBrSASaX8BJu5L0FTVaqSqGOlmfuqTFlBC\n"
                + "eWYRWjAJVHj30+sw1urOD+FTEcJcE++xPMEAvn9QjBEhjzgze9M2NQDlTwKBgFL1Vt7Ij5KnYGC3\n"
                + "XAeu+c3lwJn0/E0tSlNl5Jyq5AyV0gF4uqeCYllmMR+GNyyhzeWgj0aQh42McWdYuKENNpdurVLB\n"
                + "wFva2sXdX62zqaMZj0rJ20LF5T0YwEB2xn8Gek00oUp7lEa1nZjV/S1mFWWZQWqB4SNTmzrolUtA\n"
                + "k9tRAoGAOT/7sv09lFVd6jW3MvOH3IV4Q/+QGm+mfLuaQROvbmJr0hj85/MRDQ5Ql4Ql6M4q3yVG\n"
                + "/00OVDwluykdnY9wsRiUv0dGpNWA2e5gehwtLZaAiSjcTWdc6bFXp1HDUSNgydUejPlO8R7VKDTw\n"
                + "k+lzrLI5P/zmyEzHIeOzfWitY0I=\n"
                + "-----END PRIVATE KEY-----");
                */
        context.registerInjectActivateService(mockPrivateKey, propsMockPrivateKey);
        
        JwsBuilderFactory jwsBuilderFactoryMock = Mockito.mock(JwsBuilderFactory.class);
        Mockito.when(jwsBuilderFactoryMock.getInstance("RS256", mockPrivateKey.getPrivateKey())).thenReturn(new JwsBuilderMockClass(null));
        context.registerService(JwsBuilderFactory.class, jwsBuilderFactoryMock);
        
        
        endPointMock = new EndPointImpl();
        Map<String, Object> propsEndPointMock = new HashMap<>();
        propsEndPointMock.put("getEndPointName", "adobeImsJwtExchange");
        propsEndPointMock.put("getEndPointURL", "https://ims-na1.adobelogin.com/ims/exchange/jwt");
        context.registerInjectActivateService(endPointMock, propsEndPointMock);
        
        mock = new AdobeImsClient();
        Map<String, Object> propsMock = new HashMap<>();
        propsMock.put("imsHost", "https://ims-na1.adobelogin.com");
        propsMock.put("apiKey", "542e941a7ad14f888e71c9de96f54f44");
        propsMock.put("iss", "9BFD16EE60BDE2EE0A495C4E@AdobeOrg");
        propsMock.put("sub", "0BAE4F81612BFD980A495CE5@techacct.adobe.com");
        propsMock.put("metascopes", "[https://ims-na1.adobelogin.com/s/ent_places_sdk,https://ims-na1.adobelogin.com/s/ent_marketing_sdk]");
        propsMock.put("clientSecret", "p8e-YDe2hs3e8rDdfEv2xNNanBXxDU1P9Bm2");
        context.registerInjectActivateService(mock, propsMock);

    }

    @Test
    public void getAccessToken() {
        Assertions.assertNotNull(mock.getAccessToken());
    }

}
