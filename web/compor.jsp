<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Logar"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Login"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="modelo/cabecalhopag.jsp?atual=novo" /> <%-- inclusao dinamica --%>
        
        <main class="main">
            <div class="container">
                <div class="row">
                    <div class=" content" role="main">
                        <h2 class="ico-envelop">(Compor|Responder|Encaminhar) E-mail</h2>
                        
                        <div class="container">
                            <div class="row">
                                <form role="form" action="/servidor/salvar" method="post">
                                    <fieldset class="col-lg-4">

                                        <div class="form-group">
                                            <label for="emailFor">Para</label>
                                            <textarea class="form-control" id="emailFor" name="para" rows="2"></textarea>
                                        </div>

                                        <div class="form-group">
                                            <label for="emailCC">Cópia</label>
                                            <textarea class="form-control" id="emailCC" name="cc" rows="2"></textarea>
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
                                            <input class="form-control" type="text" name="assunto" value="" id="emailSubject" />
                                        </div>
                                        
                                        <textarea name="texto" id="emailBody"></textarea><br>
                                        
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

        <!-- Footer -->
        <footer class="footer">
            <div class="container footer-info">
                <p>
                    <span class="ico-user" style="float: none;">Gabriel Massuyoshi Sato <strong>12170</strong></span>
                    <span class="ico-user" style="float: none;">JosÃ© Carlos Cieni JÃºnior <strong>12177</strong></span><br>
                    Projeto desenvolvido para a disciplina de AplicaÃ§Ãµes Corporativas na Internet - 5Âº Semestre / InformÃ¡tica Diurno.
                </p>
            </div>
        </footer>
        
        <!-- Modal -->
        <div class="modal fade" id="emailModal" tabindex="-1" role="dialog" aria-labelledby="emailTitle" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Ã</button>
                        <h4 class="modal-title" id="emailTitle">TÃ­tulo do E-mail</h4>
                        <hr>
                        <p>
                            <strong>Recebido: </strong><span id="emailTime">22/05/2011 Ã s 11:45:31</span><br>
                            <strong>De: </strong><span id="emailFrom">JoÃ£ozinho &lt;<a href="mailto:joao@olar.com">joao@olar.com</a>&gt;</span></p>
                    </div>
                    <div class="modal-body">
                        <iframe id="emailBody" style="width: 100%; height: 250px; margin: 0; border: none;" seamless="seamless" src="readme.md"></iframe>
                    </div>
                    <div class="modal-footer" style="margin-top: 0;">
                        
                        <button id="emailReply" type="button" class="btn btn-primary ico-reply">Responder</button>
                        <button id="emailForward" type="button" class="btn btn-info ico-forward-2">Encaminhar</button>
                        <button id="emailModalClose" type="button" class="btn btn-default ico-close" data-dismiss="modal">Fechar</button>
                    </div>
                </div>
            </div>
        </div>

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