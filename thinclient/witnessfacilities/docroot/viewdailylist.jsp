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

  <% boolean varCPS = false; if (request.isUserInRole("XHBCPS")){varCPS=true;pageContext.setAttribute("varCPS",new Boolean(varCPS));}
                        %>

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
                            <td colspan="29" class="psTableHorizontalBorder" />
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
                            <td class="psTableHeaderSpace" />
                            <td class="psTableHeader" />
                            <td class="psTableHeaderSpace" />
                            <td class="psTableHeader" />
                        </tr>

                        <tr>
                            <td colspan="29" class="psTableHorizontalBorder" />
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
                                <td class="psTableMainSpace" />
                                <td width="12" class="wfTableMainTop">
                                    <c:if test="${(item.scheduled && item.witnessOnCase && item.issued) || (varCPS && item.scheduled && item.issued) || (varCPS && !item.scheduled)}">
                                        <form class="psForm" action="<c:url value="/maintainwitnessdetails"/>" method="post" name="editwitnessform<c:out value="${item.scheduledHearingId}"/>">
                                            <a href="javascript:psSubmitForm('editwitnessform<c:out value="${item.scheduledHearingId}"/>')"
                                                onMouseover="return psSetStatus('<fmt:message key="editwitness"/>')"
                                                onMouseout="return psSetStatus(' ')"><IMG src="/Static/images/editicon.gif"
                                                alt="<fmt:message key="editwitness"/>" border="0">
                                            </a>
                                            <input type="hidden" name="caseid" value="<c:out value="${item.caseId}"/>">
                                            <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                        </form>
                                    </c:if>
                                </td>
                                <td class="psTableMainSpace" />
                                <td class="psTableVerticalBorder" />
                            </tr>

                            <tr>
                                <td colspan="29" class="psTableHorizontalBorder" />
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




































<%--
                <c:forEach var="item" items="${requestScope.hearings}">

                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${item.hearing.courtRoomName}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${item.hearing.judge}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:forEach var="defendant" items="${item.hearing.defendants}">
                            <c:out value="${defendant}"/>
                            <br>
                        </c:forEach>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${item.hearing.case}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${item.hearing.hearingType}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <fmt:formatDate value="${item.hearing.notBeforeTime}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td width="12" class="wfTableMainTop">
                        <c:if test="${(item.schedule == 'true' && item.witnesses != null) || varCPS}">
                            <form class="psForm" action="<c:url value="/maintainwitnessdetails"/>" method="post" name="editwitnessform<c:out value="${item.id}"/>">
                                <a href="javascript:psSubmitForm('editwitnessform<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="editwitness"/>')"
                    onMouseout="return psSetStatus(' ')"><IMG src="/Static/images/editicon.gif" alt="<fmt:message key="editwitness"/>" border="0"></a>
                                <input type="hidden" name="caseid" value="<c:out value="${item.hearing.caseId}"/>">
                                <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                            </form>
                        </c:if>
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
                    <td  class="psPageMessage" colspan="29">
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

--%>