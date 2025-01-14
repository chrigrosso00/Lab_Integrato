document.addEventListener('DOMContentLoaded', function() {
    caricaStatistiche();
});

function caricaStatistiche() {
    fetch('/cliente/statistiche', {
        method: 'GET',
        headers: { 'Authorization': `Bearer ${localStorage.getItem('jwtToken')}` }
    })
    .then(response => response.json())
    .then(data => {
        const ordiniTotali = data.ordini_totali;
        const ordiniCompletati = data.ordini_completati;
        const ordiniInAttesa = data.ordini_in_attesa;
        const ordiniAnnullati = data.ordini_annullati;
        
        // Calcola la percentuale di ordini completati
        const percentualeCompletati = ordiniTotali > 0 
            ? ((ordiniCompletati / ordiniTotali) * 100).toFixed(2) 
            : 0; // Evita la divisione per zero

        // Aggiorna i valori nella dashboard
        document.querySelector('.small-box.bg-info h3').textContent = ordiniTotali;
        document.querySelector('.small-box.bg-success h3').textContent = `${percentualeCompletati}%`;
        document.querySelector('.small-box.bg-warning h3').textContent = ordiniInAttesa;
        document.querySelector('.small-box.bg-danger h3').textContent = ordiniAnnullati;
    })
    .catch(error => console.error('Errore nel fetch delle statistiche:', error));
}

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

//DASHBOARD CLIENTE METABASE

fetch('/metabase/embed-url')
    .then(response => response.text())
    .then(iframeUrl => {
        // Imposta l'URL nell'iframe
        console.log(iframeUrl);
        document.getElementById('metabaseIframe').src = iframeUrl;
    })
    .catch(err => {
        console.error("Errore nel recupero dell'URL della dashboard:", err);
    });
