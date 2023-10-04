/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.services.PropertyListExternalService;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;

/**
 * @author amitpatkar
 */
@Component(service = PropertyListExternalService.class)
public class PropertyListExternalServiceImpl implements PropertyListExternalService {
    private final Gson gson = new Gson();

    @Reference(target = "(getEndPointName=searchAvailabilityByOneSiteID)")
    EndPoint queryGraphql;

    @Override
    public JsonArray searchAvailabilityByOneSiteID(String oneSiteId) throws IOException {
        SearchAvailabilityByOneSiteIDRequest request = new SearchAvailabilityByOneSiteIDRequest(oneSiteId);
        String response = queryGraphql.callEndPoint(HttpMethodType.POST, gson.toJson(request));

        JsonObject obj = gson.fromJson(response, JsonObject.class);
        return obj.getAsJsonObject("data").getAsJsonArray("SearchAvailabilitiesByOneSiteId");
    }


    static class SearchAvailabilityByOneSiteIDRequest {

        private final String query = "query SearchAvailabilitiesByOneSiteId($oneSiteId: String) {\n" +
                "      SearchAvailabilitiesByOneSiteId(oneSiteId: $oneSiteId) {\n" +
                "        ...CoreUnitFields\n" +
                "        unitDetails {\n" +
                "          ...UnitDetailsFields\n" +
                "        }\n" +
                "        availability {\n" +
                "          ...UnitAvailabilityFields\n" +
                "        }\n" +
                "        address {\n" +
                "          ...UnitAddressFields\n" +
                "        }\n" +
                "        floorPlan {\n" +
                "          ...UnitFloorPlanFields\n" +
                "        }\n" +
                "        leaseOptions {\n" +
                "          ...LeaseOptionsFields\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "    \n" +
                "fragment LeaseOptionsFields on LeaseOptions {\n" +
                "  __typename\n" +
                "neededByDate\n" +
                "leaseTerm\n" +
                "rent\n" +
                "best\n" +
                "}\n" +
                "\n" +
                "fragment UnitDetailsFields on UnitDetails {\n" +
                "  __typename\n" +
                "bedrooms\n" +
                "bathrooms\n" +
                "grossSqFtCount\n" +
                "rentSqFtCount\n" +
                "floorNumber\n" +
                "description\n" +
                "noteDescription\n" +
                "}\n" +
                "\n" +
                "fragment UnitAvailabilityFields on UnitAvailability {\n" +
                "  __typename\n" +
                "unavailableCode\n" +
                "madeReadyBit\n" +
                "madeReadyDate\n" +
                "availableDate\n" +
                "availableBit\n" +
                "lastActionCode\n" +
                "lastActionDesc\n" +
                "vacantDate\n" +
                "vacantBit\n" +
                "}\n" +
                "\n" +
                "fragment UnitFloorPlanFields on UnitFloorPlan {\n" +
                "  __typename\n" +
                "floorPlanID\n" +
                "floorPlanCode\n" +
                "floorPlanName\n" +
                "floorPlanGroupName\n" +
                "floorPlanGroupID\n" +
                "image {\n" +
                "description\n" +
                "file {\n" +
                "fileName\n" +
                "url\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "\n" +
                "fragment UnitAddressFields on UnitAddress {\n" +
                "  __typename\n" +
                "address1\n" +
                "address2\n" +
                "buildingID\n" +
                "buildingNumber\n" +
                "cityName\n" +
                "countryName\n" +
                "countyName\n" +
                "state\n" +
                "unitID\n" +
                "unitNumber\n" +
                "zip\n" +
                "}\n" +
                "\n" +
                "fragment CoreUnitFields on Unit {\n" +
                "  __typename\n" +
                "city\n" +
                "state\n" +
                "pk\n" +
                "propertyContentfulId\n" +
                "propertyNumberID\n" +
                "baseRentAmount\n" +
                "floorPlanMarketRent\n" +
                "unitMarketRent\n" +
                "nonRevenueFlag\n" +
                "nonRefundFee\n" +
                "depositAmount\n" +
                "oneSiteId\n" +
                "minMoveIn\n" +
                "leaseOptions {\n" +
                "neededByDate\n" +
                "rent\n" +
                "}\n" +
                "}";
        private Variables variables;

        public SearchAvailabilityByOneSiteIDRequest(String oneSiteId) {
            this.variables = new Variables(oneSiteId);
        }

        public String getQuery() {
            return query;
        }

        public Variables getVariables() {
            return variables;
        }

        public void setVariables(Variables variables) {
            this.variables = variables;
        }

        static class Variables {

            private String oneSiteId;

            public Variables(String oneSiteId) {
                this.oneSiteId = oneSiteId;
            }

            public String getOneSiteId() {
                return oneSiteId;
            }

            public void setOneSiteId(String oneSiteId) {
                this.oneSiteId = oneSiteId;
            }
        }
    }
}
