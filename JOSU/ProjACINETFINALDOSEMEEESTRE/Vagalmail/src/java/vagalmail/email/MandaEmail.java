/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vagalmail.email;

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
        if (!isHtml) {
            if (!isHtmlArq) {
                int indice = 0;
                email.setHostName(dados[indice]);  //indice = 0  ->  hostname
                indice++;  // indice = 1  ->  usuario
                email.setSmtpPort(465);
                indice = indice + 2;  //indice = 3  ->  assunto
                email.setAuthentication(dados[indice], dados[indice + 1]);
                email.setSSLOnConnect(true);
                email.setSubject(dados[indice]);
                indice++; // indice = 4  ->  remetente
                email.setFrom(dados[indice]);
                indice++; // indice = 5  ->  mensagem
                email.setMsg(dados[indice]); // pode conter \n
                indice++;  // indice = 6  ->  numeroDestinatarios
                for (int i = 0; i <= Integer.parseInt(dados[indice]); i++) {
                    email.addTo(dados[indice + 1]); // 1 ou + vezes
                }
            } else {
                //this.insereDadosHtmlArq(dados);
            }
        } else {
            this.insereDadosHtml(dados);
        }
    }

    private void insereDadosHtml(String dados[]) throws EmailException, MalformedURLException {
        this.htmlEmail = new HtmlEmail();
        int indice = 0;
        htmlEmail.setHostName(dados[indice]);
        indice++;  // indice = 1  ->  usuario
        htmlEmail.setSmtpPort(465);
        indice = indice + 2;  //indice = 3  ->  assunto
        htmlEmail.setAuthentication(dados[indice], dados[indice + 1]);
        htmlEmail.setSSLOnConnect(true);
        htmlEmail.setSubject(dados[indice]);
        indice++; // indice = 4  ->  remetente
        htmlEmail.setFrom(dados[indice]);
        indice++; // indice = 5  ->  mensagem
        htmlEmail.setMsg(dados[indice]); // pode conter \n
        indice++;  // indice = 6  ->  numeroDestinatarios
        for (int i = 0; i <= Integer.parseInt(dados[indice]); i++) {
            htmlEmail.addTo(dados[indice + 1]); // 1 ou + vezes
        }

//        // Embute a imagem e obtem seu id
//        URL url = new URL("<enderecoNaInternetDoArquivoAnexado>");
//        String id = htmlEmail.embed(url, "<nomeDoAnexo>");
        // Compoe a mensagem HTML
        htmlEmail.setHtmlMsg(dados[5]); //????
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
     */
    public void anexar(String caminhoArquivo, String descricaoAnexo, String nomeAnexo) {
        this.anexo = new EmailAttachment();
        anexo.setPath(caminhoArquivo);
        anexo.setDisposition(EmailAttachment.ATTACHMENT);
        anexo.setDescription(descricaoAnexo);
        anexo.setName(nomeAnexo);
    }

    /**
     * Criar anexo com arquivo remoto (internet)
     *
     * @param urlAnexo
     * @param descricaoAnexo
     * @param nomeAnexo
     * @throws MalformedURLException
     */
    public void anexarUrl(String urlAnexo, String descricaoAnexo, String nomeAnexo) throws MalformedURLException {
        this.anexo = new EmailAttachment();
        anexo.setURL(new URL(urlAnexo));
        anexo.setDisposition(EmailAttachment.ATTACHMENT);
        anexo.setDescription(descricaoAnexo);
        anexo.setName(nomeAnexo);
    }

    public void enviar() throws EmailException {
        if (email != null) {
            email.attach(anexo);
            email.send();
        } else {
            email.attach(anexo);
            htmlEmail.send();
        }
            
    }
    
}
