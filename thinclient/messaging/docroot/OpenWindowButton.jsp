<%--                                                                              <%--
  - Title:       button.jsp (jsp page fragment)
  -
  - Description: This file is included by the menu and provides a common
  -              button for all pages. Note that it uses the style sheet
  -              imported by skeleton.jsp. Like all pages it should not be
  -              referenced directly but through the pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: OpenWindowButton.jsp,v $
  - Revision 1.4  2006/05/04 10:18:38  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:54  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:21  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.1  2003/04/02 13:43:09  rz3jq5
  - Extensive changes to bring in line with new framework.
  -
  - Revision 1.1  2003/03/26 17:49:01  fz0n8j
  - Moved files to shared html dir
  -
  - Revision 1.7  2003/03/24 08:23:25  fz0n8j
  - Added view daily list, and public display
  -
  - Revision 1.6  2003/03/19 21:54:14  fz0n8j
  - Allow context free
  -
  - Revision 1.5  2003/03/17 11:31:47  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.4  2003/03/11 16:31:41  fz0n8j
  - Added CVS log comments - ecawley
  -
  --%>
<%--
  - The format tag lib is used to i18n messages
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%--
  - The html to be included in the main page
  --%>

  <table <c:if test="${requestScope.buttonFill}">width="100%"</c:if> cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="5" class="buttonHorizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="buttonVerticalBorder"><img src="/Static/images/blank.gif"></td>
        <td class="buttonSpace"><img src="/Static/images/blank.gif"></td>
        <td class="buttonMainOW"
                onClick="window.status='<fmt:message key="${requestScope.buttonTextKey}"/>';openWindowWithFocus('<c:out value="${requestScope.windowUrl}"/>','<c:out value="${requestScope.windowName}"/>','<c:out value="${requestScope.windowParms}"/>'); return true"
                onMouseover="window.status='<fmt:message key="${requestScope.buttonTextKey}"/>'; return true"
                onMouseout="window.status=' '; return true">
            <fmt:message key="${requestScope.buttonTextKey}"/>
        </td>
        <td class="buttonSpace"><img src="/Static/images/blank.gif"></td>
        <td class="buttonVerticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="5" class="buttonHorizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

