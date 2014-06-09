(function ($) {
    
    var modal = $("div#emailModal"),
        titulo = $("h4#emailTitle"),
        recebido = $("span#emailTime"),
        remetente = $("span#emailFrom"),
        destinatario = $("span#emailTo"),
        copia = $("span#emailCC"),
        mensagem = $("iframe#emailBody"),
        responder = $("button#emailReply"),
        encaminhar = $("button#emailForward");
        
    var idCorrente = -1;
    var urlCorrente = "";
    
    responder.on('click', function(e) {
        e.preventDefault();
        responderEmail(idCorrente, urlCorrente);
    });
    
    encaminhar.on('click', function(e) {
        e.preventDefault();
        encaminharEmail(idCorrente, urlCorrente);
    });
    
    window.lerEmail = function (id, url) {
        $.getJSON("email.jsp", { acao: "email", id: id, url: url }, function(dados) {
            if (!dados.erro) {
                titulo.html(dados.titulo||"N/A");
                recebido.html(dados.data||"N/|");
                remetente.html(dados.de||"N/A");
                destinatario.html(dados.para||"N/A");
                copia.html(dados.cc||"N/A");
                //mensagem.attr('src', dados.mensagem);
                mensagem.attr('srcdoc', dados.mensagem);
                idCorrente = id;
                urlCorrente = url;
                
                modal.modal('show');
            } else {
                alert(dados.erro);
            }
        });
        
        return false;
    };
    
    window.responderEmail = function (id, url) {
        location.href = "/email?ak=reply&id=" + id + "&url=" + encodeURIComponent(url);
        return false;
    };
    
    window.encaminharEmail = function (id, url) {
        location.href = "/email?ak=forward&id=" + id + "&url=" + encodeURIComponent(url);
        return false;
    };
    
})(jQuery);