<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="modelo/cabecalho.jsp" %>
        <div class="container pagina-login" style="display: none">
            <div class="row">
                <div class="center-block box-parent-login" style="position: absolute; width: 50%;" id="boxLogin">
                    <div class="well bg-white box-login">
                        <h1 class="ls-login-logo">VagalMAIL <small>Login</small></h1><br>
                        
                        <%
                            String erro = (String) request.getAttribute("erro");
                            String msg = request.getParameter("mensagem");
                            
                            if (erro != null || msg != null) {
                                if (erro != null && erro.equals("invalido")) {
                        %>
                        <div class="alert alert-danger alert-dismissable">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>Combinação inválida de usuário e senha.</p>
                        </div>
                        <% } else if (erro != null && erro.equals("logout")) { %>
                        <div class="alert alert-success alert-dismissable">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>Você saiu.</p>
                        </div>
                        <% } else if (msg.equals("cadok")) { %>
                        <div class="alert alert-info alert-dismissable">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>Cadastro realizado com sucesso. Efetue o login e cadastre uma conta em seguida.</p>
                        </div>
                        <%
                                }
                            }
                        %>
                        <div class="alert alert-danger alert-dismissable" id="opa-erro">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>As senhas digitadas não são iguais.</p>
                        </div>
                        
                        <form id="formLogin" autocomplete="off" role="form" method="post" action="/login">
                            <fieldset>

                                <div class="form-group cadastro-only">
                                    <label for="userName" class="sr-only">Seu nome</label>
                                    <input name="userName" class="form-control input-lg" id="userName" type="text" aria-label="Seu nome" placeholder="Seu nome" />
                                </div>
                                
                                <div class="form-group ls-login-user">
                                    <label for="userLogin" class="sr-only">Usuário</label>
                                    <input name="userLogin" class="form-control ls-login-bg-user input-lg" id="userLogin" type="text" aria-label="Usuário" placeholder="Usuário" />
                                </div>

                                <div class="form-group ls-login-password">
                                    <label for="userPassword" class="sr-only">Senha</label>
                                    <input name="userPassword" class="form-control ls-login-bg-password input-lg" id="userPassword" type="password" aria-label="Senha" placeholder="Senha" />
                                </div>
                                
                                <div class="form-group cadastro-only">
                                    <label for="userPassword2" class="sr-only">Confirme a senha</label>
                                    <input name="userPassword2" class="form-control ls-login-bg-password input-lg" id="userPassword2" type="password" aria-label="Confirme a senha" placeholder="Confirme a senha" />
                                </div>

                                <p class="txt-center login-only">
                                    <!--<a href="#" class="ls-login-forgot">Esqueci minha senha</a> | -->
                                    Não possuo conta, <a href="#" class="ls-login-signup">cadastrar</a>
                                </p>

                                <p class="txt-center cadastro-only">
                                    Já possuo conta, <a href="#" class="ls-login-login">entrar</a>
                                </p>

                                <input type="submit" value="Entrar" class="btn btn-primary btn-lg btn-block">
                                
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        
        <%@include file="modelo/scripts.jsp" %>
        
        <script type="text/javascript">
        $(function() {
            var box = $('div#boxLogin');
            var login = $("#userLogin");
            
            $(window).on('resize', function(e) {
                box.animate({
                    position: 'absolute',
                    left: $(window).width() / 2 - box.outerWidth() / 2,
                    top: $(window).height() / 2 - box.outerHeight() / 2
                }, 100);
            });
            
            $(".ls-login-signup").on('click', function(e) {
                e.preventDefault();
                $(".alert").hide();
                $(".login-only").slideUp();
                $(".cadastro-only").slideDown(function () {
                    $(window).resize();
                });
                $("#formLogin").attr('action', '/cadastro');
                $("input[type=submit]").val('Cadastrar');
            });
            
            $(".ls-login-login").on('click', function(e) {
                e.preventDefault();
                $(".alert").hide();
                $(".cadastro-only").slideUp();
                $(".login-only").slideDown(function () {
                    $(window).resize();
                });
                $("#formLogin").attr('action', '/login');
                $("input[type=submit]").val('Entrar');
            });
            
            var ehValido = false;
            login.on('blur', function() {
                var pai = login.parent("div");
                pai.removeClass("has-success has-error");
                
                ehValido = false;
                if ($("#formLogin").attr('action') == "/cadastro") {
                    $.getJSON("/login", {olar: $(this).val()}, function(d) {
                        ehValido = d.ok;
                        
                        if (ehValido && login.val().trim() != "") {
                            pai.addClass("has-success")
                        } else {
                            pai.addClass("has-error")
                        }
                    });
                }
            });
            
            $("#formLogin").submit(function(e) {
                if ($(this).attr('action') == "/cadastro") {
                    if (!ehValido) {
                        login.focus();
                        e.preventDefault();
                        return;
                    }
                    if ($("#userPassword").val() != $("#userPassword2").val()) {
                        e.preventDefault();
                        $("#opa-erro").slideDown(function () {
                            $(window).resize();
                        });
                    }
                }
            });
            
            if (location.href.indexOf("cadastro") == -1) {
                $(".cadastro-only").hide();
            } else {
                $(".login-only").hide();
                $("#formLogin").attr('action', '/cadastro')
            }
            
            $("#opa-erro").hide();
            login.focus();
            $(".pagina-login").fadeIn('slow');
            $(window).resize();
            
            
        });
        </script>
    </body>
</html>