<%@ page import="java.io.PrintWriter,
                 java.io.StringWriter,
                 java.net.URLEncoder"%>
<%@ page isErrorPage="true"%>
<html>

	<head>
        <meta http-equiv="refresh" content="30">
		<script type="text/javascript" src="/PublicDisplay/js/browser_sniffer.js">
		</script>
        <script type="text/javascript" src="/PublicDisplay/js/common.js">
        </script>
        <script type="text/javascript">

<%
        StringWriter w= new StringWriter(1000);
        exception.printStackTrace(new PrintWriter(w));
        w.close();
        StringBuffer buffer = w.getBuffer();
        if(buffer.length() > 300) {
                buffer.setLength(300);
        }
        String stackTrace= URLEncoder.encode(buffer.toString());

%>

            top.location.href= '/PublicDisplay/errors/error_rotation_set.jsp?stack_trace=<%=stackTrace%>';
		</script>
	</head>

	<body bgcolor="red">
		<h3>Manager Failure</h3>
        <h4>Stack Trace:</h4>
        <pre>
            <%exception.printStackTrace(new PrintWriter(out));%>
        </pre>
		<pre id="log"></pre>
	</body>
</html>
