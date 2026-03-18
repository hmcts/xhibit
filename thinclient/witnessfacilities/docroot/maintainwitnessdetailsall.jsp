<%--
  - Title:       maintainwitnessdetails.jsp (jsp page fragment)
  -
  - Description: This file displays witnessdetails.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: maintainwitnessdetailsall.jsp,v $
  - Revision 1.4  2006/05/04 10:18:41  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:57  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:39  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.13  2005/01/17 13:57:54  tzj8k5
  - PRE 317 Display and sort trial session info
  -
  - Revision 1.12  2004/11/04 14:35:15  bzjrnl
  - Changes to correct problems with cookie access.
  -
  - Revision 1.11  2004/09/27 08:18:53  tzj8k5
  - PRE292 - View of Witness Age on the ThinClient
  -
  - Revision 1.10  2004/04/19 14:39:28  tzj8k5
  - Navigation when deleting a witness
  -
  - Revision 1.9  2004/02/20 12:52:26  xzmw8n
  - Added attribute to indicate the intial calling page of the action
  -
  - Revision 1.8  2003/12/08 14:33:10  cz4lvy
  - Determine the user role for identifying available functionality on the page.
  -
  - Revision 1.7  2003/10/14 14:04:11  xzmw8n
  - Added tokens and checks to identify and reject duplicate form submissions
  -
  - Revision 1.6  2003/08/01 11:48:39  rz7jlh
  - Now can edit and delete from these jsps.
  -
  - Revision 1.5  2003/05/21 14:30:32  fz0n8j
  - Bug fixes
  -
  - Revision 1.4  2003/05/08 15:36:08  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.3  2003/05/02 15:33:54  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.2  2003/04/30 11:43:59  qzd3k3
  - Merge from dev branch.
  -
  - Revision 1.1.2.2  2003/04/23 14:17:13  fz0n8j
  - Added edit case details.
  -
  - Revision 1.1.2.1  2003/04/14 13:25:13  fz0n8j
  - Now possible to view all witnesses.
  -
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
                                    <fmt:message key="maintainwitnessdetailsall.title"/>
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
                <c:choose>
                    <c:when test="${requestScope.caseDetail != null}">
                <tr>
                    <td colspan="21" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="court"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="defendantname"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="casenumber"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="policeofficer"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="cpscaseworker"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                </tr>

                <tr>
                    <td colspan="21" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>

                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.caseDetail.courtName}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:forEach var="defendant" items="${requestScope.caseDetail.defendantNames}">
                            <c:out value="${defendant}"/>
                            <br>
                        </c:forEach>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.caseDetail.caseNumber}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.caseDetail.policeOfficerAttending}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.caseDetail.cpsCaseWorker}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="21" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                </c:when>
                </c:choose>
            </table>
        </td>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
        <!-- old version, now changed to use table from maintain witness details which has delete/edit links.
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <c:choose>
                    <c:when test="${requestScope.witnesses != null}">
                <tr>
                    <td colspan="29" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="name"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="status"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="age"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="dueat"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="arrived"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="released"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="totaltime"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>


                </tr>

                <tr>
                    <td colspan="29" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <c:forEach var="item" items="${requestScope.witnesses}">

                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.name}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.status}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.age}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.dueAt}" dateStyle="short"/>
                        &nbsp;
                        <fmt:formatDate value="${item.dueAt}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.arrived}" dateStyle="short"/>
                        &nbsp;
                        <fmt:formatDate value="${item.arrived}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.released}" dateStyle="short"/>
                        &nbsp;
                        <fmt:formatDate value="${item.released}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:message key="witnesstotaltime">
                            <fmt:param value="${item.totalTimeHours}"/>
                            <fmt:param value="${item.totalTimeMinutes}"/>
                        </fmt:message>
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
                    <td  class="psPageMessage" colspan="33">
                        <fmt:message key="nowitnesses"/>
                    </td>
                </tr>
                </c:otherwise>
                </c:choose>
            </table> -->
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <c:choose>
                    <c:when test="${requestScope.witnesses != null}">
                <tr>
                    <td colspan="41" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="trialsession"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="name"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="status"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="age"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="dueat"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="arrived"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="released"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="totaltime"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>

                <tr>
                    <td colspan="41" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <c:forEach var="item" items="${requestScope.witnesses}">

                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.dayNumber}"/>
                        -
                        <fmt:formatDate value="${item.trialSession.appearanceDate}" dateStyle="short"/>
                        -
                        <c:out value="${item.sessionType}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.name}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.status}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:if test="${item.age != '-1'}">
                            <c:out value="${item.age}"/>
                        </c:if>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.dueAt}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.arrived}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.released}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:message key="witnesstotaltime">
                            <fmt:param value="${item.totalTimeHours}"/>
                            <fmt:param value="${item.totalTimeMinutes}"/>
                        </fmt:message>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain" width="12">
                        <c:choose>
                            <c:when test="${varCPS}">
                                <form class="psForm" action="<c:url value="/editwitness"/>" method="post" name="editwitness<c:out value="${item.id}"/>">
                                    <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                                    <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                    <input type="hidden" name="pagesource" value="maintainwitnessdetailsall">
                                    <a href="javascript:psSubmitForm('editwitness<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="editwitness"/>')"
                    onMouseout="return psSetStatus(' ')"><IMG src="/Static/images/editicon.gif" alt="<fmt:message key="editwitness"/>" border="0"></a>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form class="psForm" action="<c:url value="/witnesssignin"/>" method="post" name="editform<c:out value="${item.id}"/>">
                                    <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                                    <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                    <a href="javascript:psSubmitForm('editform<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="witnesssignin"/>')"
                    onMouseout="return psSetStatus(' ')"><IMG src="/Static/images/editicon.gif" alt="<fmt:message key="witnesssignin"/>" border="0"></a>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain" width="12">
                        <c:if test="${item.type != 'Defence' && varCPS}">
                            <form class="psForm" action="<c:url value="/deletewitness"/>" method="post" name="deletewitness<c:out value="${item.id}"/>">
                                <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                                <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                                <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                <input type="hidden" name="pagesource" value="maintainwitnessdetailsall">
                                <a href="javascript:psSubmitForm('deletewitness<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="deletewitness"/>')"
                    onMouseout="return psSetStatus('')"><IMG src="/Static/images/deleteicon.gif" alt="<fmt:message key="deletewitness"/>" border="0"></a>
                            </form>
                        </c:if>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain" width="12">
                        <c:if test="${item.type != 'Defence' && item.hasNumber}">
                            <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                            <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                            <a href="javascript:void window.open('/Messaging/sendwitnessadhocmessage?witnessid=<c:out value="${item.id}"/>','SendWitnessAdHocMessage','fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no,directories=no,location=no,width=640,height=240,left=200,top=200')"
                    onMouseover="return psSetStatus('<fmt:message key="messagewitness"/>')"
                    onMouseout="return psSetStatus('')"><IMG src="/Static/images/messageicon.gif" alt="<fmt:message key="messagewitness"/>" border="0"></a>
                        </c:if>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="41" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                </c:forEach>
                </c:when>
                <c:otherwise>
                <tr>
                    <td  class="psPageMessage" colspan="37">
                        <fmt:message key="nowitnesses"/>
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
    <tr>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td>
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/maintainwitnessdetails"/>" method="post" name="today">
                                                    <c:set scope="request" var="buttonTextKey" value="today"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('today')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/maintainwitnessdetailsfuture"/>" method="post" name="future">
                                                    <c:set scope="request" var="buttonTextKey" value="future"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('future')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <c:url value="/printwitnessdetailsall" var="printwitnessdetails"/>
                                                <c:set scope="request" var="buttonTextKey" value="print"/>
                                                <c:set scope="request" var="buttonRequestURL" value="javascript:void window.open('${printwitnessdetails}?caseid=${caseid}','Print','WIDTH=800,HEIGHT=600,statusbar=no,menubar=no,toolbar=no,scrollbars=yes')"/>
                                                <c:import url="${menuButtonURL}"/>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <c:choose>
                                                <c:when test="${varCPS}">
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/editcasedetails"/>" method="post" name="editcasedetails">
                                                    <c:set scope="request" var="buttonTextKey" value="editcasedetails"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('editcasedetails')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            </c:when>
                                            </c:choose>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                        </table>
                    </td>
                    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

