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
    var linkFromJson = null;
    fetch(link, {
        method: "GET",
        headers: new Headers({'content-type': 'application/json'})
    }).then(function (response) {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Invalid response");
        }
    }).then(function (myJson) {
        console.log("MyJson---" + JSON.stringify(myJson));
        var tmpl = null;
        let title = null;
        console.log(link);
        if (link.match(new RegExp("\\/contacts\\/\\d+\\/edit-form"))) {
            alert("EDIT CONTACT MATCH");
            tmpl = document.getElementById("editContactTemplate").innerHTML;
            title = "Contact edit page";
            console.log("Edit contact templ");
        } else {
            alert("DISPLAY CONTACT MATCH");
            tmpl = document.getElementById("contactTemplate").innerHTML;
            title = "Contact page";

        }
        linkFromJson = myJson.contact.photoLink; // ignore warning, works correctly in js
        console.log("Link from Json---" + linkFromJson);
        //function
        var html = Mustache.to_html(tmpl, myJson);
        document.getElementById("myDiv").innerHTML = "";
        var container = document.getElementById("myDiv");
        container.innerHTML = html;
        history.pushState(null, title, link);

        //FOR DISPLAY OF MODAL WINDOW
        // Get the modal
        var modal = document.getElementById("myModal");
        // Get the button that opens the modal
        var btn = document.getElementById("myBtn");
        // When the user clicks the button, open the modal
        btn.onclick = function() {
            modal.style.display = "flex";
        }
        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }

        callback(linkFromJson);
    }).catch(function (err) {
        alert("Что-то пошло не так 2");
    })
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
    document.getElementById("myDiv").innerHTML = "";
    var tmpl = document.getElementById("newContactTemplate").innerHTML;
    var doc = Mustache.to_html(tmpl);
    var container = document.getElementById("myDiv");
    container.innerHTML = doc;
    history.pushState(null, "New contact page", "/contacts/new-form");

    var form = document.getElementById("contactInfo");
    form.addEventListener("submit", function (e) {
        e.preventDefault();
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
    });
}

function openEditForm(callback) {
    //Check how many checkboxes are checked. Only 1 should be checked, alert if not 1.
    var link;
    var checkedId;
    var prom = Promise.resolve().then(function (res) {
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
            link = "/contacts/" + checkedId + "/edit-form";
            console.log("BUILT LINK:" + link);
        }
    }).catch(function (err) {
        console.log(err.message); //выведет сообщение "не удалось выполнить..."
    }).then(function (res) {
        navigate(link, retrieveImage);
    }).then(function (res) {
        submitEditForm(checkedId);
    }).catch(function (err) {
        console.log(err.message); //выведет сообщение "не удалось выполнить..."
    })

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
}


