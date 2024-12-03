# ETL Flask Application

## Descrizione

Questo progetto è un'applicazione ETL (Extract, Transform, Load) sviluppata in Python utilizzando Flask per esporre API web. L'applicazione estrae dati da un database PostgreSQL, li trasforma secondo specifiche logiche di business e li carica in un database MySQL. Inoltre, fornisce endpoint per avviare il processo ETL, monitorarne lo stato e visualizzare i log.

## Caratteristiche

- **Estrazione**: Recupera dati dalla tabella `RawForgiatura` in PostgreSQL con stato `PENDING`.
- **Trasformazione**: Elabora i dati, gestendo formati di data e assicurando la presenza delle chiavi necessarie.
- **Caricamento**: Inserisce i dati trasformati nella tabella `Forgiatura` in MySQL.
- **API Web**: 
  - Avvia il processo ETL tramite endpoint HTTP.
  - Monitora lo stato attuale dell'ETL.
  - Visualizza i log delle operazioni ETL.
- **Logging**: Registra tutte le operazioni e gli errori in un file di log.
- **Configurazione Sicura**: Utilizza variabili d'ambiente per gestire le configurazioni sensibili.

## Tecnologie Utilizzate

- **Python 3.x**
- **Flask**: Framework web per Python.
- **PostgreSQL**: Database di origine.
- **MySQL**: Database di destinazione.
- **psycopg2**: Libreria per connettersi a PostgreSQL.
- **mysql-connector-python**: Libreria per connettersi a MySQL.
- **python-dotenv**: Gestione delle variabili d'ambiente.
- **Logging**: Modulo standard di Python per la registrazione dei log.
