# Lab_Integrato
Repo del corso Laboratorio Integrato (ERP-Fintech)
***
![Logo](https://tse1.mm.bing.net/th?id=OIP.zciMTYy4oAMdBnHXf2OZ_wHaDF&pid=Api)
***
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
***
![Logo](https://www.lightcrest.com/wp-content/uploads/2019/04/postgresql-logo.png)
***
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
***
![Logo](https://res.cloudinary.com/secretsaas/image/upload/v1655733591/logo/Scalingo.png)
***
# Link etl python in cloude
L'applicazione è accessibile al seguente indirizzo: https://etlprj.osc-fr1.scalingo.io/
## API
- ### https://etlprj.osc-fr1.scalingo.io/run
Fa partire lo script python per modellare tutti i dati dal db postgres ancora da elaborare e li salva su mySQL
- ### https://etlprj.osc-fr1.scalingo.io/status
Restituisce i dati processati con successo
- ### https://etlprj.osc-fr1.scalingo.io/logs
Mostra i log del progetto per monitorare le attività e diagnosticare eventuali problemi.
