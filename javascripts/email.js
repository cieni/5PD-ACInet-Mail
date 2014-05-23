(function ($) {
    
    var modal = $("div#emailModal"),
        titulo = $("h4#emailTitle"),
        recebido = $("span#emailTime"),
        remetente = $("span#emailFrom"),
        mensagem = $("iframe#emailBody"),
        responder = $("button#emailReply"),
        encaminhar = $("button#emailForward");
        
    var idCorrente = -1;
    
    window.lerEmail = function (id) {
        console.log('hey');
        $.getJSON("email.jsp", { acao: "json", id: id }, function(dados) {
            console.log(dados);
            console.log('dados');
            if (!dados.erro) {
                titulo.text(dados.titulo);
                recebido.text(dados.data);
                remetente.text(dados.de);
                mensagem.attr('src', dados.mensagem);
                idCorrente = dados.id;
                
                modal.modal('show');
            } else {
                alert("O e-mail solicitado não existe");
            }
        });
        
        return false;
    }
})(jQuery);