<%--                                                                              <%--
  - Title:       menu.jsp (jsp page fragment)
  -
  - Description: This file is included by the skeleton and provides a common
  -              menu for all pages. Note that it uses the style sheet
  -              imported by skeleton.jsp. Like all pages it should not be
  -              referenced directly but through the pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  -
  --%>
<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - Get the page urls from the Pages resource bundle
  --%>

<fmt:bundle basename="Pages">
<fmt:message key="openwindowbutton" var="menuButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0"/>
<tr>
    <td colspan="7" class="psMenuHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>

    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psMenu">
        <c:set scope="request" var="buttonFill" value="true"/>
        <c:set scope="request" var="buttonTextKey" value="menu.button.sendinstantmessage"/>
        <c:set scope="request" var="windowUrl" value="sendinstantmessage"/>
        <c:set scope="request" var="windowName" value="SendInstantMessage"/>
        <c:set scope="request" var="windowParms" value="fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no,directories=no,location=no,width=640,height=325,left=200,top=200"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu">
        <c:set scope="request" var="buttonTextKey" value="menu.button.sendadhocmessage"/>
        <c:set scope="request" var="windowUrl" value="sendadhocmessage"/>
        <c:set scope="request" var="windowName" value="SendAdHocMessage"/>
        <c:set scope="request" var="windowParms" value="fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no,directories=no,location=no,width=640,height=290,left=200,top=200"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="7" class="psMenuHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
</table>
