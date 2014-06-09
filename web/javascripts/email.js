(function ($) {
    
    var modal = $("div#emailModal"),
        titulo = $("h4#emailTitle"),
        recebido = $("span#emailTime"),
        remetente = $("span#emailFrom"),
        destinatario = $("span#emailTo"),
        copia = $("span#emailCC"),
        anexo = $("span#emailAtt"),
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
                
                if (dados.anexos && dados.anexos.length > 0) {
                    anexo.empty();
                    
                    var lbl = $("<strong/>").text('Anexos:');
                    var espaco = $("<span/>").html('&nbsp;');
                    
                    var sel = $("<select id=emailQualAnexo />").addClass('form-control input-sm').css('display', 'inline-block').width(200);
                    
                    for (var i = 0; i < dados.anexos.length; ++i) {
                        sel.append($("<option/>").text(dados.anexos[i]).val(dados.anexos[i]));
                    }
                    
                    var btn = $("<button type=button />").addClass('btn btn-primary ico-download btn-sm').text('download');
                    btn.on('click', function(e) {
                        
                        location.href = '/email?acao=download&url=' + encodeURIComponent(urlCorrente) + '&id=' + idCorrente + '&anexo=' + encodeURIComponent(sel.val());
//                        $("<a>").attr('href', '/email?acao=download&url=' + encodeURIComponent(urlCorrente) + '&id=' + idCorrente + '&anexo=' + encodeURIComponent(sel.val())).click();
//                        $("<iframe>")
//                                .hide()
//                                .attr('src', '/email?acao=download&url=' + encodeURIComponent(urlCorrente) + '&id=' + idCorrente + '&anexo=' + encodeURIComponent(sel.val()))
//                                .ready(function(e) { $(this).remove(); })
//                                .appendTo(document)
                        
                    });
                    
                    anexo.append(lbl, espaco.clone(), sel, espaco.clone(), btn);
                }
                
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