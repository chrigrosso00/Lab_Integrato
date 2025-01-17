<div align="center">
  <img src="https://github.com/chrigrosso00/Lab_Integrato/blob/main/Lab%20Integrato.png" alt="Banner del Progetto" width="1000px">
</div>

<p align="center">
  <img src="https://img.shields.io/badge/Java-11-orange" alt="Java 11">
  <img src="https://img.shields.io/badge/Python-3.8-yellow.svg" alt="Python 3.8">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue.svg" alt="MySQL 8.0">
  <img src="https://img.shields.io/badge/PostgreSQL-13-blue.svg" alt="PostgreSQL 13">
</p>

<div align="center">
  <h1>Documentazione Tecnica<h1>
  <i>Un progetto per integrare macchinari industriali (FORGIA e CNC) con flussi di dati centralizzati per analisi, ML e BI.</i>
</div>

<br/>

---

# Indice
1. [🔰 Introduzione](#-introduzione)  
2. [🔨 Struttura del Sistema](#-struttura-del-sistema)  
3. [⚙️ Flusso Complessivo](#-flusso-complessivo)  
4. [📈 Vantaggi del Sistema](#-vantaggi-del-sistema)  
5. [💾 Connessioni ai Database](#-connessioni-ai-database)  
6. [🐍 ETL Python in Cloud](#-etl-python-in-cloud)  
7. [📖 Documentazione Tecnica](#-documentazione-tecnica)  
   - [📖 Descrizione](#descrizione)  
   - [✨ Funzionalità principali](#funzionalità-principali)  
   - [🔄 Flusso di lavoro (dettaglio ETL)](#flusso-di-lavoro-dettaglio-etl)  
   - [🌐 Endpoint REST](#endpoint-rest)  
   - [🔍 Monitoraggio processi ETL](#monitoraggio-processi-etl)  
   - [📊 Logging](#logging)  
   - [💡 Tecnologie utilizzate](#tecnologie-utilizzate)  
   - [⚙️ Architettura generale (ETL)](#architettura-generale-etl)  
   - [📊 Metabase per l’analisi](#metabase-per-lanalisi)  
   - [🌱 Web App Java Spring Boot](#web-app-java-spring-boot)

---

## 🔰 Introduzione
Il sistema rappresentato integra più tecnologie per raccogliere, trasformare, analizzare e visualizzare i dati provenienti da macchinari industriali (**FORGIA** e **CNC**) in un ambiente centralizzato.

### Obiettivi principali
- **Monitorare** i dati in tempo reale.
- **Analizzare** le prestazioni e identificare anomalie o inefficienze.
- **Generare previsioni** basate su dati storici, migliorando i processi decisionali aziendali.

<div align="center">
  <img src="https://github.com/chrigrosso00/Lab_Integrato/blob/main/workflow.drawio.png" alt="WorkFlow">
</div>

---

## 🔨 Struttura del Sistema
- **Macchine (FORGIA e CNC):** Generano dati di produzione (es. temperature, anomalie, pezzi prodotti).
- **Database:** Punto centrale di archiviazione dei dati.  
  - [MySQL (Produzione)](#database-mysql-produzione)  
  - [PostgreSQL (Staging)](#database-postgresql-staging)
- **Power Automate:** Automatizza i flussi di dati verso l’ETL.
- **ETL (Python/Flask):** Estrae, trasforma e carica i dati nei database di staging e produzione.
- **Power Apps:** Interfaccia in tempo reale per il monitoraggio delle macchine.
- **Metabase:** Business Intelligence per l’analisi dei dati e la creazione di report.
- **Python ML:** Addestramento di modelli di machine learning predittivi.
- **Java Web App (Spring Boot):** Fornisce un’interfaccia agli utenti (Admin/Clienti) per consultare i dati e gestire gli ordini.

---

## ⚙️ Flusso Complessivo

1. **Macchine (FORGIA e CNC)**:
   - Generano dati di produzione: temperature, pezzi prodotti, anomalie, ecc.

2. **Power Automate**:
   - Automatizza la raccolta e l’invio dei dati al modulo ETL (Flask).

3. **ETL (Extract, Transform, Load)**:
   - Riceve i dati grezzi, li trasforma e li carica nei database:
     - **PostgreSQL** (Staging)
     - **MySQL** (Produzione)

4. **Power Apps**:
   - Visualizza in tempo reale le informazioni raccolte (monitoraggio macchinari).

5. **Metabase**:
   - Fornisce analisi storiche e dashboard interattive (KPI di produzione, ordini, performance).

6. **Python ML**:
   - Elabora i dati storici (stored su MySQL) per addestrare modelli di Machine Learning (es. manutenzione predittiva).

7. **Java Web App**:
   - Consente la gestione delle operazioni, la consultazione dei report e l’interazione con i dati (sia lato Admin che lato Cliente).

---

## 📈 Vantaggi del Sistema
- **Automazione**: Riduce l'intervento manuale nei flussi di dati.
- **Monitoraggio in tempo reale**: Identifica anomalie e inefficienze rapidamente.
- **Analisi approfondita**: Permette di comprendere trend e ottimizzare le decisioni aziendali.
- **Previsioni affidabili**: Modelli di Machine Learning aiutano a prevedere e prevenire problematiche operative.

---

# 💾 Connessioni ai Database

<div align="center">
  <img src="https://aiven.io/assets/img/aiven-logo.png" alt="Aiven logo" width="600px" height="150px">
</div>

> I database (MySQL e PostgreSQL) utilizzati in questo progetto sono interamente ospitati in cloud su **Aiven**.  
> La scelta di usare due DB diversi è stata “obbligata”: nel piano gratuito di Aiven non si possono attivare più servizi dello stesso DBMS.

---

## **Database MySQL (Produzione)**
<div align="center">
  <img src="https://tse1.mm.bing.net/th?id=OIP.zciMTYy4oAMdBnHXf2OZ_wHaDF&pid=Api" alt="Logo MySQL">
</div>
<details>
  <summary>Mostra diagramma DML (MySQL)</summary>
  <div align="center">
    <img src="https://github.com/chrigrosso00/Lab_Integrato/blob/main/DML.drawio.png" alt="DML MySQL">
  </div>
</details>

---

## **Database PostgreSQL (Staging)**
<div align="center">
  <img src="https://www.lightcrest.com/wp-content/uploads/2019/04/postgresql-logo.png" alt="Logo postgreSQL">
</div>
<details>
  <summary>Mostra diagramma DML (PostgreSQL)</summary>
  <div align="center">
    <img src="https://github.com/chrigrosso00/Lab_Integrato/blob/main/Raw_DML.drawio.png" alt="Raw DML PostgreSQL">
  </div>
</details>

---

<div align="center">
  <img src="https://res.cloudinary.com/secretsaas/image/upload/v1655733591/logo/Scalingo.png" alt="Logo Scalingo">
</div>

# 🐍 **ETL Python in Cloud**

L'applicazione è accessibile al seguente indirizzo:  
**[ETL Project](https://pythonetl.osc-fr1.scalingo.io/)**  

### **API**

1. **Run Script**  
   - `GET https://pythonetl.osc-fr1.scalingo.io/run`  
   Avvia lo script Python per modellare tutti i dati dal database PostgreSQL e li salva su MySQL.

2. **Status**  
   - `GET https://pythonetl.osc-fr1.scalingo.io/status`  
   Restituisce i dati processati con successo.

3. **Logs**  
   - `GET https://pythonetl.osc-fr1.scalingo.io/logs`  
     Mostra i log del progetto per monitorare le attività e diagnosticare eventuali problemi.  
   - `GET https://pythonetl.osc-fr1.scalingo.io/log-cron`  
     Mostra i log delle attività eseguite periodicamente (cron).  
   - `GET https://pythonetl.osc-fr1.scalingo.io/clear-logs`  
     Pulisce i file di log.

4. **Dati**  
   - `GET https://pythonetl.osc-fr1.scalingo.io/data`  
     Mostra le tabelle `Forgiatura` e `dati_anomali` del database MySQL e i dati registrati.

---

# 📖 Documentazione Tecnica

In questa sezione sono incluse le informazioni specifiche sull’applicazione **ETL (Extract, Transform, Load)** in **Python/Flask**.

---

## 📖 Descrizione

Questa applicazione **ETL (Extract, Transform, Load)** è sviluppata in **Python** con il framework **Flask**.  
Si occupa di:

- Gestire l’elaborazione e la validazione di dati di produzione provenienti da **Power Automate** o altre fonti.  
- Integrare i dati su un **database di staging (PostgreSQL)** e successivamente su un **database di produzione (MySQL)**.  
- Fornire diversi **endpoint REST** per avviare, monitorare e gestire l’intero processo ETL e gli ordini di produzione.  
- Fornire dati analizzabili tramite **Metabase** e visualizzabili/gestibili in una web app **Java Spring Boot** per l’area Admin e Clienti.

---

## ✨ Funzionalità principali

### 1. **Estrazione**
- I dati JSON vengono ricevuti tramite endpoint REST o estratti dalla tabella `raw_operazione` in **PostgreSQL** (staging).  
- Integrazione con **Microsoft Power Automate** per ricevere o inviare dati relativi agli ordini o alla produzione.

### 2. **Validazione con Pydantic**
- Controlli sui dati:  
  - **Timestamp** validi.  
  - Range corretti per campi come `peso_effettivo` e `temperatura_effettiva`.  
  - Validità del tipo di operazione (`forgiatura`, `cnc`, ecc.).  
- I campi non validi vengono impostati a `None` (o `NULL`) e segnalati come anomalie in tabelle dedicate (es. `anomalia_operazione`).

### 3. **Trasformazione**
- Applicazione di logiche di business:  
  - Calcolo di parametri specifici per la **forgiatura** (es. peso/temperatura).  
  - Gestione dei pezzi prodotti da macchine **CNC** (eventuali tipi di fermo, scarti, ecc.).  
  - Registrazione di ulteriori anomalie o campi derivati.

### 4. **Caricamento**
- I dati validi (o con anomalie) vengono caricati in **MySQL** (database di produzione).  
- Aggiornamento automatico delle tabelle `operazioni`, `forgiatura`, `cnc` e `anomalia_operazione`.  

### 5. **Gestione Ordini**
- Aggiornamento degli ordini:  
  - Ordini completati quando i pezzi sono interamente prodotti.  
  - Creazione di dati fittizi per simulazioni, se necessario.  
- Decremento dei pezzi nel magazzino o riordino automatizzato se la scorta minima è raggiunta.

### 6. **Logging**
- Registrazione di tutte le attività in file di log dedicati (`etl.log`, `periodic.log`, `postgresql.log`, `mysql.log`).

### 7. **Sicurezza**
- Endpoint protetti tramite **API_KEY** da inserire nell’header (`X-API-KEY`).  
- Variabili d’ambiente per gestire credenziali e configurazioni.

### 8. **Monitoraggio**
- Tabella di tracking in **PostgreSQL** (`etl_tracked_actions`) per monitorare lo stato del processo ETL, gestire errori (`FAILURE`) e successi (`SUCCESS`).

---

## 🔄 Flusso di lavoro (dettaglio ETL)

1. **Chiamata iniziale agli ordini**  
   - **Power Automate** (o altro client) invoca `GET /ordine` per ottenere i pezzi da produrre.  
   - L’endpoint Flask risponde con i dettagli degli ordini o con un set di dati fittizi se non ci sono ordini pendenti.

2. **Invio dati di produzione**  
   - Una volta prodotti i pezzi, **Power Automate** invia un JSON con i dettagli (temperatura, peso, quantità prodotta, ecc.) a `POST /insert-postgres`.  
   - L’app Flask inserisce i dati in `raw_operazione` su **PostgreSQL** (staging).

3. **Trasferimento dati e validazione**  
   - Ogni ora (o altra schedulazione) si avvia l’endpoint `POST /process-transfer`, che:  
     - Estrae i record da `raw_operazione` in PostgreSQL.  
     - Valida i dati con **Pydantic** (settaggio di `None`/`NULL` per campi invalidi e registrazione anomalie).  
     - Inserisce i record corretti in **MySQL** nelle tabelle `operazioni`, `forgiatura`, `cnc`, ecc.

4. **Aggiornamento ordini e magazzino**  
   - L’endpoint `POST /aggiorna/ordine` aggiorna la quantità rimanente dei pezzi e setta lo stato degli ordini a `COMPLETATO` quando tutti i pezzi richiesti sono stati prodotti.  
   - In mancanza di ordini, la logica di business può prevedere la produzione per rifornire il magazzino (logica “make-to-stock”).

---

## 🌐 Endpoint REST

### Avvio / Processi ETL
| **Endpoint**               | **Descrizione**                                                       |
|----------------------------|-----------------------------------------------------------------------|
| **`POST /run-etl`**        | Avvia il processo ETL in un thread separato (o in foreground).        |
| **`POST /insert-postgres`**| Inserisce dati in `raw_operazione` (staging) di PostgreSQL.           |
| **`POST /process-transfer`**| Trasferisce e valida i dati da PostgreSQL a MySQL.                   |

### Monitoraggio
| **Endpoint**               | **Descrizione**                                                       |
|----------------------------|-----------------------------------------------------------------------|
| **`GET /status`**          | Restituisce lo stato corrente del processo ETL.                       |
| **`GET /actions/errors`**  | Restituisce i record con `FAILURE` da `etl_tracked_actions`.           |
| **`GET /actions`**         | Restituisce tutti i record di `etl_tracked_actions`, ordinati per timestamp. |

### Gestione Ordini
| **Endpoint**               | **Descrizione**                                                       |
|----------------------------|-----------------------------------------------------------------------|
| **`GET /ordine`**          | Restituisce un set di pezzi da processare o crea record fittizi.      |
| **`POST /aggiorna/ordine`**| Aggiorna lo stato degli ordini, decrementa quantità e imposta `COMPLETATO`. |

### Log e Manutenzione
| **Endpoint**               | **Descrizione**                                                       |
|----------------------------|-----------------------------------------------------------------------|
| **`GET /logs`**            | Mostra i log di `etl.log`.                                            |
| **`GET /mysql-logs`**      | Mostra i log relativi a MySQL.                                        |
| **`GET /postgresql-logs`** | Mostra i log relativi a PostgreSQL.                                   |
| **`GET /clear-logs`**      | Cancella uno o più file di log (con eventuale backup).                |

---

## 🔍 Monitoraggio processi ETL

L’applicazione registra i dettagli delle operazioni ETL nella tabella `etl_tracked_actions` su **PostgreSQL**.  
Le informazioni principali includono:

- `action_type`: Tipo di operazione (ad es. `process_transfer`, `insert_mysql`).  
- `status`: Stato (`SUCCESS` o `FAILURE`).  
- `details`: JSONB con info aggiuntive (numero di record, timestamp, ecc.).  
- `error_message`: Messaggi di errore o eccezioni.

### Endpoint di monitoraggio dedicati
- **`GET /actions`**  
  Restituisce tutti i record, ordinati in ordine decrescente di timestamp.  
- **`GET /actions/errors`**  
  Restituisce i soli record con `status = FAILURE`.  

**Benefici**  
- Rapido debug per individuare errori o processi falliti.  
- Storico completo dei processi ETL e dei volumi di dati elaborati.

---

## 📊 Logging

Tutti i log sono archiviati nella directory `logs/`. Di seguito i principali file:

- **`etl.log`**: Eventi principali del processo ETL.  
- **`periodic.log`**: Attività schedulate (cron job o task pianificati).  
- **`postgresql.log`**: Eventuali errori o avvisi relativi a PostgreSQL.  
- **`mysql.log`**: Eventuali errori o avvisi relativi a MySQL.

> **Best Practice**: consultare periodicamente questi log e archiviarli o ruotarli (log rotation) per evitare file di dimensioni eccessive.

---

## 💡 Tecnologie utilizzate

- **Python** 🐍  
  - Per la logica ETL e la creazione degli endpoint REST.  
- **Flask** 🌐  
  - Framework leggero per il web server e la definizione di rotte REST.  
- **Pydantic** ✅  
  - Validazione formale dei dati in ingresso.  
- **PostgreSQL** 🐘  
  - Database di staging per la prima memorizzazione e validazione dei dati.  
- **MySQL** 🐬  
  - Database di produzione per il consolidamento dei dati finali.  
- **Microsoft Power Automate**  
  - Orchestrazione di chiamate HTTP, invio e ricezione dati di produzione.  
- **Metabase**  
  - Analisi e creazione di dashboard per KPI di produzione, ordini, performance.  
- **Java Spring Boot**  
  - Web app per l’area Admin e Clienti, con eventuali integrazioni a Metabase.

---

## ⚙️ Architettura generale (ETL)

1. **Applicazione Flask (ETL)**  
   - Espone endpoint REST per ricevere dati di produzione e ordini.  
   - Esegue validazioni e logiche di business di primo livello.  
   - Salva i dati inizialmente su PostgreSQL (staging).  

2. **Processo ETL Orario (o programmato)**  
   - Estrae i record da `raw_operazione`.  
   - Valida i dati con Pydantic.  
   - Carica i dati puliti su MySQL (database di produzione).  

3. **Power Automate** (se presente)  
   - Effettua richieste a Flask (`GET /ordine` e `POST /insert-postgres`).  
   - Gestisce la logica di “chiamata automatica” in base ad orari o eventi.

4. **Metabase**  
   - Connesso a MySQL per la visualizzazione e l’analisi dei dati.  
   - Crea dashboard su metriche di produzione, ordini, performance macchinari.  

5. **Java Spring Boot (Web App)**  
   - Fornisce un’interfaccia di consultazione per l’Admin (proprietario/responsabili).  
   - Area riservata al cliente, per visualizzare i propri ordini e creare nuove richieste.  
   - Può integrare Metabase tramite embedded dashboard o API.

---

## 📊 Metabase per l’analisi

- **Connessione**: Metabase si collega direttamente al database MySQL di produzione.  
- **Dashboard**:  
  - **Dashboard Admin**: KPI di produzione, performance macchinari, scarti, efficienze.  
  - **Dashboard Cliente**: Stato ordini, pezzi prodotti, consegne.  

In questo modo, l’applicazione Flask si concentra sulla gestione dei dati, mentre Metabase offre una **Business Intelligence** semplice ed efficace.

---

## 🌱 Web App Java Spring Boot

1. **Area Admin**  
   - Visualizzazione dei report Metabase (embedded o tramite link).  
   - Gestione di utenti, ruoli e parametri di produzione (soglie, notifiche).  

2. **Area Clienti**  
   - Visualizzazione ordini e stato della produzione in tempo reale.  
   - Possibilità di creare nuovi ordini o richiedere modifiche.  

3. **Sicurezza e integrazione**  
   - Autenticazione (OAuth, JWT o session-based).  
   - Chiamate REST verso l’app Flask per sincronizzare i dati in tempo reale.

---
