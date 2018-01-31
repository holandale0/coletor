package coletor.sismanejo.com.br.coletor.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import coletor.sismanejo.com.br.coletor.R;
import coletor.sismanejo.com.br.coletor.daos.BancoController;
import coletor.sismanejo.com.br.coletor.daos.CriaBanco;
import coletor.sismanejo.com.br.coletor.models.Projeto;

public class ProjetoActivity extends AppCompatActivity {

    private CriaBanco criaBanco;
    private BancoController controlador;
    private Projeto projeto;
    private String empreendimento;
    private String umf;
    private String upa;
    private int tipoCoordenada;
    private int tipoInventario;
    private String anotador;
    private String botanico;
    private String plaqueteiro;
    private String lateralX;
    private String lateralY;
    private String anotadorGps;
    //private String dataInicial;
    private EditText campoEmpreendimento;
    private EditText campoUmf;
    private EditText campoUpa;
    private RadioButton radioGPS;
    private RadioButton radioXY;
    private RadioButton radioContinuo;
    private RadioButton radioAmostragem;
    private EditText campoAnotador;
    private EditText campoBotanico;
    private EditText campoPlaqueteiro;
    private EditText campoLateralX;
    private EditText campoLateralY;
    private EditText campoAnotadorGPS;
    //private EditText campoDataInicial;
    private Button salvar;
    private Button alterar;
    private Button cancelar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto);

        campoEmpreendimento = (EditText) findViewById(R.id.campoEmpreendimento);
        campoUmf = (EditText) findViewById(R.id.campoUmf);
        campoUpa = (EditText) findViewById(R.id.campoUpa);
        radioGPS = (RadioButton) findViewById(R.id.radioGPS);
        radioXY = (RadioButton) findViewById(R.id.radioXY);
        radioContinuo = (RadioButton) findViewById(R.id.radioContinuo);
        radioAmostragem = (RadioButton) findViewById(R.id.radioAmostragem);
        campoAnotador = (EditText) findViewById(R.id.campoAnotador);
        campoBotanico = (EditText) findViewById(R.id.campoBotanico);
        campoPlaqueteiro = (EditText) findViewById(R.id.campoPlaqueteiro);
        campoLateralX = (EditText) findViewById(R.id.campoLateralX);
        campoLateralY = (EditText) findViewById(R.id.campoLateralY);
        campoAnotadorGPS = (EditText) findViewById(R.id.campoAnotadorGPS);

        salvar = (Button) findViewById(R.id.btnSalvarProjeto);
        cancelar = (Button) findViewById(R.id.btnCancelarAlteracoesProjeto);

        controlador = new BancoController(ProjetoActivity.this);


        controlador.verificaProjeto();

        projeto = controlador.consultarProjeto();

        if(projeto != null){

            campoEmpreendimento.setText(projeto.getEmpreendimento());
            campoUmf.setText(projeto.getUmf());
            campoUpa.setText(projeto.getUpa());
            campoAnotador.setText(projeto.getAnotador());
            campoBotanico.setText(projeto.getBotanico());
            campoPlaqueteiro.setText(projeto.getPlaqueteiro());
            campoLateralX.setText(projeto.getLateral_x());
            campoLateralY.setText(projeto.getLateral_y());
            campoAnotadorGPS.setText(projeto.getAnotador_gps());

            if(projeto.getTipo_coordenada() == 1){
                radioGPS.setChecked(false);
                radioXY.setChecked(true);
            }else{
                radioGPS.setChecked(true);
                radioXY.setChecked(false);
            }



            if(projeto.getTipo_inventario() == 1){
                radioContinuo.setChecked(true);
                radioAmostragem.setChecked(false);
            }else{
                radioContinuo.setChecked(false);
                radioAmostragem.setChecked(true);
            }

        }else{
            projeto = new Projeto();
            campoEmpreendimento.requestFocus();
            cancelar.setEnabled(false);
        }

        addListenerOnButton();


    }


    public void addListenerOnButton() {

        final Context context = this;



        radioXY.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                radioXY.setChecked(true);
                radioGPS.setChecked(false);

            }
        });

        radioGPS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                radioGPS.setChecked(true);
                radioXY.setChecked(false);

            }
        });

        radioContinuo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                radioContinuo.setChecked(true);
                radioAmostragem.setChecked(false);

            }
        });

        radioAmostragem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                radioContinuo.setChecked(false);
                radioAmostragem.setChecked(true);

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            // APENAS RETORNA À ACTIVITY PRINCIPAL
                Intent intent = new Intent(context, PrincipalActivity.class);
                intent.putExtra("PROJETO", projeto); // PASSA O OBJETO PROJETO
                startActivity(intent);

            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!checaCamposObrigatorios()){
                    AlertDialog alertDialog = new AlertDialog.Builder(ProjetoActivity.this).create();
                    alertDialog.setMessage("Preencha todos os campos obrigatórios (Título em negrito).");
                    alertDialog.show();
                    return;
                }else{

                    projeto.setEmpreendimento(campoEmpreendimento.getText().toString());
                    projeto.setUmf(campoUmf.getText().toString());
                    projeto.setUpa(campoUpa.getText().toString());
                    //projeto.setData_inicio();

                    if(campoAnotador.getText() == null || "".equals(campoAnotador.getText())){
                        projeto.setAnotador("");
                    }else{
                        projeto.setAnotador(campoAnotador.getText().toString());
                    }

                    if(campoBotanico.getText() == null || "".equals(campoBotanico.getText())){
                        projeto.setBotanico("");
                    }else{
                        projeto.setBotanico(campoBotanico.getText().toString());
                    }

                    if(campoPlaqueteiro.getText() == null || "".equals(campoPlaqueteiro.getText())){
                        projeto.setPlaqueteiro("");
                    }else{
                        projeto.setPlaqueteiro(campoPlaqueteiro.getText().toString());
                    }

                    if(campoLateralX.getText() == null || "".equals(campoLateralX.getText())){
                        projeto.setLateral_x("");
                    }else{
                        projeto.setLateral_x(campoLateralX.getText().toString());
                    }

                    if(campoLateralY.getText() == null || "".equals(campoLateralY.getText())){
                        projeto.setLateral_y("");
                    }else{
                        projeto.setLateral_y(campoLateralY.getText().toString());
                    }

                    if(campoAnotadorGPS.getText() == null || "".equals(campoAnotadorGPS.getText())){
                        projeto.setAnotador_gps("");
                    }else{
                        projeto.setAnotador_gps(campoAnotadorGPS.getText().toString());
                    }


                    if(radioGPS.isChecked()){
                        projeto.setTipo_coordenada(2);
                    }else{
                        projeto.setTipo_coordenada(1);
                    }

                    if(radioContinuo.isChecked()){
                        projeto.setTipo_inventario(1);
                    }else{
                        projeto.setTipo_inventario(2);
                    }


                    controlador.salvarOuAtualizarProjeto(projeto);

                    cancelar.setEnabled(true);

                    Intent intent = new Intent(context, PrincipalActivity.class);
                    intent.putExtra("PROJETO", projeto); // PASSA O OBJETO PROJETO
                    startActivity(intent);
                }





            }
        });







    }




    private boolean checaCamposObrigatorios(){

        if(campoEmpreendimento.getText() == null || "".equals(campoEmpreendimento.getText()) ||
                campoUmf.getText() == null || "".equals(campoUmf.getText()) ||
                campoUpa.getText() == null || "".equals(campoUpa.getText())){
            return false;
        }else{
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



}
