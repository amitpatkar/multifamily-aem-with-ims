//find all items with data-enable-handlebar="true" data-enable-handlebar-item="<<digitalData Island>>
var els = document.body.querySelectorAll("section[data-cmp-enable-handlebar-island],div[data-cmp-enable-handlebar-island]");
for (var i=0;i<els.length;i++) {
    var el = els[i];
    var islandName = el.dataset.cmpEnableHandlebarIsland;
    if (digitalData.hasOwnProperty(islandName)) {
        var template = Handlebars.compile(el.innerHTML);
        el.innerHTML = template(digitalData[islandName]);
        // execute the compiled template and print the output to the console
    }
}