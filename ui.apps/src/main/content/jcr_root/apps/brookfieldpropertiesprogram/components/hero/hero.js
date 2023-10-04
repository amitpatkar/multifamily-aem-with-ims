"use strict";
use(function () {
    var isVirtualTour = properties["isVirtualTour"];       
    return {
        "data_cmp_class" : (isVirtualTour !== null && isVirtualTour === "true" ? "herovirtualtour": null )        
    }
});