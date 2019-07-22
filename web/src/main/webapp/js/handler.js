//js controller: checks currentURI and invokes corresponding function to display requested page
window.addEventListener('load', () => {
    //check current URI
    const currentUri = window.location.pathname;
    console.log("Current path: " + currentUri);
    let stringSplitter = "brt/";
    if (currentUri.match(new RegExp("^(\\/brt\\/contacts\\/\\d+\\/*)$"))) {
        let pathParts = currentUri.split(stringSplitter);
        let link = pathParts[0] + stringSplitter + "api/" + pathParts[1];
        //display requested contact page
        displayContact(link, retrieveImage);
    } else if (currentUri.match(new RegExp("^(\\/brt\\/contacts\\/\\d+\\/edit-form\\/*)$"))) {
        let pathParts = currentUri.split(stringSplitter);
        let link = pathParts[0] + stringSplitter + "api/" + pathParts[1];
        //display edit contact page
        navigate(link, retrieveImage);
    } else if (currentUri.match(new RegExp("^(\\/brt\\/contacts\\/new-form\\/*)$"))) {
        //display new contact form
        openContactForm();
    } else if (currentUri.match(new RegExp("^(\\/brt\\/contacts\\/search\\/?)$"))) {
        openSearchPage();
    } else {
        //display main page with default page number and pageSize
        let page = 1;
        let pageSize = 10;

        let urlParams = new URLSearchParams(window.location.search);
        if (urlParams !== null) {
            let newPage = urlParams.get('page');
            let newPageSize = urlParams.get('pageSize');
            if (newPage !== "" && newPage !== null) {
                page = newPage;
            }
            if (newPageSize !== "" && newPageSize !== null) {
                pageSize = newPageSize;
            }
        }
        displayMainPage(page, pageSize);
    }
});

window.addEventListener('popstate', function (event) {
    location.reload();
});

function displayMainPage(page, pageSize) {
    event.preventDefault();
    let sizeArray = ["5", "10", "15", "20"];
    if (sizeArray.indexOf(pageSize) < 0) {
        pageSize = sizeArray[1];
    }
    let link = "/brt/api/contacts/?page=" + page + "&pageSize=" + pageSize;

    const container = document.getElementById("myDiv");
    const template = document.getElementById("mainTableTemplate").innerHTML;
    fetch(link, {
        method: "GET",
        headers: new Headers({'content-type': 'application/json'})
    }).then(function (response) {
        return response.json();
    }).then(function (myJson) {
        const html = Mustache.to_html(template, myJson);
        container.innerHTML = html;
        return myJson;
    }).then(function (myJson) {
        let navContainer = document.getElementById("navContainer");
        let nextBtnContainer = document.getElementById("nextBtnContainer");
        let maxPage = myJson.totalSize;

        if (+page <= 1) {
            page = 1;
            document.getElementById("prevBtnContainer").setAttribute("class", "page-item disabled");
            document.getElementById("btn_prev").setAttribute("tabindex", "-1");
        }
        if (+page >= maxPage) {
            page = maxPage;
            document.getElementById("nextBtnContainer").setAttribute("class", "page-item disabled");
            document.getElementById("btn_next").setAttribute("tabindex", "-1");
        }

        let pageSizeSelect = document.getElementById("pageSize");
        pageSizeSelect.value = pageSize;
        for (let i = 1; i <= maxPage; i++) {
            let li = document.createElement('li');
            let a = document.createElement('a');
            if (i === +page) {
                li.setAttribute("class", "page-item disabled");
                a.setAttribute("class", "page-link active");
                a.setAttribute("tabindex", "-1")
            } else {
                li.setAttribute("class", "page-item");
                a.setAttribute("class", "page-link");
            }
            a.setAttribute("id", i.toString());
            a.setAttribute("href", "");
            a.onclick = function () {
                displayMainPage(this.id, pageSizeSelect.value);
            }
            let linkText = document.createTextNode(i.toString());
            a.appendChild(linkText);
            li.appendChild(a);
            navContainer.insertBefore(li, nextBtnContainer);
        }

        pageSizeSelect.onchange = function () {
            page = 1;
            displayMainPage(page, pageSizeSelect.value);
        }

        document.getElementById("btn_prev").onclick = function () {
            displayMainPage(+page - 1, document.getElementById("pageSize").value);
        }

        document.getElementById("btn_next").onclick = function () {
            displayMainPage(+page + 1, document.getElementById("pageSize").value);
        }

        let historyLink = "/brt/contacts/?page=" + page + "&pageSize=" + pageSize;
        history.pushState(null, "Display main page", historyLink);
    }).catch(function (err) {
        alert("Unable to load main page.");
    });
}

function navigate(link, callback) {
    event.preventDefault();
    event.stopPropagation();
    let tmpl = "/templates/editContactTemplate.mst";
    let title = "Contact edit page";
    let photoLink = null;
    let gender = null;
    let marital = null;
    let linkParts = link.split("api/");
    let historyLink = linkParts[0] + linkParts[1];
    console.log(historyLink + "--- formed link");
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
            history.pushState(null, "Edit contact page", historyLink);
            setSelectElementValue("gender", gender);
            setSelectElementValue("marital", marital);
            callback(photoLink);
        }).catch(function (err) {
            alert("Unable to render page with data");
        })
}

//display details about 1 chosen contact
function displayContact(link, callback) {
    event.preventDefault();
    event.stopPropagation();
    let tmpl = "/templates/contactTemplate.mst";
    let title = "Contact page";
    let pathParts = link.split("api/");
    let historyLink = pathParts[0] + pathParts[1];
    let photoLink = null;
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
            history.pushState(null, "Display contact page", historyLink);
            callback(photoLink);
        })
}

//set values of "select"
function setSelectElementValue(id, valueToSelect) {
    let element = document.getElementById(id);
    element.value = valueToSelect;
}

//fetch image for chosen contact
function retrieveImage(link) {
    console.log("The link in retrieve image is---" + link);
    var newLink = null;
    fetch("/brt/api/contacts/image", {
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

//download displayed attachment
function downloadFile(link) {
    event.preventDefault();
    event.stopPropagation();
    console.log("The link to download file is---" + link);
    var newLink = null;
    fetch("/brt/api/contacts/file", {
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

//open form for a new contact
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

            let deletePhoneBtn = document.getElementById("deletePhone");
            let deleteAttachmentBtn = document.getElementById("deleteAttachment");

            //delete chosen phones
            deletePhoneBtn.onclick = function () {
                deleteCheckedRows("phoneTable");
            }

            //delete chosen attachments
            deleteAttachmentBtn.onclick = function () {
                deleteCheckedRows("attachmentTable");
            }

        }).then(function (res) {
        let submitBtn = document.getElementById("submitContact");
        submitBtn.onclick = function () {
            submitForm(form);
        }
        history.pushState(null, "New contact page", "/brt/contacts/new-form");
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

    if (validate(formData)) {
        let photoLink;
        let contactId;
        let title;
        let historyLink;
        let tmpl = "";

        const data = fetch("/brt/api/contacts/", {
            method: 'POST',
            body: formData,
        }).then(function (response) {
            return response.json();
        }).then(function (myJson) {
            if (myJson.errorList != null) {
                tmpl = "/templates/errorPageTemplate.mst";
                historyLink = "/brt/contacts/error";
                title = "Error page";
            } else {
                // ignore warning, it works: contact is a nested object with attributes
                tmpl = "/templates/contactTemplate.mst";
                photoLink = myJson.contact.photoLink;
                contactId = myJson.contact.id;
                historyLink = "/brt/contacts/" + contactId;
                title = "Contact page";
                let list = myJson.attachmentList;
                for (let i = 0; i < list.length; i++) {
                    myJson.attachmentList[i].realFileName = myJson.attachmentList[i].link.split("---", 2)[1];
                    console.log(myJson.attachmentList[i].realFileName);
                }
            }
            let template = fetch(tmpl)
                .then(function (response){
                    return response.text();
                }).then(function (myText){
                    Mustache.parse(myText);
                    const html = Mustache.render(myText, myJson);
                    // Write out the rendered template
                    let container = document.getElementById("myDiv");
                    container.innerHTML = "";
                    return container.innerHTML = html;
                }).then(function (html){
                    history.pushState(null, title, historyLink);
                    if(title==="Contact page"){
                        retrieveImage(photoLink);
                    }
                });
        });
    } else {
        alert("Unable to render display contact page with new data");
    }
}

function openEditForm() {
    //Check how many checkboxes are checked. Only 1 should be checked, alert if not 1.
    let link;
    let checkedId;
    let path = window.location.pathname;
    let form = null;

    let prom = Promise.resolve()
        .then(function (res) {
            if (path.match(new RegExp("\\/brt\\/contacts\\/\\d+"))) {
                checkedId = path.split("/")[3];
                console.log("ID---" + checkedId);
            } else {
                let checked = document.querySelectorAll(".check:checked");
                console.log("CHECKED OBJECT---" + checked);
                let checkedChecks = checked.length;
                console.log("CHECKED quantity---" + checkedChecks);
                console.log(checkedChecks + "boxes checked now");
                if (checkedChecks !== 1) {
                    return alert("You can edit only one contact at a time! Please, check only one checkbox.");
                } else {
                    checkedId = checked.item(0).id;
                }
            }
            link = "/brt/api/contacts/" + checkedId + "/edit-form";
            console.log("BUILT LINK:" + link);
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

        let deletePhoneBtn = document.getElementById("deletePhone");
        let deleteAttachmentBtn = document.getElementById("deleteAttachment");

        //delete chosen phones
        deletePhoneBtn.onclick = function () {
            deleteCheckedRows("phoneTable");
        }

        //delete chosen attachments
        deleteAttachmentBtn.onclick = function () {
            deleteCheckedRows("attachmentTable");
        }

    }).then(function (res) {
        let submitBtn = document.getElementById("submitContact");
        submitBtn.onclick = function () {
            submitEditForm(form, checkedId);
        }
        //history.pushState(null, "Edit contact page", link);
    }).catch(function (err) {
        alert("Error on edit contact page");
    })
}

function submitEditForm(form, checkedId){
    event.preventDefault();
    event.stopPropagation();
    let formData = new FormData(form);

    //append phones to formData
    let phoneTable = document.getElementById("phoneTable");
    for (let i = 1; i < phoneTable.rows.length; i++) {

        //save phone id
        let phoneId = phoneTable.rows[i].cells[0].firstChild.id;
        let phoneIdName = "phone." + i + ".id";
        formData.append(phoneIdName, phoneId);

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

        //save attachment id
        let attachmentId = attachmentTable.rows[i].cells[0].firstChild.id;
        let attachmentIdName = "attachment." + i + ".id";
        formData.append(attachmentIdName, attachmentId);

        let name = attachmentTable.rows[i].cells[1].firstChild.name;
        let value = attachmentTable.rows[i].cells[1].firstChild.value;
        let fullName = "attachment." + i + "." + name;
        formData.append(fullName, value);
        console.log(fullName + "---" + value);

        let userFileName = "attachment." + i + "." + "submittedFileName";
        //value will give fake path with filename
        let submittedName = attachmentTable.rows[i].cells[2].getElementsByTagName("input")[0].value;
        formData.append(userFileName, submittedName);
        console.log(userFileName + "---" + submittedName);

        let fileName = attachmentTable.rows[i].cells[2].getElementsByTagName("input")[0].name;
        let file;
        // if(!!fileName){
            file = attachmentTable.rows[i].cells[2].getElementsByTagName("input")[0].files[0];
        // } else {
        //     file = null;
        // }
        let fullFileName = "attachment." + i + "." + fileName;
        formData.append(fullFileName, file);
        console.log(fullFileName+ "---" + file);
    }

    // for(let pair of formData.entries()) {
    //     console.log(pair[0]+ ', '+ pair[1]);
    //     if(pair[1]===undefined ){
    //         formData.set(pair[0], null);
    //     }
    // }

    let photoLink;
    let fetchLink = "/brt/api/contacts/" + checkedId;

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
            history.pushState(null, "Display contact after edit page", fetchLink);
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

    if (validatePhone(newCountryCode, newOperatorCode, newPhoneNumber, newComment)) {
        //create inputs for phoneTable
        let checkbox = document.createElement("input");
        checkbox.setAttribute("type", "checkbox");
        checkbox.setAttribute("class", "phoneCheck");

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
}

function saveAttachment(newAttachmentTable, modal) {
    //get filled in values from popup window
    let newAttachmentName = document.getElementById("attachmentName");
    let newAttachmentLink = document.getElementById("attachmentLink");

    if (validateAttachment(newAttachmentName, newAttachmentLink)) {

        //create inputs for attachmentTable
        let checkbox = document.createElement("input");
        checkbox.setAttribute("type", "checkbox");
        checkbox.setAttribute("class", "attachmentCheck");

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
}

function deleteContact() {
    var currentPath = window.location.pathname;
    console.log("Current pathname is --- " + currentPath);
    var idArray = [];
    let tail = "";
    if (new RegExp("^(\\/brt\\/contacts\\/)$").test(currentPath)) {
        //checkboxes are checked and delete button is pressed
        console.log("General case");
        tail = "/contacts";
        let checked = document.querySelectorAll(".check:checked");
        console.log("CHECKED OBJECT---" + checked);
        for (let i = 0; i < checked.length; i++) {
            idArray.push(checked[i].id);
        }
    } else if (new RegExp("^(\\/brt\\/contacts\\/\\d+.*)").test(currentPath)) {
        //delete button pressed on the page of individual contact
        console.log("Particular case");
        let array = currentPath.split("/");
        console.log("SPLIT ARRAY: " + array.toString());
        idArray.push(array[3]);
        tail = "/contacts/" + array[3];
    } else {
        alert("Please, choose contact(s) to delete.");
        return false;
    }
    currentPath = "/brt/api" + tail;
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
        history.pushState(null, "Display main page", "/brt/contacts");
    })
}

function deleteCheckedRows(tableId) {
    let table = document.getElementById(tableId);
    let rowCount = table.rows.length;

    for (let i = 0; i < rowCount; i++) {
        let row = table.rows[i];
        var checkbox = row.cells[0].childNodes[0];
        if (null != checkbox && true == checkbox.checked) {
            table.deleteRow(i);
            rowCount--;
            i--;
        }
    }
}

function sendEmail() {

    let currentPath = window.location.pathname;
    console.log("Current pathname is --- " + currentPath);
    let idArray = [];

    if (new RegExp("^(\\/brt\\/contacts\\/)$").test(currentPath)) {
        //checkboxes are checked and delete button is pressed
        console.log("General case");
        let checked = document.querySelectorAll(".check:checked");
        console.log("CHECKED OBJECT---" + checked);
        for (let i = 0; i < checked.length; i++) {
            idArray.push(checked[i].id);
        }
    } else if (new RegExp("^(\\/brt\\/contacts\\/\\d+.*)").test(currentPath)) {
        //delete button pressed on the page of individual contact
        console.log("Particular case");
        let array = currentPath.split("/");
        console.log(array.toString());
        idArray.push(array[3]);
    } else {
        alert("Please, choose contact(s) to send emails to.");
        return false;
    }

    if (idArray.length === 0) {
        alert("Please, choose contact(s) to send emails to.");
        return false;
    }

    let emailList = "";

    const data = fetch("/brt/api/contacts/email", {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'IdForEmails': idArray}
    }).then(function (response) {
        console.log("Received emails");
        return response.json();
    }).then(function (myJson) {
        emailList = myJson.emailList;
        console.log("EmailList:" + emailList);
    })

    const tmpl = fetch("/templates/sendEmailsTemplate.mst")
        .then(response => response.text())

    Promise.all([data, tmpl])
        .then(response => {
            let resolvedTemplate = response[1];
            // Cache the template for future use
            Mustache.parse(resolvedTemplate);
            const html = Mustache.render(resolvedTemplate);
            // Write out the rendered template
            document.getElementById("myDiv").innerHTML = "";
            return document.getElementById('myDiv').innerHTML = html;
        }).then(function (res) {
        let p = document.getElementById("sendEmailTo");
        let node = document.createTextNode(emailList);
        p.appendChild(node);
        let templateSelect = document.getElementById("emailTemplate");
        templateSelect.onchange = function () {
            let tmplValue = templateSelect.value;
            if (tmplValue !== "none") {
                fetch("/brt/api/contacts/template", {
                    method: 'GET',
                    headers: {'Content-Type': 'application/json', 'template': templateSelect.value}
                }).then(function (response) {
                    console.log("Received email template");
                    return response.text();
                }).then(function (myText) {
                    let emailTemplate = myText;
                    emailTemplate = emailTemplate.replace(new RegExp('\\\\r\\\\n', 'g'), "\n");
                    console.log("EmailTemplate:" + emailTemplate);
                    document.getElementById("emailBody").value = emailTemplate;
                })
            } else {
                document.getElementById("emailBody").value = "";
            }
            history.pushState(null, "Send email page", "/brt/contacts/email");
        }
        //submit email
        let sendEmailBtn = document.getElementById("sendEmail");
        sendEmailBtn.onclick = function () {
            let formData = new FormData();
            formData.append("emails", emailList);
            formData.append("topic", document.getElementById("topic").value);
            formData.append("body", document.getElementById("emailBody").value);

            const data = fetch("/brt/api/contacts/email", {
                method: 'POST',
                body: formData,
            }).then(function (response) {
                return response.json();
            }).then(function (myJson) {
                let tmpl = document.getElementById("mainTableTemplate").innerHTML;
                let html = Mustache.to_html(tmpl, myJson);
                let container = document.getElementById("myDiv");
                container.innerHTML = html;
                history.pushState(null, "Display main page", "/brt/contacts");
            })
        }
    }).catch(function (err) {
        alert("Unable to render email page with data");
    })
}

function openSearchPage() {
    fetch("/templates/searchTemplate.mst")
        .then(response => response.text())
        .then(res => {
            Mustache.parse(res);
            const html = Mustache.to_html(res);
            document.getElementById("myDiv").innerHTML = "";
            return document.getElementById("myDiv").innerHTML = html;
        }).then(function (res) {
        history.pushState(null, "Search contact page", "/brt/contacts/search");
        let submitBtn = document.getElementById("searchForContact");
        let form = document.getElementById("searchContactInfo");
        submitBtn.onclick = function (e) {
            e.preventDefault();
            e.stopPropagation();
            //submitSearchForm(form);
            let formData = new FormData(form);
            if (formData.entries().next.done) { //true if formData is empty
                alert("Please, enter at least one search parameter!");
            } else {
                displayFoundContacts(formData, 1, 10);
            }
        }
    })
}

function displayFoundContacts(formData, page, pageSize) {
    event.preventDefault();

    const template = fetch("/templates/searchResultsTemplate.mst")
        .then(response => response.text());

    let sizeArray = ["5", "10", "15", "20"];
    if (sizeArray.indexOf(pageSize) < 0) {
        pageSize = sizeArray[1];
    }

    let link = "/brt/api/contacts/search/?page=" + page + "&pageSize=" + pageSize;
    let historyLink = "/brt/contacts/search/?page=" + page + "&pageSize=" + pageSize;
    let maxPage;
    let searchData;

    const data = fetch(link, {
        method: 'POST',
        body: formData,
    }).then(function (response) {
        return response.json();
    }).then(function (myJson) {
        maxPage = myJson.totalSize;
        searchData = myJson.searchedContact;
        return myJson;
    })

    return Promise.all([data, template])
        .then(response => {
            let resolvedData = response[0];
            let resolvedTemplate = response[1];
            // Cache the template for future use
            Mustache.parse(resolvedTemplate);
            const html = Mustache.to_html(resolvedTemplate, resolvedData);
            // Write out the rendered template
            document.getElementById("myDiv").innerHTML = "";
            return document.getElementById('myDiv').innerHTML = html;
        }).then(function () {
            history.pushState(null, "Display found contacts", historyLink);
            let navContainer = document.getElementById("navContainer");
            let nextBtnContainer = document.getElementById("nextBtnContainer");

            if (+page <= 1) {
                page = 1;
                document.getElementById("prevBtnContainer").setAttribute("class", "page-item disabled");
                document.getElementById("btn_prev").setAttribute("tabindex", "-1");
            }
            if (+page >= maxPage) {
                page = maxPage;
                document.getElementById("nextBtnContainer").setAttribute("class", "page-item disabled");
                document.getElementById("btn_next").setAttribute("tabindex", "-1");
            }

            let pageSizeSelect = document.getElementById("pageSize");
            pageSizeSelect.value = pageSize;
            for (let i = 1; i <= maxPage; i++) {
                let li = document.createElement('li');
                let a = document.createElement('a');
                if (i === +page) {
                    li.setAttribute("class", "page-item disabled");
                    a.setAttribute("class", "page-link active");
                    a.setAttribute("tabindex", "-1")
                } else {
                    li.setAttribute("class", "page-item");
                    a.setAttribute("class", "page-link");
                }
                a.setAttribute("id", i.toString());
                a.setAttribute("href", "");
                a.onclick = function () {
                    displayFoundContacts(formData, this.id, pageSizeSelect.value);
                }
                let linkText = document.createTextNode(i.toString());
                a.appendChild(linkText);
                li.appendChild(a);
                navContainer.insertBefore(li, nextBtnContainer);
            }

            pageSizeSelect.onchange = function () {
                page = 1;
                displayFoundContacts(formData, page, pageSizeSelect.value);
            }

            document.getElementById("btn_prev").onclick = function () {
                displayFoundContacts(formData, +page - 1, document.getElementById("pageSize").value);
            }

            document.getElementById("btn_next").onclick = function () {
                displayFoundContacts(formData, +page + 1, document.getElementById("pageSize").value);
            }

            let searchParamsText = "Search parameters: ";

            for (let key in searchData){
                if (searchData.hasOwnProperty(key) && searchData[key]!=="" &&
                    searchData[key]!==null && searchData[key]!==0) {
                    if(key==="address"){
                        let address = searchData.address;
                        for (let param in address){
                            if(address[param]!=="" && address[param]!==null && address[param]!==0){
                                searchParamsText += param + ": " + address[param] + "; ";
                            }
                        }
                    } else {
                        searchParamsText += key + ": " + searchData[key] + "; ";
                    }
                }
            }
            let textNode = document.createTextNode(searchParamsText);
            document.getElementById("searchParameters").appendChild(textNode);

        }).catch(function (err) {
            alert("Unable to render found contacts page");
        })

}




