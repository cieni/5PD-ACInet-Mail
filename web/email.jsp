<%@page import="org.json.simple.JSONObject"%>
<%@page import="net.htmlparser.jericho.Element"%>
<%@page import="net.htmlparser.jericho.Source"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.servlet.Email"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="static javax.mail.Flags.Flag"%>
<%@page import="javax.mail.MessageAware"%>
<%@page import="javax.mail.FetchProfile"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.DateFormat"%>
<%@page import="javax.mail.internet.InternetAddress"%>
<%@page import="javax.mail.Address"%>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.URLName"%>
<%@page import="java.io.PrintStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.IOException"%>
<%@page import="javax.mail.MessagingException"%>
<%@page import="javax.mail.Store"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Conta"%>
<%@page import="javax.mail.Folder"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.servlet.Logar"%>
<%@page import="br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.entity.db.Login"%>
<%@page buffer="1024kb" pageEncoding="UTF-8" %>
<%
    Login usuario = null;
    Conta conta = null;
    String acao = request.getParameter("acao");
    
    try {
        usuario = Logar.getLogin(session);
        conta = (Conta) session.getAttribute("conta");
    } catch (Logar.NaoAutenticadoException ex) {
    }
    
    if (usuario == null || conta == null || acao == null) {
        if (usuario == null)
            out.print("{\"erro\": \"Não autenticado\"}");
        else if (conta == null)
            out.print("{\"erro\": \"Nenhuma conta associada\"}");
        else if (acao == null)
            out.print("{\"erro\": \"Ação não especificada\"}");
        
        return;
    }
    
    if (acao.equals("email")) {
        response.setContentType("application/json");
        
        int id = Integer.parseInt(request.getParameter("id"));
        String url = request.getParameter("url");
        URLName urln = new URLName(url);

        Conta c = (Conta) request.getSession().getAttribute("conta");
        Folder pasta = Logar.getImapStore(c).getFolder(urln);
        pasta.open(Folder.READ_WRITE);

        Message m = pasta.getMessage(id);

        String corpo = Email.getText(m);
        if (corpo == null) corpo = "<html><body></body></html>";
        
        JSONObject obj = new JSONObject();
        obj.put("titulo", m.getSubject());
        obj.put("data", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(m.getReceivedDate()));
        obj.put("de", recipientsLink(m.getFrom()));
        obj.put("para", recipientsLink(m.getRecipients(Message.RecipientType.TO)));
        obj.put("cc", recipientsLink(m.getRecipients(Message.RecipientType.CC)));
        obj.put("mensagem", corpo);
        
        obj.writeJSONString(out);
        
    } else if (acao.equals("pastas")) {
        response.setContentType("text/html");
        
        Store store = null;
        try {
            store = Logar.getImapStore(conta);
            
            //lerPastas(store.getDefaultFolder().list("*"), out);
            lerPastas(store.getDefaultFolder().list(), out);
            
        } catch (MessagingException ex) {
            out.print("{\"erro\": \"Erro ao conectar ao store IMAP\"}");
            //throw new RuntimeException(ex);
            return;
        }
        
    } else if (acao.equals("pasta")) {
        response.setContentType("text/html");
        
        String url = request.getParameter("url");
        URLName urln = new URLName(url);
        
        Store store = null;
        try {
            store = Logar.getImapStore(conta);
            Folder pasta = store.getFolder(urln);
            
            pasta.open(Folder.READ_ONLY);
            Message[] msgs = pasta.getMessages(); //TODO paginacao?
            
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.FLAGS);
            
            pasta.fetch(msgs, fp);
            
        %>
<h2 class="ico-arrow-right"><%=nomePasta(pasta.getName())%></h2><br />
                        
<%--<form enctype="multipart/form-data" action="/email?lote" method="post" class="ls-form-text">--%>
<form enctype="multipart/form-data" action="/email?lote" method="post" role="form">
    <input type="hidden" name="url" value="<%=url%>" />
    <div class="well well-sm clearfix ls-table-group-actions">
        <p class="d-inline-block">
            <strong class="counterChecks">0</strong>
            <span class="counterChecksStr">itens selecionados</span>
        </p>
        <div class="actions pull-right">
            <span id="selectFolderMove"></span>
            <button type="submit" class="btn btn-info">Mover</button>
            <%--<button type="button" class="btn btn-info">Mover</button>
            <button type="button" class="btn btn-danger">Excluir</button>--%>
        </div>
    </div>
    <% if (msgs.length > 0) { %>
    <table class="table ls-table">
        <thead>
            <tr>
                <th class="txt-center" style="width: 40px"><input type="checkbox"></th>
                <th class="ls-nowrap" style="width: 220px;">Remetente</th>
                <th class="txt-center hidden-xs" style="width: 170px;">Recebido</th>
                <th class="" colspan="2">Assunto</th>
            </tr>
        </thead>
        <tbody>
            <% 
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Message m;
            String labels;
            //for (int i = 0; i < msgs.length; ++i) {
            for (int i = msgs.length - 1; i >= 0; --i) {
                m = msgs[i];
                labels = "";
                /*
                if (m.isSet(Flag.ANSWERED))
                    labels += "<span class='label label-success ico-reply'></span>";
                */
                
                if (!m.isSet(Flag.SEEN))
                    //labels += "<span class='label label-info'>não lida</span>";
                    labels = "<span class='pull-right label label-info'>não lida</span>";
            %>
            <tr class="<%--=m.isSet(Flag.SEEN) ? "" : "success"--%>">
                <td class="txt-center"> <input name="id" value="<%=m.getMessageNumber()%>" type="checkbox"> </td>
                <td><%=recipientList(m.getFrom())%> <%=labels%></td>
                <td class="txt-center"><%=sdf.format(m.getReceivedDate())%></td>
                <td><%=m.getSubject()%></td>
                <td style="width: 100px; padding-right: 0; padding-left: 0;" class="txt-center">
                    <a style="display: inline" title="visualizar" href="#" onclick="return lerEmail(<%=m.getMessageNumber()%>, '<%=url.replace("'", "\\'")%>');" class="ico-checkmark text-primary"></a>
                    <a style="display: inline" title="responder" href="#" onclick="return responderEmail(<%=m.getMessageNumber()%>, '<%=url.replace("'", "\\'")%>');" class="ico-reply"></a>
                    <a style="display: inline" title="encaminhar" href="#" onclick="return encaminharEmail(<%=m.getMessageNumber()%>, '<%=url.replace("'", "\\'")%>');" class="ico-forward-2"></a>
                    <%--<a style="display: inline" title="excluir" href="/email?ak=delete&id=<%=m.getMessageNumber()%>&url=<%=URLEncoder.encode(url.replace("'", "\\'"), "UTF-8")%>" data-confirm-text="Confirma a exclusão deste e-mail?" class="text-danger ico-remove"><span style="display: none">Excluir</span></a>--%>
                    <a style="display: inline" title="excluir" onclick="return confirm('Confirma a exclusão deste e-mail?')" href="/email?ak=delete&id=<%=m.getMessageNumber()%>&url=<%=URLEncoder.encode(url.replace("'", "\\'"), "UTF-8")%>" class="text-danger ico-remove"><span style="display: none">Excluir</span></a>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <% } else {%>
    <div class="well">
        <p>A pasta está vazia.</p>
    </div>
    <% }%>
</form>
        <%
        } catch (MessagingException ex) {
            out.print("<div class='alert alert-danger'>Erro ao carregar as mensagens da pasta. Atualize a página e, se o erro persistir, verifique suas configurações de IMAP.</div>");
            //throw new RuntimeException(ex);
            return;
        }
    }
%>
<%!
    public String recipientList(Address[] addrs) {
        if (addrs == null) return "";
        StringBuilder ret = new StringBuilder(addrs.length * 20);
        
        for (Address addr : addrs) {
            if (addr instanceof InternetAddress) {
                InternetAddress ia = (InternetAddress) addr;
                String nome = ia.getPersonal(), email = ia.getAddress();
                
                if (nome != null && !nome.trim().equals(""))
                    ret.append(nome).append(", ");
                else 
                    ret.append(email).append(", ");
            } else {
                ret.append(addr).append(", ");
            }
        }
        
        if (ret.length() > 2)
            ret.setLength(ret.length() - 2);
        
        return ret.toString();
    }
    
    public String recipientsLink(Address[] addrs) {
        if (addrs == null) return "";
        StringBuilder ret = new StringBuilder(addrs.length * 20);
        
        for (Address addr : addrs) {
            if (addr instanceof InternetAddress) {
                InternetAddress ia = (InternetAddress) addr;
                String nome = ia.getPersonal(), email = ia.getAddress();
                
                if (nome != null && !nome.trim().equals(""))
                    ret.append(nome).append(" &lt;<a href='mailto:").append(email).append("'>").append(email).append("</a>&gt;, ");
                else 
                    ret.append("<a href='mailto:").append(email).append("'>").append(email).append("</a>, ");
            } else {
                ret.append(addr).append(", ");
            }
        }
        
        if (ret.length() > 2)
            ret.setLength(ret.length() - 2);
        
        return ret.toString();
    }
    
    public String nomePasta(String pasta) {
        if (pasta.equalsIgnoreCase("INBOX"))
            return "Caixa de entrada";
        return pasta;
    }
    
    public void lerPastas(Folder[] pastas, JspWriter out) throws MessagingException, IOException {
        out.print("[");
        
        for (int i = 0; i < pastas.length; ++i) {
            Folder pasta = pastas[i];
            
            out.print("{");
            
            out.print("\"text\": \"" + nomePasta(pasta.getName()).replace("\"", "\\\"") + "\"");
            out.print(",\"urlname\": \"" + pasta.getURLName().toString().replace("\"", "\\\"") + "\"");
            
            if ((pasta.getType() & Folder.HOLDS_MESSAGES) != 0) {
                out.print(",\"selectable\": true");
                
                int unread = pasta.getUnreadMessageCount();
                //if (unread > 0)
                    out.print(",\"tags\": [\"" + unread + "/" + pasta.getMessageCount() + "\"]");
            } else {
                    out.print(",\"selectable\": false");
            }
            
            out.print(",\"nodes\": ");
            lerPastas(pasta.list(), out);
            
            if (i < pastas.length - 1)
                out.print("},");
            else
                out.print("}");
        }
        
        
        out.print("]");
    }
%>