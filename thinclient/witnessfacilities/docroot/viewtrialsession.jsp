<%--
  - Title:       viewtrialsession.jsp (jsp page fragment)
  -
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      David Duncan
  - $Revision: 1.4 $
  - $Log: viewtrialsession.jsp,v $
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
  - Revision 1.10  2004/09/29 09:20:12  tzj8k5
  - PRE 297 - Amend JSP's so that they do not reference the application name directly
  -
  - Revision 1.9  2004/04/20 15:07:22  fz1f7w
  - Change back to Version 1.7
  -
  - Revision 1.8  2004/04/20 08:31:10  fz1f7w
  - Change to jsp so that user goes to case skeleton schedule when they press the back button.
  -
  - Revision 1.7  2004/03/03 10:34:26  tzj8k5
  - Sort out page navigation issues - setting up the pagesource parameter
  -
  - Revision 1.6  2004/02/25 11:22:50  tzj8k5
  - set up pagesource parameter for navigation
  -
  - Revision 1.5  2003/10/14 14:04:11  xzmw8n
  - Added tokens and checks to identify and reject duplicate form submissions
  -
  - Revision 1.4  2003/05/08 15:36:09  fz0n8j
  - bug fix (/> on table elements) and formatting
  -
  - Revision 1.3  2003/05/02 16:43:03  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.1.2.3  2003/04/25 15:29:15  hzf3bb
  - no message
  -
  - Revision 1.1.2.2  2003/04/24 16:34:06  hzf3bb
  - *** empty log message ***
  -
  - Revision 1.1.2.1  2003/04/22 17:35:41  hzf3bb
  - *** empty log message ***
  -
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
                                <fmt:message key="viewtrialsession.title"/>
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
            <c:when test="${requestScope.trialsessions != null}">
<tr>
   <td colspan="19" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader"><fmt:message key="trialday"/></td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader"><fmt:message key="trialdate"/></td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader"><fmt:message key="trialsession"/></td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeader"><img src="/Static/images/blank.gif"></td>
    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="19" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
</tr>
<c:forEach var="item" items="${requestScope.trialsessions}">
    <tr>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMain"><c:out value="${item.dayNumber}"/></td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableMain"><fmt:formatDate value="${item.appearanceDate}" dateStyle="short"/></td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMain"><c:out value="${item.sessionType}"/></td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td width="12" class="psTableMain">
            <form class="psForm" action="<c:url value="/edittrialsession"/>" method="post" name="editform<c:out value="${item.id}"/>">
               <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                <input type="hidden" name="trialday" value="<c:out value="${item.dayNumber}"/>"/>
                <input type="hidden" name="trialdate" value="<c:out value="${item.appearanceDate}"/>"/>
                <input type="hidden" name="trialsession" value="<c:out value="${item.sessionType}"/>"/>
                <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
                <a href="javascript:psSubmitForm('editform<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="edit"/>')"
                    onMouseout="return psSetStatus(' ')">
                    <IMG src="/Static/images/editicon.gif" alt="<fmt:message key="edit"/>" border="0">
                </a>
            </form>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td width="12" class="psTableMain">
            <form class="psForm" action="<c:url value="/deletetrialsession"/>" method="post" name="deleteform<c:out value="${item.id}"/>">
               <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
               <input type="hidden" name="trialday" value="<c:out value="${item.dayNumber}"/>"/>
               <input type="hidden" name="trialdate" value="<c:out value="${item.appearanceDate}"/>"/>
               <input type="hidden" name="trialsession" value="<c:out value="${item.sessionType}"/>"/>
               <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
                <a href="javascript:psSubmitForm('deleteform<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="delete"/>')"
                    onMouseout="return psSetStatus('')">
                    <IMG src="/Static/images/deleteicon.gif" alt="<fmt:message key="delete"/>" border="0">
                </a>
            </form>
        </td>
        <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="19" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    </c:forEach>
</c:when>
<c:otherwise>
    <tr>
        <td  class="psPageMessage" colspan="29">
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
                       <form class="psForm" action="./<c:out value="${requestScope.pagesource}"/>" method="post" name="back">
                          <c:set scope="request" var="buttonTextKey" value="back"/>
                          <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('back')"/>
                          <c:import url="${menuButtonURL}"/>
                          <input type="hidden" name="caseid" value="<c:out value="${requestScope.caseid}"/>">
                          <input type="hidden" name="id" value="<c:out value="${requestScope.caseid}"/>">
                          <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                      </form>
                    </td>
                    <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                     <td class="psPageAction">
                        <form class="psForm" action="<c:url value="/addtrialsession"/>" method="post" name="add">
                            <c:set scope="request" var="buttonTextKey" value="add"/>
                            <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('add')"/>
                            <c:import url="${menuButtonURL}"/>
                         <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                         <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
                       </form>
                    </td>
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

<tr>
<td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
<td>

</td>
<td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
</table>



