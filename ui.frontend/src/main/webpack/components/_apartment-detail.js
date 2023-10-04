const ApartmentDetail= {
    selectors: {
        self: '[data-cmp-component="apartmentdetail"]',
        accordionButton: '.cmp-apartmentDetail__accordion-button',
        accordionPanel: '.cmp-apartmentDetail__accordion-panel',
        promoMessage: '.cmp-apartmentDetail__promo',
    },
    cssClasses: {
        button: {
            expanded: "cmp-apartmentDetail__accordion-button--expanded"
        },
        panel: {
            hidden: "cmp-apartmentDetail__accordion-panel--hidden",
            expanded: "cmp-apartmentDetail__accordion-panel--expanded"
        }
    },
    registerButtonEvents: function(){
        const {selectors} = this;
        const {button, panel} = this.cssClasses;
        const sections = document.querySelectorAll(selectors.self);
        sections.forEach(section => {
            const accordionButton = section.querySelector(selectors.accordionButton);
            const accordionPanel = section.querySelector(selectors.accordionPanel);
            if (accordionButton) {
                accordionButton.addEventListener('click', ()=>{
                    if (accordionButton.classList.contains(button.expanded)) {
                        accordionButton.classList.remove(button.expanded);
                        accordionButton.setAttribute("aria-expanded", "false");
                        accordionPanel.classList.remove(panel.expanded);
                        accordionPanel.classList.add(panel.hidden);
                        accordionPanel.setAttribute("aria-hidden", "true");
                    } else {
                        accordionButton.classList.add(button.expanded);
                        accordionButton.setAttribute("aria-expanded", "true");
                        accordionPanel.classList.add(panel.expanded);
                        accordionPanel.classList.remove(panel.hidden);
                        accordionPanel.setAttribute("aria-hidden", "false");
                    }
                });
            }
        });
    },
    addPromoCssClass: function() {
        const {selectors} = this;
        const sections = document.querySelectorAll(selectors.self);
        sections.forEach(section => {
            const promoMessage = section.querySelector(selectors.promoMessage);
            if (promoMessage) {
                section.classList.add("hasPromo");
            } else {
                section.classList.remove("hasPromo");
            }
        });
    },
    init: function() {
        this.registerButtonEvents();
        this.addPromoCssClass();
    }
};

export default ApartmentDetail;