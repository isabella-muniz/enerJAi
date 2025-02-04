import pandas as pd
import json
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import os

# Read the Excel file
def excel_to_json():
    # Read Excel file
    df = pd.read_excel('alimentos.xlsx')
    
    # Convert numeric columns to float
    numeric_columns = ['calorias', 'proteinas', 'carboidratos', 'gorduras']
    for col in numeric_columns:
        if col in df.columns:
            df[col] = pd.to_numeric(df[col], errors='coerce')
    
    # Rename columns to match the app's model
    column_mapping = {
        'alimento': 'name',
        'calorias': 'calories',
        'proteinas': 'proteins',
        'carboidratos': 'carbs',
        'gorduras': 'fats'
    }
    df = df.rename(columns=column_mapping)
    
    # Convert DataFrame to list of dictionaries
    records = df.to_dict('records')
    
    # Save to JSON file first (backup)
    with open('alimentos.json', 'w', encoding='utf-8') as f:
        json.dump(records, f, ensure_ascii=False, indent=2)
    
    return records

def upload_to_firestore(records):
    try:
        # Get the current script's directory
        current_dir = os.path.dirname(os.path.abspath(__file__))
        
        # Path to the service account key file
        service_account_path = os.path.join(current_dir, 'serviceAccountKey.json')
        
        # Initialize Firebase Admin
        if not firebase_admin._apps:
            cred = credentials.Certificate(service_account_path)
            firebase_admin.initialize_app(cred)
        
        db = firestore.client()
        
        # Reference to the foods collection (changed from 'alimentos' to match the app)
        foods_ref = db.collection('foods')
        
        # Upload in batches of 500 (Firestore batch limit)
        batch_size = 500
        for i in range(0, len(records), batch_size):
            batch = db.batch()
            batch_records = records[i:i + batch_size]
            
            for record in batch_records:
                # Create a new document reference with auto-generated ID
                doc_ref = foods_ref.document()
                batch.set(doc_ref, record)
            
            # Commit the batch
            batch.commit()
            print(f"Uploaded batch {i//batch_size + 1} ({len(batch_records)} records)")
        
        print(f"Successfully uploaded {len(records)} records to Firestore")
        
    except Exception as e:
        print(f"Error uploading to Firestore: {str(e)}")
        raise e

if __name__ == "__main__":
    try:
        records = excel_to_json()
        print(f"Successfully converted {len(records)} records to JSON")
        print(f"Sample record: {records[0]}")
        
        upload = input("Do you want to upload to Firestore? (y/n): ")
        if upload.lower() == 'y':
            upload_to_firestore(records)
            
    except Exception as e:
        print(f"Error: {str(e)}") 