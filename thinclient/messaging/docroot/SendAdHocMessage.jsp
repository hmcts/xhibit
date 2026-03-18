<%--
  - Title:       sendadhocmessage.jsp (jsp page fragment)
  -
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
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
    <fmt:message key="button" var="buttonURL"/>
    <fmt:message key="closewindowbutton" var="closeButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<%
String[] deviceTypes=(String[])request.getAttribute("adHocdeviceTypes");
%>
<table width="100%" cellpadding="0" cellspacing="0" border="0"/>
<tr>
    <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    <td>
        <form name="sendadhocmessage" action="<c:url value="/adhocmessagesent"/>" method="post">
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
                                    <fmt:message key="sendadhocmessage.title"/>
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
                                <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="device"/>
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <c:choose>
                                    <c:when test="${adHocMessageBean.deviceType.errorValue == null}">
                                        <c:set var="deviceTypeText" value="${adHocMessageBean.deviceType.value}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="deviceTypeText" value="${adHocMessageBean.deviceType.errorValue}"/>
                                    </c:otherwise>
                                </c:choose>
                                <td class="psFormValue">
                                    <%-- <c:out value="${adHocMessageBean.deviceType.value}"/> --%>
                                    <select name="deviceType">
                                        <c:forEach items="${adHocdeviceTypes}" var="deviceType">
                                        <option value="<c:out value="${deviceType}"/>"
                                             <c:if test="${deviceType == deviceTypeText}"> SELECTED</c:if>>
                                             <c:out value="${deviceType}"/>
                                        </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError" width="100">
                                    <c:if test="${adHocMessageBean.deviceType.errorMessageKey != null}">
                                        <fmt:message key="${adHocMessageBean.deviceType.errorMessageKey}"/>
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
                                    <fmt:message key="number"/>
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <c:choose>
                                    <c:when test="${adHocMessageBean.number.errorValue == null}">
                                        <c:set var="numberText" value="${adHocMessageBean.number.value}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="numberText" value="${adHocMessageBean.number.errorValue}"/>
                                    </c:otherwise>
                                </c:choose>
                                <td class="psFormValue">
                                    <input size="30" maxlength="20" type="text" name="number" value="<c:out value="${numberText}"/>">
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError">
                                    <c:if test="${adHocMessageBean.number.errorMessageKey != null}">
                                        <fmt:message key="${adHocMessageBean.number.errorMessageKey}"/>
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
                                    <fmt:message key="message"/>
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <c:choose>
                                    <c:when test="${adHocMessageBean.message.errorValue == null}">
                                        <c:set var="messageText" value="${adHocMessageBean.message.value}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="messageText" value="${adHocMessageBean.message.errorValue}"/>
                                    </c:otherwise>
                                </c:choose>
                                <td class="psFormValue">
                                    <textarea rows="5" cols="30" maxlength="120" type="textarea" name="message"><c:out value="${messageText}" escapeXml="false"/></textarea>
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError">
                                    <c:if test="${adHocMessageBean.message.errorMessageKey != null}">
                                        <fmt:message key="${adHocMessageBean.message.errorMessageKey}"/>
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
                                    <c:set scope="request" var="buttonTextKey" value="sendadhocmessage.form.submit"/>
                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('sendadhocmessage')"/>
                                    <c:import url="${buttonURL}"/>
                                </td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageAction">
                                    <c:set scope="request" var="buttonTextKey" value="sendadhocmessage.form.close"/>
                                    <c:import url="${closeButtonURL}"/>
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
<input type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">
        </form>
    </td>
    <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
</table>

