package com.enerjai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.enerjai.R;

public class IniciarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar);

        Button btnConfirmar = findViewById(R.id.btnConfirmar);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    // Usuário autenticado, pode prosseguir para SelecionarAlimentoActivity
                    Intent intent = new Intent(IniciarActivity.this, SelecionarAlimentoActivity.class);
                    startActivity(intent);
                } else {
                    // Usuário não autenticado, exibir aviso
                    Toast.makeText(IniciarActivity.this, "Você precisa estar autenticado para continuar.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
