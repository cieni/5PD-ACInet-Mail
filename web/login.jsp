<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="modelo/cabecalho.html" %>
        <div class="container">
            <div class="row">
                <div class="center-block box-parent-login" style="position: absolute; width: 50%;" id="boxLogin">
                    <div class="well bg-white box-login">
                        <h1 class="ls-login-logo">VagalMAIL <small>Login</small></h1><br>
                        
                        <%
                            String erro = request.getParameter("erro");
                            if (erro != null && erro.equals("invalido")) {
                        %>
                        <div class="alert alert-danger alert-dismissable">
                            <a href="#" class="close" data-dismiss="alert" aria-hidden="true">×</a>
                            <p>Combinação inválida de usuário e senha.</p>
                        </div>
                        <%
                            }
                        %>
                        <form role="form" method="post" action="/login">
                            <fieldset>

                                <div class="form-group ls-login-user">
                                    <label for="userLogin" class="sr-only">Usuário</label>
                                    <input name="userLogin" class="form-control ls-login-bg-user input-lg" id="userLogin" type="text" aria-label="Usuário" placeholder="Usuário">
                                </div>

                                <div class="form-group ls-login-password">
                                    <label for="userPassword" class="sr-only">Senha</label>
                                    <input name="userPassword" class="form-control ls-login-bg-password input-lg" id="userPassword" type="password" aria-label="Senha" placeholder="Senha">
                                </div>

                                <p class="txt-center">
                                    <a href="#" class="ls-login-forgot">Esqueci minha senha</a> | 
                                    <a href="#" class="ls-login-signup">Cadastrar</a>
                                </p>
                                </p>

                                <input type="submit" value="Entrar" class="btn btn-primary btn-lg btn-block">
                                
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        
<%@include file="modelo/scripts.html" %>
        <script type="text/javascript">
        $(function() {
            var box = $('div#boxLogin');
            
            $(window).on('resize', function(e) {
                box.css({
                    position: 'absolute',
                    left: $(window).width() / 2 - box.outerWidth() / 2,
                    top: $(window).height() / 2 - box.outerHeight() / 2
                });
            });
            
            $(window).resize();
        });
        </script>
    </body>
</html>