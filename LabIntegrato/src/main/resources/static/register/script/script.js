function submitForm() {
		
    const formData = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        accountType: document.getElementById('accountType').value,
        nome: document.getElementById('nome').value,
        partitaIVA: document.getElementById('partitaIVA').value
    };

    if(validForm(formData)){
         console.log(formData);

    fetch('/api/utente/registrazione', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Errore:', error));
    }

   
}

// Riferimenti agli elementi
const accountTypeSelect = document.getElementById('accountType');
const clienteFields = document.getElementById('clienteFields');

// Funzione per mostrare/nascondere i campi extra per il Cliente
accountTypeSelect.addEventListener('change', function() {
    if (this.value === 'cliente') {
        clienteFields.style.display = 'block';
        document.getElementById('password').required = true;
        document.getElementById('nome').required = true;
        document.getElementById('partitaIVA').required = true;
        userFields.style.display = 'none';
        document.getElementById('username').required = true;
    } else {
        userFields.style.display = 'block';
        document.getElementById('password').required = true;
        clienteFields.style.display = 'none';
        document.getElementById('nome').required = true;
        document.getElementById('partitaIVA').required = true;
    }
});

function validForm(form){

    var username = form.username.value;
    var password = form.password.value;
    var accountType = form.accountType.value;
    var nome = form.nome.value;
    var partitaIVA = form.partitaIVA.value;

    if(username == ""){
        alert("Inserisci un username");
        return false;
    }

    

    if(password == ""){
        for(var i = 0;i<password.length();i++){
            if(password[i] == ' '){
                return false;
            }
        }
        console.log(password);
        alert("Inserisci una password valida senza spazi");
        return false;
    }
    if(accountType == ""){
        alert("Seleziona un tipo di account");
        return false;
    }
    if(accountType == "cliente"){
        if(nome == ""){
            alert("Inserisci il nome");
            return false;
        }
        if(partitaIVA == ""){
            alert("Inserisci la partita IVA");
            return false;
        }
    }
    return true;

}