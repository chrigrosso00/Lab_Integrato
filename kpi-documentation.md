# KPI Produzione

## 1. Tasso di Completamento Ordini
**Scopo**: Misura la percentuale di ordini completati rispetto al totale.
**Utilizzo**: Monitorare l'efficienza del processo produttivo.

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

## 2. Tempo Medio di Lavorazione
**Scopo**: Analizza i tempi medi di lavorazione per tipo di macchina.
**Utilizzo**: Ottimizzare la pianificazione della produzione.

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

## 3. Tasso di Anomalie
**Scopo**: Monitora la frequenza di anomalie per macchina.
**Utilizzo**: Identificare macchinari problematici e pianificare manutenzione preventiva.

```sql
SELECT 
    m.codice_macchinario,
    m.tipo,
    COUNT(o.id_operazione) as totale_operazioni,
    COUNT(a.id_anomalia) as numero_anomalie,
    (COUNT(a.id_anomalia) * 100.0 / COUNT(o.id_operazione)) as percentuale_anomalie,
    (SELECT GROUP_CONCAT(DISTINCT a2.tipo_anomalia SEPARATOR ', ')
     FROM anomalia a2
     JOIN anomalia_operazione ao2 ON a2.id_anomalia = ao2.id_anomalia
     JOIN operazioni o2 ON ao2.id_operazione = o2.id_operazione
     WHERE o2.codice_macchinario = m.codice_macchinario) as tipi_anomalie
FROM macchinari m
LEFT JOIN operazioni o ON m.codice_macchinario = o.codice_macchinario
LEFT JOIN anomalia_operazione ao ON o.id_operazione = ao.id_operazione
LEFT JOIN anomalia a ON ao.id_anomalia = a.id_anomalia
GROUP BY m.codice_macchinario, m.tipo;
```

## 4. Overall Equipment Effectiveness (OEE) 
**Scopo**: Questo KPI misura l'efficacia complessiva delle attrezzature di produzione monitorando il totale delle operazioni, il tempo medio di operazione e i guasti delle apparecchiature.
**Utilizzo**: Ottimizzare l'efficacia complessiva.

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

## 5. Analisi del Tasso delle Anomalie
**Scopo**: Monitora la qualità della produzione tracciando i tassi di difetto per tipo di prodotto.
**Utilizzo**:  Questo aiuta a identificare prodotti e processi problematici che potrebbero necessitare di miglioramento o riprogettazione.


```sql
SELECT
    p.codice_pezzo AS codice_prodotto,
    p.nome AS nome_prodotto,
    COUNT(o.id_operazione) as totale_produzioni,
    COUNT(ao.id_anomalia) as difetti,
    (COUNT(ao.id_anomalia) * 100.0 / COUNT(o.id_operazione)) as percentuale_difetti
FROM pezzi p
JOIN operazioni o ON p.codice_pezzo = o.codice_pezzo
LEFT JOIN anomalia_operazione ao ON o.id_operazione = ao.id_operazione
GROUP BY p.codice_pezzo, p.nome;
```
