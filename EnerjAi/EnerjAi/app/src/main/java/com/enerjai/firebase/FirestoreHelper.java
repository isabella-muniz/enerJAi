package com.enerjai.firebase;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FirestoreHelper extends AppCompatActivity {
    private static final String TAG = "FirestoreHelper";
    private FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    // Método para carregar o JSON e enviar ao Firestore
    public void uploadJsonToFirestore(Context context) {
        try {
            // Ler o arquivo JSON da pasta assets
            InputStream is = context.getAssets().open("alimentos.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer, StandardCharsets.UTF_8);

            // Converter string JSON para objeto JSON
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray alimentosArray = jsonObject.getJSONArray("alimentos");

            // Enviar cada alimento para o Firestore
            for (int i = 0; i < alimentosArray.length(); i++) {
                JSONObject alimento = alimentosArray.getJSONObject(i);
                Map<String, Object> alimentoMap = new HashMap<>();

                alimentoMap.put("codigo", alimento.getString("codigo"));
                alimentoMap.put("nome", alimento.getString("nome"));
                alimentoMap.put("energia_kcal", alimento.getDouble("energia_kcal"));

                // Criar o subdocumento "macronutrientes"
                Map<String, Object> macronutrientes = new HashMap<>();
                macronutrientes.put("lipidos_g", alimento.getDouble("macronutrientes_lipidos_g"));
                macronutrientes.put("hidratos_carbono_g", alimento.getDouble("macronutrientes_hidratos_carbono_g"));
                macronutrientes.put("proteina_g", alimento.getDouble("macronutrientes_proteina_g"));
                macronutrientes.put("fibra_g", alimento.getDouble("macronutrientes_fibra_g"));

                alimentoMap.put("macronutrientes", macronutrientes);

                // Enviar para o Firestore (Coleção "Alimentos")
                db.collection("Alimentos").document(alimento.getString("codigo"))
                        .set(alimentoMap)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Alimento adicionado com sucesso!"))
                        .addOnFailureListener(e -> Log.e(TAG, "Erro ao adicionar alimento", e));
            }

        } catch (Exception e) {
            Log.e(TAG, "Erro ao ler e enviar JSON", e);
        }
    }
}
