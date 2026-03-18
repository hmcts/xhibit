<%--
  - Title:       addtrialsession.jsp (jsp page fragment)
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      David Duncan
  - $Revision: 1.4 $
  - $Log: addtrialsession.jsp,v $
  - Revision 1.4  2006/05/04 10:18:40  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:57  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:37  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.9  2004/04/21 14:17:44  fz1f7w
  - Changed trial day input field to accept 2 characters not 3
  -
  - Revision 1.8  2004/04/14 12:55:47  tzj8k5
  - Amend field length sizes to only allow 8 characters max
  -
  - Revision 1.7  2004/03/31 11:52:52  tzj8k5
  - Add page source parameter to set up navigation between the pages
  -
  - Revision 1.6  2004/02/25 11:25:01  tzj8k5
  - set up pagesource parameter for navigation
  -
  - Revision 1.5  2003/10/14 14:04:09  xzmw8n
  - Added tokens and checks to identify and reject duplicate form submissions
  -
  - Revision 1.4  2003/05/08 15:36:07  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.3  2003/05/02 16:50:13  fz0n8j
  - corrected merge
  -
  - Revision 1.1.2.2  2003/04/24 16:33:08  hzf3bb
  - *** empty log message ***
  -
  - Revision 1.1.2.1  2003/04/22 17:35:40  hzf3bb
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
            <form name="addtrialsession" action="<c:url value="/inserttrialsession"/>" method="post">
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
                                        <fmt:message key="addtrialsession.title"/>
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
                                    <td colspan="17" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="trialday"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <input size="2" maxlength="2" type="text" name="trialday" value="<c:out value="${requestScope.values.trialday}"/>">
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
                                        <input size="10" maxlength="8" type="text" name="trialdate" value="<c:out value="${requestScope.values.trialdate}"/>">
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
                                        <select name="trialsession">
                                            <option value="M">
                                            <fmt:message key="morning"/>
                                            </option>
                                            <c:choose>
                                                <c:when test="${requestScope.values.trialsession == 'A'}">
                                                    <option value="A" selected>
                                                    <fmt:message key="afternoon"/>
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="A">
                                                    <fmt:message key="afternoon"/>
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">

                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>

                        <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psFormError">

                        </td>
                    </tr>
                </table>
        </td>
    </tr>
</table>
<INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
</form>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
        <td class="psFormError">
            <c:choose>
                <c:when test="${requestScope.adderror != null}">
                    <c:out value="${requestScope.adderror}"/>
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
                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('addtrialsession')"/>
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
