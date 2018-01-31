package coletor.sismanejo.com.br.coletor.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import coletor.sismanejo.com.br.coletor.R;
import coletor.sismanejo.com.br.coletor.daos.BancoController;
import coletor.sismanejo.com.br.coletor.daos.CriaBanco;
import coletor.sismanejo.com.br.coletor.models.Especie;
import coletor.sismanejo.com.br.coletor.models.Projeto;

import java.util.ArrayList;
import java.util.List;


public class InicialActivity extends AppCompatActivity {

    private Button coletor_inventario;
    private Button certificador_baldeio;
    private CriaBanco criaBanco;
    private BancoController controlador;
    private Projeto projeto;
    private List<Especie> especies = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        coletor_inventario = (Button) findViewById(R.id.coletor_inventario);
        certificador_baldeio = (Button) findViewById(R.id.certificador_baldeio);

        certificador_baldeio.setEnabled(false);

        criaBanco = new CriaBanco(InicialActivity.this);
        controlador = new BancoController(InicialActivity.this);

        projeto = controlador.consultarProjeto();

        especies = controlador.consultaEspecies();

        addListenerOnButton();


    }

    public void addListenerOnButton() {

        final Context context = this;

        coletor_inventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {

                    if(projeto == null){

                        // INICIA A ACTIVITY PROJETO

                        Intent intent = new Intent(context, ProjetoActivity.class);
                        startActivity(intent);

                    }else{

                        if(especies.isEmpty()){

                            // INICIA A ACTIVITY ESPECIES
                            Intent intent = new Intent(context, EspeciesActivity.class);
                            intent.putExtra("PROJETO", projeto);// PASSA O OBJETO PROJETO
                            startActivity(intent);

                        }else{

                            // INICIA A ACTIVITY PRINCIPAL

                            Intent intent = new Intent(context, PrincipalActivity.class);
                            intent.putExtra("PROJETO", projeto); // PASSA O OBJETO PROJETO
                            startActivity(intent);
                        }

                    }



                } catch (Exception e) {

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }


}
