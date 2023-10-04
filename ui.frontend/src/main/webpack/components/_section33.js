
const Section33 = {
    selectors: {
        self: '[data-cmp-component="section33"]',
        priorityCards: '.cmp-section33__card.priority, .cmp-section33__card--cta33, .cmp-section33__card--cta33-2',
        emptyCards: '.cmp-section33__card--empty33',
        column: '.cmp-section33__col'
    },
    init: function() {
        const {selectors} = this;
        const sections = document.querySelectorAll(selectors.self);
        sections.forEach(section => {
            const prioritycards = section.querySelectorAll(selectors.priorityCards);
            const emptycards = section.querySelectorAll(selectors.emptyCards);
            prioritycards.forEach(card => {
                const col = card.closest(selectors.column);
                if (col) {
                    col.classList.add('priority');
                }
            });
            emptycards.forEach(card => {
                const col = card.closest(selectors.column);
                if (col) {
                    col.classList.add('empty');
                }
            });
        });

    }
};
export default Section33;
