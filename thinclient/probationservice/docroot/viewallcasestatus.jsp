<%--
  - Title:       viewallcasestatus.jsp (jsp page fragment)
  -
  - Description: This file displays all case status
  -
  - Copyright:   Copyright (c) 2004
  - Company:     EDS
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
                                <fmt:message key="viewallcasestatus.title"/>
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
    <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
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
        <fmt:message key="caseno"/>
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
        <fmt:message key="type"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="notbefore"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="column.status"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

</tr>

<tr>
    <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
<c:forEach var="item" items="${requestScope.courts}">

    <tr>
        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="wfTableMainTop">
            <c:choose>
                <c:when test="${item.floating == 'true'}">
                    <fmt:message key="unassigned"/>
                </c:when>
                <c:otherwise>
                    <c:out value="${item.courtRoomName}"/>
                    <c:if test="${item.movedFromCourtRoomName != null && item.movedFromCourtRoomName != '' }">
                        <fmt:message key="movedfrom"/> <c:out value="${item.movedFromCourtRoomName}"/>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="wfTableMainTop">
            <c:out value="${item.caseNumber}"/>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="wfTableMainTop">
            <c:choose>
                <c:when test="${item.defendantName != null && item.defendantName != ''}">
                    <c:if test="${item.reportingRestricted == 'true'}">
                        <fmt:message key="asterix"/>
                    </c:if>
                    <c:out value="${item.defendantName.nameWithSurnameFirst}"/>
                </c:when>
                <c:otherwise>
                    <c:if test="${item.reportingRestricted == 'true'}">
                        <fmt:message key="asterix"/>
                    </c:if>
                    <c:out value="${item.caseTitle}"/>
                </c:otherwise>
            </c:choose>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="wfTableMainTop">
            <c:out value="${item.hearingDescription}"/>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="wfTableMainTop">
            <c:out value="${item.notBeforeTimeAsString}"/>
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
        <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
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

