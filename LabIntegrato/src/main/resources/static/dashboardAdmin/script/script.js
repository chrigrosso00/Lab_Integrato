document.addEventListener('DOMContentLoaded', function() {
    
});

function logout() {
    localStorage.removeItem('jwtToken'); // Rimuove il token JWT
    window.location.href = '/public/login'; // Reindirizza alla pagina di login
  }

document.getElementById('logoutBtn').addEventListener('click', function(event) {
      event.preventDefault();
      Swal.fire({
        title: 'Sei sicuro di voler uscire?',
        text: "La tua sessione verrà terminata!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sì, esci!',
        cancelButtonText: 'Annulla'
      }).then((result) => {
        if (result.isConfirmed) {
          // Redirigi alla pagina di logout o esegui un'operazione AJAX
          logout();
        }
      });
    });

    const themeBtn = document.getElementById('themeBtn');
    const themeIcon = document.getElementById('themeIcon');

    // Controlla se il tema è salvato nel localStorage
    if (localStorage.getItem('theme') === 'dark') {
      document.body.classList.add('dark-mode');
      themeIcon.classList.replace('fa-moon', 'fa-sun'); // Cambia icona
    }

    // Toggle al click del pulsante
    themeBtn.addEventListener('click', function () {
      document.body.classList.toggle('dark-mode');

      if (document.body.classList.contains('dark-mode')) {
        themeIcon.classList.replace('fa-moon', 'fa-sun'); // Icona Sole
        localStorage.setItem('theme', 'dark');
      } else {
        themeIcon.classList.replace('fa-sun', 'fa-moon'); // Icona Luna
        localStorage.setItem('theme', 'light');
      }
    });
    
    function displayMessage(message, type) {
        const messageBox = document.getElementById('message');
        messageBox.textContent = message;
        messageBox.className = `message ${type}`;
    }
 
    function redirectWithToken(url) {
    	const token = localStorage.getItem('jwtToken');
        if (!token) {
            throw new Error('Token non trovato. Autenticazione richiesta.');
        }
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

//DASHBOARD ADMIN METABASE

const token = localStorage.getItem('jwtToken');

fetch('/metabase/embed-url/admin', {
    headers: {
        'Authorization': 'Bearer ' + token
    }
	})
    .then(response => response.text())
    .then(iframeUrl => {
        // Imposta l'URL nell'iframe
        console.log(iframeUrl);
        document.getElementById('metabaseIframe').src = iframeUrl;
    })
    .catch(err => {
        console.error("Errore nel recupero dell'URL della dashboard:", err);
    });
