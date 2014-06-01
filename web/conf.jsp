<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Conta"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Logar"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Login"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="modelo/cabecalhopag.jsp?atual=conf" /> <%-- inclusao dinamica --%>
        <main class="main">
            <div class="container">
                <div class="row">
                    <div class="content" role="main">
                        
                        <% 
                            String acao = request.getParameter("metodo");
                            String mensagem = (String) request.getAttribute("mensagem");
                            
                            if (mensagem != null) {
                                if (mensagem.equals("erro")) { %>
                        <div class="alert alert-warning alert-dismissable">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>Ocorreu um erro no processamento dos seus dados. Tente novamente mais tarde.</p>
                        </div>
                                <% } else if (mensagem.equals("sucesso")) {
                                    String verbo;
                                    if (acao.equals("nova"))
                                        verbo = "cadastrada";
                                    else
                                        verbo = "atualizada";
                                %>
                        <div class="alert alert-success alert-dismissable">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>A conta foi <%=verbo%> com sucesso.</p>
                        </div>
                                <% } else if (mensagem.equals("antsen")) {%>
                        <div class="alert alert-danger alert-dismissable">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>A senha atual está incorreta.</p>
                        </div>
                                <% } else if (mensagem.equals("senneq")) {%>
                        <div class="alert alert-danger alert-dismissable">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>As senhas informadas são diferentes.</p>
                        </div>
                                <% }
                            }
                            
                            if (acao.equals("nova") || acao.equals("conta")) {
                                Conta conta = null;
                                String verbo = "";
                                if (acao.equals("conta")) {
                                    conta = (Conta) session.getAttribute("conta");
                                    verbo = "Atualizar";
                                }
                                if (conta == null) {
                                    acao = "nova";
                                    conta = new Conta(-1, "", "", -1, "", "", "", -1, "", "");
                                    verbo = "Cadastrar";
                                }
                                
                        %>
                        <h2 class="ico-plus"><%=verbo%> conta</h2><br />
                        
                        <form role="form" action="/conf/salvar" method="post">
                            <div class="form-group col-lg-12">
                                <label class="sr-only" for="frmEmail">E-mail</label>
                                <input value="<%=conta.getEmail()%>" required="required" placeholder="E-mail" type="email" class="form-control" name="email" id="frmEmail" />
                            </div>

                            <fieldset class="col-md-6">
                                <legend>Dados do servidor SMTP</legend>
                                <div class="form-group col-lg-9">
                                    <label for="smtpHost">Endereço (host)</label>
                                    <input value="<%=conta.getSmtpHost()%>" required="required" placeholder="Host (ex: smtp.gmail.com)" type="text" class="form-control" id="smtpHost" name="smtpHost" />
                                </div>
                                
                                <div class="form-group col-lg-3">
                                    <label for="smtpPort">Porta</label>
                                    <input value="<%=conta.getSmtpPort() != -1 ? conta.getSmtpPort() : ""%>" required="required" type="number" min="1" max="65535" class="form-control" id="smtpPort" name="smtpPort" />
                                </div>

                                <div class="form-group col-lg-8">
                                    <label for="smtpLogin">Login</label>
                                    <input value="<%=conta.getSmtpUser()%>" required="required" placeholder="Login (ex: joao@email.com)" type="text" class="form-control" id="smtpLogin" name="smtpLogin" />
                                </div>
                                
                                <div class="form-group col-lg-4">
                                    <label for="smtpPassword">Senha</label>
                                    <input value="<%=conta.getSmtpPassword()%>" required="required" placeholder="Senha" type="password" class="form-control" id="smtpPassword" name="smtpPassword" />
                                </div>
                                
                            </fieldset>
                            
                            <fieldset class="col-md-6">
                                <legend>Dados do servidor IMAP</legend>
                                <div class="form-group col-lg-9">
                                    <label for="imapHost">Endereço (host)</label>
                                    <input value="<%=conta.getImapHost()%>" required="required" placeholder="Host (ex: imap.gmail.com)" type="text" class="form-control" id="imapHost" name="imapHost" />
                                </div>
                                
                                <div class="form-group col-lg-3">
                                    <label for="imapPort">Porta</label>
                                    <input value="<%=conta.getImapPort() != -1 ? conta.getImapPort() : ""%>" required="required" type="number" min="1" max="65535" class="form-control" id="imapPort" name="imapPort" />
                                </div>

                                <div class="form-group col-lg-8">
                                    <label for="imapLogin">Login</label>
                                    <input value="<%=conta.getImapUser()%>" required="required" placeholder="Login (ex: joao@email.com)" type="text" class="form-control" id="imapLogin" name="imapLogin" />
                                </div>
                                
                                <div class="form-group col-lg-4">
                                    <label for="imapPassword">Senha</label>
                                    <input value="<%=conta.getImapPassword()%>" required="required" placeholder="Senha" type="password" class="form-control" id="imapPassword" name="imapPassword" />
                                </div>

                            </fieldset>

                            <input type="hidden" name="acao" value="<%=acao%>" />
                            
                            <div class="box-actions">
                                <div class="pull-right">
                                    <button type="reset" class="btn btn-danger ico-close">Limpar</button>
                                    <button type="submit" class="btn btn-primary ico-checkmark">Enviar</button>
                                </div>
                            </div>
                        </form>
                        
                        <% } else if (acao.equals("senha")) { %>
                        <h2 class="ico-plus">Alterar senha</h2><br />
                        
                        <form role="form" action="/conf/salvar" method="post">

                            <fieldset class="col-md-4">
                                <div class="form-group">
                                    <label for="senAtu">Senha atual</label>
                                    <input required="required" placeholder="Senha atual" type="password" class="form-control" id="senAtu" name="senAtu" />
                                </div>
                                
                                <div class="form-group">
                                    <label for="senNova">Nova senha</label>
                                    <input required="required" placeholder="Nova senha" type="password" class="form-control" id="senNova" name="senNova" />
                                </div>
                                
                                <div class="form-group">
                                    <label for="senNova2">Confirme a nova senha</label>
                                    <input required="required" placeholder="Confirme a nova senha" type="password" class="form-control" id="senNova2" name="senNova2" />
                                </div>
                                
                                <div class="box-actions">
                                    <div class="pull-right">
                                        <button type="reset" class="btn btn-danger ico-close">Limpar</button>
                                        <button type="submit" class="btn btn-primary ico-checkmark">Enviar</button>
                                    </div>
                                </div>
                            </fieldset>

                            <input type="hidden" name="acao" value="senha" />
                            
                        </form>
                        <% } %>
                        
                    </div>
                </div>
            </div>
        </main>
<%@include file="modelo/rodapepag.jsp" %>