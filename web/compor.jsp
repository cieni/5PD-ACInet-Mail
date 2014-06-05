<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.Email"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.servlet.Logar"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Login"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="modelo/cabecalhopag.jsp?atual=novo" /> <%-- inclusao dinamica --%>
<%
    Email email = null;
    String verbo = "";
    
    if (request.getAttribute("reply") != null) {
        email = (Email) request.getAttribute("reply");
        verbo = "Responder";
    } else if (request.getAttribute("forward") != null) {
        email = (Email) request.getAttribute("forward");
        verbo = "Encaminhar";
    } else {
        email = new Email();
        verbo = "Compor";
    }
%>
        <main class="main">
            <div class="container">
                <div class="row">
                    <div class=" content" role="main">
                        <h2 class="ico-envelop"><%=verbo%> E-mail</h2>
                        
                        <div class="container">
                            <div class="row">
                                <form role="form" action="/email" method="post" enctype="multipart/form-data">
                                    <fieldset class="col-lg-4">

                                        <div class="form-group">
                                            <label for="emailFor">Para</label>
                                            <textarea class="form-control" id="emailFor" name="para" rows="2"><%=email.getPara()%></textarea>
                                        </div>

                                        <div class="form-group">
                                            <label for="emailCC">Cópia</label>
                                            <textarea class="form-control" id="emailCC" name="cc" rows="2"><%=email.getCopia()%></textarea>
                                        </div>

                                        <div class="form-group">
                                            <label for="emailCCO">Cópia oculta</label>
                                            <textarea class="form-control" id="emailCCO" name="cco" rows="2"></textarea>
                                        </div>
                                        
                                        <div id="emailAttachments">
                                            <div class="form-group">
                                                <label for="emailAttachments">Anexos</label>
                                                <button type="button" id="addAttachment" class="btn btn-xs btn-default ico-plus">adicionar</button>
                                                <input type="hidden" name="contaanexo" id="attCount" value="0" />
                                            </div>
                                        </div>

                                    </fieldset>
                                    
                                    <fieldset class="col-lg-8">
                                        
                                        <div class="form-group">
                                            <label for="emailSubject">Assunto</label>
                                            <input autocomplete="off" class="form-control" type="text" name="assunto" value="<%=email.getAssunto()%>" id="emailSubject" />
                                        </div>
                                        
                                        <textarea name="corpo" id="emailBody"><%=email.getCorpo()%></textarea><br>
                                        
                                    </fieldset>
                                    
                                    <div class="box-actions">
                                        <div class="pull-right">
                                            <button type="reset" class="btn btn-danger ico-close">Limpar</button>
                                            <button type="submit" class="btn btn-primary ico-checkmark">Enviar</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </main>

        <%@include file="modelo/rodapesc.jsp" %>
        <%@include file="modelo/scripts.jsp" %>
        
        <script type="text/javascript">
        $(function() {
            $('textarea#emailBody').summernote({ lang: 'pt-BR', height: 300 });
            
            var btnAnexo = $('button#addAttachment'),
                div = $('<div class="form-group"/>'),
                arquivo = $('<input />').attr('type', 'file').css('display', 'inline'),
                rmArquivo = $('<a />').addClass('small text-danger ico-remove').html('<span class="sr-only">Remover</span>'),
                alvo = $('div#emailAttachments'),
                conta = $('input#attCount');
            
            var contaAnexos = parseInt(conta.val()); // deve ser 0, mas nunca se sabe nÃ©
            
            btnAnexo.on('click', function(e) {
                var novoArq = arquivo.clone();
                var novoRmArq = rmArquivo.clone();
                
                ++contaAnexos;
                conta.val(contaAnexos);
                
                novoArq.attr('name', 'anexo' + contaAnexos);
                novoRmArq.on('click', function(e) {
                    e.preventDefault();
                    
                    novoArq.remove();
                    novoRmArq.remove();
                    
                    --contaAnexos;
                    conta.val(contaAnexos);
                });
                
                alvo.append(div.clone().append(novoRmArq, novoArq));
            });
            
        });
        </script>
    </body>
</html>