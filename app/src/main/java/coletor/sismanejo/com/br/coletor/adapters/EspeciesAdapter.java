package coletor.sismanejo.com.br.coletor.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import coletor.sismanejo.com.br.coletor.R;

import coletor.sismanejo.com.br.coletor.models.Especie;

import java.util.List;


public class EspeciesAdapter extends ArrayAdapter<Especie> {
    public EspeciesAdapter(Context context, List<Especie> lista) {
        super(context, 0, lista);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemEspecieView = convertView;
        if(itemEspecieView == null){
            itemEspecieView = LayoutInflater.from(getContext()).inflate(R.layout.item_especie, parent, false);
        }

        Especie especie = getItem(position);

        TextView codigo = (TextView) itemEspecieView.findViewById(R.id.codigoEspecie);
        codigo.setText(String.valueOf(especie.getCodigoEspecie()));

        TextView nomePopular = (TextView) itemEspecieView.findViewById(R.id.nomePopular);
        nomePopular.setText(especie.getNomePopular());

        TextView nomeCientifico = (TextView) itemEspecieView.findViewById(R.id.nomeCientifico);
        nomeCientifico.setText(especie.getNomeCientifico());



        return itemEspecieView;
    }


}
