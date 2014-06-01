<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Logar"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Login"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="modelo/cabecalhopag.jsp?atual=emails" /> <%-- inclusao dinamica --%>

        <main class="main">
            <div class="container">
                <div class="row">
                    <aside class="col-md-3 sidebar" role="complementary" style="z-index: 0">
                        <h3 class="ico-drawer-2">suas pastas</h3>
                        <hr>
                        
                        <div class="well">
                            <a href="" class="ico-folder btn btn-block btn-default">Pasta 1</a>
                            <a href="" class="ico-folder btn btn-block btn-default">Pasta 1</a>
                            <a href="" class="ico-folder btn btn-block btn-default">Pasta 1</a>
                            <a href="" class="ico-folder-open btn btn-block btn-primary disabled">Pasta X</a>
                            <a href="" class="ico-folder btn btn-block btn-default">Pasta 1</a>
                        </div>
                    </aside>
                    <div class="col-md-9 content" role="main">
                        <h2 class="ico-arrow-right">Pasta X</h2>
                        
                        <form action="/servidor/salvar" method="post" class="ls-form-text">
                            <div class="well well-sm clearfix ls-table-group-actions">
                                <p class="d-inline-block">
                                    <strong class="counterChecks">0</strong>
                                    <span class="counterChecksStr">itens selecionados</span>
                                </p>
                                <div class="actions pull-right">
                                    <button type="button" class="btn btn-info">Mover</button>
                                    <button type="button" class="btn btn-danger">Excluir</button>
                                </div>
                            </div>
                            <table class="table ls-table">
                                <thead>
                                    <tr>
                                        <th class="txt-center" style="width: 40px"><input type="checkbox"></th>
                                        <th class="ls-nowrap" style="width: 200px;">Remetente</th>
                                        <th class="txt-center hidden-xs" style="width: 100px;">Recebido</th>
                                        <th class="" colspan="2">Assunto</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td> <input name="id" value="1" type="checkbox"> </td>
                                        <td>José Carlos</td>
                                        <td class="txt-center">11/05/2014</td>
                                        <td>Projeto do Webmail</td>
                                        <td style="width: 100px; padding-right: 0; padding-left: 0;" class="txt-center">
                                            <a style="display: inline" title="visualizar" href="#" onclick="return lerEmail(1);" class="ico-checkmark text-primary"></a>
                                            <a style="display: inline" title="responder" href="#" onclick="return responderEmail(1);" class="ico-reply"></a>
                                            <a style="display: inline" title="encaminhar" href="#" onclick="return encaminharEmail(1);" class="ico-forward-2"></a>
                                            <a style="display: inline" title="excluir" href="email.jsp?acao=excluir&id=2" data-confirm-text="Confirma a exclusão deste e-mail?" class="text-danger ico-remove"><span style="display: none">Excluir</span></a>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </form>
                        
                    </div>
                </div>
            </div>
        </main>

        <!-- Modal -->
        <div class="modal fade" id="emailModal" tabindex="-1" role="dialog" aria-labelledby="emailTitle" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title" id="emailTitle">Título do E-mail</h4>
                        <hr>
                        <p>
                            <strong>Recebido: </strong><span id="emailTime">22/05/2011 às 11:45:31</span><br>
                            <strong>De: </strong><span id="emailFrom">Joãozinho &lt;<a href="mailto:joao@olar.com">joao@olar.com</a>&gt;</span></p>
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

<%@include file="modelo/rodapepag.jsp" %>