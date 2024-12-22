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

// Inizializzazione al caricamento della pagina
document.addEventListener('DOMContentLoaded', () => {
    try {
        verifyToken();
    } catch (error) {
        console.error('Errore durante l\'inizializzazione:', error);
    }
});

async function verifyToken() {
    try {
        const token = localStorage.getItem('jwtToken');
        console.log('Token recuperato:', token);
        if (!token) {
            throw new Error('Token non trovato. Autenticazione richiesta.');
        }

        // Decodifica il token usando jwt-decode
        const payload = jwt_decode(token);
        console.log('Payload decodificato:', payload);

        if (!payload) {
            throw new Error('Token non valido.');
        }

        // Controlla il ruolo nel payload
        if (Array.isArray(payload.roles) && payload.roles.includes('ROLE_CLIENTE')) {
            // Il ruolo è autorizzato, procedi con il fetch degli ordini
            await fetchOrdini();
        } else {
            throw new Error('Ruolo non autorizzato.');
        }

    } catch (error) {
        console.error('Errore nella verifica del token:', error);
        Swal.fire({
            icon: 'error',
            title: 'Autenticazione Fallita',
            text: 'Il tuo token non è valido o è scaduto. Effettua nuovamente l\'accesso.',
            confirmButtonText: 'OK'
        }).then(() => {
            // Reindirizza l'utente alla pagina di login
            window.location.href = '/public/login';
        });
    }
}

function getQueryParameter(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

const ordersContainer = document.getElementById('orders-container'); 
const loadMoreButton = document.getElementById('load-more-btn');

// Controlla se gli elementi esistono
if (!ordersContainer) {
    console.error('Errore: il contenitore degli ordini non è stato trovato.');
}
if (!loadMoreButton) {
    console.error('Errore: il pulsante "Carica altri" non è stato trovato.');
}

// Variabili globali per la paginazione
let ordersData = []; // Ordini recuperati dal backend
let currentIndex = 0; // Indice di partenza per il caricamento
const ordersPerPage = 4; // Numero massimo di ordini da caricare per volta

async function fetchOrdini() {
    try {
        //Controllo token
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            throw new Error('Token non trovato. Autenticazione richiesta.');
        }

        console.log('Inizio fetch degli ordini...');

        //Leggi il parametro "stato" dalla query string
        const stato = getQueryParameter('stato') || 'ALL';

        //Effettua la richiesta al server
        const response = await fetch('/cliente/storico/ordini', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        console.log('Risposta HTTP ricevuta:', response.status);
        
        //Controllo se la risposta HTTP è ok
        if (!response.ok) {
            throw new Error(`Errore HTTP! Stato: ${response.status}`);
        }

        //Estrai i dati dal server (JSON una sola volta)
        const data = await response.json();
        console.log('Dati ricevuti dal server:', data);
        
        // Filtra gli ordini in base allo stato
        const ordiniFiltrati = stato === 'ALL' 
            ? data 
            : data.filter(order => order.stato === stato);
            
        console.log('Ordini filtrati:', ordiniFiltrati);

        //Cancella gli ordini precedenti nella pagina
        const orderList = document.getElementById('orders-container');
        orderList.innerHTML = '';
        
        if (ordiniFiltrati.length === 0) {
            orderList.innerHTML = `
                <div class="alert alert-warning text-center mt-3" role="alert">
                    <strong>Non ci sono ordini per lo stato "${stato}"</strong>
                </div>
            `;
            return; // Interrompe l'esecuzione
        }
        
        //Salva i dati in una variabile globale e mostra gli ordini
        ordersData = ordiniFiltrati;
        renderOrders(ordersData); 

    } catch (error) {
        console.error('Errore nel fetch degli ordini:', error);
        
        Swal.fire({
            icon: 'error',
            title: 'Errore',
            text: 'Impossibile caricare gli ordini. Riprova più tardi.',
            confirmButtonText: 'OK'
        });
    }
}

function createOrderCard(order) {
    const dataFine = order.data_fine ? order.data_fine : "N/A";

    let progressClass = "bg-danger";
    if (order.percentuale_avanzamento > 25) progressClass = "bg-warning";
    if (order.percentuale_avanzamento > 50) progressClass = "bg-info";
    if (order.percentuale_avanzamento > 75) progressClass = "bg-success";

    let statoClass = "";
    let statoIcon = "";
    switch (order.stato) {
        case "IN ATTESA":
            statoClass = "bg-warning text-dark";
            statoIcon = '<i class="fas fa-hourglass-start"></i>';
            break;
        case "COMPLETATO":
        case "TERMINATO":
            statoClass = "bg-success text-white";
            statoIcon = '<i class="fas fa-check-circle"></i>';
            break;
        case "ANNULLATO":
            statoClass = "bg-danger text-white";
            statoIcon = '<i class="fas fa-times-circle"></i>';
            break;
        default:
            statoClass = "bg-secondary text-white";
            statoIcon = '<i class="fas fa-question-circle"></i>';
            break;
    }

    const pezziHTML = order.pezzi_ordione.map(pezzo => `
        <tr>
            <td>
                <img src="${pezzo.immagineUrl}" alt="${pezzo.nome}" 
                     style="width: 50px; height: 50px; object-fit: cover; border-radius: 50px;">
            </td>
            <td>${pezzo.nome}</td>
            <td>${pezzo.quantitaTotale - pezzo.quantita}</td>
            <td>${pezzo.quantitaTotale}</td>
        </tr>
    `).join('');

    const prezziHTML = order.pezzi_ordione.map(pezzo => `
        <tr>
            <td>${pezzo.nome}</td>
            <td>${pezzo.quantitaTotale - pezzo.quantita}</td>
            <td>${pezzo.quantitaTotale}</td>
            <td>${pezzo.prezzo.toFixed(2)} €</td>
            <td>${(pezzo.prezzo * pezzo.quantitaTotale).toFixed(2)} €</td>
        </tr>
    `).join('');

    const costoTotale = order.pezzi_ordione.reduce(
        (acc, pezzo) => acc + (pezzo.prezzo * pezzo.quantitaTotale),
        0
    ).toFixed(2);
    
    // Bottone di eliminazione visibile solo se lo stato non è "ANNULLATO" o "COMPLETATO"
    const deleteButtonHTML = (order.stato !== 'ANNULLATO' && order.stato !== 'COMPLETATO') ? `
        <button class="btn btn-danger btn-sm" onclick="eliminaOrdineBtn(${order.id_ordine})">
            <i class="fas fa-trash"></i>
        </button>
    ` : '';

    return `
    <div class="card mb-3 shadow-lg border-0 mt-card-first">
        <div class="card-header ${statoClass} d-flex justify-content-between align-items-center">
            <h5 class="m-0">Ordine #${order.id_ordine}</h5>
            <h5 class="m-0">Stato: ${order.stato} ${statoIcon}</h5>
            ${deleteButtonHTML}
            <button class="btn btn-tool" type="button" data-card-widget="collapse" style="color: black;">
                <i class="fas fa-minus"></i>
            </button>
        </div>
        <div class="card-body">
            <p><strong>Data Inizio:</strong> ${order.data_inizio}</p>
            <p><strong>Data Fine:</strong> ${dataFine}</p>
            <table class="table table-sm table-striped table-hover">
                <thead class="bg-light">
                    <tr>
                        <th></th>
                        <th>Prodotto</th>
                        <th>Quantità Prodotta</th>
                        <th>Quantità Totale</th>
                    </tr>
                </thead>
                <tbody>
                    ${pezziHTML}
                </tbody>
            </table>
            <div class="progress mt-3">
                <div class="progress-bar progress-bar-striped ${progressClass}" 
                    role="progressbar" 
                    style="width: ${order.percentuale_avanzamento}%;" 
                    aria-valuenow="${order.percentuale_avanzamento}" 
                    aria-valuemin="0" 
                    aria-valuemax="100">
                    ${order.percentuale_avanzamento}%
                </div>
            </div>

            <!-- Collapse Button -->
            <button class="btn btn-secondary btn-sm mt-3 toggle-payment-details" type="button" data-bs-toggle="collapse" data-bs-target="#collapse-prezzi-${order.id_ordine}" aria-expanded="false" aria-controls="collapse-prezzi-${order.id_ordine}" style="background-color=#343a40;">
                Mostra Dettagli Pagamento
            </button>

            <div class="collapse mt-3" id="collapse-prezzi-${order.id_ordine}">
                <p><strong>Costo Totale dell'Ordine:</strong> ${costoTotale} €</p>
                <table class="table table-sm table-striped table-hover">
                    <thead class="bg-light">
                        <tr>
                            <th>Prodotto</th>
                            <th>Quantità Prodotta</th>
                            <th>Quantità Totale</th>
                            <th>Prezzo Unitario</th>
                            <th>Prezzo Totale</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${prezziHTML}
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    `;
}

document.addEventListener("click", function (e) {
    if (e.target.classList.contains("toggle-payment-details")) {
        const button = e.target;
        const isExpanded = button.getAttribute("aria-expanded") === "true";
        button.textContent = isExpanded ? "Mostra Meno" : "Mostra Dettagli Pagamento";
    }
});

function renderOrders() {
    const ordersToShow = ordersData.slice(currentIndex, currentIndex + ordersPerPage);
    ordersToShow.forEach(order => {
        const cardHTML = createOrderCard(order);
        ordersContainer.innerHTML += cardHTML;
    });

    currentIndex += ordersPerPage;

    if (currentIndex >= ordersData.length) {
        loadMoreButton.style.display = 'none';
    }
}

if (loadMoreButton) {
    loadMoreButton.addEventListener('click', renderOrders);
}

// Funzione per gestire il cambio dei tab e riordinare gli ordini
document.addEventListener('DOMContentLoaded', () => {
    // Aggiunge l'evento di cambio per i radio button dei tab
    document.querySelectorAll('input[name="tabs"]').forEach(tab => {
        tab.addEventListener('change', () => {
            console.log('Cambio del tab rilevato'); // Debugging
            const sortValue = document.querySelector('input[name="tabs"]:checked').value;

            // Ordina i dati in base al valore del tab selezionato
            if (sortValue === "recent") {
                ordersData.sort((a, b) => new Date(b.data_inizio) - new Date(a.data_inizio)); // Più recenti
            } else if (sortValue === "oldest") {
                ordersData.sort((a, b) => new Date(a.data_inizio) - new Date(b.data_inizio)); // Più vecchi
            }

            // Resetta l'indice e il contenitore
            currentIndex = 0;
            ordersContainer.innerHTML = ''; // Pulisce il contenitore degli ordini

            // Rendi nuovamente gli ordini
            renderOrders();
        });
    });
});

function eliminaOrdineBtn(orderId) {
    Swal.fire({
	    title: 'Sei sicuro di voler eliminare l\'ordine?',
	    text: "Questa azione non può essere annullata!",
	    icon: 'warning',
	    showCancelButton: true,
	    confirmButtonColor: '#d33',
	    cancelButtonColor: '#3085d6',
	    confirmButtonText: 'Sì, elimina!',
	    cancelButtonText: 'Annulla'
	}).then((result) => {
	    if (result.isConfirmed) {
	        eliminaOrdine(orderId);
	    }
	});
}

async function eliminaOrdine(ordineId) {
    try {
		if (!ordineId || ordineId <= 0) {
            throw new Error('ID Ordine non valido.');
        }
		
        Swal.fire({
            title: 'Eliminazione in corso...',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        const token = localStorage.getItem('jwtToken');
        if (!token) {
            throw new Error('Token non trovato. Autenticazione richiesta.');
        }
        
        console.log(ordineId);

        const response = await fetch(`/cliente/elimina/ordine/${ordineId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        // Controlla se la risposta è ok
        if (!response.ok) {
            if (response.status === 404) throw new Error('Ordine non trovato.');
            if (response.status === 403) throw new Error('Accesso negato.');
            if (response.status === 401) throw new Error('Autenticazione richiesta.');
            throw new Error(`Errore HTTP! Stato: ${response.status}`);
        }

        Swal.fire({
            title: 'Ordine eliminato con successo!', 
            icon: 'success',
            showConfirmButton: false,
            timer: 2500
        }).then(() => {
            document.querySelector(`#order-${ordineId}`).remove();
        });

    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Errore',
            text: 'Impossibile cancellare l\'ordine. Riprova più tardi.',
            confirmButtonText: 'OK'
        });
    }
}