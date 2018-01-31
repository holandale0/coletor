package coletor.sismanejo.com.br.coletor.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import coletor.sismanejo.com.br.coletor.models.Arvore;
import coletor.sismanejo.com.br.coletor.models.Especie;
import coletor.sismanejo.com.br.coletor.models.Projeto;


public class BancoController {

    private SQLiteDatabase db;
    private CriaBanco banco;
    private Arvore arvore;


    public BancoController(Context context){
        banco = new CriaBanco(context);
    }


    public String verificaProjeto(){

        Cursor cursor;
        String[] campos = {banco.EMPREENDIMENTO, banco.UMF, banco.UPA, banco.UT, banco.TIPO_COORDENADA, banco.TIPO_INVENTARIO, banco.ANOTADOR,
                            banco.BOTANICO, banco.PLAQUETEIRO, banco.LATERAL_X, banco.LATERAL_Y, banco.ANOTADOR_GPS};

        String where = "";

        db = banco.getReadableDatabase();
        cursor = db.query(CriaBanco.TABELA_PROJETO, campos, where, null, null, null, null, null);

        if (cursor.moveToNext()) {
            return "1";
        } else {
            return "0";
        }

    }


    public String salvarOuAtualizarProjeto(Projeto projeto){

        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.EMPREENDIMENTO, projeto.getEmpreendimento());
        valores.put(CriaBanco.UMF, projeto.getUmf());
        valores.put(CriaBanco.UPA, projeto.getUpa());
        valores.put(CriaBanco.UT, projeto.getUt());
        valores.put(CriaBanco.ANOTADOR, projeto.getAnotador());
        valores.put(CriaBanco.BOTANICO, projeto.getBotanico());
        valores.put(CriaBanco.PLAQUETEIRO, projeto.getPlaqueteiro());
        valores.put(CriaBanco.LATERAL_X, projeto.getLateral_x());
        valores.put(CriaBanco.LATERAL_Y, projeto.getLateral_y());
        valores.put(CriaBanco.ANOTADOR_GPS, projeto.getAnotador_gps());
        valores.put(CriaBanco.TIPO_COORDENADA, projeto.getTipo_coordenada());
        valores.put(CriaBanco.TIPO_INVENTARIO, projeto.getTipo_inventario());
        //valores.put(CriaBanco.DATA_INICIO, projeto.getData_inicio());

        if("0".equals(verificaProjeto())){
            resultado = db.insert(CriaBanco.TABELA_PROJETO, null, valores);
        }else{
            String where = "";
            resultado = db.update(CriaBanco.TABELA_PROJETO, valores, where, null);
        }

        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso";

    }

    public Projeto consultarProjeto(){

        Projeto projeto = null;

        try {

            Cursor cursor;
            String[] campos =  {banco.EMPREENDIMENTO, banco.UMF, banco.UPA, banco.UT, banco.TIPO_COORDENADA, banco.TIPO_INVENTARIO, banco.ANOTADOR,
                    banco.BOTANICO, banco.PLAQUETEIRO, banco.LATERAL_X, banco.LATERAL_Y, banco.ANOTADOR_GPS};

            db = banco.getReadableDatabase();
            cursor = db.query(banco.TABELA_PROJETO, campos, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                projeto = new Projeto();
                projeto.setEmpreendimento(cursor.getString(0));
                projeto.setUmf(cursor.getString(1));
                projeto.setUpa(cursor.getString(2));
                projeto.setUt(cursor.getInt(3));
                projeto.setTipo_coordenada(cursor.getInt(4));
                projeto.setTipo_inventario(cursor.getInt(5));
                projeto.setAnotador(cursor.getString(6));
                projeto.setBotanico(cursor.getString(7));
                projeto.setPlaqueteiro(cursor.getString(8));
                projeto.setLateral_x(cursor.getString(9));
                projeto.setLateral_y(cursor.getString(10));
                projeto.setAnotador_gps(cursor.getString(11));

            }
            db.close();

        }catch(Exception e){
            System.out.println(e);

        }

        return projeto;

    }

    public void deletaProjeto(){
        String where = "";
        db = banco.getReadableDatabase();
        db.delete(CriaBanco.TABELA_PROJETO,where,null);
        db.close();
    }


    public String savarOuAtualizarEspecie(int op, Especie especie){

        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();
        valores = new ContentValues();

        valores.put(CriaBanco.CODIGO_ESPECIE, especie.getCodigoEspecie());
        valores.put(CriaBanco.NOME_POPULAR, especie.getNomePopular());
        valores.put(CriaBanco.NOME_CIENTIFICO, especie.getNomeCientifico());

        String where = CriaBanco.CODIGO_ESPECIE+ " = " + especie.getCodigoEspecie();

        if(op == 1){
            resultado = db.insert(CriaBanco.TABELA_LISTA_ESPECIES, null, valores);
        }else{
            resultado = db.update(CriaBanco.TABELA_LISTA_ESPECIES, valores, where, null);
        }


        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso";

    }




    public List<Especie> consultaEspecies() {
        List<Especie> especies = new ArrayList<>();
        try {

            Cursor cursor;
            String[] campos =  {banco.CODIGO_ESPECIE,banco.NOME_POPULAR,banco.NOME_CIENTIFICO};

            db = banco.getReadableDatabase();
            cursor = db.query(banco.TABELA_LISTA_ESPECIES, campos, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                Especie esp = new Especie();
                esp.setCodigoEspecie(cursor.getInt(0));
                esp.setNomePopular(cursor.getString(1));
                esp.setNomeCientifico(cursor.getString(2));
                especies.add(esp);

            }
            db.close();

        }catch(Exception e){
            System.out.println(e);

        }
        return especies;
    }

    public Especie getEspecie(int codigo) {
        Especie esp = null;
        try {

            Cursor cursor;
            String[] campos =  {banco.CODIGO_ESPECIE,banco.NOME_POPULAR,banco.NOME_CIENTIFICO};
            String where = CriaBanco.CODIGO_ESPECIE + "=" + codigo;
            db = banco.getReadableDatabase();
            cursor = db.query(CriaBanco.TABELA_LISTA_ESPECIES,campos,where, null, null, null, null, null);

            while (cursor.moveToNext()) {
                esp = new Especie();
                esp.setCodigoEspecie(cursor.getInt(0));
                esp.setNomePopular(cursor.getString(1));
                esp.setNomeCientifico(cursor.getString(2));
            }
            db.close();

        }catch(Exception e){
            System.out.println(e);

        }
        return esp;
    }

    public boolean isEspecieExists(int codigo) {

        try {

            Cursor cursor;
            String[] campos =  {banco.CODIGO_ESPECIE,banco.NOME_POPULAR,banco.NOME_CIENTIFICO};
            String where = CriaBanco.CODIGO_ESPECIE + "=" + codigo;
            db = banco.getReadableDatabase();
            cursor = db.query(CriaBanco.TABELA_LISTA_ESPECIES,campos,where, null, null, null, null, null);

            if (cursor.moveToNext()) {
                db.close();
                return true;

            }else{
                db.close();
                return false;
            }

        }catch(Exception e){
            System.out.println(e);
            db.close();
            return false;
        }

    }


    public void deletaEspecie(Especie especie){
        String where = CriaBanco.CODIGO_ESPECIE + " = " + especie.getCodigoEspecie();
        db = banco.getReadableDatabase();
        db.delete(CriaBanco.TABELA_LISTA_ESPECIES,where,null);
        db.close();
    }

    public boolean codigoEspecieDuplicado(int codigo) {

        boolean duplicada = false;
        try {

            Cursor cursor;
            String[] campos =  {banco.CODIGO_ESPECIE,banco.NOME_POPULAR,banco.NOME_CIENTIFICO};
            String where =  CriaBanco.CODIGO_ESPECIE+ " = " + codigo;

            db = banco.getReadableDatabase();
            cursor = db.query(CriaBanco.TABELA_LISTA_ESPECIES, campos, where, null, null, null, null, null);

            if (cursor.moveToNext()) {
                duplicada =  true;
            } else {
                duplicada =  false;
            }

            db.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return duplicada;

    }

    public boolean nomePopularDuplicado(String nome) {

        boolean duplicada = false;
        try {

            Cursor cursor;
            String[] campos =  {banco.CODIGO_ESPECIE,banco.NOME_POPULAR,banco.NOME_CIENTIFICO};
            String where =  CriaBanco.NOME_POPULAR+ " = " + nome;

            db = banco.getReadableDatabase();
            cursor = db.query(CriaBanco.TABELA_LISTA_ESPECIES, campos, where, null, null, null, null, null);

            if (cursor.moveToNext()) {
                duplicada =  true;
            } else {
                duplicada =  false;
            }

            db.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return duplicada;

    }





    public String insereArvore(Arvore arvore){
        ContentValues valores;
        long resultado = 0;

        try {
            db = banco.getWritableDatabase();
            valores = new ContentValues();
            valores.put(CriaBanco.UPA, arvore.getUpa());
            valores.put(CriaBanco.UT, arvore.getUt());
            valores.put(CriaBanco.FAIXA, arvore.getFaixa());
            valores.put(CriaBanco.NUMERO, arvore.getNumero());
            valores.put(CriaBanco.ESPECIE, arvore.getEspecie());
            valores.put(CriaBanco.CAP, arvore.getCap());
            valores.put(CriaBanco.ALTURA, arvore.getAltura());
            valores.put(CriaBanco.QF, arvore.getQf());
            valores.put(CriaBanco.OBSERVACAO, arvore.getObservacao());
            valores.put(CriaBanco.COORDX, arvore.getCoordx());
            valores.put(CriaBanco.ORIENTX, arvore.getOrientx());
            valores.put(CriaBanco.COORDY, arvore.getCoordy());
            valores.put(CriaBanco.PONTO, arvore.getPonto());
            valores.put(CriaBanco.ANOTADOR, arvore.getAnotador());
            valores.put(CriaBanco.BOTANICO, arvore.getBotanico());
            valores.put(CriaBanco.PLAQUETEIRO, arvore.getPlaqueteiro());
            valores.put(CriaBanco.LATERAL_X, arvore.getLateral_x());
            valores.put(CriaBanco.LATERAL_Y, arvore.getLateral_y());
            valores.put(CriaBanco.ANOTADOR_GPS, arvore.getAnotador_gps());

            resultado = db.insert(CriaBanco.TABELA_ARVORE, null, valores);
            db.close();

            if (resultado <= 0)
                return "Erro ao inserir registro";
            else
                return "Registro Inserido com sucesso";
        }catch(Exception e){
            System.out.println(e);
            return "";
        }

    }



    public void alteraArvore(Arvore arvore){
        ContentValues valores;
        String where;

        db = banco.getWritableDatabase();

        where = CriaBanco.ID_ARVORE + " = " + arvore.getId_arvore();

        valores = new ContentValues();
        valores.put(CriaBanco.UPA, arvore.getUpa());
        valores.put(CriaBanco.UT, arvore.getUt());
        valores.put(CriaBanco.FAIXA, arvore.getFaixa());
        valores.put(CriaBanco.NUMERO, arvore.getNumero());
        valores.put(CriaBanco.ESPECIE, arvore.getEspecie());
        valores.put(CriaBanco.CAP, arvore.getCap());
        valores.put(CriaBanco.ALTURA, arvore.getAltura());
        valores.put(CriaBanco.QF, arvore.getQf());
        valores.put(CriaBanco.OBSERVACAO, arvore.getObservacao());
        valores.put(CriaBanco.COORDX, arvore.getCoordx());
        valores.put(CriaBanco.ORIENTX, arvore.getOrientx());
        valores.put(CriaBanco.COORDY, arvore.getCoordy());
        valores.put(CriaBanco.PONTO, arvore.getPonto());
        valores.put(CriaBanco.ANOTADOR, arvore.getAnotador());
        valores.put(CriaBanco.BOTANICO, arvore.getBotanico());
        valores.put(CriaBanco.PLAQUETEIRO, arvore.getPlaqueteiro());
        valores.put(CriaBanco.LATERAL_X, arvore.getLateral_x());
        valores.put(CriaBanco.LATERAL_Y, arvore.getLateral_y());
        valores.put(CriaBanco.ANOTADOR_GPS, arvore.getAnotador_gps());

        db.update(CriaBanco.TABELA_ARVORE,valores,where,null);
        db.close();
    }



    public List<Arvore> getArvores() {
        List<Arvore> arvores = new ArrayList<Arvore>();
        try {

            Cursor cursor;
            String[] campos =  {banco.UPA,banco.UT,banco.FAIXA,banco.NUMERO,banco.ESPECIE,banco.CAP,banco.ALTURA,banco.QF,banco.OBSERVACAO,
                                banco.COORDX,banco.ORIENTX,banco.COORDY,banco.PONTO,banco.ANOTADOR,banco.BOTANICO,banco.PLAQUETEIRO,banco.LATERAL_X,banco.LATERAL_Y,banco.ANOTADOR_GPS,banco.ID_ARVORE};

            db = banco.getReadableDatabase();
            cursor = db.query(banco.TABELA_ARVORE, campos, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                Arvore arv = new Arvore();
                arv.setUpa(cursor.getString(0));
                arv.setUt(cursor.getString(1));
                arv.setFaixa(cursor.getString(2));
                arv.setNumero(cursor.getString(3));
                arv.setEspecie(cursor.getString(4));
                arv.setCap(cursor.getString(5));
                arv.setAltura(cursor.getString(6));
                arv.setQf(cursor.getString(7));
                arv.setObservacao(cursor.getString(8));
                arv.setCoordx(cursor.getString(9));
                arv.setOrientx(cursor.getString(10));
                arv.setCoordy(cursor.getString(11));
                arv.setPonto(cursor.getString(12));
                arv.setAnotador(cursor.getString(13));
                arv.setBotanico(cursor.getString(14));
                arv.setPlaqueteiro(cursor.getString(15));
                arv.setLateral_x(cursor.getString(16));
                arv.setLateral_y(cursor.getString(17));
                arv.setAnotador_gps(cursor.getString(18));
                arv.setId_arvore(cursor.getInt(19));
                arvores.add(arv);

            }
            db.close();

        }catch(Exception e){
            System.out.println(e);

        }
        return arvores;
    }


    public List<Arvore> getArvoresUpa(String upa) {
        List<Arvore> arvores = new ArrayList<Arvore>();
        try {

            Cursor cursor;
            String[] campos =  {banco.UPA,banco.UT,banco.FAIXA,banco.NUMERO,banco.ESPECIE,banco.CAP,banco.ALTURA,banco.QF,banco.OBSERVACAO,
                                banco.COORDX,banco.ORIENTX,banco.COORDY,banco.PONTO,banco.ANOTADOR,banco.BOTANICO,banco.PLAQUETEIRO,banco.LATERAL_X,banco.LATERAL_Y,banco.ANOTADOR_GPS,banco.ID_ARVORE};

            String where = CriaBanco.UPA + "=" + upa;

            db = banco.getReadableDatabase();
            cursor = db.query(CriaBanco.TABELA_ARVORE,campos,where, null, null, null, null, null);

            while (cursor.moveToNext()) {
                Arvore arv = new Arvore();
                arv.setUpa(cursor.getString(0));
                arv.setUt(cursor.getString(1));
                arv.setFaixa(cursor.getString(2));
                arv.setNumero(cursor.getString(3));
                arv.setEspecie(cursor.getString(4));
                arv.setCap(cursor.getString(5));
                arv.setAltura(cursor.getString(6));
                arv.setQf(cursor.getString(7));
                arv.setObservacao(cursor.getString(8));
                arv.setCoordx(cursor.getString(9));
                arv.setOrientx(cursor.getString(10));
                arv.setCoordy(cursor.getString(11));
                arv.setPonto(cursor.getString(12));
                arv.setAnotador(cursor.getString(13));
                arv.setBotanico(cursor.getString(14));
                arv.setPlaqueteiro(cursor.getString(15));
                arv.setLateral_x(cursor.getString(16));
                arv.setLateral_y(cursor.getString(17));
                arv.setAnotador_gps(cursor.getString(18));
                arv.setId_arvore(cursor.getInt(19));
                arvores.add(arv);

            }
            db.close();

        }catch(Exception e){
            System.out.println(e);

        }
        return arvores;
    }

    public List<Arvore> getArvoresUpaUt(String upa, String ut) {
        List<Arvore> arvores = new ArrayList<Arvore>();
        try {

            Cursor cursor;
            String[] campos =  {banco.UPA,banco.UT,banco.FAIXA,banco.NUMERO,banco.ESPECIE,banco.CAP,banco.ALTURA,banco.QF,banco.OBSERVACAO,
                                banco.COORDX,banco.ORIENTX,banco.COORDY,banco.PONTO,banco.ANOTADOR,banco.BOTANICO,banco.PLAQUETEIRO,banco.LATERAL_X,banco.LATERAL_Y,banco.ANOTADOR_GPS,banco.ID_ARVORE};

            String where = CriaBanco.UPA + "=" + upa + " AND " + CriaBanco.UT + " = " + ut;

            db = banco.getReadableDatabase();
            cursor = db.query(CriaBanco.TABELA_ARVORE,campos,where, null, null, null, null, null);

            while (cursor.moveToNext()) {
                Arvore arv = new Arvore();
                arv.setUpa(cursor.getString(0));
                arv.setUt(cursor.getString(1));
                arv.setFaixa(cursor.getString(2));
                arv.setNumero(cursor.getString(3));
                arv.setEspecie(cursor.getString(4));
                arv.setCap(cursor.getString(5));
                arv.setAltura(cursor.getString(6));
                arv.setQf(cursor.getString(7));
                arv.setObservacao(cursor.getString(8));
                arv.setCoordx(cursor.getString(9));
                arv.setOrientx(cursor.getString(10));
                arv.setCoordy(cursor.getString(11));
                arv.setPonto(cursor.getString(12));
                arv.setAnotador(cursor.getString(13));
                arv.setBotanico(cursor.getString(14));
                arv.setPlaqueteiro(cursor.getString(15));
                arv.setLateral_x(cursor.getString(16));
                arv.setLateral_y(cursor.getString(17));
                arv.setAnotador_gps(cursor.getString(18));
                arv.setId_arvore(cursor.getInt(19));
                arvores.add(arv);

            }
            db.close();

        }catch(Exception e){
            System.out.println(e);

        }
        return arvores;
    }


    public Arvore arvorePorUpaUt(String upa, String ut) {
        Arvore arv = null;
        try {

            Cursor cursor;
            String[] campos =  {banco.UPA,banco.UT,banco.FAIXA,banco.NUMERO,banco.ESPECIE,banco.CAP,banco.ALTURA,banco.QF,banco.OBSERVACAO,
                                banco.COORDX,banco.ORIENTX,banco.COORDY,banco.PONTO,banco.ANOTADOR,banco.BOTANICO,banco.PLAQUETEIRO,banco.LATERAL_X,banco.LATERAL_Y,banco.ANOTADOR_GPS,banco.ID_ARVORE};

            String where = CriaBanco.UPA + "=" + upa + " AND " + CriaBanco.UT + " = " + ut;

            db = banco.getReadableDatabase();
            cursor = db.query(CriaBanco.TABELA_ARVORE,campos,where, null, null, null, null, null);

            if (cursor.moveToNext()) {
                arv = new Arvore();
                arv.setUpa(cursor.getString(0));
                arv.setUt(cursor.getString(1));
                arv.setFaixa(cursor.getString(2));
                arv.setNumero(cursor.getString(3));
                arv.setEspecie(cursor.getString(4));
                arv.setCap(cursor.getString(5));
                arv.setAltura(cursor.getString(6));
                arv.setQf(cursor.getString(7));
                arv.setObservacao(cursor.getString(8));
                arv.setCoordx(cursor.getString(9));
                arv.setOrientx(cursor.getString(10));
                arv.setCoordy(cursor.getString(11));
                arv.setPonto(cursor.getString(12));
                arv.setAnotador(cursor.getString(13));
                arv.setBotanico(cursor.getString(14));
                arv.setPlaqueteiro(cursor.getString(15));
                arv.setLateral_x(cursor.getString(16));
                arv.setLateral_y(cursor.getString(17));
                arv.setAnotador_gps(cursor.getString(18));
                arv.setId_arvore(cursor.getInt(19));

            }
            db.close();

        }catch(Exception e){
            System.out.println(e);

        }
        return arv;
    }



    public void deletaArvore(Arvore arvore){
        String where = CriaBanco.ID_ARVORE + " = " + arvore.getId_arvore();
        db = banco.getReadableDatabase();
        db.delete(CriaBanco.TABELA_ARVORE,where,null);
        db.close();
    }

    public void zeraArvore(){
        String where = "";
        db = banco.getReadableDatabase();
        db.delete(CriaBanco.TABELA_ARVORE,where,null);
        db.close();
    }




    public boolean duplicada(String id ,String num, String ut, String upa) {

        boolean duplicada = false;
        try {

            Cursor cursor;
            String[] campos = {banco.UPA, banco.UT, banco.FAIXA, banco.NUMERO, banco.ESPECIE, banco.CAP, banco.ALTURA, banco.QF, banco.OBSERVACAO,
                                banco.COORDX,banco.ORIENTX,banco.COORDY,banco.PONTO,banco.ANOTADOR,banco.BOTANICO,banco.PLAQUETEIRO,banco.LATERAL_X,banco.LATERAL_Y,banco.ANOTADOR_GPS,banco.ID_ARVORE};

            String where = CriaBanco.UPA + "=" + upa + " AND " + CriaBanco.UT + " = " + ut + " AND " + CriaBanco.NUMERO+ " = " + num + " AND " + CriaBanco.ID_ARVORE+ " <> " + id;

            db = banco.getReadableDatabase();
            cursor = db.query(CriaBanco.TABELA_ARVORE, campos, where, null, null, null, null, null);

            if (cursor.moveToNext()) {
                duplicada =  true;
            } else {
                duplicada =  false;
            }

            db.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return duplicada;

    }








}