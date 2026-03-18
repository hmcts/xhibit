<%--
  - Title:       viewsummarybyname.jsp (jsp page fragment)
  -
  - Description: This file displays summaries by name.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.5 $
--%>
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
<table width="100%" cellpadding="0" cellspacing="0" border="0"/>
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
                                <fmt:message key="viewsummarybyname.title"/>
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
        <table width="100%" cellpadding="0" cellspacing="0" border="0"/>
        <c:choose>
            <c:when test="${requestScope.defendants != null}">
<tr>
    <td colspan="13" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
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
        <fmt:message key="incourt"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader">
        <fmt:message key="notbefore"/>
    </td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

</tr>

<tr>
    <td colspan="13" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>

<c:forEach var="hearing" items="${requestScope.defendants}">
    <c:forEach var="defendant" items="${hearing.defendantNames}">
        <tr>
            <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

            <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
            <td class="psTableMain">
            <c:out value="${defendant}"/>
            </td>
            <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

            <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

            <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
            <td class="psTableMain">
                <c:choose>
                    <c:when test="${hearing.floating != 'true'}">
                        <c:out value="${hearing.courtRoomName}"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="print.isFloating"/>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

            <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

            <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
            <td class="psTableMain">
            <fmt:formatDate value="${hearing.notBeforeTime}"  pattern="HH:mm"/>
            </td>
            <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

            <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="13" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
    </c:forEach>
 </c:forEach>
</c:when>
<c:otherwise>
    <tr>
        <td  class="psPageMessage" colspan="13">
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

