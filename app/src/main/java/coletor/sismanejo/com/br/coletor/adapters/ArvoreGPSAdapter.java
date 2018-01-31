package coletor.sismanejo.com.br.coletor.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import coletor.sismanejo.com.br.coletor.R;

import coletor.sismanejo.com.br.coletor.models.Arvore;

import java.util.List;

/**
 * Created by Leonardo on 22/01/2017.
 */

public class ArvoreGPSAdapter extends ArrayAdapter<Arvore> {
    public ArvoreGPSAdapter(Context context, List<Arvore> lista) {
        super(context, 0, lista);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemArvoreView = convertView;
        if(itemArvoreView == null){
            itemArvoreView = LayoutInflater.from(getContext()).inflate(R.layout.item_arvore_gps, parent, false);
        }

        Arvore arvoreGPS = getItem(position);

        TextView ut = (TextView) itemArvoreView.findViewById(R.id.ut);
        ut.setText(String.valueOf(arvoreGPS.getUt()));

        TextView faixa = (TextView) itemArvoreView.findViewById(R.id.faixa);
        faixa.setText(String.valueOf(arvoreGPS.getFaixa()));

        TextView numero = (TextView) itemArvoreView.findViewById(R.id.numero);
        numero.setText(String.valueOf(arvoreGPS.getNumero()));

        TextView especie = (TextView) itemArvoreView.findViewById(R.id.especie);
        especie.setText(arvoreGPS.getEspecie());

        TextView cap = (TextView) itemArvoreView.findViewById(R.id.cap);
        cap.setText(String.valueOf(arvoreGPS.getCap()));

        TextView altura = (TextView) itemArvoreView.findViewById(R.id.altura);
        altura.setText(String.valueOf(arvoreGPS.getAltura()));

        TextView fuste = (TextView) itemArvoreView.findViewById(R.id.fuste);
        fuste.setText(String.valueOf(arvoreGPS.getQf()));

        TextView observacao = (TextView) itemArvoreView.findViewById(R.id.observacao);
        observacao.setText(arvoreGPS.getObservacao());

        TextView ponto = (TextView) itemArvoreView.findViewById(R.id.ponto_gps);
        ponto.setText(arvoreGPS.getPonto());

        /*
        TextView anotador = (TextView) itemArvoreView.findViewById(R.id.campoAnotador);
        anotador.setText(arvoreGPS.getAnotador());

        TextView botanico = (TextView) itemArvoreView.findViewById(R.id.campoBotanico);
        botanico.setText(arvoreGPS.getBotanico());

        TextView plaqueteiro = (TextView) itemArvoreView.findViewById(R.id.campoPlaqueteiro);
        plaqueteiro.setText(arvoreGPS.getPlaqueteiro());

        TextView anotador_gps = (TextView) itemArvoreView.findViewById(R.id.campoAnotadorGPS);
        anotador_gps.setText(arvoreGPS.getAnotador_gps());
        */

        return itemArvoreView;
    }


}
