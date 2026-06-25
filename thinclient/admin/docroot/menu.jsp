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
  - Author:      William Fardell, Xdevelopment LLP (2003)
  - $Revision: 1.1 $
  - $Log: menu.jsp,v $
  - Revision 1.1  2014/08/15 18:01:34  atwells
  - *** empty log message ***
  -
  - Revision 1.4  2006/05/04 10:18:38  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login
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
    <fmt:message key="button" var="menuButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0"/>
<tr>
    <td colspan="17" class="psMenuHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>

    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psMenu" nowrap >
        <c:set scope="request" var="buttonFill" value="true"/>
        <c:set scope="request" var="buttonTextKey" value="menu.button.terminalAdmin"/>
        <c:set scope="request" var="buttonRequestURL" value="/terminals/index.jsp"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.refreshResyncCase"/>
        <c:set scope="request" var="buttonRequestURL" value="/caseresync/caseResyncAdministration.jsp"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.setUnauthCases"/>
        <c:set scope="request" var="buttonRequestURL" value="unauthorisedcases/unauthorisedCasesAdministration.jsp"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.internetWebPages"/>
        <c:set scope="request" var="buttonRequestURL" value="/internetwebpages/listInternetWebPages.jsp"/>
        <c:import url="${menuButtonURL}"/>
    </td>
        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
    
    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.crimeMigration"/>
        <c:set scope="request" var="buttonRequestURL" value="/crimemigration/bulkUpdate.jsp"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
    
	<td class="psMenu">
		<c:set scope="request" var="buttonTextKey" value="logout.button"/>
		<c:set scope="request" var="buttonRequestURL" value="/logout"/>
		<c:import url="${menuButtonURL}"/>
		<c:set scope="request" var="buttonFill" value="false"/>
    </td>
	<td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="17" class="psMenuHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
</table>
