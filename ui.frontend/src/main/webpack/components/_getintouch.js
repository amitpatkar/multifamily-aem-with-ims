const GetInTouch = {
    selectors: {
        self: '[data-cmp-component="getintouch"]',
        contactForm: 'form',
        firstName: "#first_name",
        lastName: "#last_name",
        emailAddress: '#email_address',
        phoneNumber: '#phone_number',
        formErrorMessage: '#get_in_touch_error',
        checkElements: 'input[type="checkbox"], input[type="radio"]',
        checkBoxes: 'input[type="checkbox"]',
        postSubmissionContents: '.cmp-get-in-touch__postsubmission',
        confirmationName: '.cmp-get-in-touch__confirmation--name',
        moveInDateMonthAfter1: '#move_in_date_month_1',
        moveInDateMonthAfter2: '#move_in_date_month_2',
    },
    validateForm: function(firstName, lastName, emailAddress, phoneNumber) {
        let isFirstNameEmpty = true;
        let isLastNameEmpty = true;
        let isEmailEmpty = true;
        let isPhoneNumberEmpty = true;
        let isFormInvalid = true;
        if (firstName.value && firstName.value.trim()) {
            isFirstNameEmpty = false;
        }
        if (lastName.value && lastName.value.trim()) {
            isLastNameEmpty = false;
        }
        if (emailAddress.value && emailAddress.value.trim()) {
            isEmailEmpty = false;
        }
        if (phoneNumber.value && phoneNumber.value.trim()) {
            isPhoneNumberEmpty = false;
        }
        isFormInvalid = isFirstNameEmpty || isFirstNameEmpty || isEmailEmpty || isPhoneNumberEmpty;
        return {
            isFirstNameEmpty,
            isLastNameEmpty,
            isEmailEmpty,
            isPhoneNumberEmpty,
            isFormInvalid
        };
    },
    isInNodeList: function(node, nodeList) {
        const nodeArray = Array.prototype.slice.call(nodeList);
        return nodeArray.indexOf(node) > -1;
    },
    init: function() {
        console.log('Get In Touch init!');
        const {selectors} = this;
        const getInTouchSection = document.querySelector(selectors.self);
        const contactForm = getInTouchSection.querySelector(selectors.contactForm);
        const firstName = document.querySelector(selectors.firstName);
        const lastName = document.querySelector(selectors.lastName);
        const emailAddress = document.querySelector(selectors.emailAddress);
        const phoneNumber = document.querySelector(selectors.phoneNumber);
        const formError = document.querySelector(selectors.formErrorMessage);
        const checkElements = contactForm.querySelectorAll(selectors.checkElements);
        const postSubmissionContents = document.querySelector(selectors.postSubmissionContents);
        const confirmationName = document.querySelector(selectors.confirmationName);
        const moveInDateMonthAfter1 = document.querySelector(selectors.moveInDateMonthAfter1);
        const moveInDateMonthAfter2 = document.querySelector(selectors.moveInDateMonthAfter2);
        let submissionFailed = false;

        function formatDate(date) {
            return `${date.getDate()}-${date.getMonth()+1}-${date.getFullYear()}`
        }

        function formatURL() {
            const parts = digitalData.pageInfo.currentPagePath.replace("/jcr:content", "").split("/");
            return `${parts[parts.length - 1]}.contactus.html`;
        }

        function monthAfter(num) {
            const d = new Date();
            d.setDate(1);
            d.setMonth(d.getMonth()+num);
            return d;
        }

        let month = monthAfter(1);
        moveInDateMonthAfter1.innerHTML = month.toLocaleString('default', { month: 'short' }).toUpperCase();
        month = monthAfter(2);
        moveInDateMonthAfter2.innerHTML = month.toLocaleString('default', { month: 'short' }).toUpperCase();

        //Form submission
        contactForm.onsubmit = (e) => {
            e.preventDefault();
            const result = this.validateForm(firstName, lastName, emailAddress, phoneNumber);
            if (result.isFormInvalid) {
                if (result.isFirstNameEmpty) {
                    firstName.classList.add("invalid");
                    firstName.nextElementSibling.classList.remove("hidden");
                }
                if (result.isLastNameEmpty) {
                    lastName.classList.add("invalid");
                    lastName.nextElementSibling.classList.remove("hidden");
                }
                if (result.isEmailEmpty) {
                    emailAddress.classList.add("invalid");
                    emailAddress.nextElementSibling.classList.remove("hidden");
                }
                if (result.isPhoneNumberEmpty) {
                    phoneNumber.classList.add("invalid");
                    phoneNumber.nextElementSibling.classList.remove("hidden");
                }
                formError.classList.remove("hidden");
                submissionFailed = true;
                return false;
            } else {
                submissionFailed = false;
                const contactFormData = new FormData(contactForm);
                const data = {};
                for (var pair of contactFormData.entries()) {
                    data[pair[0]] = pair[1]
                }
                let moveInDate;
                switch (data['move_in_date']) {
                    case 'today':
                        moveInDate = new Date();
                        break;
                    case 'nextmonth':
                        moveInDate = monthAfter(2);
                        moveInDate.setDate(0);
                        break;
                    case 'next2month':
                        moveInDate = monthAfter(3);
                        moveInDate.setDate(0);
                        break;
                }
                if (moveInDate) {
                    data['move_in_date'] = formatDate(moveInDate)
                } else {
                    delete data['move_in_date']
                }

                const homeTypeKeys = ['studio', 'onebedroom', 'twobedroom', 'penthouse', 'townhome'];
                data['bedrooms'] = homeTypeKeys.filter(type => data[type]).map(type => data[type]).join(', ');
                homeTypeKeys.forEach(type => delete data[type]);

                console.log(data);
                confirmationName.innerText = data['first_name']
                contactForm.classList.add("hidden");
                postSubmissionContents.classList.remove("hidden");
                fetch('/libs/granite/csrf/token.json')
                    .then(resp => resp.json())
                    .then(data => data.token)
                    .then(token => fetch(formatURL(), {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                "CSRF-Token": token,
                            },
                            body: JSON.stringify(data)
                        })
                    ).catch(err => {
                    console.error(err)
                })
                //contactForm.submit();
            }
        };
        //Remove invalid state of name/email if a user corrects the field error.
        //Remove form error message if a user corrects all of the field errors.
        firstName.addEventListener('blur', function(){
            if (submissionFailed && firstName.value && firstName.value.trim()) {
                firstName.classList.remove("invalid");
                firstName.nextElementSibling.classList.add("hidden");
                if (!(emailAddress.classList.contains("invalid") || lastName.classList.contains("invalid") || phoneNumber.classList.contains("invalid"))){
                    formError.classList.add("hidden");
                }
            }
        });
        lastName.addEventListener('blur', function(){
            if (submissionFailed && lastName.value && lastName.value.trim()) {
                lastName.classList.remove("invalid");
                lastName.nextElementSibling.classList.add("hidden");
                if (!(emailAddress.classList.contains("invalid") || firstName.classList.contains("invalid") || phoneNumber.classList.contains("invalid"))){
                    formError.classList.add("hidden");
                }
            }
        });
        emailAddress.addEventListener('blur', function(){
            if (submissionFailed && emailAddress.value && emailAddress.value.trim()) {
                emailAddress.classList.remove("invalid");
                emailAddress.nextElementSibling.classList.add("hidden");
                if (!(lastName.classList.contains("invalid") || firstName.classList.contains("invalid") || phoneNumber.classList.contains("invalid"))){
                    formError.classList.add("hidden");
                }
            }
        });
        phoneNumber.addEventListener('blur', function(){
            if (submissionFailed && phoneNumber.value && phoneNumber.value.trim()) {
                phoneNumber.classList.remove("invalid");
                phoneNumber.nextElementSibling.classList.add("hidden");
                if (!(lastName.classList.contains("invalid") || firstName.classList.contains("invalid") || emailAddress.classList.contains("invalid"))){
                    formError.classList.add("hidden");
                }
            }
        });
        checkElements.forEach(el => {
            el.addEventListener('change', function(event){
                const closestDiv = el.closest(".checkbox-item, .radio-item");
                if (event.target.checked) {
                    closestDiv.classList.add('checked'); 
                } else {
                    closestDiv.classList.remove('checked');
                }
            });
        });
    }

}

export default GetInTouch