package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail;

public class Contar {

    public static enum TipoServidor {
        IMAP, SMTP;

        @Override
        public String toString() {
            if (this.equals(TipoServidor.IMAP)) {
                return "IMAP";
            } else {
                return "SMTP";
            }
        }
        
    };

    private int id;
    private String email;
    private TipoServidor tipo;
    private String host;
    private int porta;
    private String usuario, senha;

    public Contar(int id, String email, TipoServidor tipo, String host, int porta, String usuario, String senha) {
        this.id = id;
        this.email = email;
        this.tipo = tipo;
        this.host = host;
        this.porta = porta;
        this.usuario = usuario;
        this.senha = senha;
    }

    public Contar(TipoServidor tipo, String email, String host, int porta, String usuario, String senha) {
        this(-1, email, tipo, host, porta, usuario, senha);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public TipoServidor getTipo() {
        return tipo;
    }

    public void setTipo(TipoServidor tipo) {
        this.tipo = tipo;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}
