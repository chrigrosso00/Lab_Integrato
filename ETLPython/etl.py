import os
import psycopg2
import mysql.connector
import logging
from dotenv import load_dotenv
from datetime import datetime

# Configure logging
logging.basicConfig(
    filename='logs/etl.log',
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)

# Load environment variables from .env file
load_dotenv(dotenv_path='config/.env')

# PostgreSQL connection parameters
PG_HOST = os.getenv('PG_HOST')
PG_PORT = os.getenv('PG_PORT')
PG_DATABASE = os.getenv('PG_DATABASE')
PG_USER = os.getenv('PG_USER')
PG_PASSWORD = os.getenv('PG_PASSWORD')

# MySQL connection parameters
MYSQL_HOST = os.getenv('MYSQL_HOST')
MYSQL_PORT = os.getenv('MYSQL_PORT')
MYSQL_DATABASE = os.getenv('MYSQL_DATABASE')
MYSQL_USER = os.getenv('MYSQL_USER')
MYSQL_PASSWORD = os.getenv('MYSQL_PASSWORD')

# Connect to PostgreSQL
try:
    pg_conn = psycopg2.connect(
        host=PG_HOST,
        port=PG_PORT,
        database=PG_DATABASE,
        user=PG_USER,
        password=PG_PASSWORD
    )
    pg_cursor = pg_conn.cursor()
    logging.info('Connected to PostgreSQL.')
except Exception as e:
    logging.error(f'Error connecting to PostgreSQL: {e}')
    exit(1)

# Connect to MySQL
try:
    my_conn = mysql.connector.connect(
        host=MYSQL_HOST,
        port=MYSQL_PORT,
        database=MYSQL_DATABASE,
        user=MYSQL_USER,
        password=MYSQL_PASSWORD
    )
    my_cursor = my_conn.cursor()
    logging.info('Connected to MySQL.')
except Exception as e:
    logging.error(f'Error connecting to MySQL: {e}')
    exit(1)

def main():
    try:
        # Extract data from staging table
        pg_cursor.execute("SELECT * FROM RawForgiatura WHERE stato_processo = 'PENDING'")
        raw_data = pg_cursor.fetchall()
        colnames = [desc[0] for desc in pg_cursor.description]
        logging.info(f'Extracted {len(raw_data)} records from PostgreSQL.')
    except Exception as e:
        logging.error(f'Error extracting data: {e}')
        pg_conn.close()
        my_conn.close()
        exit(1)

    # Transform data
    transformed_data = []
    for row in raw_data:
        data = dict(zip(colnames, row))
        try:

            if isinstance(data['event_timestamp'], str):
                data['event_timestamp'] = datetime.strptime(data['event_timestamp'], '%Y-%m-%d %H:%M:%S')
        
            transformed_record = (
                data['codice_pezzo'],
                data['peso_effettivo'],
                data['temperatura_effettiva'],
                data['event_timestamp'],
                data['codice_macchinario']
            )
            transformed_data.append(transformed_record)
        except KeyError as e:
            record_id = data.get('id', 'N/A')
            logging.error(f'Data transformation error for record {record_id}: missing key {e}')
            continue
    logging.info('Data transformation complete.')

    # Load data into MySQL
    try:
        insert_query = """
        INSERT INTO Forgiatura (codice_pezzo, peso_effettivo, temperatura_effettiva, timestamp, codice_macchinario)
        VALUES (%s, %s, %s, %s, %s)
        """
        my_cursor.executemany(insert_query, transformed_data)
        my_conn.commit()
        logging.info(f'Inserted {my_cursor.rowcount} records into MySQL.')
    except Exception as e:
        logging.error(f'Error loading data into MySQL: {e}')
        my_conn.rollback()

    # Update processing status in PostgreSQL
    try:
        processed_ids = [str(data[0]) for data in raw_data]
        update_query = f"UPDATE RawForgiatura SET stato_processo = 'PROCESSED' WHERE id IN ({','.join(processed_ids)})"
        pg_cursor.execute(update_query)
        pg_conn.commit()
        logging.info('Updated processing status in PostgreSQL.')
    except Exception as e:
        logging.error(f'Error updating processing status: {e}')
        pg_conn.rollback()


    # Close connections
    pg_cursor.close()
    pg_conn.close()
    my_cursor.close()
    my_conn.close()
    logging.info('Database connections closed.')


if __name__ == '__main__':
    main()
