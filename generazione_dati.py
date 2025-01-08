import pandas as pd
import numpy as np
import random
from faker import Faker
from datetime import datetime, timedelta
import concurrent.futures
import logging

import cProfile
import pstats
import io

# -------------------------------------------------
# LOGGING SETUP
# -------------------------------------------------
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# -------------------------------------------------
# PARAMETRI PER IL VOLUME DI DATI
# -------------------------------------------------
START_DATE = datetime(2019, 1, 1)
END_DATE = datetime(2025, 1, 1)
GROWTH_RATE = 0.1  # 10% di crescita annua sugli ordini

NUM_CLIENTI = 5000
NUM_PEZZI = 8
NUM_MACCHINARI = 20
NUM_OPERATORI = 50
NUM_ANOMALIE_TIPI = 10

BASE_NUM_ORDINI_ANNO = 30000

# Per Pezzi e ordini
PEZZI_LIST = ['PZ_100', 'PZ_200', 'PZ_300']
PEZZI_PROBS = [0.5, 0.3, 0.2]  # probabilità di scelta
PREZZI_UNITARI_BASE = {'PZ_100': 50, 'PZ_200': 70, 'PZ_300': 100}

# Mappa per la frequenza degli acquisti dei clienti
FREQ_LEVELS = ["Bassa", "Media", "Alta"]
FREQ_WEIGHTS = [0.5, 0.3, 0.2]
FREQ_WEIGHTS_MAP = {"Bassa": 1, "Media": 2, "Alta": 4}
FREQ_QTA_MAP = {
    "Bassa":  (1, 100),
    "Media":  (5, 300),
    "Alta":   (10, 500)
}
FREQ_RIGHE_MAP = {
    "Bassa":  (1, 3),
    "Media":  (2, 5),
    "Alta":   (3, 8)
}

# -- Stagionalità di base (esempio)
SEASONALITY_FACTORS = {
    1: 0.7,  # Gennaio
    2: 0.8,
    3: 1.0,
    4: 1.1,
    5: 1.2,
    6: 1.3,
    7: 0.6,
    8: 0.4,  # Agosto (ferie)
    9: 1.1,
    10: 1.2,
    11: 1.1,
    12: 0.8
}

# -- Hidden patterns per anomalia
HIDDEN_PATTERNS = {
    'anomalia_predictor': {
        'high_temp_threshold': 800,
        'machine_age_threshold': 10,
        'consecutive_ops_threshold': 40
    }
}

# -------------------------------------------------
# FUNZIONI DI SUPPORTO
# -------------------------------------------------
fake = Faker('it_IT')
rng = np.random.default_rng(42)

def calculate_growth_multiplier(base_year, current_year, rate):
    diff = current_year - base_year
    return (1 + rate) ** diff

def get_adjusted_price(codice_pezzo, data_ordine):
    base_year = 2019
    current_year = data_ordine.year
    multiplier = calculate_growth_multiplier(base_year, current_year, GROWTH_RATE)
    return PREZZI_UNITARI_BASE[codice_pezzo] * multiplier

def apply_seasonality(base_value, date):
    """
    Applica un fattore stagionale in base al mese e aggiunge piccola variazione random.
    """
    factor = SEASONALITY_FACTORS[date.month]
    random_variation = np.random.uniform(0.95, 1.05)
    return int(base_value * factor * random_variation)

def calculate_anomaly_probability(operation_data, machine_data, historical_ops):
    """
    Calcola la probabilità di anomalia basata su pattern nascosti (temperatura, età macchina...).
    """
    base_prob = 0.05
    factors = HIDDEN_PATTERNS['anomalia_predictor']

    # Temperatura elevata
    if operation_data.get('temperatura_effettiva', 0) > factors['high_temp_threshold']:
        base_prob *= 1.5

    # Età macchina
    machine_age = datetime.now().year - machine_data['anno_di_acquisto']
    if machine_age > factors['machine_age_threshold']:
        base_prob *= 1 + (machine_age - factors['machine_age_threshold']) * 0.1

    # Operazioni consecutive su questa macchina
    mac_code = machine_data['codice_macchinario']
    consecutive_ops = len(historical_ops.get(mac_code, []))
    if consecutive_ops > factors['consecutive_ops_threshold']:
        base_prob *= 1 + (consecutive_ops - factors['consecutive_ops_threshold']) * 0.05

    return min(base_prob, 0.95)


# -------------------------------------------------
# FUNZIONI PER LA GENERAZIONE DELLE TABELLE BASE
# -------------------------------------------------
def generate_clienti(fake, num_clienti, rng):
    clienti_list = []
    for i in range(1, num_clienti + 1):
        freq_level = rng.choice(FREQ_LEVELS, p=FREQ_WEIGHTS)
        clienti_list.append({
            "id_cliente": f"CL_{i}",
            "nome": fake.company(),
            "citta": fake.city(),
            "frequenza_acquisti": freq_level
        })
    return pd.DataFrame(clienti_list)

def generate_acciai(rng):
    acciaio_tipi = ["Inox", "Carbonio", "LegheSpeciali", "Alluminio", "Titanio",
                   "Ottone", "Bronzo", "Ghisa", "AcciaioTool", "AltroAcciaio"]
    acciai_list = []
    for i, acc in enumerate(acciaio_tipi, start=1):
        acciai_list.append({
            "id": i,
            "tipo_acciaio": acc,
            "prezzo_al_kg": round(rng.uniform(2, 25), 2)
        })
    return pd.DataFrame(acciai_list)

def generate_pezzi(num_pezzi, acciaio_tipi, rng):
    pezzi_list = []
    for i in range(1, num_pezzi + 1):
        acciaio_scelto = rng.choice(acciaio_tipi)
        peso_min = rng.uniform(0.5, 5.0)
        peso_max = peso_min + rng.uniform(1.0, 6.0)
        temp_min = int(rng.integers(200, 601))
        temp_max = temp_min + int(rng.integers(50, 501))
        pezzi_list.append({
            "codice_pezzo": f"PZ_{i}",
            "tipo_acciaio": acciaio_scelto,
            "peso_min": round(peso_min, 2),
            "peso_max": round(peso_max, 2),
            "temperatura_min": temp_min,
            "temperatura_max": temp_max
        })
    return pd.DataFrame(pezzi_list)

def generate_costi_macchinari(num_macchinari, rng):
    costi_list = []
    macchinari_list = []
    for i in range(1, num_macchinari + 1):
        id_costo = i
        tipo_costo = rng.choice(["acquisto", "manutenzione", "leasing"])
        entita_collegata = f"MAC_{i}"
        valore = round(rng.uniform(10000, 60000), 2)
        costi_list.append({
            "id_costo": id_costo,
            "tipo_costo": tipo_costo,
            "entita_collegata": entita_collegata,
            "valore": valore
        })
        anno_acquisto = int(rng.integers(2005, 2019))
        eff_base = int(rng.integers(80, 121))
        macchinari_list.append({
            "codice_macchinario": entita_collegata,
            "tipo": rng.choice(["CNC", "Forgiatura", "Stampaggio"]),
            "anno_di_acquisto": anno_acquisto,
            "efficienza_base": eff_base,
            "id_costo": id_costo
        })

    return pd.DataFrame(costi_list), pd.DataFrame(macchinari_list)

def generate_operatori(fake, num_operatori, rng):
    operators_list = []
    for i in range(1, num_operatori + 1):
        operators_list.append({
            "codice_operatore": f"OP_{i}",
            "nome": fake.name(),
            "costo_orario": round(rng.uniform(10, 40), 2),
            "esperienza": rng.choice(["Bassa", "Media", "Alta"])
        })
    return pd.DataFrame(operators_list)

def generate_anomalia(fake, num_anomalie, rng):
    anom_list = []
    for i in range(1, num_anomalie + 1):
        anom_list.append({
            "id_anomalia": i,
            "tipo_anomalia": f"Tipo_Anomalia_{i}",
            "note": fake.sentence()
        })
    return pd.DataFrame(anom_list)


# -------------------------------------------------
# GENERAZIONE MAGAZZINO (UNICO) + RESTOCCAGGI
# -------------------------------------------------
def generate_magazzino(fake, pezzi_list, rng):
    magazzino_list = []
    NUM_RESTOCCAGGI = 1000
    for i in range(1, NUM_RESTOCCAGGI + 1):
        id_mag = 1
        anno_rif = random.randint(2019, 2024)
        growth_mult = calculate_growth_multiplier(2019, anno_rif, GROWTH_RATE)
        pz_scelto = random.choice(pezzi_list)
        base_capacita = rng.integers(5000, 20001)
        base_quantita = rng.integers(1000, 10001)
        cap_tot = int(base_capacita * growth_mult)
        qta_disp = int(base_quantita * growth_mult)
        magazzino_list.append({
            "id_magazzino": id_mag,
            "id_restocco": i,
            "codice_pezzo": pz_scelto["codice_pezzo"],
            "capacita_totale": cap_tot,
            "quantita_disponibile": qta_disp,
            "tipo_acciaio": pz_scelto["tipo_acciaio"],
            "data_ultimo_aggiornamento": fake.date_between(start_date='-3y', end_date='today'),
            "anno_riferimento": anno_rif
        })
    return pd.DataFrame(magazzino_list)


# -------------------------------------------------
# FUNZIONI GENERAZIONE ORDINI (PARALLELIZZATA)
# -------------------------------------------------
def generate_ordini_for_year(year, clienti_df, rng_seed):
    local_rng = np.random.default_rng(rng_seed + year)
    ordini_list = []
    pezzi_ordine_list = []

    clienti_weights = clienti_df['frequenza_acquisti'].map(FREQ_WEIGHTS_MAP).tolist()
    clienti_prob = np.array(clienti_weights) / np.sum(clienti_weights)
    clienti_records = clienti_df.to_dict('records')

    all_prices = list(PREZZI_UNITARI_BASE.values())
    min_price, max_price = min(all_prices), max(all_prices)
    alpha_price = 0.8

    multiplier = calculate_growth_multiplier(2019, year, GROWTH_RATE)
    num_ordini_anno = int(BASE_NUM_ORDINI_ANNO * multiplier)

    year_start = datetime(year, 1, 1)
    next_year_start = datetime(year + 1, 1, 1)
    if next_year_start > END_DATE:
        next_year_start = END_DATE

    total_days_year = (next_year_start - year_start).days

    for i in range(num_ordini_anno):
        id_ordine = f"ORD_{year}_{i}"
        cliente_scelto = local_rng.choice(clienti_records, p=clienti_prob)

        offset_days = int(local_rng.integers(0, total_days_year + 1))
        data_inizio = year_start + timedelta(days=offset_days)

        stato = local_rng.choice(["in_corso", "completato", "annullato"])
        data_fine = None
        if stato == "completato":
            delta_days = int(local_rng.integers(1, 31))
            data_fine = data_inizio + timedelta(days=delta_days)
            if data_fine > END_DATE:
                data_fine = None

        freq = cliente_scelto["frequenza_acquisti"]
        righe_min, righe_max = FREQ_RIGHE_MAP[freq]
        n_pezzi_ordine = local_rng.integers(righe_min, righe_max + 1)

        tot_pezzi = 0
        min_qta, max_qta = FREQ_QTA_MAP[freq]

        for _p in range(n_pezzi_ordine):
            chosen_pz = local_rng.choice(PEZZI_LIST)
            base_qta = local_rng.integers(min_qta, max_qta + 1)
            price = PREZZI_UNITARI_BASE[chosen_pz]
            price_factor = 1.0 + alpha_price * (max_price - price)/(max_price - min_price)
            final_qta = int(round(base_qta * price_factor))

            rim = int(local_rng.integers(0, final_qta + 1)) if stato != "in_corso" else final_qta

            pezzi_ordine_list.append({
                "id_ordine": id_ordine,
                "id_pezzo": chosen_pz,
                "quantita_totale": final_qta,
                "quantita_rimanente": rim
            })
            tot_pezzi += final_qta

        ordini_list.append({
            "id_ordine": id_ordine,
            "id_cliente": cliente_scelto["id_cliente"],
            "data_inizio": data_inizio,
            "data_fine": data_fine,
            "totale_pezzi": tot_pezzi,
            "stato": stato
        })

    return ordini_list, pezzi_ordine_list

def generate_all_ordini(clienti_df, rng_seed=42):
    anni = range(2019, 2025)
    all_ordini = []
    all_pezzi = []

    with concurrent.futures.ThreadPoolExecutor(max_workers=4) as executor:
        futures = []
        for year in anni:
            futures.append(
                executor.submit(generate_ordini_for_year, year, clienti_df, rng_seed)
            )
        for fut in concurrent.futures.as_completed(futures):
            ordini_list, pezzi_list = fut.result()
            all_ordini.extend(ordini_list)
            all_pezzi.extend(pezzi_list)

    return pd.DataFrame(all_ordini), pd.DataFrame(all_pezzi)


# -------------------------------------------------
# NUOVA VERSIONE: GENERAZIONE OPERAZIONI ENHANCED
# -------------------------------------------------
def generate_operazioni_enhanced(
    ordini_list, macchinari_df, operatori_df, anomalia_df, rng, fake
):
    """
    Versione migliorata della generate_operazioni con:
    - Stagionalità (apply_seasonality) per il numero di operazioni
    - Pattern nascosti di anomalia
    - Logging
    """
    logger.info("Inizio generazione operazioni (enhanced)...")

    operazioni_list = []
    forgiatura_list = []
    cnc_list = []
    anomalia_operazione_list = []

    # Tracking storico operazioni per macchina (pattern anomalia)
    historical_ops = {}

    id_operazione = 1
    id_forgiatura = 1
    id_cnc = 1
    internal_anom_id = 1

    macchinari_data = macchinari_df.to_dict('records')
    operatori_data = operatori_df.to_dict('records')
    anom_list = anomalia_df.to_dict('records')
    carico_lavoro = {m['codice_macchinario']: 0 for m in macchinari_data}

    for ordine in ordini_list:
        data_inizio_ordine = ordine["data_inizio"]
        if not data_inizio_ordine:
            data_inizio_ordine = datetime(2020, 1, 1)

        # Applica stagionalità al numero di operazioni
        base_num_ops = rng.integers(2, 9)
        num_ops = apply_seasonality(base_num_ops, data_inizio_ordine)

        for _ in range(num_ops):
            # Selezione macchinario
            chosen_mac = rng.choice(macchinari_data)
            mac_code = chosen_mac["codice_macchinario"]
            if mac_code not in historical_ops:
                historical_ops[mac_code] = []

            # Costruzione dell'operazione base
            chosen_op = rng.choice(operatori_data)
            ts_start = data_inizio_ordine + timedelta(
                days=int(rng.integers(0, 11)),
                hours=int(rng.integers(0, 13))
            )
            ts_end = ts_start + timedelta(hours=int(rng.integers(1, 11)))

            base_durata_min = (ts_end - ts_start).total_seconds() / 60.0
            anno_acq = chosen_mac["anno_di_acquisto"]
            op_year = ts_start.year
            eta_mac = op_year - anno_acq

            penalty_age = max(0, eta_mac - 5)
            eff_base = max(chosen_mac["efficienza_base"] - penalty_age, 10)

            usage_count = carico_lavoro[mac_code]
            penalty_usage_factor = (usage_count // 50) * 0.001

            durata_finale = base_durata_min * (100 / eff_base)
            durata_finale *= (1 + penalty_usage_factor)
            durata_finale = int(round(durata_finale))

            costo_operazione = round(durata_finale * 0.5, 2)

            op_id = f"OP_{id_operazione}"
            operazione_data = {
                "id_operazione": op_id,
                "id_ordine": ordine["id_ordine"],
                "codice_pezzo": rng.choice(PEZZI_LIST),
                "codice_macchinario": mac_code,
                "codice_operatore": chosen_op["codice_operatore"],
                "timestamp_inizio": ts_start,
                "timestamp_fine": ts_end,
                "durata_min": durata_finale,
                "costo": costo_operazione
            }
            operazioni_list.append(operazione_data)

            # Aggiungo l'operazione alla history
            historical_ops[mac_code].append(op_id)
            carico_lavoro[mac_code] += 1

            # Gestione "Forgiatura" e "CNC" con pattern anomalia
            if chosen_mac["tipo"] == "Forgiatura":
                temp_eff = int(rng.integers(300, 1201))
                peso_eff = round(rng.uniform(0.5, 10.0), 2)
                # Calcola anomalia
                anom_prob = calculate_anomaly_probability(
                    {'temperatura_effettiva': temp_eff, 'peso_effettivo': peso_eff},
                    chosen_mac,
                    historical_ops
                )
                id_forg = f"FG_{id_forgiatura}"
                forg_data = {
                    "id_forgiatura": id_forg,
                    "id_operazione": op_id,
                    "peso_effettivo": peso_eff,
                    "temperatura_effettiva": temp_eff,
                    "id_anomalia": None
                }
                if rng.random() < anom_prob:
                    chosen_anomalia = rng.choice(anom_list)
                    forg_data['id_anomalia'] = chosen_anomalia['id_anomalia']
                    anomalia_operazione_list.append({
                        "internal_id": internal_anom_id,
                        "id_anomalia": chosen_anomalia['id_anomalia'],
                        "id_operazione": op_id,
                        "note": fake.sentence()
                    })
                    internal_anom_id += 1
                forgiatura_list.append(forg_data)
                id_forgiatura += 1

            elif chosen_mac["tipo"] == "CNC":
                num_pezzi_ora = int(rng.integers(5, 61))
                tipo_fermo = rng.choice(["utensile", "software", "nessun_fermo"])
                # Se la macchina è molto utilizzata, + prob. di fermo
                if usage_count > 30 and rng.random() < 0.3:
                    tipo_fermo = rng.choice(["utensile", "software"])

                id_cnc_rec = f"CNC_{id_cnc}"
                cnc_data = {
                    "id_cnc": id_cnc_rec,
                    "id_operazione": op_id,
                    "numero_pezzi_ora": num_pezzi_ora,
                    "tipo_fermo": tipo_fermo
                }
                cnc_list.append(cnc_data)

                # Se c'è un fermo, più prob. di anomalia
                if tipo_fermo != "nessun_fermo" and rng.random() < 0.4:
                    chosen_anomalia = rng.choice(anom_list)
                    anomalia_operazione_list.append({
                        "internal_id": internal_anom_id,
                        "id_anomalia": chosen_anomalia['id_anomalia'],
                        "id_operazione": op_id,
                        "note": fake.sentence()
                    })
                    internal_anom_id += 1

                id_cnc += 1

            id_operazione += 1

    logger.info(f"Generazione operazioni completata! Totale: {len(operazioni_list)}")

    return (
        pd.DataFrame(operazioni_list),
        pd.DataFrame(forgiatura_list),
        pd.DataFrame(cnc_list),
        pd.DataFrame(anomalia_operazione_list)
    )

# -------------------------------------------------
# SALVATAGGIO DEI CSV
# -------------------------------------------------
def save_csv_files(dfs, filenames):
    for df, filename in zip(dfs, filenames):
        df.to_csv(filename, index=False)
    logger.info("Tutti i file CSV sono stati generati e salvati con successo!")

# -------------------------------------------------
# VALIDAZIONE
# -------------------------------------------------
def validate_data(ordine_df, pezzi_ordine_df):
    assert ordine_df['id_ordine'].is_unique, "ID Ordine duplicati!"
    assert pezzi_ordine_df['id_ordine'].isin(ordine_df['id_ordine']).all(), \
           "Esistono pezzi d'ordine con ID Ordine non validi."
    logger.info("Validazione dei dati completata con successo!")

# -------------------------------------------------
# MAIN GENERATION PROCESS
# -------------------------------------------------
def main():
    # 1) TABELLE BASE
    clienti_df = generate_clienti(fake, NUM_CLIENTI, rng)
    acciai_df = generate_acciai(rng)
    pezzi_df = generate_pezzi(NUM_PEZZI, acciai_df['tipo_acciaio'].tolist(), rng)
    costi_df, macchinari_df = generate_costi_macchinari(NUM_MACCHINARI, rng)
    operatori_df = generate_operatori(fake, NUM_OPERATORI, rng)
    anomalia_df = generate_anomalia(fake, NUM_ANOMALIE_TIPI, rng)
    magazzino_df = generate_magazzino(fake, pezzi_df.to_dict('records'), rng)

    # 2) ORDINI + PEZZI_ORDINE (Parallelizzati per anno)
    ordine_df, pezzi_ordine_df = generate_all_ordini(clienti_df, rng_seed=42)

    # 3) OPERAZIONI (Enhanced: con stagionalità e pattern nascosti)
    # Nota: sostituiamo la generate_operazioni standard con la "enhanced" appena definita
    operazioni_df, forgiatura_df, cnc_df, anomalia_operazione_df = generate_operazioni_enhanced(
        ordine_df.to_dict('records'),
        macchinari_df,
        operatori_df,
        anomalia_df,
        rng,
        fake
    )

    # 4) Salvataggio
    dfs = [
        clienti_df,
        acciai_df,
        pezzi_df,
        costi_df,
        macchinari_df,
        operatori_df,
        magazzino_df,
        anomalia_df,
        ordine_df,
        pezzi_ordine_df,
        operazioni_df,
        forgiatura_df,
        cnc_df,
        anomalia_operazione_df
    ]

    filenames = [
        "cliente.csv",
        "acciai.csv",
        "pezzi.csv",
        "costi.csv",
        "macchinari.csv",
        "operatori.csv",
        "magazzino.csv",
        "anomalia.csv",
        "ordine.csv",
        "pezzi_ordine.csv",
        "operazioni.csv",
        "forgiatura.csv",
        "cnc.csv",
        "anomalia_operazione.csv"
    ]

    save_csv_files(dfs, filenames)
    validate_data(ordine_df, pezzi_ordine_df)

if __name__ == "__main__":
    main()
