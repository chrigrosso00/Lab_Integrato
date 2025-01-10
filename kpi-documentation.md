# KPI Produzione

## 1. Efficacia Complessiva delle Attrezzature (OEE)
**Scopo**: Misura l'efficacia complessiva delle attrezzature di produzione monitorando il totale delle operazioni, il tempo medio di operazione e i guasti delle apparecchiature.

```sql
SELECT
    m.codice_macchinario AS codice_macchina,
    COUNT(o.id_operazione) as totale_operazioni,
    AVG(TIMESTAMPDIFF(MINUTE, o.timestamp_inizio, o.timestamp_fine)) as tempo_medio_operazione,
    COUNT(ao.id_anomalia) as totale_anomalie
FROM macchinari m
LEFT JOIN operazioni o ON m.codice_macchinario = o.codice_macchinario
LEFT JOIN anomalia_operazione ao ON o.id_operazione = ao.id_operazione
GROUP BY m.codice_macchinario;
```

## 2. Analisi del Tasso di Anomalie
**Scopo**: Monitora la qualità della produzione tracciando i tassi di anomalie per tipo di prodotto. Questo aiuta a identificare prodotti e processi problematici che potrebbero necessitare di miglioramento o riprogettazione.

```sql
SELECT
    p.codice_pezzo AS codice_prodotto,
    p.nome AS nome_prodotto,
    COUNT(o.id_operazione) as totale_produzioni,
    COUNT(ao.id_anomalia) as anomalie,
    (COUNT(ao.id_anomalia) * 100.0 / COUNT(o.id_operazione)) as percentuale_anomalie
FROM pezzi p
JOIN operazioni o ON p.codice_pezzo = o.codice_pezzo
LEFT JOIN anomalia_operazione ao ON o.id_operazione = ao.id_operazione
GROUP BY p.codice_pezzo, p.nome;
```

## 3. Metriche di Prestazione degli Operatori (rivedere)
**Scopo**: Valuta le prestazioni individuali degli operatori misurando il volume di produzione, la velocità e i tassi di errore. Queste informazioni possono essere utilizzate per la formazione e le revisioni delle prestazioni.

```sql
SELECT
    op.codice_operatore AS id_operatore,
    op.nome AS nome,
    op.cognome AS cognome,
    COUNT(o.id_operazione) as totale_operazioni,
    AVG(TIMESTAMPDIFF(MINUTE, o.timestamp_inizio, o.timestamp_fine)) as tempo_medio_operazione,
    COUNT(ao.id_anomalia) as totale_anomalie
FROM operatori op
LEFT JOIN operazioni o ON op.codice_operatore = o.codice_operatore
LEFT JOIN anomalia_operazione ao ON o.id_operazione = ao.id_operazione
GROUP BY op.codice_operatore, op.nome, op.cognome;
```

## 4. Analisi del Processo di Forgiatura
**Scopo**: Traccia parametri chiave nel processo di forgiatura, inclusa la consistenza del peso e il controllo della temperatura. Questo aiuta a mantenere la qualità del prodotto e la stabilità del processo.

```sql
SELECT
    DATE(o.timestamp_inizio) as data_produzione,
    AVG(f.peso_effettivo) as peso_medio,
    AVG(f.temperatura_effettiva) as temperatura_media,
    COUNT(f.id_anomalia) as anomalie,
    STD(f.peso_effettivo) as deviazione_peso,
    STD(f.temperatura_effettiva) as deviazione_temperatura
FROM forgiatura f
JOIN operazioni o ON f.id_operazione = o.id_operazione
GROUP BY DATE(o.timestamp_inizio)
ORDER BY data_produzione;
```

## 5. Analisi degli Ordini Clienti (rivedere)
**Scopo**: Analizza la performance nell'evasione degli ordini e le metriche di soddisfazione dei clienti. Questo aiuta a ottimizzare i tempi di consegna e migliorare il servizio al cliente.

```sql
SELECT
    c.nome as nome_cliente,
    COUNT(o.id_ordine) as totale_ordini,
    AVG(DATEDIFF(o.data_fine, o.data_inizio)) as tempo_medio_completamento_giorni,
    SUM(o.totale_pezzi) as totale_pezzi,
    COUNT(CASE WHEN o.stato = 'completed' THEN 1 END) as ordini_completati,
    COUNT(CASE WHEN o.stato = 'delayed' THEN 1 END) as ordini_in_ritardo
FROM cliente c
JOIN ordine o ON c.id_cliente = o.id_cliente
GROUP BY c.nome;
```

## 6. Gestione dell'Inventario
**Scopo**: Monitora i livelli di inventario e la rotazione delle scorte per ottimizzare la capacità di stoccaggio e prevenire carenze di stock. Questo aiuta a mantenere operazioni di magazzino efficienti.

```sql
SELECT
    m.codice_pezzo AS codice_prodotto,
    p.nome AS nome_prodotto,
    m.quantita_disponibile as scorta_attuale,
    m.capacita_totale as capacità_totale,
    (m.quantita_disponibile * 100.0 / m.capacita_totale) as percentuale_riempimento,
    m.data_ultimo_aggiornamento as ultimo_aggiornamento,
    DATEDIFF(CURRENT_DATE, m.data_ultimo_aggiornamento) as giorni_da_ultimo_aggiornamento
FROM magazzino m
JOIN pezzi p ON m.codice_pezzo = p.codice_pezzo
WHERE m.data_ultimo_aggiornamento IS NOT NULL;
```

## 7. Tasso di Completamento Ordini
**Scopo**: Misura la percentuale di ordini completati rispetto al totale. Utilizzato per monitorare l'efficienza del processo produttivo.

```sql
WITH OrderMetrics AS (
    SELECT 
        YEAR(data_inizio) as anno,
        MONTH(data_inizio) as mese,
        COUNT(*) as totale_ordini,
        COUNT(CASE WHEN stato = 'completato' THEN 1 END) as ordini_completati
    FROM ordine
    GROUP BY YEAR(data_inizio), MONTH(data_inizio)
)
SELECT 
    anno,
    mese,
    (ordini_completati * 100.0 / totale_ordini) as tasso_completamento
FROM OrderMetrics;
```

## 8. Tempo Medio di Lavorazione
**Scopo**: Analizza i tempi medi di lavorazione per tipo di macchina. Utilizzato per ottimizzare la pianificazione della produzione.

```sql
SELECT 
    m.tipo as tipo_macchina,
    COUNT(o.id_operazione) as totale_operazioni,
    AVG(TIMESTAMPDIFF(SECOND, o.timestamp_inizio, o.timestamp_fine) / 3600) as ore_medie_lavorazione,
    MIN(TIMESTAMPDIFF(SECOND, o.timestamp_inizio, o.timestamp_fine) / 3600) as ore_min_lavorazione,
    MAX(TIMESTAMPDIFF(SECOND, o.timestamp_inizio, o.timestamp_fine) / 3600) as ore_max_lavorazione
FROM operazioni o
JOIN macchinari m ON o.codice_macchinario = m.codice_macchinario
WHERE o.timestamp_fine IS NOT NULL
GROUP BY m.tipo;
```
