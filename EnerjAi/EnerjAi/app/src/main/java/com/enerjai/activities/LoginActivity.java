package com.enerjai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.enerjai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.enerjai.activities.RegistroActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button iniciarButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Carrega o layout

        // Inicializar FirebaseAuth e FirebaseFirestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referenciar os campos de email e senha
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        iniciarButton = findViewById(R.id.btnLogin);

        // Definir um listener para quando o usuário clicar no botão "Iniciar"
        iniciarButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Verificar se ambos os campos foram preenchidos
            if (!email.isEmpty() && !password.isEmpty()) {
                signInOrCreateUser(email, password); // Autenticar ou criar o usuário
            } else {
                Toast.makeText(LoginActivity.this, "Por favor, preencha os campos de email e senha!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInOrCreateUser(String email, String password) {
        // Tenta fazer login primeiro
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        Log.d("LoginActivity", "Login bem-sucedido");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkUserInFirestore(user.getUid()); // Verifica se o usuário existe no Firestore
                        }
                    } else {
                        // Se o login falhar, tenta criar uma nova conta
                        createUser(email, password);
                    }
                });
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Conta criada com sucesso
                        Log.d("LoginActivity", "Conta criada com sucesso");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserData(user); // Salva os dados no Firestore
                        }
                    } else {
                        // Falha ao criar a conta
                        Log.e("LoginActivity", "Falha ao criar a conta: " + task.getException());
                        Toast.makeText(LoginActivity.this, "Falha ao criar a conta: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserData(FirebaseUser user) {
        String userId = user.getUid();

        // Dados que você deseja salvar (exemplo)
        Map<String, Object> userData = new HashMap<>();
        userData.put("nome", "Nome do Usuário"); // Substitua por dados reais, se necessário
        userData.put("email", user.getEmail());

        // Enviar os dados para Firestore
        db.collection("usuarios").document(userId).set(userData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("LoginActivity", "Dados salvos com sucesso no Firestore!");
                    redirectToIniciarActivity(); // Redireciona para a tela IniciarActivity
                })
                .addOnFailureListener(e -> {
                    Log.e("LoginActivity", "Erro ao salvar os dados no Firestore: " + e.getMessage());
                    Toast.makeText(LoginActivity.this, "Erro ao salvar os dados no Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void checkUserInFirestore(String userId) {
        db.collection("usuarios").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            // Usuário já existe no Firestore, redireciona para IniciarActivity
                            redirectToIniciarActivity();
                        } else {
                            // Usuário não existe, redireciona para a tela de registro
                            redirectToRegistroActivity();
                        }
                    } else {
                        Log.e("LoginActivity", "Erro ao verificar usuário no Firestore: " + task.getException());
                    }
                });
    }

    private void redirectToIniciarActivity() {
        startActivity(new Intent(LoginActivity.this, IniciarActivity.class)); // Redireciona para IniciarActivity
        finish(); // Finaliza a LoginActivity para evitar que o usuário retorne
    }

    private void redirectToRegistroActivity() {
        startActivity(new Intent(LoginActivity.this, RegistroActivity.class)); // Redireciona para RegistroActivity
        finish(); // Finaliza a LoginActivity para evitar que o usuário retorne
    }
}
