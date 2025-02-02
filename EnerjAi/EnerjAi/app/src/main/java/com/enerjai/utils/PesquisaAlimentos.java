package com.enerjai.utils;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import com.enerjai.model.Alimento;


public class PesquisaAlimentos {

    private FirebaseFirestore db;

    public PesquisaAlimentos() {
        // Instancia o FirebaseFirestore
        db = FirebaseFirestore.getInstance();
    }

    // Método para buscar alimentos pelo nome
    public void buscarAlimentosPorNome(String nomePesquisa, final PesquisaCallback callback) {
        db.collection("alimentos")
                .whereEqualTo("nome", nomePesquisa)  // Filtro de pesquisa pelo nome exato
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<Alimento> alimentosEncontrados = new ArrayList<>();
                        for (DocumentSnapshot document : querySnapshot) {
                            Alimento alimento = document.toObject(Alimento.class);
                            alimentosEncontrados.add(alimento);
                        }
                        // Chama o callback passando os alimentos encontrados
                        callback.onPesquisaConcluida(alimentosEncontrados);
                    } else {
                        // Caso a consulta falhe
                        callback.onPesquisaFalhou(task.getException());
                    }
                });
    }

    // Interface de retorno (callback) para lidar com os resultados da pesquisa
    public interface PesquisaCallback {
        void onPesquisaConcluida(List<Alimento> alimentos);
        void onPesquisaFalhou(Exception e);
    }
}

