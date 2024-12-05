
# **Lab Integrato**
Repo del corso **Laboratorio Integrato (ERP-Fintech)**  
***
![Logo MySQL](https://tse1.mm.bing.net/th?id=OIP.zciMTYy4oAMdBnHXf2OZ_wHaDF&pid=Api)  

## **Stringa connessione database MySQL (Produzione)**

- **Database Name:** `defaultdb`  
- **Host:** `lab-integrato-nicola03-3bd5.f.aivencloud.com`  
- **Port:** `16921`  
- **User:** `avnadmin`  
- **Password:** `AVNS_frpyP32fGueJJfnhssZ`  
---
![Logo PostgreSQL](https://www.lightcrest.com/wp-content/uploads/2019/04/postgresql-logo.png)  

## **Stringa connessione database PostgreSQL (Staging)**

- **Database Name:** `defaultdb`  
- **Host:** `stagingdb-nicola03-3bd5.b.aivencloud.com`  
- **Port:** `16921`  
- **User:** `avnadmin`  
- **Password:** `AVNS_d8E015YWPMvo61VuSsY`  

---
![Logo Scalingo](https://res.cloudinary.com/secretsaas/image/upload/v1655733591/logo/Scalingo.png)  

## **Link ETL Python in Cloud**

L'applicazione è accessibile al seguente indirizzo:  
**[ETL Project](https://etlprj.osc-fr1.scalingo.io/)**  

### **API**

1. **Run Script:**  
   (https://etlprj.osc-fr1.scalingo.io/run)  
   Fa partire lo script Python per modellare tutti i dati dal database PostgreSQL e li salva su MySQL.  

2. **Status:**  
   (https://etlprj.osc-fr1.scalingo.io/status)  
   Restituisce i dati processati con successo.  

3. **Logs:**  
   (https://etlprj.osc-fr1.scalingo.io/logs)  
   Mostra i log del progetto per monitorare le attività e diagnosticare eventuali problemi.  

   (https://etlprj.osc-fr1.scalingo.io/log-cron)  
   Mostra i log del progetto per monitorare le attività eseguite periodicamente.  

   (https://etlprj.osc-fr1.scalingo.io/clear-logs)  
   Pulisce i file di log.  

4. **Dati:**  
   (https://etlprj.osc-fr1.scalingo.io/data)  
   Mostra le tabelle Forgiatura e dati_anomali del database MySQL e i dati registrati.
