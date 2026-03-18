<%--
  - Title:       home.body.jsp (jsp page fragment)
  -
  - Description: This file is the main part of the home (default) page.
  -              Note that it uses the style sheet imported by skeleton.jsp.
  -              Like all pages it should not be referenced directly but
  -              through the pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Bob Boothby (2003)
  --%>

<%@page import="uk.gov.courtservice.xhibit.web.messaging.IMSessionInfo,java.text.SimpleDateFormat"%>
<%--
  - The format tag lib is used to i18n messages
  --%>

<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%--
  - The html to be included in the main page
  --%>

<noscript>
<center><h1>Messaging will not work on a browser without JavaScript.</h1></center>
</noscript>

<script language="JavaScript">
<!--

var sURL = unescape(window.location.pathname);

function doLoad()
{
// The timeout value should be the same as in the "refresh" meta-tag
    setTimeout( "refresh()", 10*1000 );
<%

    if(request.getSession(false)!= null)
    {
        Object obj = request.getSession().getAttribute(IMSessionInfo.INSTANT_MESSAGE_SESSION);

        if(obj != null && obj instanceof IMSessionInfo && ((IMSessionInfo)obj).hasNewMessages())
        {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyhhmmssSS");
            String messageWindowTitle = "MessageDisplay" +sdf.format(new java.util.Date());
%>
    openWindowWithFocus(
        "displaymessages",
        "<%=messageWindowTitle%>",
        "fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no,directories=no,location=no,width=300,height=280,left=200,top=200"
    );
<%
        }
    }
    else
    {
%>
<h1>Have NOT found Session</h1>
<%
    }
%>
}

function refresh()
{
    window.location.href = sURL;
}
//-->
</script>

<script language="JavaScript1.1">
<!--
function refresh()
{
    window.location.replace( sURL );
}
//-->
</script>

<script language="JavaScript1.2">
<!--
function refresh()
{
    window.location.reload( true );
}
//-->
</script>

<script language="JavaScript">
<!--
doLoad();
//-->
</script>
<table width="100%" cellpadding="0" cellspacing="0" border="0"/>
<tr>
    <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td>
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                        </tr>
                        <tr>
                            <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            <td class="psPageTitle">
                                <center>
                                    <fmt:message key="messagecheck.text"/>
                                </center>
                            </td>
                            <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        </tr>
                        <tr>
                            <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </td>
    <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
</table>
