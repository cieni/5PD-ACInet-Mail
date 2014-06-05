package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.servlet;

import br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Conta;
import br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Login;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
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
        
        request.getRequestDispatcher("/compor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        Login usuario = Logar.getLoginOuRedireciona(request, response, request.getSession());
        
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
        } catch (EmailException | IOException ex) {
            //response.sendRedirect("/?mensagem=emailFalha");
            throw new RuntimeException(ex);
        }
    }
    
}
