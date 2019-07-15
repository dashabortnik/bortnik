const regexEmail = new RegExp("[a-zA-Z0-9_\\.\\+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-\\.]+");
const regexPostcode = new RegExp("^\\d{5,6}(?:[-\\s]\\d{4})?$");
const regexCountryCode = new RegExp("^\\+?[0-9]+$");
const regexOperatorCode = new RegExp("^\\(?[0-9]+\\)?$");
const regexPhoneNumber = new RegExp("^[0-9-]+$");

function validate(formData) {

    let errorArray = [];
    let requiredFields = ["surname", "name", "birthday", "email"];
    let limit30Chars = ["surname", "name", "patronymic"];
    let limit50Chars = ["nationality", "website", "email", "workplace", "country", "city"];

    for (let pair of formData.entries()) {
        let key = pair[0];
        let value = pair[1];
        //check if empty
        if (requiredFields.indexOf(key) >= 0) {
            checkIfEmpty(key, value, errorArray);
        }

        if (key === "email" && value !== "") {
            if (!regexEmail.test(value)) {
                let error = "Field " + key + " is invalid! Please, enter valid data.";
                errorArray.push(error);
            }
        }

        if (key === "postcode" && value !== "") {
            if (!regexPostcode.test(value)) {
                let error = "Field " + key + " is invalid! Please, enter valid data";
                errorArray.push(error);
            } else if (value.length > 15) {
                let error = "Inserted " + key + " is too long! Please, use up to 15 characters.";
                errorArray.push(error);
            }
        }

        if (limit30Chars.indexOf(key) >= 0 && value !== "") {
            if (value.length > 30) {
                let error = "Inserted " + key + " is too long! Please, use up to 30 characters.";
                errorArray.push(error);
            }
        }

        if (limit50Chars.indexOf(key) >= 0 && value !== "") {
            if (value.length > 50) {
                let error = "Inserted " + key + " is too long! Please, use up to 50 characters.";
                errorArray.push(error);
            }
        }

        if (key === "street" && value !== "") {
            if (value.length > 100) {
                let error = "Inserted address is too long! Please, use up to 100 characters.";
                errorArray.push(error);
            }
        }
    }

    if (errorArray.length !== 0) {
        alert(errorArray.join("\n"));
        return false;
    } else {
        return true;
    }
}

function checkIfEmpty(key, value, errorArray) {
    if (value === "") {
        let error = "Field " + key + " should not be empty!";
        errorArray.push(error);
    }
}

function validatePhone(newCountryCode, newOperatorCode, newPhoneNumber, newComment) {
    let errorPhone = [];
    //if any required field is empty, the row won't be saved
    if (newCountryCode.value === null || newOperatorCode.value === null || newPhoneNumber.value === null ||
        newCountryCode.value === "" || newOperatorCode.value === "" || newPhoneNumber.value === "") {
        let error = "Country code, operator code, phone number are required!";
        errorPhone.push(error);
    }

    if (!regexCountryCode.test(newCountryCode.value)) {
        let error = "Inserted country code is invalid. Please, enter valid data.";
        errorPhone.push(error);
    } else if (newCountryCode.value.length > 6) {
        let error = "Inserted country code is too long. Please, enter up to 6 digits.";
        errorPhone.push(error);
    }

    if (!regexOperatorCode.test(newOperatorCode.value)) {
        let error = "Inserted operator code is invalid. Please, enter valid data.";
        errorPhone.push(error);
    } else if (newOperatorCode.value.length > 5) {
        let error = "Inserted operator code is too long. Please, enter up to 5 digits.";
        errorPhone.push(error);
    }


    if (!regexPhoneNumber.test(newPhoneNumber.value)) {
        let error = "Phone number contains invalid symbols. Please, enter valid data.";
        errorPhone.push(error);
    } else if (newPhoneNumber.value.length > 11) {
        let error = "Inserted phone number is too long. Please, enter up to 11 digits.";
        errorPhone.push(error);
    }

    if (errorPhone.length !== 0) {
        alert(errorPhone.join("\n"));
        return false;
    } else {
        return true;
    }

}

function validateAttachment(newAttachmentName, newAttachmentLink) {
    let errorAttachment = [];
    //if any required field is empty, the row won't be saved
    if (newAttachmentName.value === null || newAttachmentLink.value === null ||
        newAttachmentName.value === "" || newAttachmentLink.value === "") {
        let error = "Attachment name and attachment link are required!";
        errorAttachment.push(error);
    }
    if (newAttachmentName.value.length > 100) {
        let error = "Inserted attachment name is too long. Please, enter up to 100 digits.";
        errorAttachment.push(error);
    }
    if (errorAttachment.length !== 0) {
        alert(errorAttachment.join("\n"));
        return false;
    } else {
        return true;
    }

}