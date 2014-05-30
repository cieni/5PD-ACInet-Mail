package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/login")
public class Logar extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login, senha;
        
        login = request.getParameter("userLogin");
        senha = request.getParameter("userPassword");
        
        if (login != null && senha != null) {
            
            EntityManager em = BD.createEntityManager();
            Query qry = em.createNamedQuery("Login.logar");
            
            qry.setParameter("login", login);
            qry.setParameter("senha", senha);
            
            try {
                Login usuario = (Login) qry.getSingleResult();
            } catch (PersistenceException ex) {
                request.setAttribute("erro", "invalido");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                
                return;
            }
            
        } else {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            
            return;
        }
    }

    public static Login getLogin(HttpSession session) throws NaoAutenticadoException {
        Login usuario;
        Boolean estaLogado = (Boolean) session.getAttribute("logou");

        if (estaLogado != null && estaLogado) {

            usuario = (Login) session.getAttribute("usuario");
            return usuario;
            
        } else {
            throw new Logar.NaoAutenticadoException();
        }
    }
    
    public static Login getLoginOuRedireciona(ServletRequest request, ServletResponse response, HttpSession session) throws ServletException, IOException {
        try {
            Login usuario = getLogin(session);
            return usuario;
        } catch (NaoAutenticadoException ex) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
            
            return null;
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
