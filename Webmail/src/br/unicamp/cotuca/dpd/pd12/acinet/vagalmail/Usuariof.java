package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail;

public class Usuariof {
    
    private int id;
    private String login, nome;

    public Usuariof(int id, String login, String nome) {
        this.id = id;
        this.login = login;
        this.nome = nome;
    }

    public Usuariof(String login, String nome) {
        this(-1, login, nome);
    }

    public int getId() {
        return id;
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Contar[] getContas() {
        return contas;
    }
}
