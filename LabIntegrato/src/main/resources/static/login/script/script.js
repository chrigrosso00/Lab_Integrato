/*const accountTypeSelect = document.getElementById('accountType');
    accountTypeSelect.addEventListener('change', function() {
        if (this.value === 'cliente') {
            document.getElementById('cliente').style.display = 'block';
            document.getElementById('user').style.display = 'none';
            document.getElementById('partitaIVA').required = true;
            document.getElementById('username').required = false;
        } else {
            document.getElementById('user').style.display = 'block';
            document.getElementById('cliente').style.display = 'none';
            document.getElementById('partitaIVA').required = false;
            document.getElementById('username').required = true;
        }
    });

    function submitLoginForm() {
        const messageBox = document.getElementById('message');
        messageBox.textContent = 'Login in corso...';

        const accountType = document.getElementById('accountType').value;
        const password = document.getElementById('password').value;

        let formData = {
            password: password,
            accountType: accountType
        };

        if (accountType === 'cliente') {
            formData.partitaIVA = document.getElementById('partitaIVA').value;
        } else {
            formData.username = document.getElementById('username').value;
        }

        const url = '/api/login';

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.token) {
                localStorage.setItem('jwtToken', data.token);
                console.log('Token JWT:', localStorage.getItem('jwtToken'));
                displayMessage('Login effettuato con successo!', 'success');
                redirectWithToken('/public/cliente/dashboard', data.token);
            } else {
                displayMessage(data.error || 'Errore durante il login', 'error');
            }
        })
        .catch(error => displayMessage('Errore di rete: ' + error, 'error'));
    }

    function displayMessage(message, type) {
        const messageBox = document.getElementById('message');
        messageBox.textContent = message;
        messageBox.className = `message ${type}`;
    }
    
    function redirectWithToken(url, token) {
        fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
        .then(response => {
            console.log('Status:', response.status);
            if (response.ok) {
                displayMessage('Accesso riuscito, reindirizzamento in corso...', 'success');
                // Aspetta 5 secondi prima di eseguire il reindirizzamento
                setTimeout(() => {
                    window.location.href = url;
                }, 1000);
            } else {
                displayMessage('Errore di accesso alla pagina cliente/ordine', 'error');
            }
        })
        .catch(error => displayMessage('Errore di rete: ' + error, 'error'));
    }
*/

const accountTypeSelect = document.getElementById('accountType');
    accountTypeSelect.addEventListener('change', function() {
        if (this.value === 'cliente') {
            document.getElementById('cliente').style.display = 'block';
            document.getElementById('user').style.display = 'none';
            document.getElementById('partitaIVA').required = true;
            document.getElementById('username').required = false;
        } else {
            document.getElementById('user').style.display = 'block';
            document.getElementById('cliente').style.display = 'none';
            document.getElementById('partitaIVA').required = false;
            document.getElementById('username').required = true;
        }
    });

    function submitLoginForm() {
        const messageBox = document.getElementById('message');
        messageBox.textContent = 'Login in corso...';

        const accountType = document.getElementById('accountType').value;
        const password = document.getElementById('password').value;

        let formData = {
            password: password,
            accountType: accountType
        };

        if (accountType === 'cliente') {
            formData.partitaIVA = document.getElementById('partitaIVA').value;
        } else {
            formData.username = document.getElementById('username').value;
        }

        const url = '/api/login';

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.token) {
                localStorage.setItem('jwtToken', data.token);
                const token = localStorage.getItem('jwtToken');
                console.log('Token JWT:', localStorage.getItem('jwtToken'));
                displayMessage('Login effettuato con successo!', 'success');
                redirectWithToken(token);
            } else {
                displayMessage(data.error || 'Errore durante il login', 'error');
            }
        })
        .catch(error => displayMessage('Errore di rete: ' + error, 'error'));
    }

    function displayMessage(message, type) {
        const messageBox = document.getElementById('message');
        messageBox.textContent = message;
        messageBox.className = `message ${type}`;
    }
    
function redirectWithToken(token) {
    fetch('/api/redirect', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => {
        console.log('Status:', response.status);
        if (response.ok) {
            return response.text(); // Ricevi il percorso come stringa
        } else {
            throw new Error('Errore di accesso alla pagina cliente/ordine');
        }
    })
    .then(url => {
        displayMessage('Accesso riuscito, reindirizzamento in corso...', 'success');
        console.log(url);
        setTimeout(() => {
            window.location.href = url; // Usa il percorso restituito dal server
        }, 1000);
    })
    .catch(error => displayMessage('Errore di rete: ' + error.message, 'error'));
}
