/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author José Carlos
 */
@WebServlet({"/conf/nova", "/conf/conta", "/conf/senha", "/conf/salvar"})
public class Configuracoes extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        
        String metodo = "", req = request.getRequestURI();
        
        if (req.contains("/nova")) {
            metodo = "nova";
        } else if (req.contains("/conta")) {
            metodo = "conta";
        } else if (req.contains("/senha")) {
            metodo = "senha";
        } else {
            response.sendRedirect("/conf/conta");
        }
        
        if (!"".equals(metodo))
            request.getRequestDispatcher("/conf.jsp?metodo=" + metodo).include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String metodo = request.getParameter("acao");
        if (metodo == null) {
            return;
        } else if (metodo.equals("nova")) {
            EntityManager em = BD.getEntityManager();
            
            try {
                em.getTransaction().begin();
                
                Login usuario = Logar.getLogin(request.getSession());
                
                Conta conta = new Conta();
                conta.setEmail(request.getParameter("email"));
                conta.setImapHost(request.getParameter("imapHost"));
                conta.setImapPort(Integer.parseInt(request.getParameter("imapPort")));
                conta.setImapPassword(request.getParameter("imapPassword"));
                conta.setImapUser(request.getParameter("imapLogin"));
                conta.setSmtpHost(request.getParameter("smtpHost"));
                conta.setSmtpPort(Integer.parseInt(request.getParameter("smtpPort")));
                conta.setSmtpPassword(request.getParameter("smtpPassword"));
                conta.setSmtpUser(request.getParameter("smtpLogin"));
                conta.setIdLogin(usuario);
                
                em.persist(conta);
                em.merge(usuario);
                
                em.getTransaction().commit();
                em.refresh(conta);
                em.refresh(usuario);
                
                request.setAttribute("mensagem", "sucesso");
            } catch (Logar.NaoAutenticadoException | PersistenceException ex) {
                em.getTransaction().rollback();
                
                request.setAttribute("mensagem", "erro");
            }
            
            request.getRequestDispatcher("/conf.jsp?metodo=nova").forward(request, response);
        } else if (metodo.equals("conta")) {
            EntityManager em = BD.getEntityManager();
            
            try {
                em.getTransaction().begin();
                
                Conta conta = (Conta) request.getSession().getAttribute("conta");
                
                em.refresh(conta);
                conta.setEmail(request.getParameter("email"));
                conta.setImapHost(request.getParameter("imapHost"));
                conta.setImapPort(Integer.parseInt(request.getParameter("imapPort")));
                conta.setImapPassword(request.getParameter("imapPassword"));
                conta.setImapUser(request.getParameter("imapLogin"));
                conta.setSmtpHost(request.getParameter("smtpHost"));
                conta.setSmtpPort(Integer.parseInt(request.getParameter("smtpPort")));
                conta.setSmtpPassword(request.getParameter("smtpPassword"));
                conta.setSmtpUser(request.getParameter("smtpLogin"));
                
                em.getTransaction().commit();
                
                request.setAttribute("mensagem", "sucesso");
            } catch (PersistenceException ex) {
                em.getTransaction().rollback();
                
                request.setAttribute("mensagem", "erro");
            }
            
            request.getRequestDispatcher("/conf.jsp?metodo=conta").forward(request, response);
            
        } else if (metodo.equals("senha")) {
            EntityManager em = BD.getEntityManager();
            
            try {
                
                Login usuario = Logar.getLogin(request.getSession());
                
                em.refresh(usuario);
                
                String senatu = request.getParameter("senAtu"),
                        novasen = request.getParameter("senNova"),
                        novasen2 = request.getParameter("senNova2");
                
                if (novasen.equals(novasen2) && senatu.equals(usuario.getSenha())) {
                    
                    em.getTransaction().begin();
                    
                    usuario.setSenha(novasen);
                    
                    em.getTransaction().commit();
                    
                    request.setAttribute("mensagem", "sucesso");
                } else {
                    if (!novasen.equals(novasen2))
                        request.setAttribute("mensagem", "senneq");
                    else
                        request.setAttribute("mensagem", "antsen");
                }
            } catch (Logar.NaoAutenticadoException | PersistenceException ex) {
                em.getTransaction().rollback();
                
                request.setAttribute("mensagem", "erro");
            }
            
            request.getRequestDispatcher("/conf.jsp?metodo=senha").forward(request, response);
            
        }
    }

}
