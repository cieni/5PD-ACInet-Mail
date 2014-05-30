<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Logar"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Login"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Login usuario = Logar.getLoginOuRedireciona(request, response, session);
    /*if (usuario == null)
        return;*/
%>
olar