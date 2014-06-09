package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.servlet;

import br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Conta;
import br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Login;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

@WebServlet("/email")
@MultipartConfig
public class Email extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        if (Logar.getLoginOuRedireciona(request, response, request.getSession()) == null)
            return;
        
        String url = request.getParameter("url");
        String ak = request.getParameter("ak");
        try {
            if (ak != null) {
                int id = Integer.parseInt(request.getParameter("id"));
                URLName urln = new URLName(url);

                Conta c = (Conta) request.getSession().getAttribute("conta");
                Folder pasta = Logar.getImapStore(c).getFolder(urln);
                pasta.open(Folder.READ_WRITE);
                
                Message m = pasta.getMessage(id);
                
                String corpo = getText(m);
                
                if (corpo == null) corpo = "<html><body></body></html>";
                Source src = new Source(corpo);
                
                Element elm = src.getFirstElement(HTMLElementName.BODY);
                if (elm != null)
                    corpo = elm.toString().replace("body>", "div>");
                corpo = "<blockquote><strong>Mensagem original:</strong>"
                        + "<br /><b>De:</b> " + plainRecipients(m.getFrom())
                        + "<br /><b>Para:</b> " + plainRecipients(m.getRecipients(Message.RecipientType.TO))
                        + "<br /><b>Cópia:</b> " + plainRecipients(m.getRecipients(Message.RecipientType.CC))
                        + "<br /><b>Data:</b> " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(m.getReceivedDate())
                        + "<br /><b>Assunto:</b> " + m.getSubject()
                        + "<hr />" + corpo + "</blockquote>";
                
                String to = "", cc  = "", bcc = "", subject = m.getSubject();
                switch (ak) {
                    case "reply":
                        m = m.reply(false);
                        to = plainRecipients(m.getRecipients(Message.RecipientType.TO));
                        cc = plainRecipients(m.getRecipients(Message.RecipientType.CC));
                        bcc = plainRecipients(m.getRecipients(Message.RecipientType.BCC));
                    case "forward":
                        br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.Email email = new br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.Email(to, cc, bcc, m.getSubject(), corpo);
                        if (ak.equals("reply")) {
                            email.setAssunto("Re: " + subject);
                            request.setAttribute("reply", email);
                        } else {
                            email.setAssunto("Enc: " + subject);
                            request.setAttribute("forward", email);
                        }
                        break;
                        
                    case "delete":
                        m.setFlag(Flags.Flag.DELETED, true);
                        pasta.expunge();
                        
                        response.getWriter().println("{\"success\":\"true\", \"message\": \"A mensagem foi excluída com sucesso.\"}");
                        break;
                }
            }
        } catch (MessagingException ex) {
            //throw new RuntimeException(ex);
            request.getRequestDispatcher("/conf.jsp?metodo=conta&erro=imap").forward(request, response);
            return;
        }
        
        String acao = request.getParameter("acao");
        if (acao != null && acao.equals("download")) {
            try {
                String anexo = request.getParameter("anexo");
                int id = Integer.parseInt(request.getParameter("id"));

                URLName urln = new URLName(url);

                Conta c = (Conta) request.getSession().getAttribute("conta");
                Folder pasta = Logar.getImapStore(c).getFolder(urln);
                pasta.open(Folder.READ_WRITE);

                Message m = pasta.getMessage(id);

                if (m.getContent() instanceof Multipart)
                    getAttachment((Multipart) m.getContent(), anexo, response);
                else
                    response.getWriter().print("Anexo inexistente");

                return;
            } catch (MessagingException ex) {
                response.getWriter().print("Erro de e-mail");
            } catch (IOException ex) {
                response.getWriter().print("Erro de I/O");
            }
            
            return;
        }
        
        request.getRequestDispatcher("/compor.jsp").forward(request, response);
    }
    
    public static String[] getAttachmentNames(Multipart content) throws MessagingException, IOException {
        ArrayList<String> ret = new ArrayList<>();
        
        for (int i = 0; i < content.getCount(); i++) {
            String nomeDoArquivo = content.getBodyPart(i).getFileName();
            if (nomeDoArquivo != null) {
                ret.add(nomeDoArquivo);
            }
        }
        
        return ret.toArray(new String[0]);
    }
    
    public static void getAttachment(Multipart content, String name, HttpServletResponse response) throws MessagingException, IOException {
        byte[] buf = new byte[4096];
        
        for (int i = 0; i < content.getCount(); i++) {
            BodyPart bp = content.getBodyPart(i);
            
            String nomeDoArquivo = bp.getFileName();
            if (nomeDoArquivo != null && nomeDoArquivo.equals(name)) {
                InputStream is = bp.getInputStream();
                
                response.reset();
                
                response.setContentType(bp.getContentType());
                response.setHeader("Content-Disposition", "attachment; filename=" + name);
                
                OutputStream os = response.getOutputStream();
                
                int bytesRead;
                while ((bytesRead = is.read(buf)) != -1) {
                    os.write(buf, 0, bytesRead);
                }
                
                os.close();
                
                return;
            }
        }
    }
    
    public static String getText(javax.mail.Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            if (!p.isMimeType("text/html")) {
                s = "<html><head><title></title></head><body><pre>" + s + "</pre></body></html>";
            }
            
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                javax.mail.Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return "<html><head><title></title></head><body><pre>" + text + "</pre></body></html>";
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }

    private static String plainRecipients(Address[] addrs) {
        if (addrs == null) return "";
        
        StringBuilder ret = new StringBuilder(addrs.length * 20);
        
        for (Address addr : addrs) {
            if (addr instanceof InternetAddress) {
                InternetAddress ia = (InternetAddress) addr;
                String nome = ia.getPersonal(), email = ia.getAddress();
                
                if (nome != null && !nome.trim().equals(""))
                    ret.append(nome).append(" &lt;").append(email).append("&gt;, ");
                else 
                    ret.append(email).append(", ");
            } else {
                ret.append(addr).append(", ");
            }
        }
        
        if (ret.length() > 2)
            ret.setLength(ret.length() - 2);
        
        return ret.toString();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        Login usuario = Logar.getLoginOuRedireciona(request, response, request.getSession());
        
        if (request.getParameter("lote") != null) {
            String[] ids = request.getParameterValues("id");
            String urls = request.getParameter("url");
            String urld = request.getParameter("moveMsg");
            
            try {
                URLName urlns = new URLName(urls);
                URLName urlnd = new URLName(urld);

                int[] id = new int[ids.length];
                for (int i = 0; i < ids.length; ++i)
                    id[i] = Integer.parseInt(ids[i]);

                Conta conta = (Conta) request.getSession().getAttribute("conta");
                Store store = Logar.getImapStore(conta);

                Folder pastaO = store.getFolder(urlns);
                pastaO.open(Folder.READ_WRITE);
                
                Message[] msgs = pastaO.getMessages(id);

                Folder pastaD = store.getFolder(urlnd);
                if (msgs.length != 0) {
                    pastaO.copyMessages(msgs, pastaD);
                    pastaO.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
                }

                pastaO.expunge();
            } catch (MessagingException ex) {
            }
            
            response.sendRedirect("/");
            
            return;
        }
        
        String para = request.getParameter("para"),
                cc = request.getParameter("cc"),
                cco = request.getParameter("cco"),
                assunto = request.getParameter("assunto"),
                corpo = request.getParameter("corpo");
        
        //int contaanexo = Integer.parseInt(request.getParameter("contaanexo"));
        Part[] anexos = request.getParts().toArray(new Part[0]);//new Part[contaanexo];
        
//        for (int i = 0; i < contaanexo; i++)
//            anexos[i] = request.getPart(para)
        
        Source src = new Source(corpo);
        String corpoTexto = src.getRenderer().toString();
        
        Conta conta = (Conta) request.getSession().getAttribute("conta");
        
        try {
            HtmlEmail email = new HtmlEmail();

            email.setHostName(conta.getSmtpHost());
            email.setSmtpPort(conta.getSmtpPort());

            email.setAuthenticator(new DefaultAuthenticator(conta.getSmtpUser(), conta.getSmtpPassword()));

            email.setSSLOnConnect(true); //TODO seria bacana salvar no BD se é necessário SSL
            email.setSubject(assunto);

            email.setFrom(conta.getEmail(), usuario.getNome());

            email.setHtmlMsg("<html><body>" + corpo + "</body></html>");
            email.setTextMsg(corpoTexto);

            // para
            String[] emails = para.split(",");
            for (String umPara : emails) {
                if (umPara.matches("<(.*)>")) {
                    Matcher m = Pattern.compile("(.*)<(.*)>").matcher(umPara);
                    email.addTo(m.group(2).trim(), m.group(1).trim());
                } else {
                    if (!umPara.trim().equals(""))
                        email.addTo(umPara.trim());
                }
            }

            // copia
            emails = cc.split(",");
            for (String umCC : emails) {
                if (umCC.matches("<(.*)>")) {
                    Matcher m = Pattern.compile("(.*)<(.*)>").matcher(umCC);
                    email.addCc(m.group(2).trim(), m.group(1).trim());
                } else {
                    if (!umCC.trim().equals(""))
                        email.addCc(umCC.trim());
                }
            }

            // copia oculta
            emails = cco.split(",");
            for (String umCCO : emails) {
                if (umCCO.matches("<(.*)>")) {
                    Matcher m = Pattern.compile("(.*)<(.*)>").matcher(umCCO);
                    email.addBcc(m.group(2).trim(), m.group(1).trim());
                } else {
                    if (!umCCO.trim().equals(""))
                        email.addBcc(umCCO.trim());
                }
            }

            EmailAttachment anexo;
            for (Part upload : anexos) {
                if (!upload.getName().startsWith("anexo"))
                    continue;
                
                anexo = new EmailAttachment();
                anexo.setName(upload.getSubmittedFileName());
                anexo.setDisposition(EmailAttachment.ATTACHMENT);
                anexo.setDescription("");

                File temp = File.createTempFile("tmp", ".att");
                temp.deleteOnExit();

                FileOutputStream fos = new FileOutputStream(temp);
                InputStream is = upload.getInputStream();
                while (true) {
                    int byt = is.read();
                    if (byt != -1)
                        fos.write(byt);
                    else
                        break;
                }

                anexo.setPath(temp.getCanonicalPath());

                email.attach(anexo);
            }

            email.send();
            response.sendRedirect("/?mensagem=emailSucesso");
        } catch (EmailException ex) {
            request.getRequestDispatcher("/conf.jsp?metodo=conta&erro=smtp").forward(request, response);
            //response.sendRedirect("/conf/conta?erro=envio");
            //throw new RuntimeException(ex);
        }
    }
    
}
