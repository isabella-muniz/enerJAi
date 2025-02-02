package com.enerjai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.enerjai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button iniciarButton, logoutButton, selecionarAlimentoButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa o Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);


        // Verifique se o usuário está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Se não estiver autenticado, redirecione para a LoginActivity
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return; // Não continue o fluxo da MainActivity se não estiver autenticado
        }

        // Referências para os botões
        iniciarButton = findViewById(R.id.iniciarButton);
        logoutButton = findViewById(R.id.logoutButton);
        selecionarAlimentoButton = findViewById(R.id.btnAlimento);

        // Listener do botão "Iniciar"
        iniciarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vai para a página IniciarActivity
                Intent intent = new Intent(MainActivity.this, IniciarActivity.class);
                startActivity(intent);
            }
        });

        // Listener do botão "Logout"
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Realiza o logout
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show();
                // Volta para a tela de login
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        // Listener do botão "Selecionar Alimento"
        selecionarAlimentoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vai para a página SelecionarAlimentoActivity
                Intent intent = new Intent(MainActivity.this, SelecionarAlimentoActivity.class);
                startActivity(intent);
            }
        });

        // Teste de gravação de dados no Firestore
        testFirestore();
    }

    private void testFirestore() {
        // Teste simples de gravação no Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> alimento = new HashMap<>();
        alimento.put("nome", "Água");
        alimento.put("energia_kcal", 0);

        // Log antes de tentar gravar
        Log.d("Firestore", "Tentando gravar alimento: " + alimento);

        // Tentando escrever no Firestore
        db.collection("alimentos")
                .add(alimento)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MainActivity.this, "Documento adicionado: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                    Log.d("Firestore", "Documento adicionado com sucesso: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Erro ao adicionar no Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Erro ao gravar no Firestore", e);
                });
    }
}
