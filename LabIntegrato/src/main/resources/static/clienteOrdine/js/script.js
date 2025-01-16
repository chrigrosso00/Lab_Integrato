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

// Definizione globale dei prodotti
    let prodotti = [];

    async function fetchProdotti() {
        try {
            const token = localStorage.getItem('jwtToken');
            if (!token) {
                throw new Error('Token non trovato. Autenticazione richiesta.');
            }

            const response = await fetch('/cliente/pezzi', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error(`Errore HTTP! Stato: ${response.status}`);
            }

            prodotti = await response.json();
            renderProductList(); // Chiama la funzione per renderizzare i prodotti dopo averli ottenuti
        } catch (error) {
            console.error('Errore nel fetch dei prodotti:', error);
            Swal.fire({
                icon: 'error',
                title: 'Errore',
                text: 'Impossibile caricare i prodotti. Riprova più tardi.',
                confirmButtonText: 'OK'
            });
        }
    }

    let cartItemCount = 0;
    let cartItems = [];

    // Funzione per alternare la visualizzazione del carrello
    function toggleCart() {
        const cartSection = document.getElementById('cartSection');
        cartSection.classList.toggle('open');
    }

    // Funzione per aggiornare il conteggio degli articoli nel carrello
	function updateCartCount() {
	    const totalItems = cartItems.reduce((acc, item) => acc + item.quantity + item.toProduce, 0);
	    document.getElementById('cartCount').textContent = totalItems;
	}

    // Funzione per aggiungere un prodotto al carrello
	function addToCart(productId) {
	    const prodotto = prodotti.find(p => p.id === productId);
	    if (!prodotto) {
	        console.error(`Prodotto con ID ${productId} non trovato`);
	        return;
	    }
	
	    // Seleziona la card del prodotto
	    const productCard = document.querySelector(`.card[data-product-id="${productId}"]`);
	    if (!productCard) {
	        console.error(`Card per il prodotto ID ${productId} non trovata`);
	        return;
	    }
	
	    // Ottieni la quantità selezionata
	    const selectElement = productCard.querySelector('.quantity-select');
	    const customInput = productCard.querySelector('.quantity-custom');
	    let desiredQuantity;
	    if (selectElement.value === 'custom') {
	        desiredQuantity = parseInt(customInput.value) || 1;
	    } else {
	        desiredQuantity = parseInt(selectElement.value) || 1;
	    }
	
	    // Calcola quanto è disponibile
	    const available = prodotto.disponibilita;
	
	    let quantityToAdd = desiredQuantity;
	    let toProduce = 0;
	
	    if (desiredQuantity > available) {
	        quantityToAdd = available;
	        toProduce = desiredQuantity - available;
	    }
	
	    // Trova se il prodotto è già nel carrello
	    const existingItem = cartItems.find(item => item.id === productId);
	    if (existingItem) {
	        existingItem.quantity += quantityToAdd;
	        existingItem.toProduce += toProduce;
	        existingItem.total = existingItem.quantity * existingItem.prezzo + existingItem.toProduce * existingItem.prezzo;
	    } else {
	        cartItems.push({
	            id: prodotto.id,
	            nome: prodotto.nome,
	            prezzo: prodotto.prezzo,
	            quantity: quantityToAdd,
	            toProduce: toProduce,
	            total: (quantityToAdd + toProduce) * prodotto.prezzo
	        });
	    }
	
	    // Aggiorna la disponibilità del prodotto
	    prodotto.disponibilita -= quantityToAdd;
	
	    // Aggiorna il conteggio totale del carrello (includendo i prodotti da produrre)
	    cartItemCount += desiredQuantity;
	
	    updateCartCount();
	    renderCartItems();
	    updateCheckoutTotals();
	}

    // Funzione per rendere dinamicamente gli articoli nel carrello
	function renderCartItems() {
	    const cartItemsContainer = document.querySelector('#cartSection .products');
	    cartItemsContainer.innerHTML = ''; // Pulisce il contenuto precedente
	
	    cartItems.forEach(item => {
	        const itemElement = document.createElement('div');
	        itemElement.classList.add('product');
	
	        itemElement.innerHTML = `
	            <div class="product-info">
	                <span>${item.nome}</span>
	                <p>Prezzo unitario: €${item.prezzo.toFixed(2)}</p>
	                <p>Quantità: ${item.quantity} (Disponibili) + ${item.toProduce} (Da produrre)</p>
	            </div>
	            <div class="quantity">
	                <button onclick="decreaseQuantity(${item.id})">-</button>
	                <span>${item.quantity + item.toProduce}</span>
	                <button onclick="increaseQuantity(${item.id})">+</button>
	            </div>
	            <div class="price">
	                €${item.total.toFixed(2)}
	            </div>
	            <button class="remove-item-button" onclick="removeItem(${item.id})" title="Rimuovi dal carrello">
	                <!-- Icona SVG del cestino -->
	                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512">
	                    <path d="M135.2 17.7L128 32H32C14.3 32 0 46.3 0 64v32c0 17.7 14.3 32 32 32h16l25.6 368c.6 8.7 7.4 16 16 16h224c8.6 0 15.4-7.3 16-16L368 128h16c17.7 0 32-14.3 32-32V64c0-17.7-14.3-32-32-32h-96l-7.2-14.3C257.1 7.5 248.3 0 238.4 0H209.6c-9.9 0-18.7 7.5-23.2 17.7zM48 96h352v32H48V96z"/>
	                </svg>
	            </button>
	        `;
	
	        cartItemsContainer.appendChild(itemElement);
	    });
	}

    // Funzione per aumentare la quantità di un prodotto nel carrello
    function increaseQuantity(productId) {
        const prodotto = prodotti.find(p => p.id === productId);
        const item = cartItems.find(item => item.id === productId);
        if (prodotto && item) {
            if (prodotto.disponibilita > 0) {
                item.quantity += 1;
                item.total = item.quantity * item.prezzo;
                prodotto.disponibilita -= 1;
                cartItemCount += 1;
            } else {
                item.toProduce += 1;
            }
            updateCartCount();
            renderCartItems();
            updateCheckoutTotals();
        }
    }

    // Funzione per diminuire la quantità di un prodotto nel carrello
    function decreaseQuantity(productId) {
        const prodotto = prodotti.find(p => p.id === productId);
        const item = cartItems.find(item => item.id === productId);
        if (prodotto && item) {
            if (item.quantity > 0) {
                item.quantity -= 1;
                item.total = item.quantity * item.prezzo;
                prodotto.disponibilita += 1;
                cartItemCount -= 1;

                if (item.toProduce > 0) {
                    item.toProduce -= 1;
                }

                if (item.quantity === 0 && item.toProduce === 0) {
                    // Rimuovi l'item se non ci sono più pezzi
                    cartItems = cartItems.filter(item => item.id !== productId);
                }
            }
            updateCartCount();
            renderCartItems();
            updateCheckoutTotals();
        }
    }

    // Funzione per rimuovere un prodotto dal carrello
    function removeItem(productId) {
        const prodotto = prodotti.find(p => p.id === productId);
        const itemIndex = cartItems.findIndex(item => item.id === productId);
        if (prodotto && itemIndex !== -1) {
            const item = cartItems[itemIndex];
            // Ripristina la disponibilità
            prodotto.disponibilita += item.quantity;
            // Rimuovi l'item dal carrello
            cartItems.splice(itemIndex, 1);
            cartItemCount -= item.quantity;
            updateCartCount();
            renderCartItems();
            updateCheckoutTotals();
        }
    }

    // Funzione per aggiornare i totali nel checkout
    function updateCheckoutTotals() {
        const subtotal = cartItems.reduce((acc, item) => acc + item.total, 0);
        const discount = 0; // Implementa la logica dei coupon se necessario
        const shipping = cartItems.length > 0 ? 4.99 : 0; // Spese di spedizione solo se ci sono articoli

        // Calcola i costi aggiuntivi per i pezzi da produrre basati sul prezzo del prodotto
        const productionTotal = cartItems.reduce((acc, item) => {
            return acc + (item.toProduce > 0 ? item.toProduce * item.prezzo : 0);
        }, 0);

        const total = subtotal - discount + shipping + productionTotal;

        // Aggiorna i valori nel checkout
        const detailsSpans = document.querySelectorAll('.checkout .details span');
        if (detailsSpans.length >= 6) {
            detailsSpans[1].textContent = `€${subtotal.toFixed(2)}`;
            detailsSpans[3].textContent = `€${discount.toFixed(2)}`;
            detailsSpans[5].textContent = `€${shipping.toFixed(2)}`;
        }

        // Aggiungi o aggiorna una sezione per i costi di produzione extra
        let productionSpan = document.getElementById('cartProduction');
        if (productionTotal > 0) {
            if (!productionSpan) {
                const detailsDiv = document.querySelector('.checkout .details');
                productionSpan = document.createElement('span');
                productionSpan.id = 'cartProduction';
                productionSpan.textContent = `Costi produzione extra: €${productionTotal.toFixed(2)}`;
                detailsDiv.appendChild(productionSpan);
            } else {
                productionSpan.textContent = `Costi produzione extra: €${productionTotal.toFixed(2)}`;
            }
        } else {
            if (productionSpan) {
                productionSpan.remove();
            }
        }

        document.getElementById('cartTotal').textContent = `€${total.toFixed(2)}`;
    }

    // Funzione checkout
    function checkout() {
        console.log('Checkout cliccato'); // Debug
        if (cartItems.length === 0) {
            // Mostra un alert di avviso se il carrello è vuoto
            Swal.fire({
                icon: 'warning',
                title: 'Il tuo carrello è vuoto!',
                text: 'Aggiungi dei prodotti al carrello per continuare.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Controlla se ci sono prodotti da produrre
        const itemsToProduce = cartItems.filter(item => item.toProduce > 0);

        if (itemsToProduce.length > 0) {
            // Costruisci un messaggio dettagliato
            let produceMessage = 'Hai aggiunto più prodotti di quelli disponibili. I seguenti articoli richiedono produzione extra:<br><ul>';
            itemsToProduce.forEach(item => {
                produceMessage += `<li>${item.nome}: ${item.toProduce} pezzo/i da produrre</li>`;
            });
            produceMessage += '</ul>Vuoi procedere comunque?';

            Swal.fire({
                icon: 'info',
                title: 'Produzione Extra Necessaria',
                html: produceMessage,
                showCancelButton: true,
                confirmButtonText: 'Procedi',
                cancelButtonText: 'Annulla',
            }).then((result) => {
                if (result.isConfirmed) {
                    proceedToConfirmOrder();
                } else {
                    Swal.fire('Ordine non confermato', 'Puoi continuare a modificare il tuo carrello.', 'info');
                }
            });
        } else {
            // Nessun prodotto extra, procedi normalmente
            proceedToConfirmOrder();
        }
    }

    // Funzione per procedere alla conferma dell'ordine
	async function proceedToConfirmOrder() {
	    const doubleCheckIcon = `
	        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" width="32">
	            <path d="M342.6 86.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L160 178.7l-57.4-57.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3l80 80c12.5 12.5 32.8 12.5 45.3 0l160-160zm96 128c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L160 402.7 54.6 297.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3l128 128c12.5 12.5 32.8 12.5 45.3 0l256-256z" fill="currentColor" />
	        </svg>
	    `;
	
	    const result = await Swal.fire({
	        icon: 'warning',
	        title: 'Sicuro di voler confermare l\'ordine?',
	        showDenyButton: true,
	        showCancelButton: true,
	        confirmButtonText: 'Sì',
	        denyButtonText: 'No',
	        customClass: {
	            actions: 'my-actions',
	            denyButtonText: 'order-1 right-gap',
	            confirmButton: 'order-2'
	        },
	    });
	
	    if (result.isConfirmed) {
	        try {
	            const ordineResponse = await creaOrdine();
	            
	            // Se l'ordine è stato creato con successo, mostra un messaggio di successo
	            Swal.fire({
	                title: 'Ordine confermato!', 
	                icon: 'success',
	                iconHtml: doubleCheckIcon,
	                customClass: {
	                    icon: 'rotate-y',
	                },
	                showConfirmButton: false,
	                timer: 2500 // L'alert scomparirà automaticamente dopo 2 secondi
	            }).then(() => {
	                // Reindirizza alla pagina personale dopo la conferma
	                window.location.replace("/public/cliente/storico/ordine");
	            });
	        } catch (error) {
	            // Se c'è stato un errore, mostralo con un alert
	            Swal.fire({
	                title: 'Errore',
	                icon: 'error',
	                text: error.message || 'Si è verificato un errore durante la creazione dell\'ordine',
	                confirmButtonText: 'OK'
	            });
	        }
	    } else if (result.isDenied) {
	        Swal.fire('Ordine non confermato', 'Puoi continuare a modificare il tuo carrello.', 'info');
	    }
	}

    // Funzione per creare le card dei prodotti
    function createProductCard(prodotto) {
        const card = document.createElement('div');
        card.classList.add('card');
        card.setAttribute('data-product-id', prodotto.id);

        card.innerHTML = `
            <div class="image_container">
                <img src="${prodotto.immagineUrl}" alt="${prodotto.nome}">
            </div>
            <div class="content">
                <h2 class="title">${prodotto.nome}</h2>
                <p class="description">${prodotto.descrizione}</p>
                <div class="unit-price">Prezzo unitario: €${prodotto.prezzo.toFixed(2)}</div>
                <div class="disponibilita">Disponibilità pezzi: ${prodotto.disponibilita}</div>

                <div class="quantity-container">
                    <span class="quantity-label">Quantità:</span>
                    <select class="quantity-select" onchange="toggleCustomQuantity(this); updatePrice(${prodotto.id}, ${prodotto.prezzo});">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="custom">Altro</option>
                    </select>
                    <input type="number" min="1" class="quantity-custom" placeholder="Inserisci" oninput="updatePrice(${prodotto.id}, ${prodotto.prezzo})" style="display: none;">
                </div>
            </div>

            <div class="action">
                <div class="price-total" id="price-${prodotto.id}">Totale: €${prodotto.prezzo.toFixed(2)}</div>
                <button class="cart-button" onclick="addToCart(${prodotto.id})">
                    Aggiungi al carrello
                </button>
            </div>
        `;

        return card;
    }

    // Funzione per alternare la visibilità dell'input personalizzato per la quantità
    function toggleCustomQuantity(selectElement) {
        const customInput = selectElement.parentElement.querySelector('.quantity-custom');
        if (selectElement.value === 'custom') {
            customInput.style.display = 'block';
            customInput.focus();
        } else {
            customInput.style.display = 'none';
        }
    }

    // Funzione per aggiornare il prezzo totale nella card del prodotto
    function updatePrice(productId, unitPrice) {
        const card = document.querySelector(`.card[data-product-id="${productId}"]`);
        const selectElement = card.querySelector('.quantity-select');
        const customInput = card.querySelector('.quantity-custom');
        let quantity = selectElement.value === 'custom' && customInput.value ? parseInt(customInput.value) : parseInt(selectElement.value);
        quantity = quantity || 1;

        const totalPrice = quantity * unitPrice;
        const priceElement = card.querySelector(`#price-${productId}`);
        priceElement.textContent = `Totale: €${totalPrice.toFixed(2)}`;
    }

    // Inizializzazione al caricamento della pagina
    document.addEventListener('DOMContentLoaded', () => {
        verifyToken();
    });

    async function verifyToken() {
        try {
            const token = localStorage.getItem('jwtToken');
            console.log(token);
            if (!token) {
                throw new Error('Token non trovato. Autenticazione richiesta.');
            }

            // Decodifica il token usando jwt-decode
            const payload = jwt_decode(token);
            if (!payload) {
                throw new Error('Token non valido.');
            }

            // Controlla il ruolo nel payload
            if (Array.isArray(payload.roles) && payload.roles.includes('ROLE_CLIENTE')) {
	            // Il ruolo è autorizzato, procedi con il fetch dei prodotti
	            await fetchProdotti();
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
                // Reindirizza l'utente alla pagina di login o esegui altre azioni necessarie
                window.location.href = '/public/login';
            });
        }
    }

    function renderProductList() {
        const productContainer = document.getElementById('product-list');
        productContainer.innerHTML = ''; // Pulisce il contenuto precedente

        prodotti.forEach(prodotto => {
            const productCard = createProductCard(prodotto);
            productContainer.appendChild(productCard);
        });
    }
    
    async function creaOrdine() {
        const token = localStorage.getItem('jwtToken');
        const prodottiCarrello = cartItems.map(item => ({
            codicePezzo: item.id, 
            quantita: item.quantity + item.toProduce
        }));
        
        console.log(prodottiCarrello);

        try {
            const response = await fetch('/cliente/crea/ordine', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(prodottiCarrello)
            });

         // Controlla se la risposta è OK
            if (!response.ok) {
                const errorData = await response.json(); // Legge il corpo della risposta
                throw new Error(errorData.message || 'Errore durante la creazione dell\'ordine');
            }

            const result = await response.json();
            return result; // Ritorna il risultato per poterlo usare in altre funzioni
        } catch (error) {
            console.error('Errore durante la creazione dell\'ordine:', error);
            return Promise.reject(error); // Rifiuta la Promise in caso di errore
        }
    }

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