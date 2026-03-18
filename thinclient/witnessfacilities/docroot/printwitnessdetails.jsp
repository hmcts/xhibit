<%--
  - Title:       printwitnessdetails.jsp (jsp page fragment)
  -
  - Description: This file prints witnessdetails.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: printwitnessdetails.jsp,v $
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
  - Revision 1.4  2003/05/08 15:36:09  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.3  2003/04/04 11:13:39  fz0n8j
  - Security Changes and Misc Fixs
  -
  - Revision 1.2  2003/04/04 08:41:35  fz0n8j
  - More pages now use database.
  -
  - Revision 1.1  2003/03/28 12:17:26  fz0n8j
  - Added print for witness details.
  -
  -
<%--
  - The format tag lib is used to i18n messages  
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%--
  - The html to be included in the main page
  --%>
<html>
<head>
    <META http-equiv="Pragma" content="no-cache"/>
    <META http-equiv="Expires" content="-1"/>
    <script src="/Static/util.js"></script>
    <title><fmt:message key="printwitnessdetails.title"/></title>
    <link href="/Static/style.css" rel="stylesheet" type="text/css"/>
</head>
<body bottommargin="5" topmargin="5" leftmargin="5" rightmargin="5" onload="window.print()">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
    <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td>
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
        <c:choose>
            <c:when test="${requestScope.caseDetail != null}">
<tr>
    <td colspan="21" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="court"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="defendantname"/>
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
        <fmt:message key="policeofficer"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="cpscaseworker"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

</tr>

<tr>
    <td colspan="21" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>

<tr>
    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
    <td class="wfTableMainTop">
        <c:out value="${requestScope.caseDetail.courtName}"/>
    </td>
    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
    <td class="wfTableMainTop">
        <c:forEach var="defendant" items="${requestScope.caseDetail.defendantNames}">
            <c:out value="${defendant}"/>
            <br>
        </c:forEach>
    </td>
    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
    <td class="wfTableMainTop">
        <c:out value="${requestScope.caseDetail.caseNumber}"/>
    </td>
    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
    <td class="wfTableMainTop">
        <c:out value="${requestScope.caseDetail.policeOfficerAttending}"/>
    </td>
    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
    <td class="wfTableMainTop">
        <c:out value="${requestScope.caseDetail.cpsCaseWorker}"/>
    </td>
    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="21" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
</c:when>
</c:choose>
</table>
</td>
<td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td>
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
        <c:choose>
            <c:when test="${requestScope.witnesses != null}">
<tr>
    <td colspan="29" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="name"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="status"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="age"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="dueat"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="arrived"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="released"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="totaltime"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
    
</tr>

<tr>
    <td colspan="29" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
<c:forEach var="item" items="${requestScope.witnesses}">

    <tr>
        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMain">
            <c:out value="${item.name}"/>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMain">
            <c:out value="${item.status}"/>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMain">
            <c:out value="${item.age}"/>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMain">
            <fmt:formatDate value="${item.dueAt}" dateStyle="short"/>&nbsp;<fmt:formatDate value="${item.dueAt}" pattern="HH:mm"/>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMain">
            <fmt:formatDate value="${item.arrived}" dateStyle="short"/>&nbsp;<fmt:formatDate value="${item.arrived}" pattern="HH:mm"/>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMain">
            <fmt:formatDate value="${item.released}" dateStyle="short"/>&nbsp;<fmt:formatDate value="${item.released}" pattern="HH:mm"/>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMain">
            <fmt:message key="witnesstotaltime">
                <fmt:param value="${item.totalTimeHours}"/>
                <fmt:param value="${item.totalTimeMinutes}"/>
            </fmt:message>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    </tr>
    <tr>
        <td colspan="29" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
</c:forEach>
</c:when>
<c:otherwise>
    <tr>
        <td  class="psPageMessage" colspan="33">
            <fmt:message key="nowitnesses"/>
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
</body>
</html>
