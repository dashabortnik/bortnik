window.onload = function () {
    fetch('/contacts/', {
        method: "GET",
        headers: new Headers({'content-type': 'application/json', "Test":"MyMethod"})
    }).then(function (response) {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Invalid response");
        }
    }).then(function (myJson) {
        var tmpl = document.getElementById("mainTableTemplate").innerHTML;
        var html = Mustache.to_html(tmpl, myJson);
        var container = document.getElementById("myDiv");
        container.innerHTML = html;
        history.pushState(null, "Contact page", "/contacts");
    }).catch(function (err) {
        alert("Что-то пошло не так 1");
    });
}


function navigate(link, callback) {
    event.preventDefault();
    event.stopPropagation();
    //var path = link.getAttribute("href");
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
        console.log(link);
        if (link.match(new RegExp("\\/contacts\\/\\d+\\/edit-form"))) {
            alert("EDIT CONTACT MATCH");
            tmpl = document.getElementById("editContactTemplate").innerHTML;
            history.pushState(null, "Contact edit page", link);
            console.log("Edit contact templ");
        } else {
            alert("DISPLAY CONTACT MATCH");
            tmpl = document.getElementById("contactTemplate").innerHTML;
            history.pushState(null, "Contact page", link);
        }
        linkFromJson = myJson.photoLink;
        console.log("Link from Json---" + linkFromJson);
        //function
        var html = Mustache.to_html(tmpl, myJson);
        document.getElementById("myDiv").innerHTML = "";
        var container = document.getElementById("myDiv");
        container.innerHTML = html;
        //history.pushState(null, "Contact page", link);
        callback(linkFromJson);
    }).catch(function (err) {
        alert("Что-то пошло не так 2");
    })
}

function retrieveImage(link) {
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
                var elem = document.getElementsByClassName("avatar");
                Array.prototype.forEach.call(elem, function (el) {
                    el.setAttribute("src", newLink);
                });
                //elem.item(0).setAttribute("src", newLink);
                // used Array for each to iterate although we should have only 1 element w/ id="avatar'
            });
        } else {
            console.log('Network response was not ok.');
        }
    })
        .catch(function (error) {
            console.log('There has been a problem with your fetch operation: ' + error.message);
        });

}


//var pathName = window.location.pathname;
// window.addEventListener('popstate', function (e) {
//     // e.state is equal to the data-attribute of the last image we clicked
//     history.back();
//     location.reload();
// });

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
            //editedForm.addEventListener("submit", function (e) {
            // document.getElementById("submitButton").addEventListener("click", function (e) {
            //     e.preventDefault();
            //     e.stopPropagation();
            //     console.log("I got the form:" + editedForm.innerText);
            //     var editedFormData = new FormData(this);
            //     console.log("Edited form data---" + editedFormData);
            //     console.log("ID WILL BE---" + checkedId);
            //     var fetchLink = "/contacts/" + checkedId;
            //     fetch(fetchLink, {
            //         method: 'POST',
            //         body: editedFormData,
            //     }).then(function (response) {
            //         return response.json();
            //     }).then(function (myJson) {
            //         let tmpl = document.getElementById("contactTemplate").innerHTML;
            //         let html = Mustache.to_html(tmpl, myJson);
            //         document.getElementById("myDiv").innerHTML = "";
            //         let container = document.getElementById("myDiv");
            //         container.innerHTML = html;
            //
            //     }).catch(function (error) {
            //         console.error("ERROR on updating a contact" + error);
            //     })
            //
            // })
        });

    }
}


// function submitEditForm() {
//     event.preventDefault();
//     event.stopPropagation();
//     console.log("submit edit form method was triggered");
//     var editedForm = document.getElementById("editedContactInfo");
//     console.log("I got the form:" + editedForm.innerText);
//     //if (editedForm) {
//     // editedForm.addEventListener("submit", function (e) {
//     //e.preventDefault();
//     var editedFormData = new FormData(this);
//     console.log("Edited form data---" + editedFormData);
//     var fetchLink = "/contacts/" + checkedId;
//     fetch(fetchLink, {
//         method: 'POST',
//         body: editedFormData,
//     }).then(function (response) {
//         return response.json();
//     }).then(function (myJson) {
//         let tmpl = document.getElementById("contactTemplate").innerHTML;
//         let html = Mustache.to_html(tmpl, myJson);
//         document.getElementById("myDiv").innerHTML = "";
//         let container = document.getElementById("myDiv");
//         container.innerHTML = html;
//
//     }).catch(function (error) {
//         console.error("ERROR on updating a contact" + error);
//     })
//     //})
//     //}
// }

function deleteContact() {
    var currentPath = window.location.pathname;
    console.log("Current pathname is --- " + currentPath);
    var idArray = [];
    if (new RegExp("^(\\/contacts)$").test(currentPath)) {
        console.log("General case");
        var checked = document.querySelectorAll(".check:checked");
        console.log("CHECKED OBJECT---" + checked);
        for (let i = 0; i < checked.length; i++) {
            idArray.push(checked[i].id);
        }
    } else if (new RegExp("^(\\/contacts\\/\\d+)$").test(currentPath)) {
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

function sendEmail() {
    var checked = document.querySelectorAll(".check:checked");
    console.log("CHECKED OBJECTS FOR EMAIL---" + checked);
    //not finished
}

//regular email about birthdays -- WORKS
// window.setInterval(function(){ // Set interval for checking
//     let date = new Date(); // Create a Date object to find out what time it is
//     console.log("Current date---" + date);
//     if(date.getHours() === 10 && date.getMinutes() === 0 && date.getSeconds() === 0){ // Check the time
//         console.log("Send email");
//     } else {
//         console.log("Not yet");
//     }
// }, 1000);


//WHY SO MANY REQUESTS ON MAIN PAGE?


window.addEventListener("popstate", e => {
    if (e.state !== null){
        navigate(e.state.id);
    }
})

history.replaceState(null, "Initial page", "contacts/")