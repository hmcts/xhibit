<%--
  - Title:       viewdailylist.jsp (jsp page fragment)
  -
  - Description: This file displays daily lists.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      David Duncan (2003)
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
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="psTableHorizontalSpace" />
    </tr>

    <tr>
        <td class="psTableVerticalSpace" />
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td colspan="3" class="psPageTitleHorizontalSpace" />
                            </tr>

                            <tr>
                                <td class="psPageTitleVerticalSpace" />
                                <td class="psPageTitle"><fmt:message key="viewdailylist.title"/></td>
                                <td class="psPageTitleVerticalSpace" />
                            </tr>

                            <tr>
                                <td colspan="3" class="psPageTitleHorizontalSpace" />
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <c:choose>
                    <c:when test="${requestScope.hearings != null}">
                        <tr>
                            <td colspan="25" class="psTableHorizontalBorder" />
                        </tr>

                        <tr>
                            <td class="psTableVerticalBorder" />
                            <td class="psTableHeaderSpace" />
                            <td class="psTableHeader"><fmt:message key="court"/></td>
                            <td class="psTableHeaderSpace" />
                            <td class="psTableVerticalBorder" />
                            <td class="psTableHeaderSpace" />
                            <td class="psTableHeader"><fmt:message key="judge"/></td>
                            <td class="psTableHeaderSpace" />
                            <td class="psTableVerticalBorder" />
                            <td class="psTableHeaderSpace" />
                            <td class="psTableHeader"><fmt:message key="name"/></td>
                            <td class="psTableHeaderSpace" />
                            <td class="psTableVerticalBorder" />
                            <td class="psTableHeaderSpace" />
                            <td class="psTableHeader"><fmt:message key="caseno"/></td>
                            <td class="psTableHeaderSpace" />
                            <td class="psTableVerticalBorder" />
                            <td class="psTableHeaderSpace" />
                            <td class="psTableHeader"><fmt:message key="type"/></td>
                            <td class="psTableHeaderSpace" />
                            <td class="psTableVerticalBorder" />
                            <td class="psTableHeaderSpace" />
                            <td class="psTableHeader"><fmt:message key="notbefore"/></td>
                            <td class="psTableHeaderSpace" />
                            <td class="psTableVerticalBorder" />
                        </tr>

                        <tr>
                            <td colspan="25" class="psTableHorizontalBorder" />
                        </tr>

                        <c:forEach var="item" items="${requestScope.hearings}">
                            <tr>
                                <td class="psTableVerticalBorder" />
                                <td class="psTableMainSpace" />
                                <td class="wfTableMainTop">
				    				<c:choose>
                                        <c:when test="${item.floating != 'true'}">
                                            <c:out value="${item.courtRoomName}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="print.isFloating"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="psTableMainSpace" />
                                <td class="psTableVerticalBorder" />
                                <td class="psTableMainSpace" />
                                <td class="wfTableMainTop"><c:out value="${item.judgeName}"/></td>
                                <td class="psTableMainSpace" />
                                <td class="psTableVerticalBorder" />
                                <td class="psTableMainSpace" />
                                <td class="wfTableMainTop">
                                    <c:forEach var="defendant" items="${item.defendantNames}">
                                        <c:out value="${defendant}"/><br>
                                    </c:forEach>
                                </td>
                                <td class="psTableMainSpace" />
                                <td class="psTableVerticalBorder" />
                                <td class="psTableMainSpace" />
                                <td class="wfTableMainTop"><c:out value="${item.case}"/></td>
                                <td class="psTableMainSpace" />
                                <td class="psTableVerticalBorder" />
                                <td class="psTableMainSpace" />
                                <td class="wfTableMainTop"><c:out value="${item.hearingType}"/></td>
                                <td class="psTableMainSpace" />
                                <td class="psTableVerticalBorder" />
                                <td class="psTableMainSpace" />
                                <td class="wfTableMainTop"><fmt:formatDate value="${item.notBeforeTime}" pattern="HH:mm"/></td>
                                <td class="psTableMainSpace" />
                                <td class="psTableVerticalBorder" />
                            </tr>

                            <tr>
                                <td colspan="25" class="psTableHorizontalBorder" />
                            </tr>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td  class="psPageMessage" colspan="25"><fmt:message key="norecords"/></td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </table>
        </td>
        <td class="psTableVerticalSpace" />
    </tr>

    <tr>
        <td colspan="3" class="psTableHorizontalSpace" />
    </tr>
</table>