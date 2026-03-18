<%--
  - Title:       viewcaseskeletons.jsp (jsp page fragment)
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      David Duncan
  - $Revision: 1.4 $
  - $Log: viewcaseskeletons.jsp,v $
  - Revision 1.4  2006/05/04 10:18:41  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:57  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:40  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.6  2003/10/14 14:29:40  xzmw8n
  - Added tokens and checks to identify and reject duplicate form submissions
  -
  - Revision 1.5  2003/05/09 15:52:10  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.4  2003/05/08 15:36:09  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.3  2003/05/02 15:33:55  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.2  2003/04/25 15:28:33  hzf3bb
  - no message
  -
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
                                    <fmt:message key="viewcaseskeletons.title"/>
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
                    <c:when test="${requestScope.skeletons != null}">
                <tr>
                    <td colspan="13" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="case"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="defendants"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">

                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                </tr>

                <tr>
                    <td colspan="13" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>

                <c:forEach var="item" items="${requestScope.skeletons}">
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.caseDetail.caseNumber}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:forEach var="defendant" items="${item.caseDetail.defendantNames}">
                            <c:out value="${defendant}"/>
                            <br>
                        </c:forEach>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain" width="12">
                        <form class="psForm" action="<c:url value="/caseskeletonschedule"/>" method="post" name="editform<c:out value="${item.id}"/>">
                            <a href="javascript:psSubmitForm('editform<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="edit"/>')"
                    onMouseout="return psSetStatus(' ')"><IMG src="/Static/images/editicon.gif" alt="<fmt:message key="edit"/>" border="0"></a>
                            <input type="hidden" name="id" value="<c:out value="${item.caseId}"/>">
                            <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                        </form>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="13" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                </c:forEach>
                </c:when>
                <c:otherwise>
                <tr>
                    <td  class="psPageMessage" colspan="13">
                        <fmt:message key="norecords"/>
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
</table>

