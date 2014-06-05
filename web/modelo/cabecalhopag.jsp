<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Conta"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.servlet.Logar"%>
<%@page import="javax.persistence.PersistenceException"%>
<%@page import="java.util.List"%>
<%@page import="javax.persistence.Query"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.BD"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Login"%>
<%@include file="cabecalho.jsp" %>
        <header class="header" role="banner">
            <div class="container">
                <span class="control-menu visible-xs ico-menu-2"></span>
                <span class="control-sidebar visible-xs ico-list"></span>
                <h1 class="project-name"><a href="#">VagalMAIL</a></h1>
                <a href="/logout" class="help-suggestions ico-exit hidden-xs">Fazer logoff</a>

                <%
                    Login usuario = Logar.getLoginOuRedireciona(request, response, session);
                    List<Conta> contas = usuario.getContaList();
                    
                    // realiza as trocas de conta, já que esse arquivo
                    // é incluído por todo mundo
                    String altConta = request.getParameter("conta");
                    if (altConta != null) {
                        for (Conta c : contas)
                            if (altConta.equals(c.getId() + ""))
                                session.setAttribute("conta", c);
                    }
                    
                    Conta conta = (Conta) session.getAttribute("conta");
                    if (conta == null) {
                        if (!request.getRequestURL().toString().contains("/conf/nova")) {
                            System.out.print(request.getRequestURL());
                            request.getRequestDispatcher("/conf/nova").forward(request, response);
                        }
                    }
                    
                    if (contas != null && !contas.isEmpty()) { %>
                <div class="dropdown hidden-xs">
                    <a href="#" data-toggle="dropdown" class="title-dropdown"><%=conta.getEmail()%></a>
                    <ul class="dropdown-menu" role="menu">
                        <% for (Conta c : contas) { %>
                        <li><a href="?conta=<%=c.getId()%>" role="menuitem"><%=c.getEmail()%></a></li>
                        <% } %>
                    </ul>
                </div>
                    <% } %>
            </div>
        </header>
<%!
    private String activeMenu(HttpServletRequest request, String atual) {
        String param = request.getParameter("atual");
        if (param != null) {
            if (atual.equals(param))
                return " active ";
        }
        return "";
    }
%>
        <!-- Menu -->
        <div class="nav-content">
            <menu class="menu">
                <ul class="container">
                    <% if (!contas.isEmpty()) { %>
                    <li><a href="/" class="<%=activeMenu(request,"emails")%> ico-envelop" role="menuitem">E-mails</a></li>
                    <li><a href="/email" role="menuitem" class="<%=activeMenu(request,"novo")%> ico-plus">Novo e-mail</a></li>
                    <li><a href="#" role="menuitem" class="<%=activeMenu(request,"conf")%> ico-checkmark-circle">Configurações de conta</a>
                        <ul>
                            <li><a href="/conf/nova" role="menuitem">Adicionar</a></li>
                            <li><a href="/conf/conta" role="menuitem">Acesso atual</a></li>
                            <li><a href="/conf/senha" role="menuitem">Alterar senha</a></li>
                        </ul>
                    </li>
                    <li><a href="#" role="menuitem" class="visible-xs ico-exit">Logoff</a></li>
                    <% } else { %>
                    <li><a href="/conf/nova" role="menuitem" class="active ico-checkmark-circle">Configurações de conta</a>
                    <% } %>
                </ul>
            </menu>
        </div>