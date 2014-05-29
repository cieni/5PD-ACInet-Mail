<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Login"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    try {
        int idUsuario = Login.getID(session);
    } catch (Login.NaoAutenticadoException e) {
        
    }
%>
