<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.servlet.Logar"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Login"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="modelo/cabecalhopag.jsp?atual=emails" /> <%-- inclusao dinamica --%>

        <main class="main">
            <div class="container">
                <div class="row">
                    <% 
                        String msg = request.getParameter("mensagem");
                        if (msg != null) {
                            if (msg.equals("emailSucesso")) { %>
                    <div class="content" role="main">
                        <div class="alert alert-success alert-dismissable">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>O e-mail foi enviado com sucesso.</p>
                        </div>
                    </div>
                                
                            <% } else if (msg.equals("emailFalha")) { %>
                    <div class="content" role="main">
                        <div class="alert alert-warning alert-dismissable">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>Houve uma falha ao enviar o e-mail. Tente novamente.</p>
                        </div>
                    </div>
                                
                            <% }
                        } %>
                    
                    <aside class="col-md-3 sidebar" role="complementary" style="z-index: 0; font-size: .8em">
                        <h3 class="ico-drawer-2">suas pastas</h3> 
                        <a href="#" id="btnAddFolder" class="btn btn-default btn-sm pull-right ico-plus"></a>
                        <hr>
                        
                        <div id="folder-treeview">
                            <!-- <a href="" class="ico-folder-open btn btn-block btn-primary disabled">Pasta X</a>
                            <a href="" class="ico-folder btn btn-block btn-default">Pasta 1</a> -->
                            <p class="text-center">Carregando...</p>
                        </div>
                    </aside>
                    <div id="folderMessages" class="col-md-9 content" role="main">
                        
                        <div class="well">
                            <p>Selecione uma pasta para exibição...</p>
                        </div>
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
                            <strong>De: </strong><span id="emailFrom">Joãozinho &lt;<a href="mailto:joao@olar.com">joao@olar.com</a>&gt;</span><br>
                            <strong>Para: </strong><span id="emailTo">Joãozinho &lt;<a href="mailto:joao@olar.com">joao@olar.com</a>&gt;</span><br>
                            <strong>Cópia: </strong><span id="emailCC">Joãozinho &lt;<a href="mailto:joao@olar.com">joao@olar.com</a>&gt;</span></p>
                    </div>
                    <div class="modal-body">
                        <iframe id="emailBody" style="width: 100%; height: 250px; margin: 0; border: none;" seamless="seamless" src=""></iframe>
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
            var folderMessages = $("#folderMessages");
            var xhr = "";
            var pastas = $("#folder-treeview");
            var url = "";
            var pastas_nodes = {};
            
            context.init({preventDoubleContext: false});
	
            $.getJSON("email.jsp", { acao: "pastas" }, function(dados) {
                if (!dados.erro) {
                    pastas_nodes = dados;
                    pastas.treeview({
                        nodeIcon: "",
                        expandIcon: "ico-folder",
                        collapseIcon: "ico-folder-open",
                        selectedBackColor: "#aaa",
                        data: dados,
                        onNodeSelected: function (e, node) {
                            folderMessages
                                    .empty()
                                    .append("<div class=well>Carregando pasta...</div>");
                            
                            if (xhr.abort)
                                xhr.abort();
                            
                            xhr = $.ajax({
                                type: "POST",
                                url: "/email.jsp",
                                data: {
                                    acao: "pasta",
                                    url: node.urlname 
                                }
                            });
                            url = node.urlname;
                            xhr.done(function(data) {
                                folderMessages.html(data);
                                locastyle.tables.init(folderMessages);
                                xhr = "";
                                
                                var sel = $("#selectFolderMove", folderMessages);
                                sel.empty();
                                
                                var nSel = $("<select class=form-control name=moveMsg/>");
                                nSel.css('width', 'auto').css('display', 'inline-block');
                                
                                var geraOp = function(i, node) {
                                    if (node.selectable) {
                                        nSel.append($("<option value='"+node.urlname+"'>"+node.text+"</option>"));
                                    }
                                    if (node.nodes)
                                        $.each(node.nodes, geraOp);
                                    if (node._nodes)
                                        $.each(node._nodes, geraOp);
                                };
                                
                                $.each(pastas_nodes, geraOp);
                                sel.append(nSel);
                                
                            });
                        }
                    });
                    context.attach("#folder-treeview ul li", [
                        { text: 'Renomear', action: function(e) {
                                var pastaClick = $('body').data('context');
                                
                                if (!pastaClick.is("li")) {
                                    pastaClick = pastaClick.parent("li");
                                }
                                var nomeAtu = pastaClick.clone();
                                nomeAtu.children("span").remove();
                                nomeAtu = nomeAtu.text();
                                
                                var nome = window.prompt("Informe um novo nome para a pasta:", nomeAtu);
                                if (nome != null && nome != nomeAtu && nome != "") {
                                    $.post("/conf/pasta", { url: url, acao: "renomear", novo: nome }, function(d) {
                                        if (d.erro)
                                            alert(d.erro);
                                        else {
                                            alert("Sucesso! Recarregando pastas...");
                                            location.href = "/";
                                        }
                                    }, "json");
                                }
                        }},
                        { divider: true },
                        { text: 'Esvaziar', class: 'text-warning', action: function(e) {
                                if (confirm('Confirma excluir todas as mensagens desta pasta?')) {
                                    $.post("/conf/pasta", { url: url, acao: "esvaziar" }, function(d) {
                                        if (d.erro)
                                            alert(d.erro);
                                        else {
                                            alert("Sucesso! Recarregando pastas...");
                                            location.href = "/";
                                        }
                                    }, "json");
                                }
                        }},
                        { text: 'Excluir', class: 'text-danger', action: function(e) {
                                if (confirm('Confirma excluir esta pasta e todas as mensagens nela contidas?')) {
                                    $.post("/conf/pasta", { url: url, acao: "excluir" }, function(d) {
                                        if (d.erro)
                                            alert(d.erro);
                                        else {
                                            alert("Sucesso! Recarregando pastas...");
                                            location.href = "/";
                                        }
                                    }, "json");
                                }
                        } }
                    ]);
                } else {
                    pastas.children("p").remove();
                    pastas.append("<div class='alert alert-danger'>Verifique suas configurações de IMAP.</div>");
                }
            });
            
            $("#btnAddFolder").click(function(e) {
                var nome = prompt("Entre com o nome para a nova pasta:", "");
                
                if (nome != null) {
                    $.post("/conf/pasta", { url: url, acao: "nova", novo: nome }, function(d) {
                        if (d.erro)
                            alert(d.erro);
                        else {
                            alert("Sucesso! Recarregando pastas...");
                            location.href = "/";
                        }
                    }, "json");
                }
            });
            
        });
        </script>
    </body>
</html>