<%--
  - Title:       
  -
  - Description: 
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley (2003)
  --%>

<%@page import="uk.gov.courtservice.xhibit.web.messaging.IMSessionInfo,java.text.SimpleDateFormat"%>
<%--
  - The format tag lib is used to i18n messages
  --%>

<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%--
  - The html for laying out the pages
  --%>
<html>
<head>
    <META http-equiv="Pragma" content="no-cache"/>
    <META http-equiv="Expires" content="-1"/>
    <script src="/Static/util.js">
    </script>
    <link href="/Static/style.css" rel="stylesheet" type="text/css"/>
</head>
<body onLoad="doLoad()" bottommargin="5" topmargin="5" leftmargin="5" rightmargin="5">
<table cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td class="psFormName">Checking for instant messages . . .</td>
    </tr>
</table>
<script language="JavaScript">

var sURL = unescape(window.location.pathname);

function doLoad()
{
    <%

    if (request.getSession(false)!= null) {
        Object obj = request.getSession().getAttribute(IMSessionInfo.INSTANT_MESSAGE_SESSION);

        if (obj != null && obj instanceof IMSessionInfo && ((IMSessionInfo)obj).hasNewMessages()) {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyhhmmssSS");
            String messageWindowTitle = "MessageDisplay" +sdf.format(new java.util.Date());
            %>
            openWindowWithFocus(
                               "displaymessages",
                               "<%=messageWindowTitle%>",
                               "fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no,directories=no,location=no,width=500,height=200,left=20,top=20"
                               );
            <%
        }
    } else {
        %>
        <h1>Have NOT found Session</h1>
        <%
    }
    %>

        the_timeout = setTimeout("window.blur();", 3000);
    checkMessageWindow();
}
</script>
</body>
</html>

