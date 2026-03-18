<%--
  - Title:       edittrialsession.jsp (jsp page fragment)
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
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <form name="edittrialsession" action="<c:url value="/saveedittrialsession"/>" method="post">
              <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                          <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
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
                                        <fmt:message key="edittrialsession.title"/>
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
                                    <td colspan="33" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="trialday"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <c:choose>
                                            <c:when test="${requestScope.trialsession.dayNumber != null}">
                                                <c:out value="${requestScope.trialsession.dayNumber}"/>
                                                <INPUT type="hidden" value="<c:out value="${requestScope.trialsession.dayNumber}"/>" name="trialday">
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${requestScope.values.trialday}"/>
                                                <INPUT type="hidden" value="<c:out value="${requestScope.values.trialday}"/>" name="trialday">
                                            </c:otherwise>
                                        </c:choose>

                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.trialday != null}">
                                            <fmt:message key="${requestScope.errors.trialday}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="trialdate"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <c:choose>
                                            <c:when test="${requestScope.trialsession.appearanceDate != null}">
                                                <fmt:formatDate value="${requestScope.trialsession.appearanceDate}" dateStyle="short" var="formatteddate"/>
                                                <input size="10" maxlength="8" type="text" name="trialdate" value="<c:out value="${formatteddate}"/>">
                                            </c:when>
                                            <c:otherwise>
                                                <%--                                      <fmt:formatDate value="${requestScope.values.trialdatevalue}" dateStyle="short" var="formattederror"/>--%>
                                                <input size="10" maxlength="8" type="text" name="trialdate" value="<c:out value="${requestScope.values.trialdate}"/>">
                                            </c:otherwise>
                                        </c:choose>

                                        <fmt:message key="dateformat"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.trialdate != null}">
                                            <fmt:message key="${requestScope.errors.trialdate}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="trialsession"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">

                                        <c:if test="${requestScope.trialsession != null}">
                                            <c:choose>
                                                <c:when test="${requestScope.trialsession.sessionType == 'A'}">
                                                    <fmt:message key="afternoon"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.trialsession.sessionType}"/>" name="trialsession">
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:message key="morning"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.trialsession.sessionType}"/>" name="trialsession">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>

                                        <c:if test="${requestScope.trialsession == null}">
                                            <c:choose>
                                                <c:when test="${requestScope.values.trialsession == 'A'}">
                                                    <fmt:message key="afternoon"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.values.trialsession}"/>" name="trialsession">
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:message key="morning"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.values.trialsession}"/>" name="trialsession">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>

                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">

                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
                <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
            </form>
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
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td colspan="7" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                        </tr>
                                        <tr>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/viewtrialsession"/>" method="post" name="cancel">
                                                    <c:set scope="request" var="buttonTextKey" value="cancel"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('cancel')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                    <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <c:set scope="request" var="buttonTextKey" value="save"/>
                                                <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('edittrialsession')"/>
                                                <c:import url="${menuButtonURL}"/>

                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="7" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
            </table>

        </td>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>
