package coletor.sismanejo.com.br.coletor.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import coletor.sismanejo.com.br.coletor.R;
import coletor.sismanejo.com.br.coletor.adapters.EspeciesAdapter;
import coletor.sismanejo.com.br.coletor.daos.BancoController;
import coletor.sismanejo.com.br.coletor.daos.CriaBanco;
import coletor.sismanejo.com.br.coletor.models.Especie;
import coletor.sismanejo.com.br.coletor.models.Projeto;

import java.util.ArrayList;
import java.util.List;

public class EspeciesActivity extends AppCompatActivity {

    private CriaBanco criaBanco;
    private BancoController controlador;
    private Projeto projeto;
    private Especie especie;
    private List<Especie> especies = new ArrayList<Especie>();
    private final List<Especie> especies_final = new ArrayList<Especie>();
    private int codigoEspecie;
    private String nomePopular;
    private String nomeCientifico;
    private EditText campoCodigoEspecie;
    private EditText campoNomePopular;
    private EditText campoNomeCientifico;
    private Button btnSalvar;
    private Button btnCancelar;
    private Button btnPrincipal;
    private Button btnProjeto;
    private int op = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especies);



        campoCodigoEspecie = (EditText) findViewById(R.id.campoCodEspecie);
        campoNomePopular = (EditText) findViewById(R.id.campoNomePopular);
        campoNomeCientifico = (EditText) findViewById(R.id.campoNomeCientifico);

        btnSalvar = (Button)  findViewById(R.id.btnSalvarAlteracoes);
        btnCancelar = (Button)  findViewById(R.id.btnCancelarAlteracoes);
        btnPrincipal = (Button)  findViewById(R.id.btnPrincipal);
        btnProjeto = (Button)  findViewById(R.id.btnProjeto);

        controlador = new BancoController(EspeciesActivity.this);

        projeto = (Projeto) getIntent().getSerializableExtra("PROJETO");

        populateListViewEspecie();

        addListenerOnButton();


    }

    public void addListenerOnButton() {
        final Context context = this;

        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

            if(!checaCamposObrigatorios()){
                AlertDialog alertDialog = new AlertDialog.Builder(EspeciesActivity.this).create();
                alertDialog.setMessage("Preencha todos os campos obrigatórios (Título em negrito).");
                alertDialog.show();
                return;
            }else{



                if(op == 1){

                    especie = new Especie();

                    if(controlador.codigoEspecieDuplicado(Integer.parseInt(campoCodigoEspecie.getText().toString()))){

                        campoCodigoEspecie.requestFocus();
                        return;
                    }

                    if(controlador.nomePopularDuplicado(campoNomePopular.getText().toString())){

                        campoNomePopular.requestFocus();
                        return;
                    }
                }

                especie.setCodigoEspecie(Integer.parseInt(campoCodigoEspecie.getText().toString()));
                especie.setNomePopular(campoNomePopular.getText().toString());

                if(campoNomeCientifico.getText() != null || !"".equals(campoNomeCientifico.getText())){
                    especie.setNomeCientifico(campoNomeCientifico.getText().toString());
                }

                controlador.savarOuAtualizarEspecie(op,especie);

                especies.add(especie);

                especie = null;

                op = 1;

                limparCampos();

                refreshListViewEspecie();

            }

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        limparCampos();
                        btnSalvar.setEnabled(true);
                        btnProjeto.setEnabled(true);
                        btnPrincipal.setEnabled(true);
                        especie = null;
                    }
                });


            }

        });

        btnPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {

                    Intent intent = new Intent(context, PrincipalActivity.class);
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


    }






    public void populateListViewEspecie() {

        especies.clear();
        especies_final.clear();
        especies = controlador.consultaEspecies();


        for (int i = 0; i < especies.size(); i++) {
            especies_final.add(especies.get(i));
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
            System.out.println(especies.get(i).getCodigoEspecie());
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
        }

        LayoutInflater infalter = getLayoutInflater();
        ListView lista = (ListView) findViewById(R.id.lista_especies);
        EspeciesAdapter adapter = new EspeciesAdapter(this, especies_final);
        lista.setAdapter(adapter);
        View header = infalter.inflate(R.layout.cabecalho_especies, lista, false);
        lista.addHeaderView(header,null,false);
        lista.setHeaderDividersEnabled(true);




        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Especie esp = especies.get(position);
                escolherAcao(EspeciesActivity.this, esp);
                return true;
            }
        });

    }

    public void refreshListViewEspecie() {


        especies_final.clear();


        for (int i = 0; i < especies.size(); i++) {
            especies_final.add(especies.get(i));
        }

        LayoutInflater infalter = getLayoutInflater();
        ListView lista = (ListView) findViewById(R.id.lista_especies);
        EspeciesAdapter adapter = new EspeciesAdapter(this, especies_final);
        lista.setAdapter(adapter);


        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Especie esp = especies.get(position);

                escolherAcao(EspeciesActivity.this, esp);

                return true;
            }
        });

    }




    private void escolherAcao(Context context, final Especie esp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Escolha a ação")
                .setMessage("Deseja EDITAR ou EXCLUIR a espécoe " + String.valueOf(esp.getCodigoEspecie()) + " - " + String.valueOf(esp.getNomePopular()) + " ?")
                .setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        acaoEditar(esp);
                    }
                }).setNegativeButton("EXCLUIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                acaoExcluir(esp);
            }
        }).setNeutralButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();
    }

    private final void acaoEditar(Especie esp) {


        especie = esp;
        op = 2;

        campoCodigoEspecie.setText(String.valueOf(especie.getCodigoEspecie()));
        campoNomePopular.setText(especie.getNomePopular());
        if (especie.getNomeCientifico() != null || !"".equals(especie.getNomeCientifico())) {
            campoNomeCientifico.setText(especie.getNomeCientifico());
        }
    }

    private final void acaoExcluir(Especie esp){

        confirmaExclusão(EspeciesActivity.this,esp);

    }

    private void confirmaExclusão(final Context context, final Especie esp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Confirma a exclusão da espécie "+esp.getCodigoEspecie()+" ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        controlador.deletaEspecie(esp);
                        especies.remove(esp);
                        refreshListViewEspecie();


                        sucessoExclusao(context);

                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void sucessoExclusao(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Espécie excluída com sucesso !")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void openDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Erro na entrada de dados ! ?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }


    private void codigoDuplicado(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Registro Duplicado")
                .setMessage("Já existe uma espécie com o código " + String.valueOf(campoCodigoEspecie.getText()))
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void nomePopularDuplicado(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Registro Duplicado")
                .setMessage("Já existe uma espécie com o nome popular " + campoNomePopular.getText())
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }





    private boolean checaCamposObrigatorios(){

        if(campoCodigoEspecie.getText() == null || "".equals(campoCodigoEspecie.getText()) ||
                campoNomePopular.getText() == null || "".equals(campoNomePopular.getText())){
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


    private void limparCampos(){
        campoCodigoEspecie.setText("");
        campoNomePopular.setText("");
        campoNomeCientifico.setText("");
    }

}
