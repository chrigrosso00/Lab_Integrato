<div align="center">
  <h1>ETL Flask Application</h1>
</div>
<div align="center">
  <!-- Sostituisci questo link con il tuo banner o un'immagine rappresentativa del progetto -->
  <img src="https://res.cloudinary.com/practicaldev/image/fetch/s--QsmIiz9y--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://thepracticaldev.s3.amazonaws.com/i/lnm6ybztq944ikym1s8f.JPG" alt="Banner del Progetto" width="1000px">
</div>

![Python](https://img.shields.io/badge/python-3.10%2B-blue)
![Flask](https://img.shields.io/badge/flask-2.3%2B-red)

---

## 📜 Indice
1. [📖 Descrizione](#Descrizione)  
2. [✨ Funzionalità principali](#Funzionalità-principali)
3. [🔄 Flusso di lavoro](#Flusso-di-lavoro)
4. [🌐 Endpoint REST](#Endpoint-REST)  
5. [📊 Logging](#Logging)  
6. [💡 Tecnologie utilizzate](#Tecnologie-utilizzate)  

---

## 📖 **Descrizione**

Questa applicazione ETL (**Extract, Transform, Load**) è sviluppata in **Python** con il framework **Flask**. Gestisce l'elaborazione di dati di produzione tra database **PostgreSQL** e **MySQL**, convalidando i dati con **Pydantic**, applicando logiche di business e registrando anomalie.

L'applicazione fornisce un'interfaccia REST per:
- 🚀 Avviare e monitorare i processi ETL.
- 🔄 Gestire gli ordini e aggiornare il magazzino.
- 📊 Visualizzare e gestire i log.

---

## ✨ **Funzionalità principali**

### 📂 **1. Estrazione**
- I dati JSON vengono ricevuti tramite endpoint REST o estratti dalla tabella `raw_operazione` in **PostgreSQL**.

### ✅ **2. Validazione con Pydantic**
- Controlli sui dati:
  - **Timestamp validi**.
  - Range corretti per campi come `peso_effettivo` e `temperatura_effettiva`.
  - Validità del tipo di operazione (`forgiatura` o `cnc`).
- Campi non validi impostati a `None` e segnalati come anomalie.

### 🔄 **3. Trasformazione**
- Applicazione di logiche di business:
  - 🛠️ Forgiatura: calcolo peso/temperatura.
  - 🖋️ CNC: gestione pezzi prodotti e tipo di fermo.
  - 🔍 Registrazione anomalie rilevate.

### 📤 **4. Caricamento**
- Dati validi (o con anomalie) caricati in **MySQL**.
- Tabelle gestite:
  - `operazioni`, `forgiatura`, `cnc`, `anomalia_operazione`.

### 📦 **5. Gestione Ordini**
- Aggiorna automaticamente gli ordini:
  - ✅ Ordini completati quando tutti i pezzi sono processati.
  - 🏗️ Creazione dati fittizi per simulazioni.
- Decremento dei pezzi nel magazzino.

### 📜 **6. Logging**
- Tutte le attività sono registrate in file di log:
  - 🗂️ `etl.log`, `periodic.log`, `postgresql.log`, `mysql.log`.

### 🔒 **7. Sicurezza**
- Endpoint protetti da **API_KEY** (`X-API-KEY` nell'header).
- Credenziali e configurazioni tramite **variabili d'ambiente**.

---

## 🔄 **Flusso di lavoro**

Il processo ETL segue una sequenza di passaggi ben definiti, integrati con il flusso di **Power Automate** e le API REST esposte dall'applicazione.

### **1. Chiamata iniziale agli ordini**
- **Power Automate** invoca l'endpoint `GET /ordine` per ottenere informazioni sui pezzi da produrre per gli ordini.
- I dati restituiti includono:
  - ID ordine.
  - Pezzi da produrre.
  - Quantità rimanente per ogni pezzo.

### **2. Invio dati di produzione**
- Una volta prodotti i pezzi, **Power Automate** invia un file JSON con i dettagli di produzione all'endpoint `POST /insert-postgres`.
- Questo endpoint inserisce i dati nella tabella di staging `raw_operazione` in **PostgreSQL**.

### **3. Trasferimento dati e validazione**
- Ogni ora, viene invocato automaticamente l'endpoint `POST /process-transfer`.
- Questo processo esegue le seguenti operazioni:
  - **Estrazione**: I record dalla tabella `raw_operazione` vengono letti.
  - **Validazione**: I dati vengono validati tramite **Pydantic**.
    - I campi non validi sono impostati a `NULL`.
    - Le anomalie vengono registrate nella tabella `anomalia_operazione`.
  - **Caricamento**: I record validati vengono inseriti nelle tabelle corrispondenti in **MySQL** (`operazioni`, `forgiatura`, `cnc`, ecc.).

### **4. Aggiornamento degli ordini**
- Ogni ora, l'endpoint `POST /aggiorna/ordine` viene invocato per aggiornare lo stato degli ordini:
  - La quantità rimanente dei pezzi per ogni ordine viene decrementata in base alla produzione.
  - Gli ordini con tutti i pezzi prodotti vengono aggiornati da `IN ATTESA` a `COMPLETATO`.

---

## 🌐 **Endpoint REST**

### 🚀 **Avvio e Monitoraggio**
| **Endpoint**               | **Descrizione**                                                       |
|----------------------------|-----------------------------------------------------------------------|
| **`POST /run-etl`**        | Avvia l'ETL in un thread separato per elaborare dati JSON ricevuti.   |
| **`POST /insert-postgres`**| Inserisce dati in `raw_operazione` di PostgreSQL.                    |
| **`POST /process-transfer`**| Trasferisce e valida i dati da PostgreSQL a MySQL.                   |
| **`GET /status`**          | Restituisce lo stato corrente del processo ETL.                      |

---

### 📦 **Gestione Ordini**
| **Endpoint**               | **Descrizione**                                                       |
|----------------------------|-----------------------------------------------------------------------|
| **`GET /ordine`**          | Restituisce 5 pezzi da processare o crea record fittizi.             |
| **`POST /aggiorna/ordine`**| Aggiorna lo stato degli ordini da `IN ATTESA` a `COMPLETATO`.         |

---

### 🗂️ **Log e Manutenzione**
| **Endpoint**               | **Descrizione**                                                       |
|----------------------------|-----------------------------------------------------------------------|
| **`GET /logs`**            | Mostra i log di `etl.log`.                                           |
| **`GET /mysql-logs`**      | Mostra i log relativi a MySQL.                                       |
| **`GET /postgresql-logs`** | Mostra i log relativi a PostgreSQL.                                  |
| **`GET /clear-logs`**      | Cancella uno o più file di log (opzionale: backup).                  |

---

## 📊 **Logging**

- 🗂️ **`etl.log`**: Processi principali di ETL.
- 🔄 **`periodic.log`**: Attività pianificate.
- 🐘 **`postgresql.log`**: Log operazioni PostgreSQL.
- 🐬 **`mysql.log`**: Log operazioni MySQL.

Tutti i log sono archiviati nella directory `logs/`.

---

## 💡 **Tecnologie utilizzate**
- **Python** 🐍
- **Flask** 🌐
- **Pydantic** ✅
- **PostgreSQL** 🐘
- **MySQL** 🐬
