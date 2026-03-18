<%@ page import="uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderer,
                 uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.impl.FileStorer,
                 uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet.InitializationService,
                 java.io.PrintWriter"%>
<html>
<% request.setAttribute("title", "Status");%>
<%@ include file="header.jsp"%>

        <h2>Public Display Application Status Page</h2>
        <p>The web application must process some initialization before it is ready to accept incoming messages. The
           current status is displayed below.
        </p>

        <%if(InitializationService.getInstance().isInitialized()) {%>
            <h3>Public Display application is initialized.</h3>
        <% } else { %>
            <%
            Throwable initialisationFailure = InitializationService.getInstance().getInitialisationFailure();
            if(initialisationFailure != null) {
                out.println("<h3 style='color:red;'>Public Display application failed to initialize.</h3><pre>");
                initialisationFailure.printStackTrace(new PrintWriter(out));
                out.println("</pre>");
            } else {

               out.println("<h3 style='color:blue;'>Public Display application is not yet initialized.</h3><script>setTimeout('window.location.reload(true)',5000);</script>");
            }
        }
     %>


<%@ include file="footer.jsp"%>
