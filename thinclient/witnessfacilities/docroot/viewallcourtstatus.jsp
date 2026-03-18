<%--
  - Title:       viewallcourtstatus.jsp (jsp page fragment)
  -
  - Description: This file displays all ccourt status (Whatever that means).
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.5 $
  - $Log: viewallcourtstatus.jsp,v $
  - Revision 1.5  2006/05/04 10:18:41  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.4  2005/12/15 15:36:08  tzj8k5
  - RFC 1447 - Amend jsp to pick up the Event details from Event Helper
  -
  - Revision 1.3  2005/04/27 08:26:57  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:40  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.9  2004/07/22 07:28:41  tzj8k5
  - 56247 - Display court log time
  -
  - Revision 1.8  2004/07/13 14:30:32  tzj8k5
  - 55636 - Thin Client All Court Status to use new flr
  -
  - Revision 1.7  2004/06/15 12:42:46  tzj8k5
  - court status message
  -
  - Revision 1.6  2004/02/03 08:58:00  tzj8k5
  - Amended condition for display of case number
  -
  - Revision 1.5  2004/01/15 11:07:41  xzmw8n
  - Refactored All Court Status page to only display the status for the most recent
  - event, bringing this more in line with public displays and internet web page.
  -
  - Revision 1.4  2003/05/08 15:36:09  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.3  2003/04/01 13:59:52  fz0n8j
  - Added actions again and modified jsps/properites.
  -
  - Revision 1.2  2003/04/01 11:30:51  fz0n8j
  - Formatting and new calls to passed data.
  -
  - Revision 1.1  2003/03/28 09:58:46  fz0n8j
  - Added witness details and court status.
  -
  -
<%--
  - The format tag lib is used to i18n messages  
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<fmt:bundle basename="Pages">
    <fmt:message key="button" var="menuButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
    <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
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
                                <fmt:message key="viewallcourtstatus.title"/>
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
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
        <c:choose>
            <c:when test="${requestScope.courts != null}">
<tr>
    <td colspan="17" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="courtnumber"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="casenumber"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="name"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="currentstatus"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

</tr>

<tr>
    <td colspan="17" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
<c:forEach var="item" items="${requestScope.courts}">

    <tr>
        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="wfTableMainTop">
            <c:out value="${item.courtRoomName}"/>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="wfTableMainTop">
            <c:if test="${item.caseNumber!='0'}">
                <c:out value="${item.caseNumber}"/>
            </c:if>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="wfTableMainTop">
            <c:forEach var="defendant" items="${item.defendantNames}">
                <c:out value="${defendant.name}"/>
                <br>
            </c:forEach>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="wfTableMainTop">
        <%-- Set up a page scope variable to hold the item value to be used by the EventHandler --%>
        <c:set var="pageItem" value="${item}" scope="page"/>
        <%= uk.gov.courtservice.xhibit.web.event.EventHandler.getEvent(pageContext.getAttribute("pageItem")) %>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="17" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
</c:forEach>
</c:when>
<c:otherwise>
    <tr>
        <td  class="psPageMessage" colspan="17">
            <fmt:message key="norecords"/>
        </td>
    </tr>
</c:otherwise>
</c:choose>
</table>
</td>
<td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
</table>

