package com.enerjai.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.enerjai.R;
import com.enerjai.model.Alimento;
import com.enerjai.utils.PesquisaAlimentos;

import java.util.ArrayList;
import java.util.List;
import com.enerjai.adapters.AlimentoAdapter;

public class SelecionarAlimentoActivity extends AppCompatActivity {

    private EditText edtPesquisarAlimento;
    private ListView listViewAlimentos;
    private PesquisaAlimentos pesquisaAlimentos;
    private AlimentoAdapter alimentoAdapter;
    private List<Alimento> listaAlimentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_alimento);

        edtPesquisarAlimento = findViewById(R.id.edtPesquisarAlimento);
        listViewAlimentos = findViewById(R.id.listViewAlimentos);
        pesquisaAlimentos = new PesquisaAlimentos();
        listaAlimentos = new ArrayList<>();

        // Inicializa o adaptador e define na ListView
        alimentoAdapter = new AlimentoAdapter(this, listaAlimentos);
        listViewAlimentos.setAdapter(alimentoAdapter);

        // Adiciona um listener para detectar digitação em tempo real
        edtPesquisarAlimento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) { // Evita buscas desnecessárias com menos de 2 caracteres
                    pesquisarAlimentos(s.toString());
                } else {
                    listaAlimentos.clear();
                    alimentoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void pesquisarAlimentos(String nomeAlimento) {
        pesquisaAlimentos.buscarAlimentosPorNome(nomeAlimento, new PesquisaAlimentos.PesquisaCallback() {
            @Override
            public void onPesquisaConcluida(List<Alimento> alimentos) {
                listaAlimentos.clear();
                listaAlimentos.addAll(alimentos);
                alimentoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPesquisaFalhou(Exception e) {
                Toast.makeText(SelecionarAlimentoActivity.this, "Erro ao buscar alimentos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
