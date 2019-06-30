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
        alert("Что-то пошло не так 1");
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
        return myJson;
    })
    //fetch template
    const template = fetch(tmpl).then(response => response.text());
    Promise.all([data, template])
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
        alert("Unable to render page with data");
    }).then(function () {
        setSelectElementValue("gender", gender);
        setSelectElementValue("marital", marital);
        callback(photoLink);
    })
    history.pushState(null, title, link);
}

function displayContact(link, callback){
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
        gender = myJson.contact.gender;
        marital = myJson.contact.maritalStatus;
        return myJson;
    })
    //fetch template
    const template = fetch(tmpl).then(response => response.text());
    Promise.all([data, template])
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
        alert("Unable to render page with data");
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

function openContactForm() {
    let form = null;
    //fetch template
    fetch("/templates/newContactTemplate.mst")
        .then(response => response.text())
        .catch(function (err) {
            alert("Unable to get text from response");
        })
        .then(res => {
            Mustache.parse(res);
            const html = Mustache.to_html(res);
            document.getElementById("myDiv").innerHTML = "";
            document.getElementById("myDiv").innerHTML = html;
        }).catch(function (err) {
        alert("Unable to render new contact page");
    })
        .then(function () {
            form = document.getElementById("contactInfo");
        }).then(function (res) {
        form.addEventListener("submit", submitForm(e), false);
    })
    history.pushState(null, "New contact page", "/contacts/new-form");

    function submitForm(e) {
        e.preventDefault();
        e.stopPropagation();
        var formData = new FormData(this);

        fetch("/contacts/", {
            method: 'POST',
            body: formData,
        }).then(function (response) {
            return response.json();
        }).then(function (myJson) {
            var tmpl = document.getElementById("contactTemplate").innerHTML;
            var html = Mustache.to_html(tmpl, myJson);
            document.getElementById("myDiv").innerHTML = "";
            var container = document.getElementById("myDiv");
            container.innerHTML = html;
        }).catch(function (error) {
            console.error("An error here" + error);
        })
    }
}

function openEditForm() {
    //Check how many checkboxes are checked. Only 1 should be checked, alert if not 1.
    var link;
    var checkedId;
    let path = window.location.pathname;

        var prom = Promise.resolve().then(function (res) {
            if (path.match(new RegExp("\\/contacts\\/\\d+"))) {
                checkedId = path.split("/")[2];
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
            console.log(err.message); //выведет сообщение "не удалось выполнить..."
        }).then(function (res) {
            navigate(link, retrieveImage);



        }).then(function (res) {
            //FOR DISPLAY OF MODAL WINDOW
            // Get the modal
            let modal = document.getElementById("myModal");
            // Get the button that opens the modal
            let btn = document.getElementById("myBtn");
            // When the user clicks the button, open the modal
            btn.onclick = function () {
                modal.style.display = "flex";
            }
            // When the user clicks anywhere outside of the modal, close it
            window.onclick = function (event) {
                if (event.target === modal) {
                    modal.style.display = "none";
                }
            }
        }).then(function (res) {
            submitEditForm(checkedId);
        }).catch(function (err) {
            console.log(err.message); //выведет сообщение "не удалось выполнить..."
        })
}

function submitEditForm(checkedId) {
        console.log("submit method here");
        var editedForm;

        window.addEventListener('load', function () {
            editedForm = document.getElementById("editedContact");
            editedForm.addEventListener("submit", function (e) {
                document.getElementById("submitButton").addEventListener("click", function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    console.log("I got the form:" + editedForm.innerText);
                    var editedFormData = new FormData(this);
                    console.log("Edited form data---" + editedFormData);
                    console.log("ID WILL BE---" + checkedId);
                    var fetchLink = "/contacts/" + checkedId;
                    fetch(fetchLink, {
                        method: 'POST',
                        body: editedFormData,
                    }).then(function (response) {
                        return response.json();
                    }).then(function (myJson) {
                        let tmpl = document.getElementById("contactTemplate").innerHTML;
                        let html = Mustache.to_html(tmpl, myJson);
                        document.getElementById("myDiv").innerHTML = "";
                        let container = document.getElementById("myDiv");
                        container.innerHTML = html;

                    }).catch(function (error) {
                        console.error("ERROR on updating a contact" + error);
                    })

                })
            });

        })

    }


