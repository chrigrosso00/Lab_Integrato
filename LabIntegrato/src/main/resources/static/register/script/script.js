function submitForm() {
		
    const formData = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        accountType: document.getElementById('accountType').value,
        nome: document.getElementById('nome').value,
        partitaIVA: document.getElementById('partitaIVA').value
    };
    
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
        document.getElementById('username').required = false;
    } else {
        userFields.style.display = 'block';
        document.getElementById('password').required = true;
        clienteFields.style.display = 'none';
        document.getElementById('nome').required = false;
        document.getElementById('partitaIVA').required = false;
    }
});