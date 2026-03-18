<%--
  - Title:       viewwitnessbycaseref.jsp (jsp page fragment)
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      David Duncan
  - $Revision: 1.4 $
  - $Log: viewwitnessbycaseref.jsp,v $
  - Revision 1.4  2006/05/04 10:18:41  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:58  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:41  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.2  2003/05/08 15:36:09  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.1  2003/04/02 15:40:49  hzf3bb
  - *** empty log message ***
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
<%--        <form name="viewwitnessbycaseref" action="<c:url value="/viewwitnessbycaseref"/>" method="post">--%>
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
                                    <fmt:message key="viewwitnessbycaseref.title"/>
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
                                    <fmt:message key="casenumber"/>:
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
<%--                                <c:choose>--%>
<%--                                    <c:when test="${probationServiceDetails.officeName.errorValue == null}">--%>
<%--                                        <c:set var="officeNameText" value="${probationServiceDetails.officeName.value}"/>--%>
<%--                                    </c:when>--%>
<%--                                    <c:otherwise>--%>
<%--                                        <c:set var="officeNameText" value="${probationServiceDetails.officeName.errorValue}"/>--%>
<%--                                    </c:otherwise>--%>
<%--                                </c:choose>--%>
                                <td class="psFormValue">
                                    <input size="10" maxlength="30" type="text" name="casenumber" value="">
                                </td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError">
<%--                                    <c:if test="${probationServiceDetails.officeName.errorMessageKey != null}">--%>
<%--                                        <fmt:message key="${probationServiceDetails.officeName.errorMessageKey}"/>--%>
<%--                                    </c:if>--%>
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
                                <c:set scope="request" var="buttonTextKey" value="ok"/>
                                <c:set scope="request" var="buttonRequestURL" value="/viewwitnessbycaseref"/>
                                <c:import url="${menuButtonURL}"/>
                            </td>
                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
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
<%--<input type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">--%>
<%--        </form>--%>
    </td>
    <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
</table>
