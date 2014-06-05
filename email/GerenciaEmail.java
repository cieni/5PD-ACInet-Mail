/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package email;

import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.*;

/**
 *
 * @author u12170
 */
public class GerenciaEmail {

    public void apagarMensagem(Message msg) throws MessagingException {
        Folder pasta = msg.getFolder();
        msg.setFlag(Flags.Flag.DELETED, true);
        pasta.expunge();
        pasta.close(true);
    }

    public void lerMensagem(Message msg) throws MessagingException, IOException {
        System.out.println("DE:" + msg.getFrom()[0]);
        System.out.println("DATA DE ENVIO: " + msg.getSentDate());
        System.out.println("ASSUNTO: " + msg.getSubject());
        Multipart partes = (Multipart) msg.getContent();
        for (int i = 0; i < partes.getCount(); i++) {
            BodyPart corpo = partes.getBodyPart(i);
            String tipo = corpo.getDisposition();
            if (tipo != null && tipo.equals(BodyPart.ATTACHMENT)) {
                DataHandler arquivo = corpo.getDataHandler();
                System.out.println("Anexo : " + arquivo.getName());
            } else {
                System.out.println("Texto: " + corpo.getContent());
            }

        }
    }

    public Message getMensagem(String enderecoImap, String emailDest, String senhaDest, String nomePasta, int numMsg) throws NoSuchProviderException, MessagingException {
        Properties propriedades = new Properties();
        propriedades.setProperty("mail.store.protocol", "imaps");
        Session sessao = Session.getInstance(propriedades, null);
        Store deposito = sessao.getStore();
        deposito.connect(enderecoImap, emailDest, senhaDest);
        Folder pasta = deposito.getFolder(nomePasta);
        pasta.open(Folder.READ_WRITE);
        Message msg = pasta.getMessage(numMsg);
        pasta.close(true);
        return msg;
    }

    public void enviarEmail(Message msg, String msgResposta) throws MessagingException, IOException {
        String textoMensagem = (String) msg.getContent();
        Message resposta = msg.reply(true);
        String textoResposta = textoMensagem.replaceAll("(?m)^", "> ");
        textoResposta += msgResposta;
        resposta.setText(textoResposta);
        Transport.send(resposta);
    }
}
