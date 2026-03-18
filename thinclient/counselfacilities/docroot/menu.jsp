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
  - Author:      Edwrad CAwley, Xdevelopment LLP (2003)
  - $Revision: 1.5 $
  - $Log: menu.jsp,v $
  - Revision 1.5  2006/05/10 08:01:42  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Security Fix & Orders Clobs
  -
  - Revision 1.4  2006/05/04 10:18:37  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:54  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:19  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.5  2004/07/16 10:00:37  tzj8k5
  - 56238 - Connsel Facilities security issue - disable ctrl-h
  -
  - Revision 1.4  2003/10/29 11:30:27  xzmw8n
  - Refactored cousel facilities sign in
  -
  - Revision 1.3  2003/04/23 17:39:55  nz5zpz
  - made the urls context aware
  -
  - Revision 1.2  2003/04/23 16:05:50  nz5zpz
  - merged from devBranch-2b-030404
  -
  - Revision 1.1.2.4  2003/04/23 10:43:44  nz5zpz
  - updates
  -
  - Revision 1.1.2.3  2003/04/15 13:52:05  wz0htf
  - Enabled navigation
  -
  - Revision 1.1.2.2  2003/04/15 13:29:09  wz0htf
  - Updated to follow cf design
  -
  - Revision 1.1.2.1  2003/04/11 12:56:32  fz0n8j
  - Initial Version
  -
  --%>
<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<!-- set up key disable script-->
<script src="/Static/keyScript.js">
</script>

<%--
  - Get the page urls from the Pages resource bundle
  --%>

<fmt:bundle basename="Pages">
<fmt:message key="button" var="menuButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0"/>
<tr>
    <td colspan="11" class="psMenuHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>

    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psMenu">
        <c:set scope="request" var="buttonFill" value="true"/>
        <c:set scope="request" var="buttonTextKey" value="menu.button.one"/>
        <c:set scope="request" var="buttonRequestURL" value="./home"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu">
        <c:set scope="request" var="buttonTextKey" value="menu.button.two"/>
        <c:set scope="request" var="buttonRequestURL" value="./counselsigninoverview"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="7" class="psMenuHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
</table>
