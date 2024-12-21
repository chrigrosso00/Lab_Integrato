![Logo](https://res.cloudinary.com/practicaldev/image/fetch/s--QsmIiz9y--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://thepracticaldev.s3.amazonaws.com/i/lnm6ybztq944ikym1s8f.JPG)

# ETL Flask Application

## Descrizione

Questo progetto fornisce un'**applicazione ETL (Extract, Transform, Load)** sviluppata in **Python** con il framework **Flask**. L'applicazione riceve dati di produzione (in formato JSON), li valida tramite **Pydantic**, li trasforma secondo logiche di business e infine li carica in un **database MySQL**.

Inoltre, l'applicazione gestisce l'**aggiornamento degli ordini** (stato e quantità), supportando le seguenti operazioni:

- Se tutti i pezzi di un ordine hanno `quantita_rimanente = 0`, l'ordine viene segnato come **COMPLETATO**.
- Se non ci sono ordini in attesa, il sistema può creare **dati fittizi** per test o per la gestione di magazzino.
- Aggiorna il magazzino e decrementa le quantità dei pezzi dopo ogni inserimento ETL.

Il progetto espone diversi endpoint **REST** per avviare e monitorare i processi ETL, visualizzare i log, aggiornare gli ordini e gestire i dati di produzione.

---

## Funzionalità principali

### 1. **Estrazione**
- Riceve i dati JSON da Power Automate (o altre fonti) tramite una chiamata HTTP (`POST /run-etl`).

### 2. **Validazione con Pydantic**
- Verifica la correttezza dei campi (`id_ordine`, `timestamp_inizio`, `tipo_operazione`, ecc.) e rileva eventuali anomalie (es. valori fuori range).

### 3. **Trasformazione**
- Se ci sono campi invalidi, li imposta a `None` e registra un'anomalia (`INSERT INTO anomalia_operazione`).
- Decrementa la `quantita_rimanente` dei pezzi associati all'ordine.
- Gestisce logiche di business come forgiatura, CNC, calcolo di temperature, pesi, ecc.

### 4. **Caricamento**
- I dati validi (o parzialmente validi con anomalie) vengono salvati nelle tabelle `operazioni`, `forgiatura`, `cnc` e correlate in MySQL.

### 5. **Gestione Ordini**
- **Aggiornamento Ordini**: Gli ordini con tutti i pezzi aventi `quantita_rimanente = 0` vengono automaticamente aggiornati allo stato **COMPLETATO** (con aggiornamento della `data_fine`).
- **Creazione Fittizia**: In assenza di ordini in attesa, l'app crea 10 record "fittizi" per agevolare i test e aggiorna il magazzino.
- **Magazzino**: Aggiorna le quantità disponibili (`quantita_disponibile`) in base ai pezzi elaborati.

### 6. **API Web**
- Avvio del processo ETL e monitoraggio dello stato.
- Aggiornamento dello stato degli ordini e decremento delle quantità.
- Visualizzazione e pulizia dei file di log (`etl.log`, `periodic.log`).

### 7. **Logging**
- Tutte le operazioni vengono registrate in `logs/etl.log`, mentre le attività periodiche in `logs/periodic.log`.

### 8. **Sicurezza**
- Gli endpoint sensibili sono protetti da una **API_KEY** (inviata nell'header `X-API-KEY`).
- Le configurazioni sensibili (DB, API_KEY, ecc.) sono gestite come **variabili d'ambiente**.

---

## Endpoint Principali

### **`POST /run-etl`**
- Avvia il processo ETL in un thread separato.
- Richiede l'header `X-API-KEY`.

### **`GET /status`**
- Restituisce lo stato corrente dell'ETL (running, last_run, last_success, last_error).

### **`GET /clear-logs`**
- Pulisce i file di log (`etl.log` e `periodic.log`).
- Richiede l'header `X-API-KEY`.

### **`POST /insert`**
- Inserisce dati (in formato JSON, un array di record) nel database tramite il processo ETL.
- Richiede l'header `X-API-KEY`.

### **`GET /ordine`**
- Ritorna i primi 5 pezzi con `id_ordine` minore ancora IN ATTESA.
- Se non ci sono ordini, crea pezzi fittizi (con `id_ordine=None`) e aggiorna il magazzino.
- Richiede l'header `X-API-KEY`.

### **`POST /aggiorna/ordine`**
- Forza il controllo di tutti gli ordini e aggiorna quelli che possono passare a COMPLETATO.

### **`GET /logs`**
- Visualizza il contenuto del file `etl.log`.

### **`GET /log-cron`**
- Visualizza il contenuto del file `periodic.log`.

---

## Logging

L'applicazione registra tutte le attività in due file di log:

- **`etl.log`**: Operazioni del processo ETL.
- **`periodic.log`**: Operazioni periodiche o pianificate.

L'endpoint `/clear-logs` consente di cancellare i file di log e creare un backup.

---

## Sicurezza

- Gli endpoint sensibili sono protetti da API Key (header `X-API-KEY`).
- Le credenziali e i parametri sensibili sono caricati dalle variabili d'ambiente.
