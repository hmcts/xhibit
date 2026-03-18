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
  - $Revision: 1.4 $
  - $Log: menu.jsp,v $
  - Revision 1.4  2006/05/04 10:18:38  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:55  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:25  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.16  2004/09/24 06:51:15  tzj8k5
  - CR65 - All Case Status Page ThinClient
  -
  - Revision 1.15  2004/03/15 13:12:48  xzmw8n
  - Increased size of instant message window and enabled resizable
  - Increased height of sms message window
  -
  - Revision 1.14  2003/10/07 10:28:32  xzmw8n
  - bug fix 54643: shortened some button names
  -
  - Revision 1.13  2003/10/02 16:25:23  xzmw8n
  - Increased the height of the Instant Message pop-up window (again)
  -
  - Revision 1.12  2003/09/30 16:16:04  xzmw8n
  - Increased the height of the Instant Message pop-up window
  -
  - Revision 1.11  2003/05/16 08:59:38  fz0n8j
  - Added logout button.
  -
  - Revision 1.10  2003/05/09 08:05:08  rz3jq5
  - Added correct menu buttons.
  -
  - Revision 1.9  2003/05/08 15:35:37  fz0n8j
  - Bug fix (/> on table elements) and formatting
  -
  - Revision 1.8  2003/04/30 10:03:01  fz0n8j
  - Added messaging to probation service. (EFC)
  -
  - Revision 1.7  2003/03/24 08:23:25  fz0n8j
  - Added view daily list, and public display
  -
  - Revision 1.6  2003/03/19 21:54:15  fz0n8j
  - Allow context free
  -
  - Revision 1.5  2003/03/17 11:31:51  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.4  2003/03/11 16:31:42  fz0n8j
  - Added CVS log comments - ecawley
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
        <c:set scope="request" var="buttonTextKey" value="menu.button.psrRequest"/>
        <c:set scope="request" var="buttonRequestURL" value="/viewunissuedpsrrequestsummaries"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.psrRecipient"/>
        <c:set scope="request" var="buttonRequestURL" value="/viewrecipientsummaries"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.probationLiaisonDept"/>
        <c:set scope="request" var="buttonRequestURL" value="/editprobationdetails"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.viewDailyList"/>
        <c:url value="/viewdailylist" var="viewDailyListUrl" />
        <c:set scope="request" var="buttonRequestURL" value="/viewdailylist"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.viewAllCourtStatus"/>
        <c:set scope="request" var="buttonRequestURL" value="/viewallcourtstatus"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.viewAllCaseStatus"/>
        <c:set scope="request" var="buttonRequestURL" value="/viewallcasestatus"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.viewSummaryByName"/>
        <c:set scope="request" var="buttonRequestURL" value="/viewsummarybyname"/>
        <c:import url="${menuButtonURL}"/>
    </td>
     <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.sendinstantmessage"/>
        <c:set scope="request" var="buttonRequestURL" value="javascript:void window.open('/Messaging/sendinstantmessage','SendInstantMessage','fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=yes,directories=no,location=no,width=740,height=520,left=200,top=200')"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psMenu" nowrap>
        <c:set scope="request" var="buttonTextKey" value="menu.button.sendadhocmessage"/>
        <c:set scope="request" var="buttonRequestURL" value="javascript:void window.open('/Messaging/sendadhocmessage','SendAdHocMessage','fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no,directories=no,location=no,width=640,height=350,left=200,top=200')"/>
        <c:import url="${menuButtonURL}"/>
    </td>
    <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psMenu" nowrap>
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
