package coletor.sismanejo.com.br.coletor.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import coletor.sismanejo.com.br.coletor.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coletor.sismanejo.com.br.coletor.adapters.ArvoreGPSAdapter;
import coletor.sismanejo.com.br.coletor.adapters.ArvoreXYAdapter;
import coletor.sismanejo.com.br.coletor.daos.BancoController;
import coletor.sismanejo.com.br.coletor.daos.CriaBanco;
import coletor.sismanejo.com.br.coletor.models.Arvore;

public class ConsultaActivity extends AppCompatActivity {

    private CriaBanco criaBanco;
    private BancoController controlador;
    private Button voltar;
    private Button exportar;
    private Button excluir;
    private Button salvar_sismanejo;

    private TextView col_pessoa4;
    private TextView col_pessoa5;
    private TextView col_coordx;
    private TextView col_coordy;
    private TextView col_orientx;
    private TextView col_ponto_gps;
    private String nome_arquivo = "";
    private List<String[]> dados = new ArrayList<String[]>();
    private final List<Arvore> arvores_final = new ArrayList<Arvore>();
    private List<Arvore> arvores = new ArrayList<Arvore>();
    private int clicks = 0;
    private Date data;
    private File arquivo;
    private int ano_upa;
    private int cfg = 0;
    private FileWriter mFileWriter;
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        Bundle bundle = getIntent().getExtras();
        cfg = bundle.getInt("CFG");
        ano_upa = bundle.getInt("ANO_UPA");
        verifyStoragePermissions(ConsultaActivity.this);
        criaBanco = new CriaBanco(ConsultaActivity.this);
        controlador = new BancoController(ConsultaActivity.this);
        col_coordx = (TextView) findViewById(R.id.col_coordx);
        col_orientx = (TextView) findViewById(R.id.col_orientx);
        col_coordy = (TextView) findViewById(R.id.col_coordy);
        col_ponto_gps = (TextView) findViewById(R.id.col_ponto_gps);
        col_pessoa4 = (TextView) findViewById(R.id.col_pessoa4);
        col_pessoa5 = (TextView) findViewById(R.id.col_pessoa5);

        arvores = controlador.getArvoresUpa(String.valueOf(ano_upa));

        addListenerOnButton();

        if(cfg == 2){
            col_coordx.setVisibility(View.INVISIBLE);
            col_orientx.setVisibility(View.INVISIBLE);
            col_coordy.setVisibility(View.INVISIBLE);
            col_pessoa4.setVisibility(View.INVISIBLE);
            col_pessoa5.setVisibility(View.INVISIBLE);
            populateListViewGps();
        }else{
            col_ponto_gps.setVisibility(View.INVISIBLE);
            populateListViewXy();
        }

        salvar_sismanejo.setEnabled(false);
        salvar_sismanejo.setVisibility(View.INVISIBLE);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void populateListViewGps(){

        arvores.clear();
        arvores_final.clear();
        arvores = controlador.getArvoresUpa(String.valueOf(ano_upa));

        for(int i = 0 ; i < arvores.size() ; i++){
            arvores_final.add(arvores.get(i));
        }

        ListView lista = (ListView) findViewById(R.id.lista_arvores);

        ArvoreGPSAdapter adapter = new ArvoreGPSAdapter(this, arvores_final);

        lista.setAdapter(adapter);


    }

    public void populateListViewXy(){

        arvores.clear();
        arvores_final.clear();
        arvores = controlador.getArvoresUpa(String.valueOf(ano_upa));

        for(int i = 0 ; i < arvores.size() ; i++){
            arvores_final.add(arvores.get(i));
        }

        ListView lista = (ListView) findViewById(R.id.lista_arvores);

        ArvoreXYAdapter adapter = new ArvoreXYAdapter(this, arvores_final);

        lista.setAdapter(adapter);

    }


    public void addListenerOnButton() {

        final Context context = this;

        voltar = (Button) findViewById(R.id.voltar);
        exportar = (Button) findViewById(R.id.exportar);
        excluir = (Button) findViewById(R.id.excluir);
        salvar_sismanejo = (Button) findViewById(R.id.salvar_sismanejo);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, PrincipalActivity.class);
                confirmDialog(context, intent);
            }
        });


        exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, PrincipalActivity.class);

                if(cfg == 2){
                    exportarCsvGps();
                }else{
                    exportarCsvXy();
                }



            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, PrincipalActivity.class);
                confirmaExcluxao(context);
            }
        });



    }


    private void confirmDialog(Context context, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirmação")
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



    private void confirmaExcluxao(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setTitle("Exclusão de registros")
                .setMessage("Deseja excluir todas as árvores ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            controlador.zeraArvore();
                            arvores = controlador.getArvoresUpa(String.valueOf(ano_upa));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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







    public void sucessoExportacao(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Dados exportados com sucesso")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void erroExportacao(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Erro ao exportar arquivo")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }



    private void exportarCsvGps(){
        try {
            String nome_arquivo = "inventario_upa_"+ano_upa+".csv";
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
            sucessoExportacao(ConsultaActivity.this);
        }
        catch (IOException ioe)
        {ioe.printStackTrace();}
    }


    private void exportarCsvXy(){
        try {
            String nome_arquivo = "inventario_upa_"+ano_upa+".csv";
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
            sucessoExportacao(ConsultaActivity.this);
        }
        catch (IOException ioe)
        {ioe.printStackTrace();}
    }



    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public String getNome_arquivo() {
        return nome_arquivo;
    }

    public void setNome_arquivo(String nome_arquivo) {
        this.nome_arquivo = nome_arquivo;
    }







}
