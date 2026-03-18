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
            <form name="witnesssigninedit" action="<c:url value="/witnesssigninedit"/>" method="post">
               <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
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
                                        <fmt:message key="witnesssignin.title"/>
                                        <c:if test="${statusKey != null}">
                                            : <fmt:message key="${statusKey}"/>
                                        </c:if>
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
                                        <fmt:message key="name"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormValue">
                                        <c:out value="${requestScope.witness.name}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError"></td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="status"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormValue">
                                        <c:out value="${requestScope.witness.status}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError"></td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="age"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormValue">
                                        <c:choose>
                                            <c:when test="${requestScope.witness.age == '-1'}">
                                               <input type="hidden" value="<c:out value=""/>" name="witnessage">
                                            </c:when>
                                            <c:otherwise>
                                               <input type="hidden" value="<c:out value="${requestScope.witness.age}"/>" name="witnessage">
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError"></td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="expected"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormValue">
                                        <fmt:formatDate value="${requestScope.witness.expected}" pattern="HH:mm"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError"></td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="arrived"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormValue">
                                        <input size="5" maxlength="5" type="text" name="arrived" value="<c:choose><c:when test="${requestScope.errors.arrivedErrorValue == null}"><fmt:formatDate value="${requestScope.witness.arrived}" pattern="HH:mm"/></c:when><c:otherwise><c:out value="${requestScope.errors.arrivedErrorValue}"/></c:otherwise></c:choose>">
                                        <fmt:message key="hhmm"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.arrivedError != null}">
                                            <fmt:message key="${requestScope.errors.arrivedError}"/>
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
                                        <fmt:message key="released"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormValue">
                                        <fmt:formatDate value="${requestScope.witness.released}" pattern="HH:mm"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError"></td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="devicenumber"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormValue">
                                        <select name="device">
                                            <option value="-">-</option>
                                            <c:forEach var="device" items="${requestScope.witnessref.pagerNetworks}">
                                                <option value="<c:out value="${device}"/>"
                                            <c:choose>
                                                <c:when test="${requestScope.errors.deviceErrorValue == null}">
                                                    <c:choose>
                                                        <c:when test="${requestScope.witness.pagerNetwork == device}">
                                                            SELECTED
                                                        </c:when>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${requestScope.errors.deviceErrorValue == device}">
                                                            SELECTED
                                                        </c:when>
                                                    </c:choose>
                                                </c:otherwise>
                                                </c:choose>
                                                >
                                                <c:out value="${device}"/>
                                                </option>
                                            </c:forEach>
                                        </select>

                                        <input size="11" maxlength="11" type="text" name="number" value="<c:if test="${requestScope.errors.numberErrorValue != null}"><c:out value="${requestScope.errors.numberErrorValue}"/></c:if>">
                                    </td>

                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.numberError != null}">
                                            <fmt:message key="${requestScope.errors.numberError}"/>
                                        </c:if>
                                        <c:if test="${requestScope.errors.deviceError != null}">
                                            <fmt:message key="${requestScope.errors.deviceError}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>

                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError" colspan="5">
                                        <c:if test="${requestScope.witness.pagerNumber != null || requestScope.witness.mobileNumber != null}">
                                            <fmt:message key="numberentered"/>
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
                                        <fmt:message key="notes"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormValue" colspan="3"><TEXTAREA name="notes" cols="80" rows="2" width="95%" wrap><c:choose><c:when test="${requestScope.errors.notesErrorValue == null}"><c:out value="${requestScope.witness.notes}"/></c:when><c:otherwise><c:out value="${requestScope.errors.notesErrorValue}"/></c:otherwise></c:choose></TEXTAREA></td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError" colspan="3">
                                        <c:if test="${requestScope.errors.notesError != null}">
                                            <fmt:message key="${requestScope.errors.notesError}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <input type="hidden" value="<c:out value="${requestScope.witness.id}"/>" name="witnessid">
                <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
            </form>
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
                                                <form class="psForm" action="<c:url value="/maintainwitnessdetails"/>" method="post" name="back">
                                                    <c:set scope="request" var="buttonTextKey" value="back"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('back')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <input type="hidden" name="caseid" value="<c:out value="${requestScope.caseid}"/>">
                                                     <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                </form> 
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <c:set scope="request" var="buttonTextKey" value="save"/>
                                                <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('witnesssigninedit')"/>
                                                <c:import url="${menuButtonURL}"/>
                                            </td>
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
