package coletor.sismanejo.com.br.coletor.models;


import java.io.Serializable;

public class Projeto implements Serializable{

    private String empreendimento;

    private String umf;

    private String upa;

    private int ut;

    private int tipo_inventario; // 1 = CONTÍNUO, 2 = AMOSTRAGEM

    private int tipo_coordenada; // 1 = X Y, 2 = GPS

    private String anotador;

    private String botanico;

    private String plaqueteiro;

    private String lateral_x;

    private String lateral_y;

    private String anotador_gps;

    private String data_inicio;

    public String getEmpreendimento() {
        return empreendimento;
    }

    public void setEmpreendimento(String empreendimento) {
        this.empreendimento = empreendimento;
    }

    public String getUmf() {
        return umf;
    }

    public void setUmf(String umf) {
        this.umf = umf;
    }

    public String getUpa() {
        return upa;
    }

    public void setUpa(String upa) {
        this.upa = upa;
    }

    public int getTipo_inventario() {
        return tipo_inventario;
    }

    public void setTipo_inventario(int tipo_inventario) {
        this.tipo_inventario = tipo_inventario;
    }

    public int getTipo_coordenada() {
        return tipo_coordenada;
    }

    public void setTipo_coordenada(int tipo_coordenada) {
        this.tipo_coordenada = tipo_coordenada;
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

    public String getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public int getUt() {
        return ut;
    }

    public void setUt(int ut) {
        this.ut = ut;
    }
}
