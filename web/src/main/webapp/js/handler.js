//main page loading on
window.addEventListener('load', () => {
    const container = document.getElementById("myDiv");
    const template = document.getElementById("mainTableTemplate").innerHTML;
    //const startPageUri = ""; //add index.html route
    //check current URI
    const currentUri = window.location.pathname;
    console.log("Current path: " + currentUri);
    fetch('contacts', {
        method: "GET",
        headers: new Headers({'content-type': 'application/json', "Test": "MyMethod"})
    }).then(function (response) {
        return response.json();
    }).then(function (myJson) {
        const html = Mustache.to_html(template, myJson);
        container.innerHTML = html;
        history.replaceState(null, "Contact page", "/contacts");
    }).catch(function (err) {
        alert("Unable to load main page.");
    });
});

function navigate(link, callback) {
    event.preventDefault();
    event.stopPropagation();
    let tmpl = "/templates/editContactTemplate.mst";
    let title = "Contact edit page";
    let photoLink = null;
    let gender = null;
    let marital = null;
    //fetch data
    const data = fetch(link, {
        method: "GET",
        headers: new Headers({'content-type': 'application/json'})
    }).then(function (response) {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Invalid response");
        }
    }).then(function (myJson) {
        // ignore warning, it works: contact is a nested object with attributes
        photoLink = myJson.contact.photoLink;
        gender = myJson.contact.gender;
        marital = myJson.contact.maritalStatus;
        var list = myJson.attachmentList;
        for (var i = 0; i < list.length; i++) {
            myJson.attachmentList[i].realFileName = myJson.attachmentList[i].link.split("---", 2)[1];
            console.log(myJson.attachmentList[i].realFileName);
        }
        return myJson;
    })
    //fetch template
    const template = fetch(tmpl).then(response => response.text());
    return Promise.all([data, template])
        .then(response => {
            let resolvedData = response[0];
            let resolvedTemplate = response[1];
            // Cache the template for future use
            Mustache.parse(resolvedTemplate);
            const html = Mustache.render(resolvedTemplate, resolvedData);
            // Write out the rendered template
            document.getElementById("myDiv").innerHTML = "";
            return document.getElementById('myDiv').innerHTML = html;
        }).then(function () {
            history.pushState(null, title, link);
            setSelectElementValue("gender", gender);
            setSelectElementValue("marital", marital);
            callback(photoLink);
        }).catch(function (err) {
            alert("Unable to render page with data");
        })
}

function displayContact(link, callback) {
    event.preventDefault();
    event.stopPropagation();
    let tmpl = "/templates/contactTemplate.mst";
    let title = "Contact page";
    let photoLink = null;
    let gender = null;
    let marital = null;
    //fetch data
    const data = fetch(link, {
        method: "GET",
        headers: new Headers({'content-type': 'application/json'})
    }).then(function (response) {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Invalid response");
        }
    }).then(function (myJson) {
        // ignore warning, it works: contact is a nested object with attributes
        photoLink = myJson.contact.photoLink;
        var list = myJson.attachmentList;
        for (var i = 0; i < list.length; i++) {
            myJson.attachmentList[i].realFileName = myJson.attachmentList[i].link.split("---", 2)[1];
            console.log(myJson.attachmentList[i].realFileName);
        }
        return myJson;
    })
    //fetch template
    const template = fetch(tmpl).then(response => response.text());
    return Promise.all([data, template])
        .then(response => {
            let resolvedData = response[0];
            let resolvedTemplate = response[1];
            // Cache the template for future use
            Mustache.parse(resolvedTemplate);
            const html = Mustache.render(resolvedTemplate, resolvedData);
            // Write out the rendered template
            document.getElementById("myDiv").innerHTML = "";
            return document.getElementById('myDiv').innerHTML = html;
        }).catch(function (err) {
            alert("Unable to render display contact page");
        }).then(function () {
            callback(photoLink);
        })
    history.pushState(null, title, link);

}

//set values of "select"
function setSelectElementValue(id, valueToSelect) {
    let element = document.getElementById(id);
    element.value = valueToSelect;
}

function retrieveImage(link) {
    console.log("The link in retrieve image is---" + link);
    var newLink = null;
    fetch("/contacts/image", {
        method: "GET",
        headers: new Headers({'fileLink': link})
    }).then(function (response) {
        if (response.ok) {
            response.blob().then(function (myBlob) {
                let objectURL = URL.createObjectURL(myBlob);
                newLink = objectURL;
                //var elem = document.getElementById("avatarka");
                let elem = document.getElementsByClassName("avatar");
                Array.prototype.forEach.call(elem, function (el) {
                    el.setAttribute("src", newLink);
                });
                //elem.item(0).setAttribute("src", newLink);
                // used Array for each to iterate although we should have only 1 element w/ id="avatar'
            });
        } else {
            console.log('Network response was not ok. Image exists but download failed.');
        }
    })
        .catch(function (error) {
            console.log('There has been a problem with your fetch operation: ' + error.message);
        });
}

function downloadFile(link) {
    event.preventDefault();
    event.stopPropagation();
    console.log("The link to download file is---" + link);
    var newLink = null;
    fetch("/contacts/file", {
        method: "GET",
        headers: new Headers({'Content-Type': 'application/json; charset=UTF-8', 'fileLink': encodeURI(link)})
    }).then(function (response) {
        if (response.ok) {
            response.blob().then(function (myBlob) {
                let objectURL = URL.createObjectURL(myBlob);
                newLink = objectURL;
                console.log("New link---" + newLink);
                //create new link with the new link to the file, click it to initiate file download and remove a
                let a = document.createElement("a");
                a.href = newLink;
                a.style.display = "none";
                let fileName = link.split("---", 2)[1];
                a.download = fileName;
                document.body.appendChild(a);
                a.click();
                a.remove();
            });
        } else {
            console.log('Network response was not ok. File download failed.');
        }
    })
        .catch(function (error) {
            console.log('There has been a problem with your fetch operation: ' + error.message);
        });
}

function openContactForm() {
    let form = null;
    //fetch template
    fetch("/templates/newContactTemplate.mst")
        .then(response => response.text())
        .then(res => {
            Mustache.parse(res);
            const html = Mustache.to_html(res);
            document.getElementById("myDiv").innerHTML = "";
            document.getElementById("myDiv").innerHTML = html;
        })
        .then(function (res) {
            form = document.getElementById("contactInfo");

            //FOR DISPLAY OF MODAL WINDOW --- PHONES
            let modalPhone = document.getElementById("myModalPhone");
            let createPhoneBtn = document.getElementById("createPhone");
            let resetPhoneBtn = document.getElementById("resetPhone");
            let savePhoneBtn = document.getElementById("savePhone");
            let newPhoneTable = document.getElementById("newPhoneTable");
            // When the user clicks the button, open the modal
            createPhoneBtn.onclick = function () {
                modalPhone.style.display = "flex";
            }

            savePhoneBtn.onclick = function () {
                savePhone(newPhoneTable, modalPhone);
            }

            resetPhoneBtn.onclick = function () {
                resetTable(newPhoneTable, modalPhone);
            }

            //FOR DISPLAY OF MODAL WINDOW --- ATTACHMENTS
            let modalAttach = document.getElementById("myModalAttachment");
            let createAttach = document.getElementById("createAttachment");
            let resetAttach = document.getElementById("resetAttachment");
            let saveAttach = document.getElementById("saveAttachment");
            let newAttachmentTable = document.getElementById("newAttachmentTable");
            // When the user clicks the button, open the modal
            createAttach.onclick = function () {
                modalAttach.style.display = "flex";
            }
            //reset the values and close modal window
            resetAttach.onclick = function () {
                resetTable(newAttachmentTable, modalAttach)
            }

            //save attachment into attachmentTable
            saveAttach.onclick = function () {
                saveAttachment(newAttachmentTable, modalAttach)
            }

            // When the user clicks anywhere outside of ANY modal, close it
            window.onclick = function (event) {
                if (event.target == modalPhone) {
                    resetTable(newPhoneTable, modalPhone);
                } else if (event.target == modalAttach) {
                    resetTable(newAttachmentTable, modalAttach);
                }
            }
        }).then(function (res) {
        let submitBtn = document.getElementById("submitContact");
        submitBtn.onclick = function () {
            submitForm(form);
        }
        history.pushState(null, "New contact page", "/contacts/new-form");
    }).catch(function (err) {
        alert("Unable to render new contact page");
    })
}

function submitForm(form) {
    event.preventDefault();
    event.stopPropagation();
    let formData = new FormData(form);

    //append phones to formData
    let phoneTable = document.getElementById("phoneTable");
    for (let i = 1; i < phoneTable.rows.length; i++) {
        for (let j = 1; j < phoneTable.rows[i].cells.length; j++) {
            let name = phoneTable.rows[i].cells[j].firstChild.name;
            let value = phoneTable.rows[i].cells[j].firstChild.value;
            let fullName = "phone." + i + "." + name;
            console.log(fullName + "---" + value);
            formData.append(fullName, value);
        }
    }

    //append attachments to formData
    let attachmentTable = document.getElementById("attachmentTable");
    for (let i = 1; i < attachmentTable.rows.length; i++) {
        let name = attachmentTable.rows[i].cells[1].firstChild.name;
        let value = attachmentTable.rows[i].cells[1].firstChild.value;
        let fullName = "attachment." + i + "." + name;
        formData.append(fullName, value);

        let userFileName = "attachment." + i + "." + "submittedFileName";
        //value will give fake path with filename
        let submittedName = attachmentTable.rows[i].cells[2].firstChild.value;
        formData.append(userFileName, submittedName);

        let fileName = attachmentTable.rows[i].cells[2].firstChild.name;
        let file = attachmentTable.rows[i].cells[2].firstChild.files[0];
        let fullFileName = "attachment." + i + "." + fileName;
        formData.append(fullFileName, file);
    }

    let photoLink;
    let contactId;

    const data = fetch("/contacts/", {
        method: 'POST',
        body: formData,
    }).then(function (response) {
        return response.json();
    }).then(function (myJson) {
        // ignore warning, it works: contact is a nested object with attributes
        photoLink = myJson.contact.photoLink;
        contactId = myJson.contact.id;
        let list = myJson.attachmentList;
        for (let i = 0; i < list.length; i++) {
            myJson.attachmentList[i].realFileName = myJson.attachmentList[i].link.split("---", 2)[1];
            console.log(myJson.attachmentList[i].realFileName);
        }
        return myJson;
    })

    let tmpl = "/templates/contactTemplate.mst";
    const template = fetch(tmpl).then(response => response.text());
    let title = "Contact page";
    let link = "/contacts/" +  contactId;

    return Promise.all([data, template])
        .then(response => {
            let resolvedData = response[0];
            let resolvedTemplate = response[1];
            // Cache the template for future use
            Mustache.parse(resolvedTemplate);
            const html = Mustache.render(resolvedTemplate, resolvedData);
            // Write out the rendered template
            let container = document.getElementById("myDiv");
            container.innerHTML = "";
            return container.innerHTML = html;
        }).then(function () {
            retrieveImage(photoLink);
            history.pushState(null, title, link);
        }).catch(function (err) {
            alert("Unable to render display contact page with new data");
        })
}

function openEditForm() {
    //Check how many checkboxes are checked. Only 1 should be checked, alert if not 1.
    var link;
    var checkedId;
    let path = window.location.pathname;
    let form = null;

    var prom = Promise.resolve()
        .then(function (res) {
            if (path.match(new RegExp("\\/contacts\\/\\d+"))) {
                checkedId = path.split("/")[1];
                console.log("ID---" + checkedId);
            } else {
                var checked = document.querySelectorAll(".check:checked");
                console.log("CHECKED OBJECT---" + checked);
                var checkedChecks = checked.length;
                console.log("CHECKED quantity---" + checkedChecks);
                console.log(checkedChecks + "boxes checked now");
                if (checkedChecks !== 1) {
                    alert("You can edit only one contact at a time! Please, check only one checkbox.");
                    return false;
                } else {
                    checkedId = checked.item(0).id;
                }
                link = "/contacts/" + checkedId + "/edit-form";
                console.log("BUILT LINK:" + link);
            }
        }).catch(function (err) {
            console.log(err.message);
        }).then(function () {
            return navigate(link, retrieveImage);
        })
    prom.then(function () {
        form = document.getElementById("editedContact");

        //FOR DISPLAY OF MODAL WINDOW
        // Get the modal
        let modalPhone = document.getElementById("myModalPhone");
        let createPhoneBtn = document.getElementById("createPhone");
        let resetPhoneBtn = document.getElementById("resetPhone");
        let savePhoneBtn = document.getElementById("savePhone");
        let newPhoneTable = document.getElementById("newPhoneTable");
        // When the user clicks the button, open the modal
        createPhoneBtn.onclick = function () {
            modalPhone.style.display = "flex";
        }

        savePhoneBtn.onclick = function () {
            savePhone(newPhoneTable, modalPhone);
        }

        resetPhoneBtn.onclick = function () {
            resetTable(newPhoneTable, modalPhone);
        }

        //FOR DISPLAY OF MODAL WINDOW --- ATTACHMENTS
        let modalAttach = document.getElementById("myModalAttachment");
        let createAttach = document.getElementById("createAttachment");
        let resetAttach = document.getElementById("resetAttachment");
        let saveAttach = document.getElementById("saveAttachment");
        let newAttachmentTable = document.getElementById("newAttachmentTable");
        // When the user clicks the button, open the modal
        createAttach.onclick = function () {
            modalAttach.style.display = "flex";
        }

        //save attachment into attachmentTable
        saveAttach.onclick = function () {
            saveAttachment(newAttachmentTable, modalAttach)
        }

        //reset the values and close modal window
        resetAttach.onclick = function () {
            resetTable(newAttachmentTable, modalAttach)
        }

        // When the user clicks anywhere outside of ANY modal, close it
        window.onclick = function (event) {
            if (event.target == modalPhone) {
                resetTable(newPhoneTable, modalPhone);
            } else if (event.target == modalAttach) {
                resetTable(newAttachmentTable, modalAttach);
            }
        }
    }).then(function (res) {
        let submitBtn = document.getElementById("submitContact");
        submitBtn.onclick = function () {
            submitEditForm(form, checkedId);
        }
        history.pushState(null, "Edit contact page", link);
    }).catch(function (err) {
        alert("Error on edit contact page");
    })
}

function submitEditForm(form, checkedId) {
    event.preventDefault();
    event.stopPropagation();
    let formData = new FormData(form);

    //append phones to formData
    let phoneTable = document.getElementById("phoneTable");
    for (let i = 1; i < phoneTable.rows.length; i++) {
        for (let j = 1; j < phoneTable.rows[i].cells.length; j++) {
            let name = phoneTable.rows[i].cells[j].firstChild.name;
            let value = phoneTable.rows[i].cells[j].firstChild.value;
            let fullName = "phone." + i + "." + name;
            console.log(fullName + "---" + value);
            formData.append(fullName, value);
        }
    }

    //append attachments to formData
    let attachmentTable = document.getElementById("attachmentTable");
    for (let i = 1; i < attachmentTable.rows.length; i++) {
        let name = attachmentTable.rows[i].cells[1].firstChild.name;
        let value = attachmentTable.rows[i].cells[1].firstChild.value;
        let fullName = "attachment." + i + "." + name;
        formData.append(fullName, value);

        let userFileName = "attachment." + i + "." + "submittedFileName";
        //value will give fake path with filename
        let submittedName = attachmentTable.rows[i].cells[2].childNodes[3].value;
        formData.append(userFileName, submittedName);

        let fileName = attachmentTable.rows[i].cells[2].childNodes[3].name;
        let file = attachmentTable.rows[i].cells[2].childNodes[3].files[0];
        let fullFileName = "attachment." + i + "." + fileName;
        formData.append(fullFileName, file);
    }

    let photoLink;
    let fetchLink = "/contacts/" + checkedId;

    const data = fetch(fetchLink, {
        method: 'POST',
        body: formData,
    }).then(function (response) {
        return response.json();
    }).then(function (myJson) {
        // ignore warning, it works: contact is a nested object with attributes
        photoLink = myJson.contact.photoLink;
        let list = myJson.attachmentList;
        for (let i = 0; i < list.length; i++) {
            myJson.attachmentList[i].realFileName = myJson.attachmentList[i].link.split("---", 2)[1];
            console.log(myJson.attachmentList[i].realFileName);
        }
        return myJson;
    })

    let tmpl = "/templates/contactTemplate.mst";
    const template = fetch(tmpl).then(response => response.text());
    let title = "Contact page";

    return Promise.all([data, template])
        .then(response => {
            let resolvedData = response[0];
            let resolvedTemplate = response[1];
            // Cache the template for future use
            Mustache.parse(resolvedTemplate);
            const html = Mustache.render(resolvedTemplate, resolvedData);
            // Write out the rendered template
            let container = document.getElementById("myDiv");
            container.innerHTML = "";
            return container.innerHTML = html;
        }).then(function () {
            retrieveImage(photoLink);
            history.pushState(null, title, fetchLink);
        }).catch(function (err) {
            alert("Unable to update contact with new data");
        })
}

function resetTable(tableName, modalWindow) {
    for (let i = 0; i < tableName.rows.length; i++) {
        // assign empty string into every input in the table
        tableName.rows[i].cells[1].firstChild.value = "";
    }
    modalWindow.style.display = "none";
}

function savePhone(newPhoneTable, modal) {
    //get filled in inputs from popup window
    let newCountryCode = document.getElementById("newCountryCode");
    let newOperatorCode = document.getElementById("newOperatorCode");
    let newPhoneNumber = document.getElementById("newPhoneNumber");
    let newPhoneType = document.getElementById("newPhoneType");
    let newComment = document.getElementById("newComment");

    //if any required field is empty, the row won't be saved
    if (newCountryCode.value === null || newOperatorCode.value === null || newPhoneNumber.value === null ||
        newCountryCode.value === "" || newOperatorCode.value === "" || newPhoneNumber.value === "") {
        return alert("Some fields are empty, can not save the phone number.");
    }

    //create inputs for phoneTable
    let checkbox = document.createElement("input");
    checkbox.setAttribute("type", "checkbox");

    //take table and insert a new row
    let table = document.getElementById("phoneTable");
    //when index in insertRow is omitted it is -1 by default, so the row appends as the last in the table
    let newRow = table.insertRow();
    newRow.insertCell(0).appendChild(checkbox);
    newRow.insertCell(1).appendChild(newCountryCode);
    newRow.insertCell(2).appendChild(newOperatorCode);
    newRow.insertCell(3).appendChild(newPhoneNumber);
    newRow.insertCell(4).appendChild(newPhoneType);
    newRow.insertCell(5).appendChild(newComment);

    //create empty inputs for modal window
    let inputCountryCode = document.createElement("input");
    inputCountryCode.setAttribute("type", "text");
    inputCountryCode.setAttribute("id", "newCountryCode");
    inputCountryCode.setAttribute("name", "countryCode");

    let inputOperatorCode = document.createElement("input");
    inputOperatorCode.setAttribute("type", "text");
    inputOperatorCode.setAttribute("id", "newOperatorCode");
    inputOperatorCode.setAttribute("name", "operatorCode");

    let inputPhoneNumber = document.createElement("input");
    inputPhoneNumber.setAttribute("type", "text");
    inputPhoneNumber.setAttribute("id", "newPhoneNumber");
    inputPhoneNumber.setAttribute("name", "phoneNumber");

    let inputPhoneType = document.createElement("select");
    inputPhoneType.setAttribute("id", "newPhoneType");
    inputPhoneType.setAttribute("name", "phoneType");
    inputPhoneType.options[0] = new Option("home", "home");
    inputPhoneType.options[1] = new Option("mobile", "mobile");

    let inputComment = document.createElement("input");
    inputComment.setAttribute("type", "text");
    inputComment.setAttribute("id", "newComment");
    inputComment.setAttribute("name", "comment");

    //append empty input fields to modal window
    document.getElementById("countryCodeHolder").appendChild(inputCountryCode);
    document.getElementById("operatorCodeHolder").appendChild(inputOperatorCode);
    document.getElementById("phoneNumberHolder").appendChild(inputPhoneNumber);
    document.getElementById("phoneTypeHolder").appendChild(inputPhoneType);
    document.getElementById("commentHolder").appendChild(inputComment);

    //hide modal window
    modal.style.display = "none";
}

function saveAttachment(newAttachmentTable, modal) {
    //get filled in values from popup window
    let newAttachmentName = document.getElementById("attachmentName");
    let newAttachmentLink = document.getElementById("attachmentLink");

    //if any required field is empty, the row won't be saved
    if (newAttachmentName.value === null || newAttachmentLink.value === null ||
        newAttachmentName.value === "" || newAttachmentLink.value === "") {
        return alert("Some fields are empty, can not save the attachment.");
    }

    //create inputs for attachmentTable
    let checkbox = document.createElement("input");
    checkbox.setAttribute("type", "checkbox");

    //take table and insert a new row
    let table = document.getElementById("attachmentTable");
    //when index in insertRow is omitted it is -1 by default, so the row appends as the last in the table
    let newRow = table.insertRow();
    newRow.insertCell(0).appendChild(checkbox);
    newRow.insertCell(1).appendChild(newAttachmentName);
    newRow.insertCell(2).appendChild(newAttachmentLink);

    //create new input fields for modal window, as the existing ones are appended to attachmentTable
    let inputAttachmentName = document.createElement("input");
    inputAttachmentName.setAttribute("type", "text");
    inputAttachmentName.setAttribute("id", "attachmentName");
    inputAttachmentName.setAttribute("name", "attachmentName");

    let inputAttachmentLink = document.createElement("input");
    inputAttachmentLink.setAttribute("type", "file");
    inputAttachmentLink.setAttribute("id", "attachmentLink");
    inputAttachmentLink.setAttribute("name", "attachmentLink");

    //append empty input fields to modal window
    document.getElementById("attachmentNameHolder").appendChild(inputAttachmentName);
    document.getElementById("attachmentLinkHolder").appendChild(inputAttachmentLink);

    //hide modal window
    modal.style.display = "none";
}

function deleteContact() {
    var currentPath = window.location.pathname;
    console.log("Current pathname is --- " + currentPath);
    var idArray = [];
    if (new RegExp("^(\\/contacts)$").test(currentPath)) {
        //checkboxes are checked and delete button is pressed
        console.log("General case");
        var checked = document.querySelectorAll(".check:checked");
        console.log("CHECKED OBJECT---" + checked);
        for (let i = 0; i < checked.length; i++) {
            idArray.push(checked[i].id);
        }
    } else if (new RegExp("^(\\/contacts\\/\\d+)$").test(currentPath)) {
        //delete button pressed on the page of individual contact
        console.log("Particular case");
        idArray.push(currentPath.split("/")[2]);
    }
    fetch(currentPath, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json', 'IdToDelete': idArray}
    }).then(function (response) {
        console.log("Deleted contact from its page");
        return response.json();
    }).then(function (myJson) {
        console.log("Received reply:" + myJson);
        var tmpl = document.getElementById("mainTableTemplate").innerHTML;
        var html = Mustache.to_html(tmpl, myJson);
        var container = document.getElementById("myDiv");
        container.innerHTML = html;
        history.pushState(null, "Contact page", "/contacts");
    })
}


