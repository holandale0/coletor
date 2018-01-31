package coletor.sismanejo.com.br.coletor.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import coletor.sismanejo.com.br.coletor.R;
import coletor.sismanejo.com.br.coletor.daos.BancoController;
import coletor.sismanejo.com.br.coletor.daos.CriaBanco;
import coletor.sismanejo.com.br.coletor.models.Projeto;

import java.io.File;

public class PrincipalActivity extends AppCompatActivity {

    private CriaBanco criaBanco;
    private BancoController controlador;
    private Projeto projeto;
    private EditText campoUt;
    private EditText campoUpa;
    private EditText campoAnotador;
    private EditText campoBotanico;
    private EditText campoPlaqueteiro;
    private EditText campoLateralX;
    private EditText campoLateralY;
    private EditText campoAnotadorGPS;
    private TextView lbLateralX;
    private TextView lbLateralY;
    private TextView lbAnotadorGPS;
    private Button btnIniciar;
    private Button btnConsultar;
    private Button btnSalvar;
    private Button btnAlterar;
    private Button btnCancelar;
    private Button btnProjeto;
    private Button btnEspecies;

    private File pasta = new File(Environment.getExternalStorageDirectory() + "/coletor_inventario");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);




        campoUpa = (EditText) findViewById(R.id.campoUpa);
        campoUt = (EditText) findViewById(R.id.campoUt);
        campoAnotador = (EditText) findViewById(R.id.campoAnotador);
        campoBotanico = (EditText) findViewById(R.id.campoBotanico);
        campoPlaqueteiro = (EditText) findViewById(R.id.campoPlaqueteiro);
        campoLateralX = (EditText) findViewById(R.id.campoLateralX);
        campoLateralY = (EditText) findViewById(R.id.campoLateralY);
        campoAnotadorGPS = (EditText) findViewById(R.id.campoAnotadorGPS);

        lbLateralX = (TextView) findViewById(R.id.lbLateralX);
        lbLateralY = (TextView) findViewById(R.id.lbLateralY);
        lbAnotadorGPS = (TextView) findViewById(R.id.lbAnotadorGPS);

        btnSalvar = (Button)  findViewById(R.id.btnSalvarDadosUt);
        btnAlterar = (Button)  findViewById(R.id.btnAlterarDadosUt);
        btnCancelar = (Button)  findViewById(R.id.btnCancelarAlteracoes);
        btnIniciar = (Button) findViewById(R.id.btnIniciarColeta);
        btnProjeto = (Button)  findViewById(R.id.btnProjeto);
        btnEspecies = (Button)  findViewById(R.id.btnEspecies);
        btnConsultar = (Button) findViewById(R.id.btnConsultarDados);



        //criaBanco = new CriaBanco(PrincipalActivity.this);
        controlador = new BancoController(PrincipalActivity.this);

        projeto = controlador.consultarProjeto();

        //CHECA O TIPO DE COORDENADAS UTILIZADO NO INVENTÁRIO E EXIBE / OCULTA CAMPOS DE ACORDO COM O TIPO
        if(projeto.getTipo_coordenada() == 1){
            lbAnotadorGPS.setVisibility(View.INVISIBLE);
            campoAnotadorGPS.setVisibility(View.INVISIBLE);
        }else{
            lbLateralX.setVisibility(View.INVISIBLE);
            lbLateralY.setVisibility(View.INVISIBLE);
            campoLateralX.setVisibility(View.INVISIBLE);
            campoLateralY.setVisibility(View.INVISIBLE);
        }


        // PREENCHENDO OS CAMPOS
        if(isInteger(String.valueOf(projeto.getUt())) && projeto.getUt() > 0){
            campoUt.setText(String.valueOf(projeto.getUt()));
        }else{
            campoUt.setText("");
        }

        if(projeto.getAnotador() != null || !"".equals(projeto.getAnotador())){
            campoAnotador.setText(projeto.getAnotador());
        }else{
            campoAnotador.setText("");
        }

        if(projeto.getBotanico() != null || !"".equals(projeto.getBotanico())){
            campoBotanico.setText(projeto.getBotanico());
        }else{
            campoBotanico.setText("");
        }

        if(projeto.getPlaqueteiro() != null || !"".equals(projeto.getPlaqueteiro())){
            campoPlaqueteiro.setText(projeto.getPlaqueteiro());
        }else{
            campoPlaqueteiro.setText("");
        }

        if(projeto.getLateral_x() != null || !"".equals(projeto.getLateral_x())){
            campoLateralX.setText(projeto.getLateral_x());
        }else{
            campoLateralX.setText("");
        }

        if(projeto.getLateral_y() != null || !"".equals(projeto.getLateral_y())){
            campoLateralY.setText(projeto.getLateral_y());
        }else{
            campoLateralY.setText("");
        }

        if(projeto.getAnotador_gps() != null || !"".equals(projeto.getAnotador_gps())){
            campoAnotadorGPS.setText(projeto.getAnotador_gps());
        }else{
            campoAnotadorGPS.setText("");
        }


        //
        habilitarDesabilitarCampos(false);



        if(!pasta.exists()){
            pasta.mkdir();
        }



        addListenerOnButton();



    }






    public void addListenerOnButton() {

        final Context context = this;

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                habilitarDesabilitarCampos(true);
                btnAlterar.setEnabled(false);
                btnEspecies.setEnabled(false);
                btnProjeto.setEnabled(false);
                btnConsultar.setEnabled(false);
                btnIniciar.setEnabled(false);


            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                habilitarDesabilitarCampos(false);
                btnAlterar.setEnabled(true);
                btnEspecies.setEnabled(true);
                btnProjeto.setEnabled(true);
                btnConsultar.setEnabled(true);
                btnIniciar.setEnabled(true);


            }
        });


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(!checaCamposObrigatorios()){
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Erro !");
                    alertDialog.setMessage("Preencha os campos obrigatórios!");
                    alertDialog.show();
                    return;
                }else{
                    projeto.setUt(Integer.parseInt(campoUt.getText().toString()));
                    projeto.setAnotador(campoAnotador.getText().toString());
                    projeto.setBotanico(campoBotanico.getText().toString());
                    projeto.setPlaqueteiro(campoPlaqueteiro.getText().toString());

                    if(projeto.getTipo_coordenada() == 1){
                        projeto.setLateral_x(campoLateralX.getText().toString());
                        projeto.setLateral_y(campoLateralY.getText().toString());
                    }else{
                        projeto.setAnotador_gps(campoAnotadorGPS.getText().toString());
                    }

                    controlador.salvarOuAtualizarProjeto(projeto);

                    habilitarDesabilitarCampos(false);
                    btnAlterar.setEnabled(true);
                    btnEspecies.setEnabled(true);
                    btnProjeto.setEnabled(true);
                    btnConsultar.setEnabled(true);
                    btnIniciar.setEnabled(true);

                }




            }
        });



        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {

                    if (!checaCamposObrigatorios()) {

                        AlertDialog alertDialog;
                        alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Erro !");
                        alertDialog.setMessage("Preencha os campos obrigatórios!");
                        alertDialog.show();
                        return;

                    } else {

                        Intent intent = new Intent(PrincipalActivity.this, ColetaActivity.class);
                        intent.putExtra("PROJETO", projeto); // PASSA O OBJETO PROJETO
                        startActivity(intent);

                    }

                } catch (Exception e) {
                    System.out.println(e);
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Erro !");
                    alertDialog.setMessage("Informe os campos obrigatórios !");
                    alertDialog.show();
                }
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {

                        Intent intent = new Intent(context, ConsultaActivity.class);
                        intent.putExtra("PROJETO", projeto); // PASSA O OBJETO PROJETO
                        startActivity(intent);


                } catch (Exception e) {

                }

            }
        });

        btnProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {

                    Intent intent = new Intent(context, ProjetoActivity.class);
                    intent.putExtra("PROJETO", projeto); // PASSA O OBJETO PROJETO
                    startActivity(intent);


                } catch (Exception e) {

                }

            }
        });

        btnEspecies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {

                    Intent intent = new Intent(context, EspeciesActivity.class);
                    intent.putExtra("PROJETO", projeto); // PASSA O OBJETO PROJETO
                    startActivity(intent);


                } catch (Exception e) {

                }

            }
        });


    }


    private boolean checaCamposObrigatorios(){

        if(projeto.getTipo_coordenada() == 1){
            if(campoUt.getText() == null || "".equals(campoUt.getText()) ||
                    campoAnotador.getText() == null || "".equals(campoAnotador.getText()) ||
                    campoBotanico.getText() == null || "".equals(campoBotanico.getText()) ||
                    campoPlaqueteiro.getText() == null || "".equals(campoPlaqueteiro.getText()) ||
                    campoLateralX.getText() == null || "".equals(campoLateralX.getText()) ||
                    campoLateralY.getText() == null || "".equals(campoLateralY.getText())){

            }
            return false;
        }else{
            if(campoUt.getText() == null || "".equals(campoUt.getText()) ||
                    campoAnotador.getText() == null || "".equals(campoAnotador.getText()) ||
                    campoBotanico.getText() == null || "".equals(campoBotanico.getText()) ||
                    campoPlaqueteiro.getText() == null || "".equals(campoPlaqueteiro.getText()) ||
                    campoAnotadorGPS.getText() == null || "".equals(campoAnotadorGPS.getText())){

            }
            return true;

        }



    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ne) {
            return false;
        }
    }

    private void habilitarDesabilitarCampos(boolean valor){

        campoUt.setEnabled(valor);
        campoAnotador.setEnabled(valor);
        campoBotanico.setEnabled(valor);
        campoPlaqueteiro.setEnabled(valor);
        campoLateralX.setEnabled(valor);
        campoLateralY.setEnabled(valor);
        campoAnotadorGPS.setEnabled(valor);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
