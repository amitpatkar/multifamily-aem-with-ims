const Accordions = {
    selectors: {
        self: '[data-cmp-component="accordions"] .cmp-accordions__group',
        accordionItem: '.cmp-accordions__item',
        accordionHeader: '.cmp-accordions__header',
        accordionButton: '.cmp-accordions__button',
        accordionButtonExpanded: '.cmp-accordions__button--expanded',
        accordionPanel: '.cmp-accordions__panel'
    },
    cssClasses: {
        button: {
            expanded: "cmp-accordions__button--expanded"
        },
        panel: {
            hidden: "cmp-accordions__panel--hidden",
            expanded: "cmp-accordions__panel--expanded"
        }
    },
    expandItem: function(accordionButtons, accordionPanels, itemIndex){
        const {button, panel} = this.cssClasses;
        for(let i = 0; i < accordionButtons.length; i++) {
            if (i != itemIndex) {
                accordionButtons[i].classList.remove(button.expanded);
                accordionPanels[i].classList.remove(panel.expanded);
                accordionButtons[i].setAttribute("aria-expanded", "false");
                if (!accordionPanels[i].classList.contains(panel.hidden)) {
                    accordionPanels[i].classList.add(panel.hidden);
                    accordionPanels[i].setAttribute("aria-hidden", "true");
                }
            } else {
                accordionButtons[i].classList.add(button.expanded);
                accordionButtons[i].setAttribute("aria-expanded", "true");
                accordionPanels[i].classList.remove(panel.hidden);
                accordionPanels[i].classList.add(panel.expanded);
                accordionPanels[i].setAttribute("aria-hidden", "false");
            }
        }
    },
    registerButtonEvents: function(){
        const {selectors} = this;
        const self = document.querySelectorAll(selectors.self);
        self.forEach(accordionGroup => {
            const accordionButtons = Array.prototype.slice.call(accordionGroup.querySelectorAll(selectors.accordionButton));
            const accordionPanels = Array.prototype.slice.call(accordionGroup.querySelectorAll(selectors.accordionPanel));
            this.expandItem(accordionButtons, accordionPanels, 0);
            for (let i = 0; i <accordionButtons.length; i++) {
                accordionButtons[i].addEventListener('click', ()=>{
                    const {button, panel} = this.cssClasses;
                    if(accordionButtons[i].classList.contains(button.expanded)){
                        accordionButtons[i].classList.remove(button.expanded);
                        accordionButtons[i].setAttribute("aria-expanded", "false");
                        accordionPanels[i].classList.remove(panel.expanded);
                        accordionPanels[i].classList.add(panel.hidden);
                        accordionPanels[i].setAttribute("aria-hidden", "true");
                    } else {
                        this.expandItem(accordionButtons, accordionPanels, i);
                    }
                });
            }
            
        });
    },
    init: function() {
        this.registerButtonEvents();
    }
};

export default Accordions;