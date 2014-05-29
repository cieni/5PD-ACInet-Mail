package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

public class Loginf {

    public static int getID(HttpSession session) throws NaoAutenticadoException {
        Integer id;
        Boolean estaLogado = (Boolean) session.getAttribute("logou");

        if (estaLogado != null && estaLogado) {

            id = (Integer) session.getAttribute("usuario");
            return id;
            
        } else {
            throw new Loginf.NaoAutenticadoException();
        }
    }
    
    public static int getIDOuLogin(HttpSession session) {
        try {
            int id = getID(session);
            return id;
        } catch (NaoAutenticadoException ex) {
            Logger.getLogger(Loginf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class NaoAutenticadoException extends Exception {

        public NaoAutenticadoException() {
            super();
        }

        public NaoAutenticadoException(String message) {
            super(message);
        }

        public NaoAutenticadoException(String message, Throwable cause) {
            super(message, cause);
        }

        public NaoAutenticadoException(Throwable cause) {
            super(cause);
        }
        
    }

}
