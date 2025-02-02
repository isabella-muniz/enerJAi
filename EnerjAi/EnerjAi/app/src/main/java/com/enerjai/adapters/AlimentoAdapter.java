package com.enerjai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.enerjai.R;
import com.enerjai.model.Alimento;

import java.util.List;

public class AlimentoAdapter extends BaseAdapter {
    private Context context;
    private List<Alimento> alimentos;

    public AlimentoAdapter(Context context, List<Alimento> alimentos) {
        this.context = context;
        this.alimentos = alimentos;
    }

    @Override
    public int getCount() {
        return alimentos.size();
    }

    @Override
    public Object getItem(int position) {
        return alimentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_alimento, parent, false);
        }

        Alimento alimento = alimentos.get(position);

        TextView txtNomeAlimento = convertView.findViewById(R.id.txtNomeAlimento);
        txtNomeAlimento.setText(alimento.getNome());

        return convertView;
    }
}

