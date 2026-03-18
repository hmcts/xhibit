<%--                                                                              
  - Title:       loginmenu.jsp (jsp page fragment)
  -
  - Description: This file is included by the logon page and spaces an empty 
  -              menu.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  -
  - $Revision: 1.4 $
  - $Log: loginmenu.jsp,v $
  - Revision 1.4  2006/05/04 10:18:39  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:56  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:33  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.2  2003/05/08 15:57:00  fz0n8j
  - Fixed bug formatting
  -
  - Revision 1.1  2003/03/26 17:49:03  fz0n8j
  - Moved files to shared html dir
  -
  - Revision 1.2  2003/03/19 21:54:14  fz0n8j
  - Allow context free
  -
  - Revision 1.1  2003/03/18 17:44:57  fz0n8j
  - Changed to create login pages
  -
  -
  --%>
<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - Get the page urls from the Pages resource bundle
  --%>

<fmt:bundle basename="Pages">
    <fmt:message key="buttonSpacer" var="menuButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="11" class="psMenuHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td class="psMenu">
            <c:import url="${menuButtonURL}"/>
        </td>
        <td class="psMenuVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="11" class="psMenuHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>
