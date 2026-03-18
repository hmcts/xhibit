<%--
  - Title:       deletewitness.jsp (jsp page fragment)
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      David Duncan
  - $Revision: 1.4 $
  - $Log: deletewitness.jsp,v $
  - Revision 1.4  2006/05/04 10:18:40  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:57  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:38  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.8  2004/09/29 09:20:12  tzj8k5
  - PRE 297 - Amend JSP's so that they do not reference the application name directly
  -
  - Revision 1.7  2004/04/19 14:39:28  tzj8k5
  - Navigation when deleting a witness
  -
  - Revision 1.6  2004/03/26 13:04:47  tzj8k5
  - Formatting of date and time to keep consistent with rest of Witness
  -
  - Revision 1.5  2003/10/14 14:04:10  xzmw8n
  - Added tokens and checks to identify and reject duplicate form submissions
  -
  - Revision 1.4  2003/05/08 15:36:08  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.3  2003/04/30 11:43:58  qzd3k3
  - Merge from dev branch.
  -
  - Revision 1.2.2.3  2003/04/30 11:25:36  fz0n8j
  - Commited before merge
  -
  - Revision 1.2.2.2  2003/04/25 15:29:15  hzf3bb
  - no message
  -
  - Revision 1.2.2.1  2003/04/22 17:35:40  hzf3bb
  - *** empty log message ***
  -
  - Revision 1.2  2003/04/02 15:40:36  hzf3bb
  - *** empty log message ***
  -
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
                                    <fmt:message key="deletewitness.title"/>
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
                                    <fmt:message key="casetype"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">
                                    <c:out value="${requestScope.casedetail.caseType}"/>
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
                                    <c:out value="${requestScope.trialsession.dayNumber}"/>
                                    <c:if test="${requestScope.trialsession.appearanceDate != null}">
                                        -
                                        <fmt:formatDate value="${requestScope.trialsession.appearanceDate}" pattern="dd/MM/yy"/>
                                    </c:if>
                                    -
                                    <c:out value="${requestScope.trialsession.sessionType}"/>
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
                                    <fmt:message key="witnessname"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">
                                    <c:out value="${requestScope.witnessdetail.name}"/>
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
                                    <fmt:message key="witnessstatus"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">
                                    <c:choose>
                                        <c:when test="${requestScope.witnessdetail.status != null}">
                                            <c:out value="${requestScope.witnessdetail.status}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="NA"/>
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
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="expectedarrivaltime"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">
                                    <fmt:formatDate value="${requestScope.witnessdetail.expected}" pattern="HH:mm"/>
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
                                    <fmt:message key="notes"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">
                                    <c:choose>
                                        <c:when test="${requestScope.witnessdetail.status != null}">
                                            <c:out value="${requestScope.witnessdetail.notes}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="NA"/>
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
                    <td>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td colspan="8" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageAction">
                                    <form class="psForm" action="./<c:out value="${requestScope.pagesource}"/>" method="post" name="cancel">
                                        <c:set scope="request" var="buttonTextKey" value="cancel"/>
                                        <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('cancel')"/>
                                        <c:import url="${menuButtonURL}"/>
                                        <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                        <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                    </form>
                                </td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageAction">
                                    <form action="<c:url value="/confirmdeletewitness"/>" method="post" name="smallform">
                                        <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                        <INPUT type="hidden" value="<c:out value="${requestScope.witnessdetail.id}"/>" name="id">
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

