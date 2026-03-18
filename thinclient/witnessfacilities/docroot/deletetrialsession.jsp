<%--
  - Title:       deletetrialdaylist.jsp (jsp page fragment)
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      David Duncan
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
                                    <fmt:message key="deletetrialsession.title"/>
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
                <tr>
                    <td>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="trialday"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">
                                    <c:out value="${requestScope.trialsession.dayNumber}"/>
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="trialdate"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">
                                    <fmt:formatDate value="${requestScope.trialsession.appearanceDate}" dateStyle="short"/>
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="trialsession"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">
                                    <c:choose>
                                        <c:when test="${requestScope.trialsession.sessionType == 'A'}">
                                            <fmt:message key="afternoon"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="morning"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                        </table>
                    </td>
                </tr>

            </table>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:choose>
                            <c:when test="${requestScope.errormessage != null}">
                                <c:out value="${requestScope.errormessage}"/>
                            </c:when>
                            <c:otherwise>
                                &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                            </c:otherwise>
                        </c:choose>
                    </td>
                <tr>
            </table>
            <table>
                <tr>
                    <td class="psFormError">
                        <c:if test="${requestScope.haswitnesses != null}">
                            <fmt:message key="confirmdeletetrialsession"/>
                        </c:if>
                    </td>
                </tr>
            </table>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td colspan="8" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageAction">
                                    <form class="psForm" action="<c:url value="/viewtrialsession"/>" method="post" name="cancel">
                                        <c:set scope="request" var="buttonTextKey" value="cancel"/>
                                        <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('cancel')"/>
                                        <c:import url="${menuButtonURL}"/>
                                        <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                        <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
                                        <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                    </form>
                                </td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageAction">
                                    <form action="<c:url value="/confirmdeletetrialsession"/>" method="post" name="smallform">
                                        <INPUT type="hidden" value="<c:out value="${requestScope.trialsession.dayNumber}"/>" name="trialday">
                                        <INPUT type="hidden" value="<c:out value="${requestScope.trialsession.appearanceDate}"/>" name="trialdate">
                                        <INPUT type="hidden" value="<c:out value="${requestScope.trialsession.sessionType}"/>" name="trialsession">
                                        <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                        <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                        <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
                                        <c:set scope="request" var="buttonTextKey" value="ok"/>
                                        <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('smallform')"/>
                                        <c:import url="${menuButtonURL}"/>
                                </td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="8" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            </form>
        </td>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

