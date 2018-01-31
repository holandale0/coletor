package coletor.sismanejo.com.br.coletor.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import coletor.sismanejo.com.br.coletor.adapters.ArvoreGPSAdapter;
import coletor.sismanejo.com.br.coletor.daos.BancoController;
import coletor.sismanejo.com.br.coletor.daos.CriaBanco;
import coletor.sismanejo.com.br.coletor.models.Arvore;
import coletor.sismanejo.com.br.coletor.models.Especie;
import coletor.sismanejo.com.br.coletor.models.Projeto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import coletor.sismanejo.com.br.coletor.R;

public class ColetaActivity extends AppCompatActivity implements LocationListener {

    private float x1, x2, y1;

    private HorizontalScrollView registros;
    private ListView listaArvores;
    private CriaBanco criaBanco;
    private BancoController controlador;
    private Projeto projeto;
    private List<Especie> especies = new ArrayList<Especie>();
    private Especie esp;
    private Arvore arv;
    private EditText lbUt;
    private EditText campoNumero;
    private EditText campoEspecie;
    private EditText campoCap;
    private EditText campoAltura;
    private EditText campoFuste;
    private EditText campoFaixa;
    private EditText campoObs;
    private EditText campoCoordX;
    private Switch comboDE;
    private EditText campoCoordY;
    private TextView campo_ponto_gps;
    private TextView lb_coordx;
    private TextView lb_orientx;
    private TextView lb_coordy;
    private TextView lb_ponto_gps;
    private TextView lbNomePopular;
    private File arquivo;
    private Button botao_salvar;
    private Button botao_voltar;
    private Button botao_atualizar;
    private Button botao_cancelar;
    private Button botao_exportar_ut;
    private int total_arvores_ut = 0;
    private final List<Arvore> arvores_final = new ArrayList<Arvore>();
    private List<Arvore> arvores = new ArrayList<Arvore>();
    private String nome_arquivo = "";
    private File file;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_FINE_LOCATION = 1;
    private static String[] PERMISSIONS_FINE_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION};
    private LocationManager locationmanager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coleta);

        //criaBanco = new CriaBanco(ColetaActivity.this);
        controlador = new BancoController(ColetaActivity.this);

        verifyStoragePermissions(ColetaActivity.this);
        verifyLocationManagerPermissions(ColetaActivity.this);


        Bundle bundle = getIntent().getExtras();
        projeto = (Projeto) getIntent().getSerializableExtra("PROJETO");
        especies = controlador.consultaEspecies();

        registros = (HorizontalScrollView) findViewById(R.id.registros);

        lbUt = (EditText) findViewById(R.id.campoUt);
        campoFaixa = (EditText) findViewById(R.id.campoFaixa);
        campoNumero = (EditText) findViewById(R.id.campoNumeroArv);
        campoEspecie = (EditText) findViewById(R.id.campoCodEspecie);
        campoCap = (EditText) findViewById(R.id.campoCap);
        campoAltura = (EditText) findViewById(R.id.campoAltura);
        campoFuste = (EditText) findViewById(R.id.campoQf);
        campoObs = (EditText) findViewById(R.id.campoObservacao);
        campoCoordX = (EditText) findViewById(R.id.campoX);
        comboDE = (Switch) findViewById(R.id.comboOrientacaoX);
        campoCoordY = (EditText) findViewById(R.id.campoY);
        campo_ponto_gps = (EditText) findViewById(R.id.campoGPS);

        lb_coordx = (TextView) findViewById(R.id.lb_coordx);
        lb_orientx = (TextView) findViewById(R.id.lb_orientx);
        lb_coordy = (TextView) findViewById(R.id.lb_coordy);
        lb_ponto_gps = (TextView) findViewById(R.id.lb_ponto_gps);



        lbUt.setText(String.valueOf(projeto.getUt()));

        arvores = controlador.getArvoresUpaUt(String.valueOf(projeto.getUpa()), String.valueOf(projeto.getUt()));

        if (!arvores.isEmpty()) {
            campoFaixa.setText(arvores.get(arvores.size() - 1).getFaixa());
            campoNumero.requestFocus();
        } else {
            campoFaixa.requestFocus();

        }

        addListenerOnButton();
        //addListenerOnField();


        if (projeto.getTipo_coordenada() == 2) {
            lb_coordx.setVisibility(View.INVISIBLE);
            lb_coordx.setWidth(0);
            lb_orientx.setVisibility(View.INVISIBLE);
            lb_orientx.setWidth(0);
            lb_coordy.setVisibility(View.INVISIBLE);
            lb_coordy.setWidth(0);
            campoCoordX.setVisibility(View.INVISIBLE);
            campoCoordX.setWidth(0);
            comboDE.setVisibility(View.INVISIBLE);
            comboDE.setMinimumWidth(0);
            campoCoordY.setVisibility(View.INVISIBLE);
            campoCoordY.setWidth(0);
            populateListViewGps();
        } else {
            campo_ponto_gps.setVisibility(View.INVISIBLE);
            campo_ponto_gps.setWidth(0);
            lb_ponto_gps.setVisibility(View.INVISIBLE);
            lb_ponto_gps.setWidth(0);
            populateListViewXy();
        }



    }


    public void addListenerOnField() {

        final Context context = this;

        campoEspecie.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (campoEspecie.getText() != null || !"".equals(campoEspecie.getText().toString())) {
                    if (!controlador.isEspecieExists(Integer.parseInt(campoEspecie.getText().toString()))) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                        alertDialog.setMessage("Código da espécie inválido ou inexistente...");
                        alertDialog.show();
                        lbNomePopular.setText("...não encontrado...");
                        campoEspecie.requestFocus();
                        return false;
                    } else {
                        esp = new Especie();
                        esp = controlador.getEspecie(Integer.parseInt(event.getCharacters()));
                        lbNomePopular.setText(esp.getNomePopular());
                        return true;
                    }
                }
                return true;
            }
        });
    }









    public void addListenerOnButton() {

        final Context context = this;

        botao_voltar = (Button) findViewById(R.id.btnVoltar);
        botao_salvar = (Button) findViewById(R.id.btnSalvar);
        botao_atualizar = (Button) findViewById(R.id.btnAtualizar);
        botao_cancelar = (Button) findViewById(R.id.btnCancelar);
        botao_exportar_ut = (Button) findViewById(R.id.btnExportar);

        //botao_exportar_ut.setWidth(380);

        botao_exportar_ut.setText("EXPORTAR UT "+projeto.getUt()+" .csv");



        botao_atualizar.setVisibility(View.INVISIBLE);
        botao_cancelar.setVisibility(View.INVISIBLE);

        comboDE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(comboDE.isChecked()){
                    lb_orientx.setText("DIR");
                }else{
                    lb_orientx.setText("ESQ");
                }
            }
        });

        botao_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(ColetaActivity.this, PrincipalActivity.class);
                confirmDialog(ColetaActivity.this, intent);
            }
        });


        botao_salvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (projeto.getTipo_coordenada() == 2) {
                    salvarArvoreGps();
                } else {
                    salvarArvoreXy();
                }

                arv = null;

            }

        });

        botao_atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (projeto.getTipo_coordenada() == 2) {
                    atualizarArvoreGps();
                } else {
                    atualizarArvoreXy();
                }

                arv = null;
            }
        });


        botao_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arv = null;
                limparCampos();
                botao_salvar.setVisibility(View.VISIBLE);
                botao_atualizar.setVisibility(View.INVISIBLE);
                botao_cancelar.setVisibility(View.INVISIBLE);
                campoFaixa.setText(String.valueOf(arvores.get(arvores.size() - 1).getFaixa()));
                campoNumero.setText(String.valueOf(Integer.parseInt(arvores.get(arvores.size() - 1).getNumero()) + 1));
            }
        });

        botao_exportar_ut.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                if (projeto.getTipo_coordenada() == 2) {
                    exportarUtGps();
                } else {
                    exportarUtXy();
                }
            }
        });


    }


    private void confirmDialog(Context context, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Voltar à tela inicial ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(intent);
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

    private void confirmaExclusão(final Context context, final Arvore arvore) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Confirma a exclusão da árvore "+arvore.getNumero()+" ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        controlador.deletaArvore(arvore);

                        if (projeto.getTipo_coordenada() == 2){
                            arvores.remove(arvore);
                            refreshListViewGps();

                        }else{
                            arvores.remove(arvore);
                            refreshListViewXy();

                        }

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
                .setMessage("Árvore excluída com sucesso !")
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


    private void mensagemDuplicada(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Registro Duplicado")
                .setMessage("Já existe uma árvore com o número " + String.valueOf(campoNumero.getText()) + " na UT " + String.valueOf(projeto.getUt()))
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }



    private void escolherAcao(Context context, final Arvore arvore) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Escolha a ação")
                .setMessage("Deseja EDITAR ou EXCLUIR a árvore " + String.valueOf(campoNumero.getText()) + " na UT " + String.valueOf(projeto.getUt()) + " ?")
                .setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        acaoEditar(arvore);
                    }
                }).setNegativeButton("EXCLUIR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        acaoExcluir(arvore);
                    }
                }).setNeutralButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
    }


    private void limparCampos() {
        campoNumero.setText("");
        campoEspecie.setText("");
        campoCap.setText("");
        campoAltura.setText("");
        campoFuste.setText("");
        campoFaixa.setText("");
        campoObs.setText("");
        campoCoordX.setText("");
        campoCoordY.setText("");
    }


    public void populateListViewGps() {

        arvores.clear();
        arvores_final.clear();
        arvores = controlador.getArvoresUpaUt(String.valueOf(projeto.getUpa()), String.valueOf(projeto.getUt()));


        for (int i = 0; i < arvores.size(); i++) {
            arvores_final.add(arvores.get(i));
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
            System.out.println(arvores.get(i).getNumero());
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
        }

        LayoutInflater infalter = getLayoutInflater();
        ListView lista = (ListView) findViewById(R.id.lista_arvores);
        ArvoreGPSAdapter adapter = new ArvoreGPSAdapter(this, arvores_final);
        lista.setAdapter(adapter);
        View header = infalter.inflate(R.layout.cabecalho_gps, lista, false);
        lista.addHeaderView(header,null,false);
        lista.setHeaderDividersEnabled(true);




        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Arvore arvore = arvores.get(position);
                escolherAcao(ColetaActivity.this, arvore);
                return true;
            }
        });

    }

    public void populateListViewXy() {

        arvores.clear();
        arvores_final.clear();
        arvores = controlador.getArvoresUpaUt(String.valueOf(projeto.getUpa()), String.valueOf(projeto.getUt()));

        for (int i = 0; i < arvores.size(); i++) {
            arvores_final.add(arvores.get(i));
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
            System.out.println(arvores.get(i).getNumero());
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
        }

        LayoutInflater infalter = getLayoutInflater();
        ListView lista = (ListView) findViewById(R.id.lista_arvores);
        ArvoreGPSAdapter adapter = new ArvoreGPSAdapter(this, arvores_final);
        lista.setAdapter(adapter);
        View header = infalter.inflate(R.layout.cabecalho_xy, lista, false);
        lista.addHeaderView(header,null,false);
        lista.setHeaderDividersEnabled(true);


        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Arvore arvore = arvores.get(position);

                escolherAcao(ColetaActivity.this, arvore);

                return true;
            }
        });

    }


    public void refreshListViewGps() {


        campoFaixa.setText(String.valueOf(arvores.get(arvores.size() - 1).getFaixa()));
        campoNumero.setText(String.valueOf(Integer.parseInt(arvores.get(arvores.size() - 1).getNumero()) + 1));

        arvores_final.clear();


        for (int i = 0; i < arvores.size(); i++) {
            arvores_final.add(arvores.get(i));
        }

        LayoutInflater infalter = getLayoutInflater();
        ListView lista = (ListView) findViewById(R.id.lista_arvores);
        ArvoreGPSAdapter adapter = new ArvoreGPSAdapter(this, arvores_final);
        lista.setAdapter(adapter);
        //View header = infalter.inflate(R.layout.cabecalho_gps, lista, false);
        //lista.addHeaderView(header);


        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Arvore arvore = arvores.get(position);

                escolherAcao(ColetaActivity.this, arvore);

                return true;
            }
        });

    }

    public void refreshListViewXy() {


        campoFaixa.setText(String.valueOf(arvores.get(arvores.size() - 1).getFaixa()));
        campoNumero.setText(String.valueOf(Integer.parseInt(arvores.get(arvores.size() - 1).getNumero()) + 1));

        arvores_final.clear();

        for (int i = 0; i < arvores.size(); i++) {
            arvores_final.add(arvores.get(i));
        }

        LayoutInflater infalter = getLayoutInflater();
        ListView lista = (ListView) findViewById(R.id.lista_arvores);
        ArvoreGPSAdapter adapter = new ArvoreGPSAdapter(this, arvores_final);
        lista.setAdapter(adapter);
        //View header = infalter.inflate(R.layout.cabecalho_xy, lista, false);
        //lista.addHeaderView(header);


        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Arvore arvore = arvores.get(position);

                escolherAcao(ColetaActivity.this, arvore);

                return true;
            }
        });

    }

    private final void acaoExcluir(Arvore arvore){

        confirmaExclusão(ColetaActivity.this,arvore);

    }


    private final void acaoEditar(Arvore arvore) {


        arv = arvore;

        botao_atualizar.setVisibility(View.VISIBLE);
        botao_cancelar.setVisibility(View.VISIBLE);
        botao_salvar.setVisibility(View.INVISIBLE);

        lbUt.setText(String.valueOf(arvore.getUt()));
        campoFaixa.setText(String.valueOf(arvore.getFaixa()));
        campoNumero.setText(String.valueOf(arvore.getNumero()));
        campoEspecie.setText(arvore.getEspecie());
        campoCap.setText(String.valueOf(arvore.getCap()));
        campoAltura.setText(String.valueOf(arvore.getAltura()));
        campoFuste.setText(String.valueOf(arvore.getQf()));
        campoObs.setText(String.valueOf(arvore.getObservacao()));
        campoCoordX.setText(String.valueOf(arvore.getCoordx()));
        if (arvore.getOrientx().contains("D")) {
            comboDE.setChecked(true);
            lb_orientx.setText("DIR");
        } else {
            comboDE.setChecked(false);
            lb_orientx.setText("ESQ");
        }
        campoCoordY.setText(String.valueOf(arvore.getCoordy()));
    }


    private void salvarArvoreGps() {
        try {

            arv = new Arvore();

            arv.setUpa(String.valueOf(projeto.getUpa()));

            arv.setUt(String.valueOf(projeto.getUt()));

            if (!isInteger(campoFaixa.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para faixa...");
                alertDialog.show();
                campoFaixa.requestFocus();
                return;
            }

            if (!isInteger(campoNumero.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para número da árvore...");
                alertDialog.show();
                campoNumero.requestFocus();
                return;
            }

            if(!controlador.isEspecieExists(Integer.parseInt(campoEspecie.getText().toString()))){
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Código da espécie inválido ou inexistente...");
                alertDialog.show();
                return;
            }else{
                esp = new Especie();
                esp = controlador.getEspecie(Integer.parseInt(campoEspecie.getText().toString()));
            }


            if (!isDouble(campoCap.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para CAP...");
                alertDialog.show();
                campoCap.requestFocus();
                return;
            }

            if (!isDouble(campoAltura.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para altura...");
                alertDialog.show();
                campoAltura.requestFocus();
                return;
            }

            if (!isInteger(campoFuste.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para QF...");
                alertDialog.show();
                campoFuste.requestFocus();
                return;
            }

            if (!isInteger(campo_ponto_gps.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para ponto GPS...");
                alertDialog.show();
                campoFuste.requestFocus();
                return;
            }


            arv.setNumero(campoNumero.getText().toString());
            arv.setEspecie(esp.getNomePopular());
            arv.setCap(campoCap.getText().toString());
            arv.setAltura(campoAltura.getText().toString());
            arv.setQf(campoFuste.getText().toString());
            arv.setFaixa(campoFaixa.getText().toString());
            arv.setPonto(campo_ponto_gps.getText().toString());
            arv.setOrientx("D");
            arv.setCoordx("0");
            arv.setCoordy("0");



            if (campoObs.getText() == null || "".equals(campoObs.getText().toString()) || campoObs.getText().length() < 1) {
                arv.setObservacao("");
            } else {
                arv.setObservacao(campoObs.getText().toString());
            }

            arv.setAnotador(projeto.getAnotador());
            arv.setBotanico(projeto.getBotanico());
            arv.setPlaqueteiro(projeto.getPlaqueteiro());
            arv.setLateral_x("");
            arv.setLateral_y("");
            arv.setAnotador_gps(projeto.getAnotador_gps());


            if (controlador.duplicada("0" ,arv.getNumero(), String.valueOf(projeto.getUt()), String.valueOf(projeto.getUpa()))) {

                    mensagemDuplicada(ColetaActivity.this);

                    botao_cancelar.setVisibility(View.VISIBLE);
                    botao_salvar.setVisibility(View.VISIBLE);
                    botao_atualizar.setVisibility(View.INVISIBLE);

                } else {

                    System.out.println(arv);

                    controlador.insereArvore(arv);

                    arvores.add(arv);

                    limparCampos();

                    refreshListViewGps();

                    botao_cancelar.setVisibility(View.VISIBLE);
                    botao_salvar.setVisibility(View.VISIBLE);
                    botao_atualizar.setVisibility(View.INVISIBLE);

                    campoFaixa.setText(String.valueOf(arvores.get(arvores.size() - 1).getFaixa()));
                    //campoNumero.setText(String.valueOf(Integer.parseInt(arvores.get(arvores.size() - 1).getNumero()) + 1));
                    //campo_ponto_gps.setText(String.valueOf(Integer.parseInt(arvores.get(arvores.size() - 1).getPonto()) + 1));
                    campoNumero.requestFocus();

                }

        } catch (Exception e) {

            System.out.println(e);

            openDialog(ColetaActivity.this);

        }
    }


    private void atualizarArvoreGps() {
        try {


            arv.setUpa(String.valueOf(projeto.getUpa()));
            arv.setUt(String.valueOf(projeto.getUt()));

            if (!isInteger(campoFaixa.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para faixa...");
                alertDialog.show();
                campoFaixa.requestFocus();
                return;
            }

            if (!isInteger(campoNumero.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para número da árvore...");
                alertDialog.show();
                campoNumero.requestFocus();
                return;
            }

            if(!controlador.isEspecieExists(Integer.parseInt(campoEspecie.getText().toString()))){
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Código da espécie inválido ou inexistente...");
                alertDialog.show();
                return;
            }else{
                esp = new Especie();
                esp = controlador.getEspecie(Integer.parseInt(campoEspecie.getText().toString()));
            }

            if (!isDouble(campoCap.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para CAP...");
                alertDialog.show();
                campoCap.requestFocus();
                return;
            }

            if (!isDouble(campoAltura.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para altura...");
                alertDialog.show();
                campoAltura.requestFocus();
                return;
            }

            if (!isInteger(campoFuste.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para QF...");
                alertDialog.show();
                campoFuste.requestFocus();
                return;
            }

            if (!isInteger(campo_ponto_gps.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para ponto GPS...");
                alertDialog.show();
                campoFuste.requestFocus();
                return;
            }

            arv.setNumero(campoNumero.getText().toString());
            arv.setEspecie(esp.getNomePopular());
            arv.setCap(campoCap.getText().toString());
            arv.setAltura(campoAltura.getText().toString());
            arv.setQf(campoFuste.getText().toString());
            arv.setFaixa(campoFaixa.getText().toString());
            arv.setPonto(campo_ponto_gps.getText().toString());
            arv.setOrientx("D");
            arv.setCoordx("0");
            arv.setCoordy("0");


            if (campoObs.getText() == null || "".equals(campoObs.getText().toString()) || campoObs.getText().length() < 1) {
                arv.setObservacao("");
            } else {
                arv.setObservacao(campoObs.getText().toString());
            }

            arv.setAnotador(projeto.getAnotador());
            arv.setBotanico(projeto.getBotanico());
            arv.setPlaqueteiro(projeto.getPlaqueteiro());
            arv.setLateral_x("");
            arv.setLateral_y("");
            arv.setAnotador_gps(projeto.getAnotador_gps());

            if (controlador.duplicada(String.valueOf(arv.getId_arvore()) ,arv.getNumero(), String.valueOf(projeto.getUt()), String.valueOf(projeto.getUpa()))) {

                mensagemDuplicada(ColetaActivity.this);

                botao_cancelar.setVisibility(View.VISIBLE);
                botao_salvar.setVisibility(View.INVISIBLE);
                botao_atualizar.setVisibility(View.VISIBLE);

            }else{

                System.out.println(arv);

                controlador.alteraArvore(arv);

                campoEspecie.requestFocus();

                limparCampos();

                refreshListViewGps();

                botao_cancelar.setVisibility(View.VISIBLE);
                botao_salvar.setVisibility(View.VISIBLE);
                botao_atualizar.setVisibility(View.INVISIBLE);
            }




        } catch (Exception e) {

            System.out.println(e);

            openDialog(ColetaActivity.this);

        }
    }


    private void salvarArvoreXy() {
        try {

            arv = new Arvore();

            arv.setUt(String.valueOf(projeto.getUt()));
            arv.setUpa(String.valueOf(projeto.getUpa()));

            if (!isInteger(campoFaixa.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para faixa...");
                alertDialog.show();
                campoFaixa.requestFocus();
                return;
            }

            if (!isInteger(campoNumero.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para número da árvore...");
                alertDialog.show();
                campoNumero.requestFocus();
                return;
            }

            if(!controlador.isEspecieExists(Integer.parseInt(campoEspecie.getText().toString()))){
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Código da espécie inválido ou inexistente...");
                alertDialog.show();
                return;
            }else{
                esp = new Especie();
                esp = controlador.getEspecie(Integer.parseInt(campoEspecie.getText().toString()));
            }

            if (!isDouble(campoCap.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para CAP...");
                alertDialog.show();
                campoCap.requestFocus();
                return;
            }

            if (!isDouble(campoAltura.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para altura...");
                alertDialog.show();
                campoAltura.requestFocus();
                return;
            }

            if (!isInteger(campoFuste.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para QF...");
                alertDialog.show();
                campoFuste.requestFocus();
                return;
            }

            if (!isDouble(campoCoordX.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para coordenada X...");
                alertDialog.show();
                campoCoordX.requestFocus();
                return;
            }

            if (!isDouble(campoCoordY.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido coordenada Y...");
                alertDialog.show();
                campoCoordY.requestFocus();
                return;
            }


            arv.setNumero(campoNumero.getText().toString());
            arv.setEspecie(esp.getNomePopular());
            arv.setCap(campoCap.getText().toString());
            arv.setAltura(campoAltura.getText().toString());
            arv.setQf(campoFuste.getText().toString());
            arv.setFaixa(campoFaixa.getText().toString());
            arv.setPonto(campoNumero.getText().toString());
            arv.setCoordx(campoCoordX.getText().toString());
            if (comboDE.isChecked()) {
                arv.setOrientx("D");
            } else {
                arv.setOrientx("E");
            }
            arv.setCoordy(campoCoordY.getText().toString());


            if (campoObs.getText() == null || "".equals(campoObs.getText().toString()) || campoObs.getText().length() < 1) {
                arv.setObservacao("");
            } else {
                arv.setObservacao(campoObs.getText().toString());
            }

            arv.setAnotador(projeto.getAnotador());
            arv.setBotanico(projeto.getBotanico());
            arv.setPlaqueteiro(projeto.getPlaqueteiro());
            arv.setLateral_x(projeto.getLateral_x());
            arv.setLateral_y(projeto.getLateral_y());
            arv.setAnotador_gps("");


            if (controlador.duplicada("0" ,arv.getNumero(), String.valueOf(projeto.getUt()), String.valueOf(projeto.getUpa()))) {

                mensagemDuplicada(ColetaActivity.this);

                botao_cancelar.setVisibility(View.VISIBLE);
                botao_salvar.setVisibility(View.VISIBLE);
                botao_atualizar.setVisibility(View.INVISIBLE);

            } else {

                System.out.println(arv);

                controlador.insereArvore(arv);

                arvores.add(arv);

                limparCampos();

                refreshListViewXy();

                botao_cancelar.setVisibility(View.VISIBLE);
                botao_salvar.setVisibility(View.VISIBLE);
                botao_atualizar.setVisibility(View.INVISIBLE);

                campoFaixa.setText(String.valueOf(arvores.get(arvores.size() - 1).getFaixa()));
                //campoNumero.setText(String.valueOf(Integer.parseInt(arvores.get(arvores.size() - 1).getNumero()) + 1));
                campoNumero.requestFocus();
            }


        } catch (Exception e) {

            System.out.println(e);

            openDialog(ColetaActivity.this);

        }
    }


    private void atualizarArvoreXy() {
        try {


            arv.setUpa(String.valueOf(projeto.getUpa()));
            arv.setUt(String.valueOf(projeto.getUt()));

            if (!isInteger(campoFaixa.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para faixa...");
                alertDialog.show();
                campoFaixa.requestFocus();
                return;
            }

            if (!isInteger(campoNumero.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para número da árvore...");
                alertDialog.show();
                campoNumero.requestFocus();
                return;
            }

            if(!controlador.isEspecieExists(Integer.parseInt(campoEspecie.getText().toString()))){
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Código da espécie inválido ou inexistente...");
                alertDialog.show();
                return;
            }else{
                esp = new Especie();
                esp = controlador.getEspecie(Integer.parseInt(campoEspecie.getText().toString()));
            }

            if (!isDouble(campoCap.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para CAP...");
                alertDialog.show();
                campoCap.requestFocus();
                return;
            }

            if (!isDouble(campoAltura.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para altura...");
                alertDialog.show();
                campoAltura.requestFocus();
                return;
            }

            if (!isInteger(campoFuste.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para QF...");
                alertDialog.show();
                campoFuste.requestFocus();
                return;
            }

            if (!isDouble(campoCoordX.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido para coordenada X...");
                alertDialog.show();
                campoCoordX.requestFocus();
                return;
            }

            if (!isDouble(campoCoordY.getText().toString())) {
                AlertDialog alertDialog = new AlertDialog.Builder(ColetaActivity.this).create();
                alertDialog.setMessage("Informe um valor válido coordenada Y...");
                alertDialog.show();
                campoCoordY.requestFocus();
                return;
            }


            arv.setNumero(campoNumero.getText().toString());
            arv.setEspecie(esp.getNomePopular());
            arv.setCap(campoCap.getText().toString());
            arv.setAltura(campoAltura.getText().toString());
            arv.setQf(campoFuste.getText().toString());
            arv.setFaixa(campoFaixa.getText().toString());
            arv.setPonto(campoNumero.getText().toString());
            arv.setCoordx(campoCoordX.getText().toString());
            if (comboDE.isChecked()) {
                arv.setOrientx("D");
            } else {
                arv.setOrientx("E");
            }
            arv.setCoordy(campoCoordY.getText().toString());


            if (campoObs.getText() == null || "".equals(campoObs.getText().toString()) || campoObs.getText().length() < 1) {
                arv.setObservacao("");
            } else {
                arv.setObservacao(campoObs.getText().toString());
            }

            arv.setAnotador(projeto.getAnotador());
            arv.setBotanico(projeto.getBotanico());
            arv.setPlaqueteiro(projeto.getPlaqueteiro());
            arv.setLateral_x(projeto.getLateral_x());
            arv.setLateral_y(projeto.getLateral_y());
            arv.setAnotador_gps("");


            if (controlador.duplicada(String.valueOf(arv.getId_arvore()) ,arv.getNumero(), String.valueOf(projeto.getUt()), String.valueOf(projeto.getUpa()))) {

                mensagemDuplicada(ColetaActivity.this);

                botao_cancelar.setVisibility(View.VISIBLE);
                botao_salvar.setVisibility(View.INVISIBLE);
                botao_atualizar.setVisibility(View.VISIBLE);

            }else{

                System.out.println(arv);

                controlador.alteraArvore(arv);

                campoEspecie.requestFocus();

                botao_cancelar.setVisibility(View.VISIBLE);
                botao_salvar.setVisibility(View.VISIBLE);
                botao_atualizar.setVisibility(View.INVISIBLE);

                limparCampos();

                refreshListViewGps();
            }


        } catch (Exception e) {

            System.out.println(e);

            openDialog(ColetaActivity.this);

        }
    }


    private void exportarUtGps() {
        try {
            String nome_arquivo = "upa_" + projeto.getUpa() + "_ut_" + projeto.getUt() + ".csv";
            File diretorio = new File(android.os.Environment.getExternalStorageDirectory(), "/coletor_inventario");
            if (!diretorio.exists()) {
                diretorio.mkdir();
                diretorio.mkdirs();
                //myFile.createNewFile();
            }

            arquivo = new File(diretorio, nome_arquivo);

            if (arquivo.exists()) {
                arquivo.delete();
                arquivo = new File(diretorio, nome_arquivo);
            }

            String TestString = "";


            FileOutputStream outputStream = new FileOutputStream(arquivo, true);

            Writer outputStreamWriter = new OutputStreamWriter(outputStream, "ISO-8859-1");

            TestString += "UT";
            TestString += ";";
            TestString += "FAIXA";
            TestString += ";";
            TestString += "NR ÁRVORE";
            TestString += ";";
            TestString += "ESPÉCIE";
            TestString += ";";
            TestString += "CAP";
            TestString += ";";
            TestString += "ALTURA";
            TestString += ";";
            TestString += "QF";
            TestString += ";";
            TestString += "OBSERVAÇÃO";
            TestString += ";";
            TestString += "PONTO GPS";
            TestString += ";";
            TestString += "ANOTADOR";
            TestString += ";";
            TestString += "BOTÂNICO";
            TestString += ";";
            TestString += "PLAQUETEIRO";
            TestString += "\n";


            for (int i = 1; i < arvores.size(); i++) {


                TestString += arvores.get(i).getUt();
                TestString += ";";
                TestString += arvores.get(i).getFaixa();
                TestString += ";";
                TestString += arvores.get(i).getNumero();
                TestString += ";";
                TestString += arvores.get(i).getEspecie();
                TestString += ";";
                TestString += arvores.get(i).getCap();
                TestString += ";";
                TestString += arvores.get(i).getAltura();
                TestString += ";";
                TestString += arvores.get(i).getQf();
                TestString += ";";
                TestString += arvores.get(i).getObservacao();
                TestString += ";";
                TestString += arvores.get(i).getPonto();
                TestString += ";";
                TestString += arvores.get(i).getAnotador();
                TestString += ";";
                TestString += arvores.get(i).getBotanico();
                TestString += ";";
                TestString += arvores.get(i).getPlaqueteiro();
                TestString += "\n";
            }
            Log.v("the string is", TestString);
            outputStreamWriter.write(TestString);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            sucessoExportacaoUt(ColetaActivity.this, projeto.getUpa(), projeto.getUt(), nome_arquivo);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void exportarUtXy() {
        try {
            String nome_arquivo = "upa_" + projeto.getUpa() + "_ut_" + projeto.getUt() + ".csv";
            File diretorio = new File(android.os.Environment.getExternalStorageDirectory(), "/coletor_inventario");
            if (!diretorio.exists()) {
                diretorio.mkdir();
                diretorio.mkdirs();
                //myFile.createNewFile();
            }

            arquivo = new File(diretorio, nome_arquivo);

            if (arquivo.exists()) {
                arquivo.delete();
                arquivo = new File(diretorio, nome_arquivo);
            }

            String TestString = "";


            FileOutputStream outputStream = new FileOutputStream(arquivo, true);

            Writer outputStreamWriter = new OutputStreamWriter(outputStream, "ISO-8859-1");

            TestString += "UT";
            TestString += ";";
            TestString += "FAIXA";
            TestString += ";";
            TestString += "NR ÁRVORE";
            TestString += ";";
            TestString += "ESPÉCIE";
            TestString += ";";
            TestString += "CAP";
            TestString += ";";
            TestString += "ALTURA";
            TestString += ";";
            TestString += "QF";
            TestString += ";";
            TestString += "OBSERVAÇÃO";
            TestString += ";";
            TestString += "COORDENADA X";
            TestString += ";";
            TestString += "DIR / ESQ";
            TestString += ";";
            TestString += "COORDENADA Y";
            TestString += ";";
            TestString += "ANOTADOR";
            TestString += ";";
            TestString += "BOTÂNICO";
            TestString += ";";
            TestString += "PLAQUETEIRO";
            TestString += ";";
            TestString += "ANOTADOR X";
            TestString += ";";
            TestString += "ANOTADOR Y";
            TestString += "\n";


            for (int i = 1; i < arvores.size(); i++) {


                TestString += arvores.get(i).getUt();
                TestString += ";";
                TestString += arvores.get(i).getFaixa();
                TestString += ";";
                TestString += arvores.get(i).getNumero();
                TestString += ";";
                TestString += arvores.get(i).getEspecie();
                TestString += ";";
                TestString += arvores.get(i).getCap();
                TestString += ";";
                TestString += arvores.get(i).getAltura();
                TestString += ";";
                TestString += arvores.get(i).getQf();
                TestString += ";";
                TestString += arvores.get(i).getObservacao();
                TestString += ";";
                TestString += arvores.get(i).getCoordx();
                TestString += ";";
                TestString += arvores.get(i).getOrientx();
                TestString += ";";
                TestString += arvores.get(i).getCoordy();
                TestString += ";";
                TestString += arvores.get(i).getAnotador();
                TestString += ";";
                TestString += arvores.get(i).getBotanico();
                TestString += ";";
                TestString += arvores.get(i).getPlaqueteiro();
                TestString += ";";
                TestString += arvores.get(i).getLateral_x();
                TestString += ";";
                TestString += arvores.get(i).getLateral_y();
                TestString += "\n";
            }
            Log.v("the string is", TestString);
            outputStreamWriter.write(TestString);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            sucessoExportacaoUt(ColetaActivity.this, projeto.getUpa(), projeto.getUt(), nome_arquivo);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    private void exportarUpaGps(){
        try {
            String nome_arquivo = "inventario_upa_"+projeto.getUpa()+".csv";
            File diretorio = new File(android.os.Environment.getExternalStorageDirectory(),"/coletor_inventario");
            if (!diretorio.exists()) {
                diretorio.mkdir();
                diretorio.mkdirs();
                //myFile.createNewFile();
            }



            arquivo = new File(diretorio, nome_arquivo);

            if (arquivo.exists()) {
                arquivo.delete();
                arquivo = new File(diretorio, nome_arquivo);
            }

            String TestString="";


            FileOutputStream outputStream = new FileOutputStream (arquivo, true);

            Writer outputStreamWriter = new OutputStreamWriter(outputStream, "ISO-8859-1");


            TestString+="UT";
            TestString += ";";
            TestString+="FAIXA";
            TestString += ";";
            TestString+="NR ÁRVORE";
            TestString += ";";
            TestString+="ESPÉCIE";
            TestString += ";";
            TestString+="CAP";
            TestString += ";";
            TestString+="ALTURA";
            TestString += ";";
            TestString+="QF";
            TestString += ";";
            TestString+="OBSERVAÇÃO";
            TestString += ";";
            TestString+="PONTO GPS";
            TestString += ";";
            TestString+="ANOTADOR";
            TestString += ";";
            TestString+="BOTÂNICO";
            TestString += ";";
            TestString+="PLAQUETEIRO";
            TestString+="\n";


            for(int  i=1; i<arvores.size(); i++)
            {


                TestString+=arvores.get(i).getUt();
                TestString += ";";
                TestString+=arvores.get(i).getFaixa();
                TestString += ";";
                TestString+=arvores.get(i).getNumero();
                TestString += ";";
                TestString+=arvores.get(i).getEspecie();
                TestString += ";";
                TestString+=arvores.get(i).getCap();
                TestString += ";";
                TestString+=arvores.get(i).getAltura();
                TestString += ";";
                TestString+=arvores.get(i).getQf();
                TestString += ";";
                TestString+=arvores.get(i).getObservacao();
                TestString += ";";
                TestString+=arvores.get(i).getPonto();
                TestString += ";";
                TestString+=arvores.get(i).getAnotador();
                TestString += ";";
                TestString+=arvores.get(i).getBotanico();
                TestString += ";";
                TestString+=arvores.get(i).getPlaqueteiro();
                TestString+="\n";
            }
            Log.v("the string is",TestString);
            outputStreamWriter.write(TestString);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            sucessoExportacaoUpa(ColetaActivity.this, projeto.getUpa(), nome_arquivo);
        }
        catch (IOException ioe)
        {ioe.printStackTrace();}
    }


    private void exportarUpaXy(){
        try {
            String nome_arquivo = "inventario_upa_"+projeto.getUpa()+".csv";
            File diretorio = new File(android.os.Environment.getExternalStorageDirectory(),"/coletor_inventario");
            if (!diretorio.exists()) {
                diretorio.mkdir();
                diretorio.mkdirs();
                //myFile.createNewFile();
            }

            arquivo = new File(diretorio, nome_arquivo);

            if (arquivo.exists()) {
                arquivo.delete();
                arquivo = new File(diretorio, nome_arquivo);
            }

            String TestString="";


            FileOutputStream outputStream = new FileOutputStream (arquivo, true);

            Writer outputStreamWriter = new OutputStreamWriter(outputStream, "ISO-8859-1");

            TestString+="UT";
            TestString += ";";
            TestString+="FAIXA";
            TestString += ";";
            TestString+="NR ÁRVORE";
            TestString += ";";
            TestString+="ESPÉCIE";
            TestString += ";";
            TestString+="CAP";
            TestString += ";";
            TestString+="ALTURA";
            TestString += ";";
            TestString+="QF";
            TestString += ";";
            TestString+="OBSERVAÇÃO";
            TestString += ";";
            TestString+="COORDENADA X";
            TestString += ";";
            TestString+="DIR / ESQ";
            TestString += ";";
            TestString+="COORDENADA Y";
            TestString += ";";
            TestString+="ANOTADOR";
            TestString += ";";
            TestString+="BOTÂNICO";
            TestString += ";";
            TestString+="PLAQUETEIRO";
            TestString += ";";
            TestString+="ANOTADOR X";
            TestString += ";";
            TestString+="ANOTADOR Y";
            TestString+="\n";


            for(int  i=1; i<arvores.size(); i++)
            {


                TestString+=arvores.get(i).getUt();
                TestString += ";";
                TestString+=arvores.get(i).getFaixa();
                TestString += ";";
                TestString+=arvores.get(i).getNumero();
                TestString += ";";
                TestString+=arvores.get(i).getEspecie();
                TestString += ";";
                TestString+=arvores.get(i).getCap();
                TestString += ";";
                TestString+=arvores.get(i).getAltura();
                TestString += ";";
                TestString+=arvores.get(i).getQf();
                TestString += ";";
                TestString+=arvores.get(i).getObservacao();
                TestString += ";";
                TestString+=arvores.get(i).getCoordx();
                TestString += ";";
                TestString+=arvores.get(i).getOrientx();
                TestString += ";";
                TestString+=arvores.get(i).getCoordy();
                TestString += ";";
                TestString+=arvores.get(i).getAnotador();
                TestString += ";";
                TestString+=arvores.get(i).getBotanico();
                TestString += ";";
                TestString+=arvores.get(i).getPlaqueteiro();
                TestString += ";";
                TestString+=arvores.get(i).getLateral_x();
                TestString += ";";
                TestString+=arvores.get(i).getLateral_y();
                TestString+="\n";
            }
            Log.v("the string is",TestString);
            outputStreamWriter.write(TestString);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            sucessoExportacaoUpa(ColetaActivity.this, projeto.getUpa(), nome_arquivo);
        }
        catch (IOException ioe)
        {ioe.printStackTrace();}
    }



    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException ne) {
            return false;
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


    public void sucessoExportacaoUt(Context context, final String ano_upa, final int num_ut, final String nome_arquivo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Inventário da UT "+num_ut+" (UPA "+ano_upa +") exportados com sucesso !\nVerifique na memória interna do seu dispositivo.\n\n"+nome_arquivo)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void sucessoExportacaoUpa(Context context, final String ano_upa, final String nome_arquivo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Inventário da UPA "+ano_upa +" exportados com sucesso !\nVerifique na memória interna do seu dispositivo.\n\n"+nome_arquivo)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }


    private void showLocation() {
        Geocoder geocoder;
        String bestProvider;
        List<Address> user = null;
        double lat;
        double lng;

        LocationManager lm = (LocationManager) ColetaActivity.this.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(bestProvider);

        if (location == null) {
            Toast.makeText(ColetaActivity.this, "Location Not found", Toast.LENGTH_LONG).show();
        } else {
            geocoder = new Geocoder(ColetaActivity.this);
            try {
                user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                lat = (double) user.get(0).getLatitude();
                lng = (double) user.get(0).getLongitude();
                System.out.println(" DDD lat: " + lat + ",  longitude: " + lng);
                //vw_lat.setText(String.valueOf(lat));
                //vw_lon.setText(String.valueOf(lng));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void updateLocation() {
        try {

            double latitude = 0.0;
            double longitude = 0.0;

            locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = null;
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = locationmanager.getBestProvider(criteria, false);


            if (provider != null & !provider.equals("")) {
                //location = locationmanager.getLastKnownLocation(provider);
                if (ActivityCompat.checkSelfPermission(ColetaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ColetaActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationmanager.requestLocationUpdates(provider, 2000, 1, ColetaActivity.this);
                if (location != null) {
                    onLocationChanged(location);
                } else {
                    Toast.makeText(getApplicationContext(), "location not found", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Provider is null", Toast.LENGTH_LONG).show();
            }

            if (locationmanager != null) {
                locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                location = locationmanager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    //vw_lat.setText(String.valueOf(latitude));
                    //vw_lon.setText(String.valueOf(longitude));

                } else {


                    String CurLat = String.valueOf(getLocation().getLatitude());
                    String Curlongi = String.valueOf(getLocation().getLongitude());

                    //vw_lat.setText(CurLat);
                    //vw_lon.setText(Curlongi);

                    Toast.makeText(
                            ColetaActivity.this,
                            "Location Null", Toast.LENGTH_SHORT).show();
                }
            }


            if (location == null) {
                //vw_lat.setText(String.valueOf(0.0));
                //vw_lon.setText(String.valueOf(0.0));
            } else {
                //vw_lat.setText(String.valueOf(location.getLatitude()));
                //vw_lon.setText(String.valueOf(location.getLongitude()));

            }

        } catch (Exception e) {

        }
    }


    private Location getLocation() {
        locationmanager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationmanager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location l = locationmanager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }



    public static void verifyStoragePermissions(Activity activity) {
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }




    public static void verifyLocationManagerPermissions(Activity activity){

        int acessFineLocation = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int acessCoarseLocation = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int internet = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);

        if(acessFineLocation != PackageManager.PERMISSION_GRANTED || acessCoarseLocation != PackageManager.PERMISSION_GRANTED ||internet != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISSIONS_FINE_LOCATION,REQUEST_FINE_LOCATION);
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        //vw_lat.setText(String.valueOf(location.getLatitude()));
        //vw_lon.setText(String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}


