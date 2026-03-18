<%--
  - Title:       caseskeletonschedule.jsp (jsp page fragment)
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      David Duncan
  - $Revision: 1.4 $
  - $Log: caseskeletonschedule.jsp,v $
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
  - Revision 1.11  2004/02/25 11:23:55  tzj8k5
  - set up pagesource parameter for navigation and new button added to view/create skeleton schedules
  -
  - Revision 1.10  2004/01/23 15:03:07  xzmw8n
  - Fixed judge not found exception
  -
  - Revision 1.9  2003/11/21 16:44:37  xzmw8n
  - Added judges name
  -
  - Revision 1.8  2003/10/14 14:29:40  xzmw8n
  - Added tokens and checks to identify and reject duplicate form submissions
  -
  - Revision 1.7  2003/06/17 12:51:38  cawleye
  - Added a a jstl catch to stop an error, may change this back according to business requirements.
  -
  - Revision 1.6  2003/05/14 13:09:56  fz0n8j
  - Bug fixes and first version of print by day.
  -
  - Revision 1.5  2003/05/13 14:42:51  fz0n8j
  - More updates to print schedule by week.
  -
  - Revision 1.4  2003/05/13 12:55:31  fz0n8j
  - Adding print for schedule by week.
  -
  - Revision 1.3  2003/05/09 15:52:10  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.2  2003/05/08 15:36:08  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.1  2003/04/25 15:28:32  hzf3bb
  - no message
  -
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
  <% boolean varCPS = false; if (request.isUserInRole("XHBCPS")){varCPS=true;pageContext.setAttribute("varCPS",new Boolean(varCPS));}
                        %>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <%--        <form name="editprobationdetails" action="<c:url value="/updateprobationdetails"/>" method="post">--%>
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
                                    <fmt:message key="caseskeletonschedule.title"/>
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
                    <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
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
                        <fmt:message key="padjudge"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="estimatedcaseduration"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="weeknumber"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                </tr>

                <tr>
                    <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>

                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.caseSkeleton.caseDetail.courtName}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:forEach var="defendant" items="${requestScope.caseSkeleton.caseDetail.defendantNames}">
                            <c:out value="${defendant}"/>
                            <br>
                        </c:forEach>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.caseSkeleton.caseDetail.caseNumber}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop"><c:out value="${requestScope.judgeName}"/></td> 
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                    <c:catch>
                        <c:out value="${requestScope.caseSkeleton.caseDetail.estimatedCaseDuration}"/>
                    </c:catch>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.week}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
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
                <c:choose>
                    <c:when test="${requestScope.witnessSessions != null}">
                <tr>
                    <td colspan="28" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="day"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="sessiontype"/>
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
                        <fmt:message key="type"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="expectedarrivaltime"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace" width="12">
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>

                <tr>
                    <td colspan="28" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <c:forEach var="item" items="${requestScope.witnessSessions}">
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.dayNumber}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
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
                        <c:out value="${item.type}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.expected}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain" width="12">
                        <c:if test="${item.type != 'Defence'}">
                            <form class="psForm" action="<c:url value="/editwitness"/>" method="post" name="editwitness<c:out value="${item.id}"/>">
                                <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                                <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                                <input type="hidden" value="<c:out value="${requestScope.week}"/>" name="week"/>
                                <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                <input type="hidden" name="pagesource" value="caseskeletonschedule">
                                <a href="javascript:psSubmitForm('editwitness<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="editwitness"/>')"
                    onMouseout="return psSetStatus(' ')"><IMG src="/Static/images/editicon.gif" alt="<fmt:message key="editwitness"/>" border="0"></a>
                            </form>
                        </c:if>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="28" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                </c:forEach>
                </c:when>
                <c:otherwise>
                <tr>
                    <td  class="psPageMessage" colspan="25">
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
                                                <form class="psForm" action="<c:url value="/addwitness"/>" method="post" name="addwitness">
                                                    <c:set scope="request" var="buttonTextKey" value="add"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('addwitness')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                                                    <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                                                    <input type="hidden" value="<c:out value="${requestScope.week}"/>" name="week"/>
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                            <c:choose>
                                                <c:when test="${requestScope.week > 1}">
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/caseskeletonschedule"/>" method="post" name="previous">
                                                    <c:set scope="request" var="buttonTextKey" value="previous"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('previous')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="id">
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.week - 1}"/>" name="week">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            </c:when>
                                            </c:choose>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/caseskeletonschedule"/>" method="post" name="next">
                                                    <c:set scope="request" var="buttonTextKey" value="next"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('next')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="id">
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.week + 1}"/>" name="week">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <c:url value="/printskeletonbyweek" var="printskeletonbyweek"/>
                                                <c:set scope="request" var="buttonTextKey" value="printbyweek"/>
                                                <c:set scope="request" var="buttonRequestURL" value="javascript:void window.open('${printskeletonbyweek}?caseid=${requestScope.caseid}','Print','WIDTH=800,HEIGHT=600,statusbar=no,menubar=no,toolbar=no,scrollbars=yes')"/>
                                                <c:import url="${menuButtonURL}"/>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <c:url value="/printskeletonbyday" var="printskeletonbyday"/>
                                                <c:set scope="request" var="buttonTextKey" value="printbyday"/>
                                                <c:set scope="request" var="buttonRequestURL" value="javascript:void window.open('${printskeletonbyday}?caseid=${requestScope.caseid}','Print','WIDTH=800,HEIGHT=600,statusbar=no,menubar=no,toolbar=no,scrollbars=yes')"/>
                                                <c:import url="${menuButtonURL}"/>
                                            </td>

                                            <c:if test="${varCPS}">
                                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                                <td class="psPageAction">
                                                    <form class="psForm" action="<c:url value="/viewtrialsession"/>" method="post" name="viewts">
                                                        <c:set scope="request" var="buttonTextKey" value="viewts"/>
                                                        <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('viewts')"/>
                                                        <c:import url="${menuButtonURL}"/>
                                                        <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                        <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                        <input type="hidden" name="pagesource" value="caseskeletonschedule">
                                                    </form>
                                                </td>
                                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            </c:if>
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
                    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
            </table>
            <%--<input type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">--%>
            <%--        </form>--%></td>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>
