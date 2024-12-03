# Lab_Integrato
Repo del corso Laboratorio Integrato (ERP-Fintech)

# Stringa connessione database MySQL (produzione)
## Database name
defaultdb
## Host
lab-integrato-nicola03-3bd5.f.aivencloud.com
## Port
16921
## User
avnadmin
## Password
AVNS_frpyP32fGueJJfnhssZ

# Stringa connessione database Postgres (staging)
## Database name
defaultdb
## Host
stagingdb-nicola03-3bd5.b.aivencloud.com
## Port
16921
## User
avnadmin
## Password
AVNS_d8E015YWPMvo61VuSsY

# Link etl python in cloude
L'applicazione è accessibile al seguente indirizzo: https://etlprj.osc-fr1.scalingo.io/
## API
- ### https://etlprj.osc-fr1.scalingo.io/run
Fa partire lo script python per modellare tutti i dati dal db postgres ancora da elaborare e li salva su mySQL
- ### https://etlprj.osc-fr1.scalingo.io/status
Restituisce i dati processati con successo
- ### https://etlprj.osc-fr1.scalingo.io/logs
Mostra i log del progetto per monitorare le attività e diagnosticare eventuali problemi.
