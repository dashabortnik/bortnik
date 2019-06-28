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
        history.pushState(null, "Contact page", "/contacts");
    }).catch(function (err) {
        alert("Что-то пошло не так 1");
    });


    // switch(currentUri){
    //     case "":
    //     case "/":
    //     case "/contacts":
    //     case "/contacts/":
    //         // display main page
    //     default:
    //         //display index.html
    //         fetch('http://localhost:8080/')
    //             .then(function (response) {
    //                 return response.body;
    //             })
    //             .then(function (body) {
    //                 console.log(body);
    //             });
    //         //fetch get to given uri
    //         // if goes wrong, main page will be
    //
    // }


});

function navigate() {

}