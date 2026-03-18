<%--
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
  - $Revision: 1.6 $
  - $Log: menu.jsp,v $
  - Revision 1.6  2014/08/15 18:00:18  atwells
  - *** empty log message ***
  -
  - Revision 1.5  2014/08/04 23:58:37  atwells
  - Removed check for CPS user to allow some functionality to be tested whilst a fix is being developed.
  -
  - Revision 1.4  2006/05/04 10:18:41  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:57  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:39  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.9  2004/09/24 06:49:37  tzj8k5
  - CR65 - All Case Status Page ThinClient
  -
  - Revision 1.8  2004/03/15 13:12:30  xzmw8n
  - Increased size of instant message window and enabled resizable
  - Increased height of sms message window
  -
  - Revision 1.7  2003/10/02 16:25:24  xzmw8n
  - Increased the height of the Instant Message pop-up window (again)
  -
  - Revision 1.6  2003/09/30 16:17:35  xzmw8n
  - Increased the height of the Instant Message pop-up window
  -
  - Revision 1.5  2003/05/16 08:59:39  fz0n8j
  - Added logout button.
  -
  - Revision 1.4  2003/05/08 15:36:09  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.3  2003/05/02 15:33:54  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.2  2003/04/30 15:38:56  fz0n8j
  - Security fixes
  -
  - Revision 1.1  2003/03/27 11:01:23  fz0n8j
  - Added to cvs
  -
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
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="13" class="psMenuHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>

        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psMenu">
            <c:set scope="request" var="buttonFill" value="true"/>
            <c:set scope="request" var="buttonTextKey" value="menu.button.viewDailyList"/>
            <c:set scope="request" var="buttonRequestURL" value="/viewdailylist"/>
            <c:import url="${menuButtonURL}"/>
        </td>
        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psMenu">
            <c:set scope="request" var="buttonTextKey" value="menu.button.viewAllCourtStatus"/>
            <c:set scope="request" var="buttonRequestURL" value="/viewallcourtstatus"/>
            <c:import url="${menuButtonURL}"/>
        </td>
        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psMenu">
            <c:set scope="request" var="buttonTextKey" value="menu.button.viewAllCaseStatus"/>
            <c:set scope="request" var="buttonRequestURL" value="/viewallcasestatus"/>
            <c:import url="${menuButtonURL}"/>
        </td>
        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psMenu">
            <c:set scope="request" var="buttonTextKey" value="menu.button.viewSummaryByName"/>
            <c:set scope="request" var="buttonRequestURL" value="/viewsummarybyname"/>
            <c:import url="${menuButtonURL}"/>
        </td>
        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
<% if (request.getSession() != null) {
     if ((request.getSession().getAttribute("isCPS") != null) && (request.getSession().getAttribute("isCPS").equals("true"))) { // Check if user is a CPS user so that they can see these 2 menu options
%>
        <td class="psMenu">
            <c:set scope="request" var="buttonTextKey" value="menu.button.searchCases"/>
            <c:set scope="request" var="buttonRequestURL" value="/searchcases"/>
            <c:import url="${menuButtonURL}"/>
        </td>
        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psMenu">
            <c:set scope="request" var="buttonTextKey" value="menu.button.viewCaseSkeletons"/>
            <c:set scope="request" var="buttonRequestURL" value="/viewcaseskeletons"/>
            <c:import url="${menuButtonURL}"/>
        </td>
        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

<%	  }
	} 
%>
        <td class="psMenu">
            <c:set scope="request" var="buttonTextKey" value="menu.button.sendinstantmessage"/>
            <c:set scope="request" var="buttonRequestURL" value="javascript:void window.open('/Messaging/sendinstantmessage','SendInstantMessage','fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=yes,directories=no,location=no,width=740,height=520,left=200,top=200')"/>
            <c:import url="${menuButtonURL}"/>
        </td>
        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psMenu">
            <c:set scope="request" var="buttonTextKey" value="menu.button.sendadhocmessage"/>
            <c:set scope="request" var="buttonRequestURL" value="javascript:void window.open('/Messaging/sendadhocmessage','SendAdHocMessage','fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no,directories=no,location=no,width=640,height=350,left=200,top=200')"/>
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
        <td colspan="13" class="psMenuHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>
