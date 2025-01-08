<div align="center"><h1>Pipeline di Generazione Dati per il Settore Manifatturiero e Rilevamento Anomalie</h1></div>

<div align="center">
  <img src="https://learn.temporal.io/assets/images/banner_python-0d345d125b6892840c54f7e1460c8a5a.png" alt="Banner">
</div>

---

## **Indice dei Contenuti**  
1. 📋 [Introduzione](#introduzione)  
2. 🎯 [Funzionalità Principali](#funzionalità-principali)  
3. 🛠️ [Stack Tecnologico](#stack-tecnologico)  
4. 📁 [Modello dei Dati](#modello-dei-dati)  

---

## <div align="center">**Introduzione**</div>  

Questo repository contiene una **pipeline di generazione dati** ideata per simulare i processi di un'azienda manifatturiera. Lo scopo è **produrre dataset realistici** per:  

🔹 **Clienti**, con profili di acquisto differenziati.  
🔹 **Macchinari**, con dati relativi a costi di acquisto e manutenzione.  
🔹 **Operatori**, con costi orari ed esperienze diversificate.  
🔹 **Tipi di Acciaio / Leghe** e **Pezzi** con specifiche tecniche.  
🔹 **Magazzino** per monitorare la disponibilità di materiale e i cicli di restoccaggio.  
🔹 **Ordini** e relative **righe d’ordine**, con stagionalità e tassi di crescita.  
🔹 **Operazioni** di produzione, incluse lavorazioni specifiche di **Forgiatura** e **CNC**.  
🔹 **Anomalie** collegate a macchinari e operazioni, per simulare malfunzionamenti reali.  

🎯 L’obiettivo è fornire dati ad **alto volume e varietà**, da utilizzare per analisi, test di algoritmi di machine learning o per simulare scenari industriali complessi.  

---

## <div align="center">**Funzionalità Principali**</div>  

1. **Tasso di Crescita & Stagionalità**  
   📈 Imposta un fattore di crescita annuale (`GROWTH_RATE`) per il numero di ordini.  
   📅 Applica **fattori di stagionalità** per simulare variazioni di domanda mensili (es. cali ad agosto).  

2. **Pattern Nascosti di Anomalia**  
   🔥 Verifica soglie di **temperatura**, **età della macchina** e **numero di operazioni consecutive**.  
   ⚠️ Aumenta la probabilità di anomalia in condizioni particolari (es. macchine vetuste).  

3. **Generazione di Ordini in Parallelo**  
   ⚙️ Utilizza `ThreadPoolExecutor` per velocizzare la creazione degli ordini tra **2019** e **2025**.  
   🚀 Riduce sensibilmente il tempo di generazione per volumi di dati elevati.  

4. **Operazioni Avanzate (Enhanced)**  
   🕒 Calcolo di **timestamp inizio/fine**, **durata** e **costo** per ogni operazione.  
   🛠️ Adjust dinamico della durata in base all’**età** e all’**uso** del macchinario.  

5. **Output Multiplo in Formato CSV**  
   📁 Produce diversi file CSV (`cliente.csv`, `ordine.csv`, `operazioni.csv`, ecc.), ognuno corrispondente a un’entità o relazione specifica.  

---

## <div align="center">**Stack Tecnologico**</div>  

| **🔧 Tecnologia**         | **📋 Descrizione**                                                |
|--------------------------|------------------------------------------------------------------|
| **Python 3.8+**          | Linguaggio di programmazione principale.                        |
| **pandas**               | Utilizzato per gestire i DataFrame e l’export in CSV.           |
| **numpy**                | Fornisce generatori casuali e funzioni di calcolo su array.     |
| **faker**                | Genera nomi, indirizzi e testi fittizi e verosimili.            |
| **concurrent.futures**   | Abilita l’elaborazione in parallelo (multithreading).           |
| **cProfile** & **pstats**| Strumenti per profilare e analizzare le prestazioni del codice. |

---

## <div align="center">**Modello dei Dati**</div>  

La seguente rappresentazione **semplificata** mostra le principali relazioni tra le tabelle generate dalla pipeline:

### Diagramma delle Relazioni  

```mermaid
erDiagram
    CLIENTI {
        string id_cliente PK
        string nome
        string citta
        string frequenza_acquisti
    }
    
    ORDINE {
        string id_ordine PK
        string id_cliente FK
        datetime data_inizio
        datetime data_fine
        int totale_pezzi
        string stato
    }

    PEZZI_ORDINE {
        string id_ordine FK
        string id_pezzo
        int quantita_totale
        int quantita_rimanente
    }

    OPERAZIONI {
        string id_operazione PK
        string id_ordine FK
        string codice_pezzo
        string codice_macchinario FK
        string codice_operatore FK
        datetime timestamp_inizio
        datetime timestamp_fine
        int durata_min
        float costo
    }

    FORGIATURA {
        string id_forgiatura PK
        string id_operazione FK
        float peso_effettivo
        int temperatura_effettiva
        int id_anomalia FK
    }

    CNC {
        string id_cnc PK
        string id_operazione FK
        int numero_pezzi_ora
        string tipo_fermo
    }

    ANOMALIA_OPERAZIONE {
        int internal_id PK
        int id_anomalia FK
        string id_operazione FK
        string note
    }

    CLIENTI ||--|{ ORDINE : "effettua"
    ORDINE ||--|{ PEZZI_ORDINE : "comprende"
    ORDINE ||--|{ OPERAZIONI : "richiede"
    OPERAZIONI ||--|{ FORGIATURA : "eventuale"
    OPERAZIONI ||--|{ CNC : "eventuale"
    FORGIATURA }|--|| ANOMALIA_OPERAZIONE : "può generare anomalia"
    CNC }|--|| ANOMALIA_OPERAZIONE : "può generare anomalia"
```
