<%@page import="uk.gov.courtservice.xhibit.web.messaging.IMSessionInfo,uk.gov.courtservice.xhibit.web.messaging.Utility"%>
<%--
  - The format tag lib is used to i18n messages
  --%>

<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<fmt:bundle basename="Pages">
    <fmt:message key="button" var="buttonURL"/>
    <fmt:message key="closewindowbutton" var="closeButtonURL"/>
</fmt:bundle>
<table width="100%">
    <tr>
        <td colspan="3" width="100%" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
<%
if(request.getSession(false)!= null)
{
    Object obj = request.getSession().getAttribute(IMSessionInfo.INSTANT_MESSAGE_SESSION);
    if(obj != null && obj instanceof IMSessionInfo)
    {
        IMSessionInfo imsi = (IMSessionInfo)obj;
        String[] messages = imsi.getMessages();
        for(int i = 0; i < messages.length; i++)
        {
            if (i != 0){%>
    <tr>
        <td height="1" width="100%" colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
            <%}%>
        <tr> 
            <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td class="psPageMessage">
            <p>
<%= Utility.getHTMLFormattedString(messages[i], 40)%>
            </p>
    </td>
            <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
<%
        }
    }
}
%>
    
        <tr>
            <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td align="center">
                <c:set scope="request" var="buttonTextKey" value="ok"/>
                <c:import url="${closeButtonURL}"/>
            </td>
            <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
</table>
