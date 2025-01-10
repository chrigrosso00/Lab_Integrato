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
    FROM Ordine
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
    STRING_AGG(DISTINCT a.tipo_anomalia, ', ') as tipi_anomalie
FROM Macchinari m
LEFT JOIN Operazioni o ON m.codice_macchinario = o.codice_macchinario
LEFT JOIN Anomalia_operazione ao ON o.id_operazione = ao.id_operazione
LEFT JOIN Anomalia a ON ao.id_anomalia = a.id_anomalia
GROUP BY m.codice_macchinario, m.tipo;
```

## 4. Utilizzo Magazzino
**Scopo**: Monitora l'occupazione del magazzino per tipo di acciaio.
**Utilizzo**: Ottimizzare gestione scorte e pianificare approvvigionamenti.

```sql
SELECT 
    m.tipo_acciaio,
    a.prezzo_al_kg,
    SUM(m.quantita_disponibile) as quantita_totale,
    MAX(m.capacita_totale) as capacita_massima,
    (SUM(m.quantita_disponibile) * 100.0 / MAX(m.capacita_totale)) as percentuale_utilizzo,
    SUM(m.quantita_disponibile * a.prezzo_al_kg) as valore_inventario
FROM Magazzino m
JOIN Acciai a ON m.tipo_acciaio = a.tipo_acciaio
GROUP BY m.tipo_acciaio, a.prezzo_al_kg;
```

## 5. Costo Operativo
**Scopo**: Analizza i costi operativi per macchina e tipo di lavorazione.
**Utilizzo**: Controllo costi e ottimizzazione risorse.

```sql
SELECT 
    m.tipo as tipo_macchina,
    op.nome as operatore,
    COUNT(o.id_operazione) as numero_operazioni,
    SUM(TIMESTAMPDIFF(HOUR, o.timestamp_inizio, o.timestamp_fine) * op.costo_orario) as costo_manodopera,
    SUM(c.valore) as costo_macchina,
    (SUM(TIMESTAMPDIFF(HOUR, o.timestamp_inizio, o.timestamp_fine) * op.costo_orario) + SUM(c.valore)) as costo_totale
FROM Operazioni o
JOIN Macchinari m ON o.codice_macchinario = m.codice_macchinario
JOIN Operatori op ON o.codice_operatore = op.codice_operatore
JOIN Costi c ON m.id_costo = c.id_costo
WHERE o.timestamp_fine IS NOT NULL
GROUP BY m.tipo, op.nome;
```
