package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity;

public class Email {
    
    private String para, copia, copiaCarbono, assunto, corpo;

    public Email() {
        this("", "", "", "", "");
    }

    public Email(String para, String copia, String copiaCarbono, String assunto, String corpo) {
        this.para = para;
        this.copia = copia;
        this.copiaCarbono = copiaCarbono;
        this.assunto = assunto;
        this.corpo = corpo;
    }

    public Email(String para, String copia, String assunto, String corpo) {
        this.para = para;
        this.copia = copia;
        this.assunto = assunto;
        this.corpo = corpo;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getCopia() {
        return copia;
    }

    public void setCopia(String copia) {
        this.copia = copia;
    }

    public String getCopiaCarbono() {
        return copiaCarbono;
    }

    public void setCopiaCarbono(String copiaCarbono) {
        this.copiaCarbono = copiaCarbono;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }
    
    
    
}
