const AvailabilityFeed = {
    selectors: {
        self: '[data-cmp-component="availabilityfeed"]',
        filters: '#availability-filters',
        sortFieldList: '#availability-sort-field',
        results: ".cmp-availability__results",
        resultTbody: '#availability-result-table tbody',
        resultThead: '#availability-result-table thead',
        resultList: '#availability-result-list',
        emptyResult: '.cmp-availability__emptyResults',
        tablePagination: '.cmp-availability__paginate--table',
        listPagination: '.cmp-availability__paginate--list',
        moveInDateLabelNextMonth: 'input[name="moveindate"][value="nextmonth"] + span',
        moveInDateLabelNext2Month: 'input[name="moveindate"][value="next2month"] + span'
    },
    groupsData: [],
    sortedGroupsData: [],
    sortKey: 'minRent',
    sortOrder: 'asc',
    updateGroupsData: function(newGroups) {
        this.groupsData = newGroups;
    },
    updateSortedGroupsData: function(newSortedGroups) {
        this.sortedGroupsData = newSortedGroups;
    },
    groupUnits: function(units){
        const groups = units.reduce((groups, unit) => {
            if (groups[unit.floorPlan.floorPlanName]) {
                const item = groups[unit.floorPlan.floorPlanName];
                item.units.push(unit);
                item.availability++;
                const unitRent = parseFloat(unit.rent);
                if (unitRent < item.minRent) {
                    item.minRent = unitRent;
                }
                if (unitRent > item.maxRent) {
                    item.maxRent = unitRent;
                }
            } else {
                groups[unit.floorPlan.floorPlanName] = {
                    name: unit.floorPlan.floorPlanName,
                    type: `${unit.unitDetails.bedrooms === '0'
                        ? `Studio | ${unit.unitDetails.bathrooms} Bath`
                        : `${unit.unitDetails.bedrooms} Bed | ${unit.unitDetails.bathrooms} Bath`}`,
                    bedrooms: parseInt(unit.unitDetails.bedrooms),
                    bathrooms: parseInt(unit.unitDetails.bathrooms),
                    area: parseFloat(unit.unitDetails.grossSqFtCount),
                    minRent: parseFloat(unit.rent),
                    maxRent: parseFloat(unit.rent),
                    availability: 1,
                    units: [unit]
                };
            }
            return groups;
        }, {});
        return Object.values(groups);
    },
    formatAvailabilityURL: function(oneSiteID) {
        const parts = digitalData.pageInfo.currentPagePath.replace("/jcr:content", "").split("/");
        return `${parts[parts.length - 1]}.availability.json?oneSiteID=${oneSiteID}`;
    },
    formatDate: function(date) {
        return `${date.getMonth()+1}/${date.getDate()}/${date.getFullYear()}`;
    },
    parseDate: function(str) {
        const parts = str.split("/");
        return new Date(parseInt(parts[2]), parseInt(parts[0])-1, parseInt(parts[1]));
    },
    updateUnitRent: function(unit, moveInDate, property) {
        const formatedDate = this.formatDate(moveInDate || new Date());
        const unitProperty = property || unit.property;
        const building = (unitProperty && unitProperty.buildings ? unitProperty.buildings : []).find(
            (building) => building.oneSiteId === parseInt(unit.oneSiteId),
        );
        const isLroPricing =
            building && building.lroPricing !== null
                ? building.lroPricing
                : unitProperty && unitProperty.lroPricing !== null
                ? unitProperty.lroPricing
                : !!unit.leaseOptions;

        let leaseRent = null;
        if (!isLroPricing) {
            leaseRent = unit.baseRentAmount;
        } else if (unit.leaseOptions && unit.leaseOptions.length > 0) {
            const option = unit.leaseOptions.find((option) => option.neededByDate === formatedDate);
            if (option) {
                leaseRent = option.rent;
            }
        }
        return {
            ...unit,
            rent: leaseRent ? parseFloat(leaseRent).toFixed(0) : null,
        };
    },
    updateRent: function(units, moveInDate, property) {
        return units.map((unit) => this.updateUnitRent(unit, moveInDate, property));
    },
    filterUnitMaxRent: function(maxRent, unit) {
        if (maxRent > 0) {
            if (parseFloat(unit.rent) > maxRent) {
                return false;
            }
        }
        return true;
    },
    filterUnitMoveInDate: function(moveInDate, unit) {
        if (moveInDate) {
            if (!unit.availability.availableDate || !unit.availability.madeReadyDate) {
                return false;
            }
            if (this.parseDate(unit.availability.availableDate).getTime() > moveInDate.getTime()) {
                return false;
            }
            if (this.parseDate(unit.availability.madeReadyDate).getTime() > moveInDate.getTime()) {
                return false;
            }
        }
        return true;
    },
    filterUnitHomeType: function(homeType, unit) {
        if (homeType) {
            switch (homeType) {
                case "studio": {
                    return parseInt(unit.unitDetails.bedrooms) === 0;
                }
                case "1bedroom": {
                    return parseInt(unit.unitDetails.bedrooms) === 1;
                }
                case "2bedroom+": {
                    return parseInt(unit.unitDetails.bedrooms) >= 2;
                }
                case "pent": {
                    if (unit && unit.floorPlan) {
                        if (unit.floorPlan.floorPlanName && unit.floorPlan.floorPlanName.includes('Penthouse')) {
                            return true;
                        }
                        if (unit.floorPlan.floorPlanName && unit.floorPlan.floorPlanName.includes('PH')) {
                            return true;
                        }
                        if (unit.floorPlan.floorPlanGroupName && unit.floorPlan.floorPlanGroupName.includes('PH')) {
                            return true;
                        }
                    }
                    return !!(unit && unit.address && unit.address.unitNumber && unit.address.unitNumber.includes('PH'));

                }
                case "town": {
                    if (unit && unit.floorPlan) {
                        if (unit.floorPlan.floorPlanName && unit.floorPlan.floorPlanName.includes('TH')) {
                            return true;
                        }
                        if (unit.floorPlan.floorPlanGroupName && unit.floorPlan.floorPlanGroupName.includes('TH')) {
                            return true;
                        }
                    }
                    return false;
                }
                default:
                    return false;
            }
        }
        return true;
    },
    sortItems: function(field, order) {
        return items => items.sort(this.compareFunction(field, order))
    },
    filterUnits: function(filterFn){
        digitalData.propertyInfo.units
            .then(units => this.updateRent(units, null)) /* TODO: need to pass property here*/
            .then(units => units.filter(unit => unit.availability.availableBit))
            .then(units => units.filter(unit => unit.rent && unit.unitDetails && unit.unitDetails.bedrooms))
            .then(units => units.filter(filterFn))
            .then(this.groupUnits)
            .then(this.sortItems(this.sortKey, this.sortOrder)) //the default sorting: rent, asc
            .then(groups => {
                this.updateGroupsData(groups);
                this.updateSortedGroupsData(groups);
                this.renderGroupsIntoPage(groups);
                this.renderPagination(groups);
            });
    },
    fetchUnits: function(){
        if (digitalData) {
            const oneSiteID = digitalData.propertyInfo.onesiteID;
            console.log("oneSiteID: ", oneSiteID);
            digitalData.propertyInfo.units = fetch(this.formatAvailabilityURL(oneSiteID)).then(response => response.json());
            digitalData.propertyInfo.units.then(() => {
                this.filterUnits(this.createFiltersFunction({moveindate: new Date()}));
            });
        } else {
            digitalData.propertyInfo.units = Promise.reject("digitalData not found");
        }
    },
    registerFilterEvents: function() {
        const filters = document.querySelector(this.selectors.filters);
        const checkElements = Array.prototype.slice.call(filters.querySelectorAll('input[type="checkbox"]'));
        const moveInDateElements = Array.prototype.slice.call(filters.querySelectorAll('.move-in-date input[type="checkbox"]'));
        checkElements.forEach(el => {
            el.addEventListener('change', ()=>{
                const closestDiv = el.closest('.checkbox-item');
                if (el.checked) {
                    closestDiv.classList.add('checked');
                    if (moveInDateElements.includes(el)) {
                        moveInDateElements.forEach(checkbox => {
                            if (checkbox !== el) {
                                checkbox.checked = false;
                                checkbox.closest('.checkbox-item').classList.remove('checked');
                            }
                        })
                    }
                } else {
                    closestDiv.classList.remove('checked');
                }
            });
        });

    },
    renderGroupsIntoPage: function(groups) {
        const {selectors} = this;
        const self = document.querySelector(selectors.self);
        const results = self.querySelector(selectors.results);
        const resultTbody = document.querySelector(selectors.resultTbody);
        const resultList = document.querySelector(selectors.resultList);
        const emptyResult = self.querySelector(selectors.emptyResult);
        if (groups.length) {
            this.renderGroupsIntoTable(groups, 1, resultTbody);
            this.renderGroupsIntoList(groups, 1, resultList);
            results.classList.remove("hidden");
            emptyResult.classList.add("hidden");
        } else {
            results.classList.add("hidden");
            emptyResult.classList.remove("hidden");
        }
    },
    renderGroupsIntoTable: function(groups, pageNumber, tbody) {
            tbody.innerHTML = '';
            groups.slice((pageNumber-1) * 10, pageNumber * 10)
                .map(group => `<tr>
                    <td><span class="small-title">${group.name}</span></td>
                    <td><span class="label-text">${group.type}</span></td>
                    <td><span class="label-text">${group.area} sq. ft.</span></td>
                    <td><span class="label-text">Starting at $${group.minRent}/month*</span></td>
                    <td><span class="label-text">${group.availability} Available</span></td>
                    <td><a class="btn btn-secondary btn-ghost">View Apartment</a></td>
                </tr>`)
                .forEach(item => tbody.innerHTML += item);
    },
    renderGroupsIntoList: function(groups, pageNumber, listContainer) {
            // clear previous data
            listContainer.innerHTML = '';
            groups.slice((pageNumber-1) * 3, pageNumber * 3)
                .map(group => `<li class="cmp-availability__resultCard">
                    <h3><span class="small-title">${group.name}</span></h3>
                    <p><span class="label-text">${group.type} | ${group.area} sq. ft.</span></p>
                    <p><span class="label-text">Starting at $${group.minRent}/month*</span></p>
                    <p><span class="label-text">${group.availability} Available</span></p>
                    <p><a class="btn btn-secondary btn-ghost">View Apartment</a></p>
                </li>`)
                .forEach(item => listContainer.innerHTML += item);
    },
    renderPagination: function(groups) {
        const listPagination = document.querySelector(this.selectors.listPagination);
        const tablePagination = document.querySelector(this.selectors.tablePagination);
        const listPageCount = Math.ceil(groups.length/3);
        const tablePageCount = Math.ceil(groups.length/10);
        listPagination.innerHTML = '';
        tablePagination.innerHTML = '';
        if (groups.length > 3) {      
            listPagination.innerHTML = this.createPaginationStatus(groups.length, 3, 1) + this.createPaginationButtons(listPageCount);
            this.registerPaginationEvents(groups, listPagination, 3);
        } 
        if (groups.length > 10) {      
            tablePagination.innerHTML = this.createPaginationStatus(groups.length, 10, 1) + this.createPaginationButtons(tablePageCount);
            this.registerPaginationEvents(groups, tablePagination, 10);
        } 
    },
    createPaginationStatus: function(totalItems, itemsPerPage, pageNumber){
        return `<p class="cmp-availability__paginate-status" 
                role="status" 
                aria-live="polite" 
                class="result-status">${itemsPerPage * (pageNumber -1) + 1}-${itemsPerPage * pageNumber} of ${totalItems} Results</p>`;
    },
    createPaginationButtons: function(pageCount) {
        const prevButton = `<div class="cmp-availability__paginate-buttons">
                             <button class="paginate-button previous" tabindex="-1">
                                <span class="visually-hidden">previous</span>
                                <svg width="7" height="10" viewBox="0 0 7 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <g>
                                    <path d="M6 9L2 5L6 1" stroke="#191919" stroke-width="2" stroke-linecap="round"/>
                                    </g>
                                </svg>                                
                            </button>
                            <button class="paginate-button current" data-page-index="1">
                                <span>1</span>
                            </button>`;
        const nextButton = `<button class="paginate-button next">
                                <span class="visually-hidden">next</span>
                                <svg width="7" height="10" viewBox="0 0 7 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <g>
                                    <path d="M1 9L5 5L1 1" stroke="#191919" stroke-width="2" stroke-linecap="round"/>
                                    </g>
                                </svg>                                
                            </button>
                            </div>`;
        let otherButtons='';
        for (let i = 2; i <= pageCount; i++) {
            otherButtons += `<button class="paginate-button" data-page-index="${i}">
                                <span>${i}</span>
                            </button>`;
        }
        return prevButton + otherButtons + nextButton;
    },
    registerPaginationEvents: function(groups, paginationElement, itemsPerPage){
        const totalItems = groups.length;
        const pageCount = Math.ceil(groups.length/itemsPerPage);
        const buttonArray = Array.prototype.slice.call(paginationElement.querySelectorAll("button.paginate-button"));
        const resultTbody = document.querySelector(this.selectors.resultTbody);
        const resultList = document.querySelector(this.selectors.resultList);
        const statusElement = paginationElement.querySelector(".cmp-availability__paginate-status");
        buttonArray.forEach((el, index) => {
            el.addEventListener('click', ()=>{
                const currentPageIndex = parseInt(paginationElement.querySelector("button.paginate-button.current").getAttribute("data-page-index"));
                if(index === 0){
                    if(currentPageIndex !== 1) {
                        if (currentPageIndex === 2) {
                            buttonArray[0].setAttribute("tabindex", "-1");
                        }
                        buttonArray[pageCount + 1].removeAttribute("tabindex");
                        buttonArray[currentPageIndex].classList.remove("current");
                        buttonArray[currentPageIndex - 1].classList.add("current");
                        if(itemsPerPage === 3) {
                            this.renderGroupsIntoList(groups, currentPageIndex - 1, resultList);
                        } 
                        if(itemsPerPage === 10) {
                            this.renderGroupsIntoTable(groups, currentPageIndex - 1, resultTbody);
                        } 
                        statusElement.innerHTML = this.createPaginationStatus(totalItems, itemsPerPage, currentPageIndex - 1);
                    } 
                } else if (index === pageCount + 1) {
                    if(currentPageIndex !== pageCount) {
                        if (currentPageIndex === pageCount -1) {
                            buttonArray[pageCount + 1].setAttribute("tabindex", "-1");
                        }
                        buttonArray[0].removeAttribute("tabindex");
                        buttonArray[currentPageIndex].classList.remove("current");
                        buttonArray[currentPageIndex + 1].classList.add("current");
                        if(itemsPerPage === 3) {
                            this.renderGroupsIntoList(groups, currentPageIndex + 1, resultList);
                        } 
                        if(itemsPerPage === 10) {
                            this.renderGroupsIntoTable(groups, currentPageIndex + 1, resultTbody);
                        } 
                        statusElement.innerHTML = this.createPaginationStatus(totalItems, itemsPerPage, currentPageIndex + 1);
                    } 
                } else {
                    if (index !== currentPageIndex) {
                        buttonArray[currentPageIndex].classList.remove("current");
                        buttonArray[index].classList.add("current");
                        if(itemsPerPage === 3) {
                            this.renderGroupsIntoList(groups, index, resultList);
                        } 
                        if(itemsPerPage === 10) {
                            this.renderGroupsIntoTable(groups, index, resultTbody);
                        } 
                        if (index === 1) {
                            buttonArray[0].setAttribute("tabindex", "-1");
                            buttonArray[pageCount + 1].removeAttribute("tabindex");
                        } else if (index === pageCount) {
                            buttonArray[pageCount + 1].setAttribute("tabindex", "-1");
                            buttonArray[0].removeAttribute("tabindex");
                        } else {
                            buttonArray[0].removeAttribute("tabindex");
                            buttonArray[pageCount + 1].removeAttribute("tabindex");
                        }
                        statusElement.innerHTML = this.createPaginationStatus(totalItems, itemsPerPage, index);
                    }

                }
            });
        });
    },
    registerSortingEvents: function() {
        const sortByFieldList = document.querySelector(this.selectors.sortFieldList);
        const resultThead = document.querySelector(this.selectors.resultThead);
        const sortersInTable = resultThead.querySelectorAll("th[data-sort-field]");
        this.updateSortedGroupsData(this.groupsData);
        if (sortByFieldList) {
            sortByFieldList.addEventListener('change', (event)=>{
                if (event.target.value) {
                    this.sortKey = event.target.value;
                    this.sortOrder = 'asc';
                    this.sortedGroupsData.sort(this.compareFunction(event.target.value, 'asc'));
                    this.renderGroupsIntoPage(this.sortedGroupsData);
                    this.renderPagination(this.sortedGroupsData);
                }
            }); 
        }
        if (sortersInTable) {
            sortersInTable.forEach(sorter=>{
                sorter.addEventListener('click', ()=>{
                    if (sorter.classList.contains("asc")) {
                        sorter.classList.remove("asc");
                        sorter.classList.add("desc");
                        sorter.setAttribute("aria-sort", "descending");
                        this.sortKey = sorter.getAttribute("data-sort-field");
                        this.sortOrder = 'desc';
                        this.sortedGroupsData.sort(this.compareFunction(sorter.getAttribute("data-sort-field"), 'desc'));
                    } else if (sorter.classList.contains("desc")){
                        sorter.classList.remove("desc");
                        sorter.classList.add("asc");
                        sorter.setAttribute("aria-sort", "ascending");
                        this.sortKey = sorter.getAttribute("data-sort-field");
                        this.sortOrder = 'asc';
                        this.sortedGroupsData.sort(this.compareFunction(sorter.getAttribute("data-sort-field"), 'asc'));
                    } else {
                        const currentSortedElement = resultThead.querySelector("th.asc") || resultThead.querySelector("th.desc");
                        currentSortedElement.classList.remove("asc", "desc");
                        currentSortedElement.removeAttribute("aria-sort");
                        sorter.classList.add("asc");
                        sorter.setAttribute("aria-sort", "ascending");
                        this.sortKey = sorter.getAttribute("data-sort-field");
                        this.sortOrder = 'asc';
                        this.sortedGroupsData.sort(this.compareFunction(sorter.getAttribute("data-sort-field"), 'asc'));
                    }
                    this.renderGroupsIntoPage(this.sortedGroupsData);
                    this.renderPagination(this.sortedGroupsData);
                });    
            });
        }
    },
    compareFunction: function(sortByField, sortByOrder){
        return (a, b) => {
            if (!a.hasOwnProperty(sortByField) || !b.hasOwnProperty(sortByField)) {
                return 0;
            }
            let comparison = 0; 
            if (sortByField === 'type') {
                if(a['bedrooms'] > b['bedrooms']) {
                    comparison = 1;
                } else if(a['bedrooms'] < b['bedrooms']){
                    comparison = -1;
                } else {
                    if (a['bathrooms'] > b['bathrooms']) {
                        comparison = 1;
                    } else if (a['bathrooms'] < b['bathrooms']) {
                        comparison = -1;
                    }
                }
            } else {
                const varA = (typeof a[sortByField] === 'string')
                ? a[sortByField].toUpperCase() : a[sortByField];
                const varB = (typeof b[sortByField] === 'string')
                ? b[sortByField].toUpperCase() : b[sortByField];
                if (varA > varB) {
                    comparison = 1;
                } else if (varA < varB) {
                    comparison = -1;
                }
            }
        
            return (
                (sortByOrder === 'desc') ? (comparison * -1) : comparison
            );
        };
    },
    monthAfter: function(num) {
        const d = new Date();
        d.setDate(1);
        d.setMonth(d.getMonth()+num);
        return d;
    },
    createFiltersFunction: function(filters) {
        return unit => {
            let result = this.filterUnitMoveInDate(filters.moveindate, unit);
            result = result && (filters.hometype ? filters.hometype.map(type => this.filterUnitHomeType(type, unit)).find(val => val) : true);
            result = result && this.filterUnitMaxRent(filters.maxrent, unit);
            return result;
        }
    },
    init: function(){
        const {selectors} = this;
        const filtersForm = document.querySelector(selectors.filters);
        this.registerFilterEvents();
        if (filtersForm) {
            let month = this.monthAfter(1);
            filtersForm.querySelector(selectors.moveInDateLabelNextMonth).innerHTML = month.toLocaleString('default', { month: 'short' }).toUpperCase();
            month = this.monthAfter(2);
            filtersForm.querySelector(selectors.moveInDateLabelNextMonth).innerHTML = month.toLocaleString('default', { month: 'short' }).toUpperCase();

            this.fetchUnits()
            filtersForm.addEventListener('submit', e => {
                e.preventDefault();
                const filters = new FormData(filtersForm);
                const data = {};
                for (var pair of filters.entries()) {
                    let moveInDate;
                    switch (pair[0]) {
                        case 'hometype': {
                            data['hometype'] = data['hometype'] || [];
                            data['hometype'].push(pair[1])
                            break;
                        }
                        case 'maxrent': {
                            data['maxrent'] = parseFloat(pair[1]);
                            break
                        }
                        case 'moveindate': {
                            switch (pair[1]) {
                                case 'today': {
                                    moveInDate = new Date();
                                    break;
                                }
                                case 'nextmonth': {
                                    moveInDate = this.monthAfter(2);
                                    moveInDate.setDate(0);
                                    break;
                                }
                                case 'next2month': {
                                    moveInDate = this.monthAfter(3);
                                    moveInDate.setDate(0);
                                    break;
                                }
                            }
                            data['moveindate'] = moveInDate;
                            break;
                        }
                    }
                }
                this.filterUnits(this.createFiltersFunction(data));
            });
           this.registerSortingEvents();
        }
    }
};

export default AvailabilityFeed;
