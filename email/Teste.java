/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package email;

/**
 *
 * @author Gabriel Sato
 */
public class Teste {

    public static void main(String[] args) {
        String[] dados = {"smtp.gmail.com", "sato031297@gmail.com", Constantes.SENHA, "teste", "sato031297@gmail.com", "<font color='red'>Capone gay</font>", "1", "sato031297@hotmail.com"};
        MandaEmail m = new MandaEmail();
        try {
            m.insereDados(dados, true, false);
            m.anexar("http://4.bp.blogspot.com/-2R5-lKqEqJQ/TpkAVyUumiI/AAAAAAAAAEk/PznA4oxON0c/s1600/teste2.png", "é um anexo", "Nomeeeee", true);
            m.enviar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
