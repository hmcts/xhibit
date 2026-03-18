<%@ page contentType="text/html; charset=UTF-8" import="java.net.URL" %>

<%
    // Get the fully qualified url for the keepalive page for use later.    
    URL requestUrl = new URL(request.getRequestURL().toString());
    URL keepAliveUrl = new URL(requestUrl, "/keepalive.htm");    
    String keepAlive = keepAliveUrl.toExternalForm();
%>

<html>
<head><title>Content Switch Heartbeat: Home</title></head>
<body>
    <h3>Content Switch Heartbeat: Home</h3>
    <p>This web application should be the default for the server. It contains the keep alive page used as the heatbeat for the content switch.
    The content switch looks for the page <a href="<%=keepAlive%>"><%=keepAlive%></a>.</p>
</body>
</html>

