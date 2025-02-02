package com.enerjai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.enerjai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText editNome, editEmail, editUsername, editDataNascimento;
    private EditText editPeso, editAltura, editMetaKcal;
    private Button btnRegistrar, btnLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicialização dos campos de entrada e botões
        editNome = findViewById(R.id.editNome);
        editUsername = findViewById(R.id.editUsername);
        editDataNascimento = findViewById(R.id.editDataNascimento);
        editEmail = findViewById(R.id.editEmail);
        editPeso = findViewById(R.id.editPeso);
        editAltura = findViewById(R.id.editAltura);
        editMetaKcal = findViewById(R.id.editMetaKcal);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegistrar.setOnClickListener(v -> registrarUsuario());

        btnLogin.setOnClickListener(v -> {
            // Direcionar para a tela de login caso o usuário já tenha uma conta
            Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registrarUsuario() {
        String nome = editNome.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String dataNascimento = editDataNascimento.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String peso = editPeso.getText().toString().trim();
        String altura = editAltura.getText().toString().trim();
        String metaKcal = editMetaKcal.getText().toString().trim();

        if (nome.isEmpty() || username.isEmpty() || dataNascimento.isEmpty() || email.isEmpty() || peso.isEmpty() || altura.isEmpty() || metaKcal.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Autenticação com e-mail (sem senha)
        mAuth.createUserWithEmailAndPassword(email, "alguma_senha_temporaria")  // Você pode usar uma senha temporária
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        // Criar um mapa com os dados do usuário
                        Map<String, Object> usuario = new HashMap<>();
                        usuario.put("nome", nome);
                        usuario.put("username", username);
                        usuario.put("email", email);
                        usuario.put("dataNascimento", dataNascimento);
                        usuario.put("peso", peso);
                        usuario.put("altura", altura);
                        usuario.put("metaKcal", metaKcal);
                        usuario.put("userId", userId);

                        // Salvar os dados no Firestore
                        db.collection("usuarios").document(userId).set(usuario)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegistroActivity.this, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show();
                                    // Direcionar para a tela de login após registro
                                    startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegistroActivity.this, "Erro ao salvar no Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    } else {
                        Toast.makeText(RegistroActivity.this, "Erro ao registrar: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
