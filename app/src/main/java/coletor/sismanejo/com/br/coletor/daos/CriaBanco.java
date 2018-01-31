package coletor.sismanejo.com.br.coletor.daos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CriaBanco extends SQLiteOpenHelper{

    //NOME BANCO
    public static final String NOME_BANCO = "coletor.db";

    //TABELAS
    public static final String TABELA_PROJETO = "projeto";
    public static final String TABELA_LISTA_ESPECIES = "lista_especies";
    public static final String TABELA_ARVORE = "arvore";


    // PROJETO
    public static final String EMPREENDIMENTO = "empreendimento";
    public static final String UMF = "umf";
    public static final String UPA = "upa";
    public static final String TIPO_INVENTARIO = "tipo_inventario";
    public static final String TIPO_COORDENADA = "tipo_coordenada";
    public static final String ANOTADOR = "anotador";
    public static final String BOTANICO = "botanico";
    public static final String PLAQUETEIRO = "plaqueteiro";
    public static final String LATERAL_X = "lateral_x";
    public static final String LATERAL_Y = "lateral_y";
    public static final String ANOTADOR_GPS = "anotador_gps";
    public static final String DATA_INICIO = "data_inicio";

    // LISTA DE ESPÉCIES
    public static final String CODIGO_ESPECIE = "codigo_especie";
    public static final String NOME_POPULAR = "nome_popular";
    public static final String NOME_CIENTIFICO = "nome_cientifico";

    // INVENTÁRIO DE ÁRVORES
    public static final String UT = "ut";
    public static final String ID_ARVORE = "id_arvore";
    public static final String FAIXA = "faixa";
    public static final String PONTO = "ponto";
    public static final String NUMERO = "numero";
    public static final String ESPECIE = "especie";
    public static final String CAP = "cap";
    public static final String ALTURA = "altura";
    public static final String QF = "qf";
    public static final String OBSERVACAO = "observacao";
    public static final String COORDX = "coordx";
    public static final String ORIENTX = "orientx";
    public static final String COORDY = "coordy";
    public static final String DATA_DIGITACAO = "data_digitacao";


    public static final int VERSAO = 1;

    private Context contexto;

    public CriaBanco(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }


    public CriaBanco(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql1 = "CREATE TABLE "+TABELA_PROJETO+"("
                + EMPREENDIMENTO + " text,"
                + UMF + " text,"
                + UPA + " text,"
                + UT + " text,"
                + TIPO_COORDENADA + " integer,"
                + TIPO_INVENTARIO + " integer,"
                + ANOTADOR + " text,"
                + BOTANICO + " text,"
                + PLAQUETEIRO + " text,"
                + LATERAL_X + " text,"
                + LATERAL_Y + " text,"
                + ANOTADOR_GPS + " text,"
                + DATA_INICIO + " text"
                +");";

        String sql2 = "CREATE TABLE "+TABELA_LISTA_ESPECIES+"("
                + CODIGO_ESPECIE + " integer,"
                + NOME_POPULAR + " text,"
                + NOME_CIENTIFICO + " text"
                +");";

        String sql3 = "CREATE TABLE "+TABELA_ARVORE+"("
                + ID_ARVORE + " integer primary key autoincrement,"
                + UPA + " text,"
                + UT + " text,"
                + FAIXA + " text,"
                + NUMERO + " text,"
                + ESPECIE + " text,"
                + CAP + " text,"
                + ALTURA + " text,"
                + QF + " text,"
                + OBSERVACAO + " text,"
                + COORDX + " text,"
                + ORIENTX + " text,"
                + COORDY + " text,"
                + PONTO + " text,"
                + ANOTADOR + " text,"
                + BOTANICO + " text,"
                + PLAQUETEIRO + " text,"
                + LATERAL_X + " text,"
                + LATERAL_Y + " text,"
                + ANOTADOR_GPS + " text,"
                + DATA_DIGITACAO + " text"
                +");";

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS" + TABELA_ARVORE);
        db.execSQL("DROP TABLE IF EXISTS" + TABELA_PROJETO);
        onCreate(db);
    }


}
