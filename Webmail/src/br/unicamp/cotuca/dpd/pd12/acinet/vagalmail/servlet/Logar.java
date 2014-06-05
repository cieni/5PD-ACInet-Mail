package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.servlet;

import br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.BD;
import br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Conta;
import br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Login;
import java.io.IOException;
import java.util.List;
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

@WebServlet({"/login", "/logout", "/cadastro"})
public class Logar extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String login, senha, nome, senha2;
        
        String contexto = request.getRequestURI();
        
        login = request.getParameter("userLogin");
        senha = request.getParameter("userPassword");
        nome = request.getParameter("userName");
//        senha2 = request.getParameter("userPassword2");
        
        EntityManager em = BD.getEntityManager();
        
        if (!contexto.contains("/cadastro") && login != null && senha != null) {
            Query qry = em.createNamedQuery("Login.logar");
            
            qry.setParameter("login", login);
            qry.setParameter("senha", senha);
            
            try {
                Login usuario = (Login) qry.getSingleResult();
                
                Conta conta = null;
                List<Conta> contas = usuario.getContaList();
                
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("conta", conta);
                
                response.sendRedirect("/");
                
                return;
            } catch (PersistenceException ex) {
                request.setAttribute("erro", "invalido");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                
                return;
            }
            
        } else if (contexto.contains("/cadastro")) {
            Login novoLogin = new Login();
            
            novoLogin.setNome(nome);
            novoLogin.setLogin(login);
            novoLogin.setSenha(senha);
            
            em.getTransaction().begin();
            em.persist(novoLogin);
            em.getTransaction().commit();
            
            response.sendRedirect("/login?mensagem=cadok");
            
        } else {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        if (request.getParameter("olar") != null) { // teste de login
            String teste = request.getParameter("olar");
            
            EntityManager em = BD.getEntityManager();
            Query qry = em.createNamedQuery("Login.findByLogin");
            qry.setParameter("login", request.getParameter("olar"));
            
            if (qry.getResultList().isEmpty()) {
                response.getWriter().println("{\"ok\":true}");
            } else {
                response.getWriter().println("{\"ok\":false}");
            }
            
            return;
        }
        
        if (request.getRequestURI().equals("/logout")) {
            request.setAttribute("erro", "logout");
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    public static Login getLogin(HttpSession session) throws NaoAutenticadoException {
        Login usuario = (Login) session.getAttribute("usuario");
        //Query qry = BD.getEntityManager().createNamedQuery("Login.findById");

        if (usuario != null) {
            // atualiza o objeto de login para refletir possiveis mudancas de estado
            usuario = BD.getEntityManager().find(Login.class, usuario.getId());
            session.setAttribute("usuario", usuario);
            
            // atualiza na sessão o objeto de conta
            Conta conta = (Conta) session.getAttribute("conta");
            if (conta != null) {
                conta = BD.getEntityManager().find(Conta.class, conta.getId());
            }
            if (conta == null) {
                // pega primeira conta disponivel
                if (!usuario.getContaList().isEmpty())
                    conta = usuario.getContaList().get(0);
            }
            
            session.setAttribute("conta", conta);
            
            return usuario;
        } else {
            throw new Logar.NaoAutenticadoException();
        }
    }
    
    public static void Logout(HttpServletRequest request) {
        request.getSession().invalidate();
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
