// function submitForm() {

//     const formData = {
//         username: document.getElementById('username').value,
//         password: document.getElementById('password').value,
//         accountType: document.getElementById('accountType').value,
//         nome: document.getElementById('nome').value,
//         partitaIVA: document.getElementById('partitaIVA').value
//     };

//     if (validForm(formData)) {
//         console.log(formData);

//         fetch('/api/utente/registrazione', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(formData)
//         })
//             .then(response => response.json())
//             .then(data => console.log(data))
//             .catch(error => console.error('Errore:', error));
//     }


// }

function submitForm() {

    const formData = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        accountType: document.getElementById('accountType').value,
        nome: document.getElementById('nome').value,
        partitaIVA: document.getElementById('partitaIVA').value
    };

    if (validForm(formData)) {
        console.log(formData);

        fetch('/api/utente/registrazione', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (!response.ok) {
                    // Controlla se la risposta contiene un corpo JSON
                    return response.json()
                        .then(err => { 
                            throw new Error(err.message || "Errore sconosciuto durante la registrazione");
                        });
                }
                return response.json(); // Continua normalmente se OK
            })
            .then(data => {
                console.log(data.message);
                alert('Registrazione avvenuta con successo: ' + data.message);
            })
            .catch(error => {
                console.error('Errore:', error.message);
                alert('Errore durante la registrazione: ' + error.message);
            });
    }
}


// Riferimenti agli elementi
const accountTypeSelect = document.getElementById('accountType');
const clienteFields = document.getElementById('clienteFields');

// Funzione per mostrare/nascondere i campi extra per il Cliente
accountTypeSelect.addEventListener('change', function () {
    if (this.value === 'cliente') {
        userFields.style.display = 'none';
        clienteFields.style.display = 'block';
        document.getElementById('password').required = true;
        document.getElementById('nome').required = true;
        document.getElementById('partitaIVA').required = true;


    } else {
        clienteFields.style.display = 'none';
        userFields.style.display = 'block';
        document.getElementById('password').required = true;
        document.getElementById('username').required = true;
    }
});

// function validForm(form) {

//     var username = form.username.value;
//     var password = form.password.value;
//     var accountType = form.accountType.value;
//     var nome = form.nome.value;
//     var partitaIVA = form.partitaIVA.value;

//     if (username == "") {
//         alert("Inserisci un username");
//         return false;
//     }



//     if (password == "") {
//         for (var i = 0; i < password.length(); i++) {
//             if (password[i] == ' ') {
//                 return false;
//             }
//         }
//         console.log(password);
//         alert("Inserisci una password valida senza spazi");
//         return false;
//     }
//     if (accountType == "") {
//         alert("Seleziona un tipo di account");
//         return false;
//     }
//     if (accountType == "cliente") {
//         if (nome == "") {
//             alert("Inserisci il nome");
//             return false;
//         }
//         if (partitaIVA == "") {
//             alert("Inserisci la partita IVA");
//             return false;
//         }
//     }
//     return true;

// }

function validForm(form) {
    if (!form.username || form.username.trim() === "") {
        alert("Inserisci un username");
        return false;
    }

    if (!form.password || form.password.trim() === "") {
        alert("Inserisci una password valida senza spazi");
        return false;
    }

    if (!form.accountType) {
        alert("Seleziona un tipo di account");
        return false;
    }

    if (form.accountType === "cliente") {
        if (!form.nome || form.nome.trim() === "") {
            alert("Inserisci il nome");
            return false;
        }
        if (!form.partitaIVA || form.partitaIVA.trim().length !== 11) {
            alert("Inserisci una Partita IVA valida (11 caratteri)");
            return false;
        }
    }
    return true;
}
