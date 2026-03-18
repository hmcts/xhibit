<%--
  - Title:       viewunissuedrequestsummaries.jsp (jsp page fragment)
  -
  - Description: This file displays the summary of unissued requests.
  -              Like all pages it should not be referenced directly but through the 
  -              pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: viewunissuedpsrrequestsummaries.jsp,v $
  - Revision 1.4  2006/05/04 10:18:39  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:56  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:28  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.19  2003/12/10 14:35:04  xzmw8n
  - Implemented check token functionality
  -
  - Revision 1.18  2003/12/09 16:39:54  xzmw8n
  - Implemented check token functionality
  -
  - Revision 1.17  2003/05/08 15:35:37  fz0n8j
  - Bug fix (/> on table elements) and formatting
  -
  - Revision 1.16  2003/03/26 16:54:49  fz0n8j
  - Bug fixes.
  -
  - Revision 1.15  2003/03/24 08:23:26  fz0n8j
  - Added view daily list, and public display
  -
  - Revision 1.14  2003/03/21 18:35:56  fz0n8j
  - more print functionality
  -
  - Revision 1.13  2003/03/21 17:28:37  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.12  2003/03/20 11:37:06  fz0n8j
  - Changed form actions to use the c:url tag
  -
  - Revision 1.11  2003/03/19 21:54:15  fz0n8j
  - Allow context free
  -
  - Revision 1.10  2003/03/19 19:36:20  fz0n8j
  - Added button for edit.
  -
  - Revision 1.9  2003/03/18 22:02:42  fz0n8j
  - Added object id.
  -
  - Revision 1.8  2003/03/18 13:28:12  fz0n8j
  - Fixed space after from by adding form style.
  -
  - Revision 1.7  2003/03/18 09:37:25  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.6  2003/03/17 11:31:58  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.5  2003/03/13 15:59:00  fz0n8j
  - Added link to print
  -
  - Revision 1.4  2003/03/11 15:59:46  fz0n8j
  - Added CVS Log comment
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
                                    <fmt:message key="viewunissuedpsrrequestsummaries.title"/>
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
                    <c:when test="${requestScope.psrRequestSummaries != null}">
                <tr>
                    <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="defendantName"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="caseNumber"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="courtNumber"/>
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
                    <td class="psTableHeader"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>

                <tr>
                    <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>

                <c:forEach var="item" items="${requestScope.psrRequestSummaries}">
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.defendantName}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.caseNumber}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.courtNumber}"/>
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
                    <td width="12" class="psTableMain"><a href="javascript:void window.open('<c:url value="/printpsrrequestdetails"/>?id=<c:out value="${item.id}"/>&objectid=<c:out value="${requestScope.objectid}"/>','Print','WIDTH=800,HEIGHT=600,statusbar=no,menubar=no,toolbar=no,scrollbars=yes')"
                onMouseover="return psSetStatus('<fmt:message key="alt.print"/>')"
                onMouseout="return psSetStatus(' ')"><img src="/Static/images/printicon.gif" alt="<fmt:message key="alt.print"/>" border="0"></a></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td width="12" class="psTableMain">
                        <form class="psForm" action="<c:url value="/viewpsrrequestdetails"/>" method="post" name="viewform<c:out value="${item.id}"/>">
                            <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                            <INPUT type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">
                            <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">  
                            <a href="javascript:psSubmitForm('viewform<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="alt.view"/>')"
                    onMouseout="return psSetStatus(' ')"><img src="/Static/images/viewicon.gif" alt="<fmt:message key="alt.view"/>" border="0"></a>
                        </form>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td width="12" class="psTableMain">
                        <form class="psForm" action="<c:url value="/editpsrrequestdetails"/>" method="post" name="editform<c:out value="${item.id}"/>">
                            <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                            <INPUT type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">
                            <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                            <a href="javascript:psSubmitForm('editform<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="alt.edit"/>')"
                    onMouseout="return psSetStatus(' ')"><IMG src="/Static/images/editicon.gif" alt="<fmt:message key="alt.edit"/>" border="0"></a>
                        </form> 
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                </c:forEach>
                </c:when>
                <c:otherwise>
                <tr>
                    <td  class="psPageMessage">
                        <fmt:message key="norecords"/>
                    </td>
                </tr>
                </c:otherwise>
                </c:choose>
            </table>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td colspan="3" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageAction">
                                    <c:set scope="request" var="buttonTextKey" value="viewissuedpsrrequestsummaries"/>
                                    <c:set scope="request" var="buttonRequestURL" value="/viewissuedpsrrequestsummaries"/>
                                    <c:import url="${menuButtonURL}"/>
                                </td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="3" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

