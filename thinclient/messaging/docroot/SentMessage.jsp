<%@page import="uk.gov.courtservice.xhibit.web.messaging.bean.FlattenedTree,uk.gov.courtservice.xhibit.web.messaging.bean.DisplayLineNode"%>
<%--
  - The format tag lib is used to i18n messages
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%--
  - Get the page urls from the Pages resource bundle
  --%>

<fmt:bundle basename="Pages">
    <fmt:message key="button" var="buttonURL"/>
    <fmt:message key="closewindowbutton" var="closeButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0"/>
<tr>
    <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psFormNameLeft">
        <fmt:message key="messagesent"/>
    </td>
    <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
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
