package coletor.sismanejo.com.br.coletor.models;

import java.io.Serializable;

/**
 * Created by Leonardo on 26/01/2017.
 */

public class Arvore implements Serializable{

    private int id_arvore;
    private String upa;
    private String ut;
    private String numero;
    private String faixa;
    private String especie;
    private String cap;
    private String altura;
    private String qf;
    private String coordx;
    private String orientx;
    private String coordy;
    private String ponto;
    private String observacao;
    private String anotador;
    private String botanico;
    private String plaqueteiro;
    private String lateral_x;
    private String lateral_y;
    private String anotador_gps;
    private String data_digitacao;

    public Arvore(){

    }

    public int getId_arvore() {
        return id_arvore;
    }

    public void setId_arvore(int id_arvore) {
        this.id_arvore = id_arvore;
    }

    public String getUpa() {
        return upa;
    }

    public void setUpa(String upa) {
        this.upa = upa;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFaixa() {
        return faixa;
    }

    public void setFaixa(String faixa) {
        this.faixa = faixa;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getQf() {
        return qf;
    }

    public void setQf(String qf) {
        this.qf = qf;
    }

    public String getCoordx() {
        return coordx;
    }

    public void setCoordx(String coordx) {
        this.coordx = coordx;
    }

    public String getOrientx() {
        return orientx;
    }

    public void setOrientx(String orientx) {
        this.orientx = orientx;
    }

    public String getCoordy() {
        return coordy;
    }

    public void setCoordy(String coordy) {
        this.coordy = coordy;
    }

    public String getPonto() {
        return ponto;
    }

    public void setPonto(String ponto) {
        this.ponto = ponto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getAnotador() {
        return anotador;
    }

    public void setAnotador(String anotador) {
        this.anotador = anotador;
    }

    public String getBotanico() {
        return botanico;
    }

    public void setBotanico(String botanico) {
        this.botanico = botanico;
    }

    public String getPlaqueteiro() {
        return plaqueteiro;
    }

    public void setPlaqueteiro(String plaqueteiro) {
        this.plaqueteiro = plaqueteiro;
    }

    public String getLateral_x() {
        return lateral_x;
    }

    public void setLateral_x(String lateral_x) {
        this.lateral_x = lateral_x;
    }

    public String getLateral_y() {
        return lateral_y;
    }

    public void setLateral_y(String lateral_y) {
        this.lateral_y = lateral_y;
    }

    public String getAnotador_gps() {
        return anotador_gps;
    }

    public void setAnotador_gps(String anotador_gps) {
        this.anotador_gps = anotador_gps;
    }

    public String getData_digitacao() {
        return data_digitacao;
    }

    //

    public void setData_digitacao(String data_digitacao) {
        this.data_digitacao = data_digitacao;
    }
}
