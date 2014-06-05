/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package email;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.mail.*;

/**
 *
 * @author u12170
 */
public class MandaEmail {

    private EmailAttachment anexo;
    private MultiPartEmail email;
    private HtmlEmail htmlEmail;
    ////////////////////////////////////////////////////
    //[0] - hostname
    //[1] - usuario
    //[2] - senha
    //[3] - assunto
    //[4] - remetente
    //[5] - mensagem
    //[6] - numeroDestinatarios
    // ate o final do vetor serao os destinatarios 
    ////////////////////////////////////////////////////
    /**
     * Insere os dados do email como remetente e destinatario(s)
     *
     * @param dados dados do email, como remetente
     * @param isHtml se a pagina contem tags HMTL
     * @throws EmailException
     * @throws MalformedURLException
     */
    public void insereDados(String[] dados, boolean isHtml, boolean isHtmlArq) throws EmailException, MalformedURLException {
        email = new MultiPartEmail();
        if (!isHtmlArq) {
            int indice = 0;
            email.setHostName(dados[indice]);  //indice = 0  ->  hostname
            indice++;  // indice = 1  ->  usuario
            email.setSmtpPort(587);
            DefaultAuthenticator autenticador = new DefaultAuthenticator(dados[indice], dados[indice + 1]);
            email.setAuthenticator(autenticador);
            indice = indice + 2;  //indice = 3  ->  assunto
            email.setSSLOnConnect(true);
            email.setSubject(dados[indice]);
            indice++; // indice = 4  ->  remetente
            email.setFrom(dados[indice]);
            indice++; // indice = 5  ->  mensagem
            if (!isHtml) {
                email.setMsg(dados[indice]); // pode conter \n
            } else {
                email.setContent(dados[indice], "text/html");
            }
            indice++;  // indice = 6  ->  numeroDestinatarios
            for (int i = 0; i <= Integer.parseInt(dados[indice]); i++) {
                email.addTo(dados[indice + 1]); // 1 ou + vezes
            }
        } else {
            //this.insereDadosHtmlArq(dados);
        }
    }

//    private void insereDadosHtmlArq(String dados[]) throws MalformedURLException, EmailException {
//        this.htmlEmail = new HtmlEmail();
//        // define a URL base para resolver especificacoes
//        // de localizacao relativas
//        URL url = new URL("<enderecoNaInternet");
//        // Especifica o caminho para o arquivo HTML que constitui
//        // a mensagem
//        htmlEmail.setDataSourceResolver(new DataSourceResolverImpl(url));
//
//        int indice = 0;
//        htmlEmail.setHostName(dados[indice]);
//        indice++;  // indice = 1  ->  usuario
//        htmlEmail.setSmtpPort(465);
//        indice = indice + 2;  //indice = 3  ->  assunto
//        htmlEmail.setAuthentication(dados[indice], dados[indice + 1]);
//        htmlEmail.setSSLOnConnect(true);
//        htmlEmail.setSubject(dados[indice]);
//        indice++; // indice = 4  ->  remetente
//        htmlEmail.setFrom(dados[indice]);
//        indice++; // indice = 5  ->  mensagem
//        htmlEmail.setHtmlMsg(dados[indice]); // pode conter \n
//        indice++;  // indice = 6  ->  numeroDestinatarios
//        for (int i = 0; i <= Integer.parseInt(dados[indice]); i++) {
//            htmlEmail.addTo(dados[indice + 1]); // 1 ou + vezes
//        }
//        htmlEmail.setTextMsg("<texto>");
//    }
    /**
     * Criar anexo coma rquivo local
     *
     * @param caminhoArquivo
     * @param descricaoAnexo
     * @param nomeAnexo
     * @param url se o caminho é na verdade uma url
     * @throws java.net.MalformedURLException
     */
    public void anexar(String caminhoArquivo, String descricaoAnexo, String nomeAnexo, boolean url) throws MalformedURLException {
        this.anexo = new EmailAttachment();
        if (!url) {
            this.anexo.setPath(caminhoArquivo);
        } else {
            this.anexo.setURL(new URL(caminhoArquivo));
        }
        this.anexo.setDisposition(EmailAttachment.ATTACHMENT);
        this.anexo.setDescription(descricaoAnexo);
        this.anexo.setName(nomeAnexo);
    }

    /**
     * Envia o email
     * @throws EmailException 
     */
    public void enviar() throws EmailException {
        if (this.email != null) {
            if (this.anexo != null) {
                this.email.attach(this.anexo);
                this.email.send();
            } else {
                this.email.send();
            }
        } else {
            this.email.attach(this.anexo);
            this.htmlEmail.send();
        }

    }

}
