/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.servlet;

import br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.BD;
import br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Conta;
import br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Login;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.URLName;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author José Carlos
 */
@WebServlet({"/conf/nova", "/conf/conta", "/conf/pasta", "/conf/senha", "/conf/salvar"})
public class Configuracoes extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
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
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        if (request.getRequestURI().contains("/pasta")) {
            String acao = request.getParameter("acao"),
                    url = request.getParameter("url"),
                    novo = request.getParameter("novo");
            
            JSONObject obj = new  JSONObject();
            if (acao == null || url == null) {
                obj.put("erro", "Solicitação inválida");
                obj.writeJSONString(response.getWriter()); 
                return;
            }
            
            if ((acao.equals("renomear") || acao.equals("nova")) && novo == null) {
                obj.put("erro", "Solicitação inválida");
                obj.writeJSONString(response.getWriter()); 
                return;
            }
            
            try {
                Conta conta = (Conta) request.getSession().getAttribute("conta");
                Store store = Logar.getImapStore(conta);

                Folder pasta = null;
                
                if (!acao.equals("nova")) {
                    URLName urln = new URLName(url);
                    pasta = store.getFolder(urln);

                    if (pasta.isOpen())
                        pasta.close(false);
                }

                switch (acao) {
                    case "renomear":
                        if (pasta.renameTo(store.getFolder(novo))) {
                            obj.put("sucesso", "Renomeado com sucesso");
                        } else {
                            obj.put("erro", "Erro ao renomear a pasta");
                        }
                        break;
                    case "esvaziar":
                        pasta.open(Folder.READ_WRITE);
                        pasta.setFlags(1, pasta.getMessageCount(), new Flags(Flags.Flag.DELETED), true);
                        pasta.expunge();
                        obj.put("sucesso", "Esvaziado com sucesso");
                        break;
                    case "excluir":
                        if (pasta.delete(true)) {
                            obj.put("sucesso", "Excluído com sucesso");
                        } else {
                            obj.put("erro", "Erro ao excluir a pasta");
                        }
                        break;
                    case "nova":
                        pasta = store.getFolder(novo);
                        if (!pasta.exists()) {
                            if (pasta.create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES)) {
                                obj.put("sucesso", "Criado com sucesso");
                            } else {
                                obj.put("erro", "Erro ao criar a pasta");
                            }
                        } else {
                            obj.put("erro", "Erro ao criar a pasta");
                        }
                        break;
                }
            } catch (MessagingException ex) {
                obj.put("erro", "Erro ao processar solicitação");
            }

            obj.writeJSONString(response.getWriter());
            
            return;
        }
        
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
