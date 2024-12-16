![Logo](https://res.cloudinary.com/practicaldev/image/fetch/s--QsmIiz9y--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://thepracticaldev.s3.amazonaws.com/i/lnm6ybztq944ikym1s8f.JPG)

# ETL Flask Application

## Descrizione

Questo progetto è un'applicazione ETL (Extract, Transform, Load) sviluppata in Python utilizzando Flask per esporre API web. L'applicazione riceve i dati di produzione grezzi dal flusso power automate tramite una chiamata API, li trasforma secondo specifiche logiche di business e li carica in un database MySQL. Inoltre, fornisce endpoint per avviare il processo ETL, monitorarne lo stato e visualizzare i log.

## Caratteristiche

- **Estrazione**: Riceve i dati in formato Json dal flusso in power automate tramite una chiamata HTTP.
- **Trasformazione**: Elabora i dati, gestendo formati di data e assicurando la presenza delle chiavi necessarie.
- **Controllo**: Controlla i dati estratti e trasformati per controllora eventuali anomalie.
- **Caricamento**: Inserisce i dati trasformati nella tabella `Forgiatura` in MySQL.
- **API Web**: 
  - Avvia il processo ETL tramite endpoint HTTP.
  - Monitora lo stato attuale dell'ETL.
  - Visualizza i log delle operazioni ETL.
- **Logging**: Registra tutte le operazioni e gli errori in un file di log.
- **Configurazione Sicura**: Utilizza variabili d'ambiente per gestire le configurazioni sensibili.
- **API_KEY**: Chiave per le API per proteggere gli endpoint più sensibili.

## Tecnologie Utilizzate

- **Python 3.x**
- **Flask**: Framework web per Python.
- **MySQL**: Database di destinazione.
- **psycopg2**: Libreria per connettersi a PostgreSQL.
- **mysql-connector-python**: Libreria per connettersi a MySQL.
- **python-dotenv**: Gestione delle variabili d'ambiente.
- **Logging**: Modulo standard di Python per la registrazione dei log.
