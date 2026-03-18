<%--
  - Title:       header.jsp (jsp page fragment)
  -
  - Description: This file is included by the skeleton and provides a common
  -              header for all pages. Note that it uses the style sheet
  -              imported by skeleton.jsp. Like all pages it should not be
  -              referenced directly but through the pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: header.jsp,v $
  - Revision 1.4  2006/05/04 10:18:39  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:56  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:32  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.3  2004/08/25 15:59:47  zz7n1c
  - Merged from BRANCH_6_X
  -
  - Revision 1.2  2003/05/08 15:57:00  fz0n8j
  - Fixed bug formatting
  -
  - Revision 1.1  2003/03/26 17:49:02  fz0n8j
  - Moved files to shared html dir
  -
  - Revision 1.4  2003/03/19 21:54:14  fz0n8j
  - Allow context free
  -
  - Revision 1.3  2003/03/17 11:31:49  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.2  2003/03/11 16:31:42  fz0n8j
  - Added CVS log comments - ecawley
  -
  --%>
<%--
  - The format tag lib is used to i18n messages  
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td><img src="/Static/images/header.banner.jpg" height="70" width="295"/></td>
        <td width="100%" class="headerText">
            <fmt:message key="header.text"/>
        </td>
        <td><img src="/Static/images/blank.gif" height="70" width="295"/></td>
    </tr>
</table>
