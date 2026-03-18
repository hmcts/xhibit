<%--
  - Title:       selectrecipient.jsp (jsp page fragment)
  -
  - Description: This page shows a list of psr recipients to be selected for a request.
  -              Like all pages it should not be referenced directly but through the 
  -              pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: selectrecipient.jsp,v $
  - Revision 1.4  2006/05/04 10:18:39  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:55  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:26  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.9  2004/10/14 13:06:28  tzj8k5
  - PRE 305 - Missing dates entered when selecting a recipient
  -
  - Revision 1.8  2004/10/13 12:37:13  tzj8k5
  - PRE 304 - Select Recipient display
  -
  - Revision 1.7  2003/12/10 14:35:03  xzmw8n
  - Implemented check token functionality
  -
  - Revision 1.6  2003/05/08 15:35:37  fz0n8j
  - Bug fix (/> on table elements) and formatting
  -
  - Revision 1.5  2003/03/26 16:54:48  fz0n8j
  - Bug fixes.
  -
  - Revision 1.4  2003/03/21 18:35:56  fz0n8j
  - more print functionality
  -
  - Revision 1.3  2003/03/20 11:37:06  fz0n8j
  - Changed form actions to use the c:url tag
  -
  - Revision 1.2  2003/03/19 21:54:15  fz0n8j
  - Allow context free
  -
  - Revision 1.1  2003/03/19 19:34:09  fz0n8j
  - Added to cvs.
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
                                    <fmt:message key="viewrecipientsummaries.title"/>
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
                    <c:when test="${requestScope.recipientsummaries != null}">
                <tr>
                    <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="column.officeName"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="column.address"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="column.telephone"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="column.fax"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="column.email"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader"></td>
                </tr>
                <tr>
                    <td colspan="25" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <c:forEach var="item" items="${requestScope.recipientsummaries}">
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.officeName}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.address}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.telephone}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.fax}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.email}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <form class="psForm" action="<c:url value="/updatepsrrequestrecipient"/>" method="post" name="selectform<c:out value="${item.id}"/>">
                            <INPUT type="hidden" value="<c:out value="${item.id}"/>" name="id">
                            <INPUT type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">
                            <INPUT type="hidden" value="<c:out value="${requestScope.userhearingdate}"/>" name="hrgdate">
                            <INPUT type="hidden" value="<c:out value="${requestScope.usernolaterdate}"/>" name="nolaterdate">
                            <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                            <a href="javascript:psSubmitForm('selectform<c:out value="${item.id}"/>')" onClick="window.status='SELECT'; return true" onMouseover="window.status='SELECT'; return true" onMouseout="window.status=' '; return true">
                            <img src="/Static/images/selecticon.gif" border="0"></a>
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
                    <td  class="psPageMessage" colspan="27">
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
<table>
    <tr>
        <td>
            <form class="psForm" action="<c:url value="/editpsrrequestdetails"/>" method="post" name="cancel">
                <c:set scope="request" var="buttonTextKey" value="cancel"/>
                <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('cancel')"/>
                <c:import url="${menuButtonURL}"/>
                <INPUT type="hidden" value="<c:out value="${requestScope.requestObjectId}"/>" name="objectid">
                <INPUT type="hidden" value="<c:out value="${requestScope.userhearingdate}"/>" name="hrgdate">
                <INPUT type="hidden" value="<c:out value="${requestScope.usernolaterdate}"/>" name="nolaterdate">
                <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
            </form>
        </td>
    </tr>
</table>

