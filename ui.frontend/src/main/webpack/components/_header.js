const Header = {
    selectors: {
        self: '[data-cmp-component="header"]',
        navBar: '[data-cmp-header-element="navbar"]',
        menuTrigger: '[data-cmp-header-element="menuTrigger"]',
        desktopMenu: '[data-cmp-header-element="desktopMenu"]',
        desktopMenuLeft: '[data-cmp-header-element="desktopMenuLeft"]',
        desktopMenuRight: '[data-cmp-header-element="desktopMenuRight"]',
        mobileMenu: '[data-cmp-header-element="mobileMenu"]',
        mobileMenuLeft: '[data-cmp-header-element="mobileMenuLeft"]',
        mobileMenuRight: '[data-cmp-header-element="mobileMenuRight"]',
        mobilenavBar: '.home-page .collapsed[data-cmp-header-element="navbar"]',
        headerLogo: '.home-page .cmp-header__logo a'
    },
    setHeaderBreakpoint: function() {
        const {selectors} = this;
        const navBar = document.querySelector(selectors.navBar);
        const desktopMenuLeft = document.querySelector(selectors.desktopMenuLeft);
        const desktopMenuRight = document.querySelector(selectors.desktopMenuRight);
        const mobileMenu = document.querySelector(selectors.mobileMenu);
        const menuTrigger = document.querySelector(selectors.menuTrigger);
        let breakpoint;
        if (desktopMenuLeft && desktopMenuRight && 
            (desktopMenuLeft.offsetWidth + desktopMenuRight.offsetWidth + 600 ) > 1024) {
            breakpoint = desktopMenuLeft.offsetWidth + desktopMenuRight.offsetWidth + 600;
        } else {
            breakpoint = 1024;
        }
        console.log('initial breakpoint: ', breakpoint);
        const mQuery = window.matchMedia('(min-width: ' + breakpoint +'px)');
        if (mQuery.matches) {
            navBar.classList.remove("collapsed");
        } else {
            navBar.classList.add("collapsed");
        }
        mQuery.addEventListener('change', (e) => {
            if (e.matches) {
                navBar.classList.remove("collapsed");
                mobileMenu.classList.add("hidden");
                mobileMenu.setAttribute("aria-hidden", "true");
                menuTrigger.setAttribute("aria-expanded", "false");
            } else {
                navBar.classList.add("collapsed");
            }
        });
    },
    registerMenuToggleEvent: function() {
        const {selectors} = this;
        const mobileMenu = document.querySelector(selectors.mobileMenu);
        const menuTrigger = document.querySelector(selectors.menuTrigger);
        const header = document.querySelector(selectors.self);
        if ( menuTrigger) {
            menuTrigger.addEventListener("click", () => {
                mobileMenu.classList.toggle("hidden");
                header.classList.toggle("mobile-menu-open");
                if (mobileMenu.classList.contains("hidden")){
                    mobileMenu.setAttribute("aria-hidden", "true");
                    menuTrigger.setAttribute("aria-expanded", "false");
                } else {
                    mobileMenu.setAttribute("aria-hidden", "false");
                    menuTrigger.setAttribute("aria-expanded", "true");
                }
            });
        } 
    },
    registerScrollEvent: function(){
        const header = document.querySelector(this.selectors.self);
        const scrollOffSet =  200;
        if (document.body.clientHeight > (window.innerHeight + 200)) {
            header.classList.add('with-scroll-animation');
            window.addEventListener("scroll", function(){

                    if (window.scrollY > scrollOffSet) {
                        header.classList.add("scrolled");
                    } else {
                        header.classList.remove("scrolled");
                    }
            }); 
        } 
    },
    init: function() {
        console.log('Header init!');
        this.setHeaderBreakpoint();
        this.registerMenuToggleEvent();
        this.registerScrollEvent();
    },

};
export default Header;

