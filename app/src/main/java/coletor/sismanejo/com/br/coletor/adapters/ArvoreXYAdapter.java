package coletor.sismanejo.com.br.coletor.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import coletor.sismanejo.com.br.coletor.R;
import coletor.sismanejo.com.br.coletor.models.Arvore;


/**
 * Created by Leonardo on 25/01/2017.
 */

public class ArvoreXYAdapter extends ArrayAdapter<Arvore> {

    public ArvoreXYAdapter(Context context, List<Arvore> lista) {
        super(context, 0, lista);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemArvoreView = convertView;
        if(itemArvoreView == null){
            itemArvoreView = LayoutInflater.from(getContext()).inflate(R.layout.item_arvore_xy, parent, false);
        }

        Arvore arvoreXY = getItem(position);

        TextView ut = (TextView) itemArvoreView.findViewById(R.id.ut);
        ut.setText(String.valueOf(arvoreXY.getUt()));

        TextView faixa = (TextView) itemArvoreView.findViewById(R.id.faixa);
        faixa.setText(String.valueOf(arvoreXY.getFaixa()));

        TextView numero = (TextView) itemArvoreView.findViewById(R.id.numero);
        numero.setText(String.valueOf(arvoreXY.getNumero()));

        TextView especie = (TextView) itemArvoreView.findViewById(R.id.especie);
        especie.setText(arvoreXY.getEspecie());

        TextView cap = (TextView) itemArvoreView.findViewById(R.id.cap);
        cap.setText(String.valueOf(arvoreXY.getCap()));

        TextView altura = (TextView) itemArvoreView.findViewById(R.id.altura);
        altura.setText(String.valueOf(arvoreXY.getAltura()));

        TextView fuste = (TextView) itemArvoreView.findViewById(R.id.fuste);
        fuste.setText(String.valueOf(arvoreXY.getQf()));

        TextView observacao = (TextView) itemArvoreView.findViewById(R.id.observacao);
        observacao.setText(arvoreXY.getObservacao());

        TextView coordx = (TextView) itemArvoreView.findViewById(R.id.coordx);
        coordx.setText(String.valueOf(arvoreXY.getCoordx()));

        TextView orix = (TextView) itemArvoreView.findViewById(R.id.orix);
        orix.setText(arvoreXY.getOrientx());

        TextView coordy = (TextView) itemArvoreView.findViewById(R.id.coordy);
        coordy.setText(String.valueOf(arvoreXY.getCoordx()));

        /*
        TextView pessoa1 = (TextView) itemArvoreView.findViewById(R.id.pessoa1);
        pessoa1.setText(arvoreXY.getPessoa1());

        TextView pessoa2 = (TextView) itemArvoreView.findViewById(R.id.pessoa2);
        pessoa2.setText(arvoreXY.getPessoa2());

        TextView pessoa3 = (TextView) itemArvoreView.findViewById(R.id.pessoa3);
        pessoa3.setText(arvoreXY.getPessoa3());

        TextView pessoa4 = (TextView) itemArvoreView.findViewById(R.id.pessoa4);
        pessoa4.setText(arvoreXY.getPessoa4());

        TextView pessoa5 = (TextView) itemArvoreView.findViewById(R.id.pessoa5);
        pessoa5.setText(arvoreXY.getPessoa5());
        */

        return itemArvoreView;
    }

}
