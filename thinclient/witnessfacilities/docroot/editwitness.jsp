<%--
  - Title:       editwitness.jsp (jsp page fragment)
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      David Duncan
  - $Revision: 1.5 $
  - $Log: editwitness.jsp,v $
  - Revision 1.5  2006/05/04 10:18:40  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.4  2006/02/09 12:19:10  xztnfq
  - Change: PR 58315
  - Comment: Error when signing in a witness when logged on to Witness Services as WS user
  -
  - Revision 1.3  2005/04/27 08:26:57  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:38  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.12  2005/03/03 15:52:35  tzj8k5
  - PRE 330 - Witness Facilities ThinClient - Enforce Witness Age on edit and signin
  -
  - Revision 1.11  2004/09/29 09:20:12  tzj8k5
  - PRE 297 - Amend JSP's so that they do not reference the application name directly
  -
  - Revision 1.10  2004/07/15 12:23:22  tzj8k5
  - 56126 - Witness age on the thinclient
  -
  - Revision 1.9  2004/02/20 12:52:25  xzmw8n
  - Added attribute to indicate the intial calling page of the action
  -
  - Revision 1.8  2004/02/20 09:52:55  xzmw8n
  - Added attribute to indicate the intial calling page of the action
  -
  - Revision 1.7  2003/10/14 14:04:10  xzmw8n
  - Added tokens and checks to identify and reject duplicate form submissions
  -
  - Revision 1.6  2003/05/09 15:52:10  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.5  2003/05/08 15:36:08  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.4  2003/05/02 16:57:22  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.3  2003/04/30 11:43:58  qzd3k3
  - Merge from dev branch.
  -
  - Revision 1.2.2.3  2003/04/30 11:25:37  fz0n8j
  - Commited before merge
  -
  - Revision 1.2.2.2  2003/04/25 15:29:15  hzf3bb
  - no message
  -
  - Revision 1.2.2.1  2003/04/22 17:35:40  hzf3bb
  - *** empty log message ***
  -
  - Revision 1.2  2003/04/02 15:40:35  hzf3bb
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
            <form name="editwitness" action="<c:url value="/saveeditwitness"/>" method="post">
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
                                        <fmt:message key="editwitness.title"/>
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
                                        <fmt:message key="casetype"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <c:out value="${requestScope.casedetail.caseType}"/>
                                        <INPUT type="hidden" value="<c:out value="${requestScope.casedetail.caseType}"/>" name="casetype">
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">

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

                                    <c:choose>
                                        <c:when test="${requestScope.values.trialsession == null}">
                                            <c:set var="trialsession" value="${requestScope.trialsession.id}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="trialsession" value="${requestScope.values.trialsession.id}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <select name="trialsession">
                                            <option value="-1">-</option>
                                            <c:forEach var="item" items="${requestScope.ts}">
                                                <option value="<c:out value="${item.id}"/>"
                                                <c:if test="${item.id == trialsession}">SELECTED
                                                </c:if>
                                                >
                                                <c:out value="${item.dayNumber}"/>
                                                -
                                                <fmt:formatDate value="${item.appearanceDate}" dateStyle="short"/>
                                                -
                                                <c:out value="${item.sessionType}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.trialsession != null}">
                                            <fmt:message key="${requestScope.errors.trialsession}"/>
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
                                        <fmt:message key="witnessname"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <c:choose>
                                            <c:when test="${requestScope.values.witnessname != null}">
                                                <INPUT type="text" value="<c:out value="${requestScope.values.witnessname}"/>" name="witnessname">
                                            </c:when>
                                            <c:otherwise>
                                                <INPUT type="text" value="<c:out value="${requestScope.witnessdetail.name}"/>" name="witnessname">
                                            </c:otherwise>
                                        </c:choose>

                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.witnessname != null}">
                                            <fmt:message key="${requestScope.errors.witnessname}"/>
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
                                        <fmt:message key="witnessstatus"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${requestScope.values.witnessstatus == null}">
                                            <c:set var="status" value="${requestScope.witnessdetail.status}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="status" value="${requestScope.values.witnessstatus}"/>
                                        </c:otherwise>
                                    </c:choose>

                                    <td class="psFormValue">
                                        <select name="witnessstatus">
                                            <option
                                            <c:if test="${status == 'Ordinary'}">SELECTED
                                            </c:if>
                                            >
                                            <fmt:message key = "ordinary"/>
                                            </option> <option
                                            <c:if test="${status == 'Expert'}">SELECTED
                                            </c:if>
                                            >
                                            <fmt:message key = "expert"/>
                                            </option> <option
                                            <c:if test="${status == 'Interpreter'}">SELECTED
                                            </c:if>
                                            >
                                            <fmt:message key = "interpreter"/>
                                            </option> <option
                                            <c:if test="${status == 'Juvenile'}">SELECTED
                                            </c:if>
                                            >
                                            <fmt:message key = "juvenile"/>
                                            </option> <option
                                            <c:if test="${status == 'Police Officer'}">SELECTED
                                            </c:if>
                                            >
                                            <fmt:message key = "policeofficer"/>
                                            </option>
                                        </select>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.witnessstatus != null}">
                                            <fmt:message key="${requestScope.errors.witnessstatus}"/>
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
                                        <fmt:message key="age"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <c:choose>
                                            <c:when test="${requestScope.values.age != null}">
                                                <INPUT size="3" maxlength="3" type="text" value="<c:out value="${requestScope.values.age}"/>" name="witnessage">
                                            </c:when>
                                            <c:when test="${requestScope.witnessdetail.age == '-1'}">
                                                <INPUT size="3" maxlength="3" type="text" value="<c:out value=""/>" name="witnessage">
                                            </c:when>
                                            <c:otherwise>
                                                <INPUT size="3" maxlength="3" type="text" value="<c:out value="${requestScope.witnessdetail.age}"/>" name="witnessage">
                                            </c:otherwise>
                                        </c:choose>

                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.age != null}">
                                            <fmt:message key="${requestScope.errors.age}"/>
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
                                        <fmt:message key="expectedarrivaltime"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <c:choose>
                                            <c:when test="${requestScope.values.expected}">
                                                <INPUT size="5" maxlength="5" type="text" value="<c:out value="${requestScope.values.expected}"/>" name="expected">
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:formatDate value="${requestScope.witnessdetail.expected}" pattern="HH:mm" var="formattedTime"/>
                                                <INPUT size="5" maxlength="5" type="text" value="<c:out value="${formattedTime}"/>" name="expected">
                                            </c:otherwise>
                                        </c:choose>
                                        <fmt:message key="timeformat"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.expected != null}">
                                            <fmt:message key="${requestScope.errors.expected}"/>
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

                                    <td class="psFormValue">
                                        <c:choose>
                                            <c:when test="${requestScope.values.notes != null}">
                                                <TEXTAREA name="notes" cols="80" rows="2" wrap="OFF" width="95%"><c:out value="${requestScope.values.notes}"/></TEXTAREA>
                                            </c:when>
                                            <c:otherwise>
                                                <TEXTAREA name="notes" cols="80" rows="2" wrap="OFF" width="95%"><c:out value="${requestScope.witnessdetail.notes}"/></TEXTAREA>
                                            </c:otherwise>
                                        </c:choose>

                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.notes != null}">
                                            <fmt:message key="${requestScope.errors.notes}"/>
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
                    <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                    <input type="hidden" value="<c:out value="${requestScope.witnessdetail.id}"/>" name="id">
                    <c:if test="${requestScope.week != null}">
                        <input type="hidden" value="<c:out value="${requestScope.week}"/>" name="week">
                    </c:if>
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
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <c:choose>
                                                    <c:when test="${requestScope.week != null}">
                                                        <form class="psForm" action="<c:url value="/caseskeletonschedule"/>" method="post" name="cancel">
                                                    </c:when>
                                                            <c:otherwise>
                                                                <form class="psForm" action="./<c:out value="${requestScope.pagesource}"/>" method="post" name="cancel">
                                                            </c:otherwise>
                                                </c:choose>
                                                                    <c:set scope="request" var="buttonTextKey" value="cancel"/>
                                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('cancel')"/>
                                                                    <c:import url="${menuButtonURL}"/>
                                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="id">
                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                                    <c:if test="${requestScope.week != null}">
                                                                        <input type="hidden" value="<c:out value="${requestScope.week}"/>" name="week">
                                                                    </c:if>
                                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <c:set scope="request" var="buttonTextKey" value="save"/>
                                                <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('editwitness')"/>
                                                <c:import url="${menuButtonURL}"/>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <c:if test="${requestScope.week == null}">
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/witnesssignin"/>" method="post" name="signinform">
                                                    <input type="hidden" value="<c:out value="${requestScope.witnessdetail.id}"/>" name="id"/>
                                                    <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                                                    <input type="hidden" value="<c:out value="${requestScope.casedetail.caseType}"/>" name="casetype">
                                                    <c:choose>
                                                        <c:when test="${requestScope.values.witnessname != null}">
                                                            <input type="hidden" value="<c:out value="${requestScope.values.witnessname}"/>" name="witnessname">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" value="<c:out value="${requestScope.witnessdetail.name}"/>" name="witnessname">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${requestScope.values.age != null}">
                                                            <input type="hidden" value="<c:out value="${requestScope.values.age}"/>" name="witnessage">
                                                        </c:when>
                                                        <c:when test="${requestScope.witnessdetail.age == '-1'}">
                                                            <input type="hidden" value="<c:out value=""/>" name="witnessage">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" value="<c:out value="${requestScope.witnessdetail.age}"/>" name="witnessage">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${requestScope.values.trialsession == null}">
                                                            <input type="hidden" value="<c:out value="${requestScope.trialsession.id}"/>" name="trialsession">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" value="<c:out value="${requestScope.values.trialsession}"/>" name="trialsession">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${requestScope.values.witnessstatus == null}">
                                                            <input type="hidden" value="<c:out value="${requestScope.witnessdetail.status}"/>" name="witnessstatus">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" value="<c:out value="${requestScope.values.witnessstatus}"/>" name="witnessstatus">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${requestScope.values.expected}">
                                                            <input type="hidden" value="<c:out value="${requestScope.values.expected}"/>" name="expected">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <fmt:formatDate value="${requestScope.witnessdetail.expected}" pattern="HH:mm" var="formattedTime"/>
                                                            <input type="hidden" value="<c:out value="${formattedTime}"/>" name="expected">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${requestScope.values.notes != null}">
                                                            <input type="hidden" value="<c:out value="${requestScope.values.notes}"/>" name="notes">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" value="<c:out value="${requestScope.witnessdetail.notes}"/>" name="notes">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                    <c:set scope="request" var="buttonTextKey" value="signin"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('signinform')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                </form>
                                            </td>
                                            </c:if>
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
